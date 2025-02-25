package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.business.domain.api.mabang.Response;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.*;
import org.jeecg.modules.business.domain.api.mabang.stockDoAddStock.SkuAddRequest;
import org.jeecg.modules.business.domain.api.mabang.stockDoAddStock.SkuAddRequestBody;
import org.jeecg.modules.business.domain.api.mabang.stockDoAddStock.SkuAddResponse;
import org.jeecg.modules.business.domain.api.mabang.stockDoChangeStock.SkuChangeRequest;
import org.jeecg.modules.business.domain.api.mabang.stockDoChangeStock.SkuChangeRequestBody;
import org.jeecg.modules.business.domain.api.mabang.stockDoChangeStock.SkuChangeResponse;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.SkuStockData;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.SkuStockRawStream;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.SkuStockRequestBody;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.SkuStockStream;
import org.jeecg.modules.business.domain.job.ThrottlingExecutorService;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.SkuListMabangMapper;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.ResponsesWithMsg;
import org.jeecg.modules.business.vo.SkuOrderPage;
import org.jeecg.modules.online.cgform.mapper.OnlCgformFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.ceil;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class SkuListMabangServiceImpl extends ServiceImpl<SkuListMabangMapper, SkuData> implements ISkuListMabangService {
    @Autowired
    private ISensitiveAttributeService sensitiveAttributeService;
    @Autowired
    private SkuListMabangMapper skuListMabangMapper;
    @Autowired
    private ISkuService skuService;
    @Autowired
    private ISkuPriceService skuPriceService;
    @Autowired
    private ISkuDeclaredValueService skuDeclaredValueService;
    @Autowired
    private ISkuWeightService skuWeightService;
    @Autowired
    private IClientSkuService clientSkuService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SkuMongoService skuMongoService;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    Environment env;

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private static final Integer MABANG_API_RATE_LIMIT_PER_MINUTE = 10;

    private final static String DEFAULT_WAREHOUSE_NAME = "SZBA宝安仓";

    // In NameCN field on top of the product name we also get the customer code in the beginning of the string : "XX Description of the product"
    final Pattern cnNamePattern = Pattern.compile("^([a-zA-Z]{2,5})\\s(.*)$");
    // In NameEN field on top of the product name we also get the customer code in the beginning of the string : "XX-En name of product"
    final Pattern enNamePattern = Pattern.compile("^([a-zA-Z]{2,5})-(.*)$");

    final Pattern saleRemarkPattern = Pattern.compile("^([0-9]*)(.*)$");
    /**
     * Save skus to DB from mabang api.
     *
     * @param skuDataList skus to save.
     */
    @Override
    @Transactional
    public Map<Sku, String> saveSkuFromMabang(List<SkuData> skuDataList) {
        Map<Sku, String> newSkusMap = new HashMap<>();

        if (skuDataList.isEmpty()) {
            return newSkusMap;
        }
        // we collect all erpCode
        List<String> allSkuErpCode = skuDataList.stream()
                .map(SkuData::getErpCode)
                .collect(toList());
        // find Skus that already exist in DB
        List<Sku> existingSkuList = skuListMabangMapper.searchExistence(allSkuErpCode);
        // We map all existing Skus in DB with erpCode as key
        Map<String, Sku> existingSkusIDMap = existingSkuList.stream()
                .collect(
                        Collectors.toMap(
                                Sku::getErpCode, Function.identity()
                        )
                );

        ArrayList<SkuData> newSkuDatas = new ArrayList<>();
        for (SkuData retrievedSkuData : skuDataList) {
            Sku skuInDatabase = existingSkusIDMap.get(retrievedSkuData.getErpCode());
            // the current SkuData's erpCode is not in DB, so we add it to the list of newSkuDatas
            if (skuInDatabase == null) {
                newSkuDatas.add(retrievedSkuData);
            }
        }

        /* for new skuDatas, insert them to DB */
        try {
            if (!newSkuDatas.isEmpty()) {
                // we can proceed to create new sku_declare_value associated with the new Sku and also sku_price after we create new skus
//                Map<String, String> productNeedTreatment = saveProductFromMabang(newSkuDatas);
                log.info("{} skus to be inserted.", newSkuDatas.size());
                //create a new sku for each sku data
                newSkusMap = createSkus(newSkuDatas);
                List<Sku> newSkus = new ArrayList<>(newSkusMap.keySet());

                // attributing sku to client
                List<String> unknownClientSkus = clientSkuService.saveClientSku(newSkus);

                // send email for manual check
                if(!unknownClientSkus.isEmpty()) {
                    log.info("Sending email for manual check.");
                    Properties prop = emailService.getMailSender();
                    Session session = Session.getInstance(prop, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                        }
                    });

                    String subject = "Association of Sku to Client failed while creating new Sku";
                    String destEmail = env.getProperty("spring.mail.username");
                    Map<String, Object> templateModel = new HashMap<>();
                    templateModel.put("skus", unknownClientSkus);
                    try {
                        freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                        Template template = freemarkerConfigurer.getConfiguration().getTemplate("admin/unknownClientForSku.ftl");
                        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
                        emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                        log.info("Mail sent successfully");
                    } catch (Exception e) {
                        log.error("Error sending mail: {}", e.getMessage());
                    }
                }

                //we insert sku_prices and sku_declared_values associated with the new skus
                saveSkuPrices(newSkuDatas);
                saveSkuDeclaredValues(newSkuDatas);
            }
        } catch (RuntimeException e) {
            log.error(e.getLocalizedMessage());
        }
        return newSkusMap;
    }

    @Override
    @Transactional
    public Map<Sku, String> updateSkusFromMabang(List<SkuData> skuDataList) {
        Map<Sku, String> updatedSkusRemarkMap = new HashMap<>();
        if (skuDataList.isEmpty()) {
            return updatedSkusRemarkMap;
        }
        // we collect all erpCode
        List<String> allSkuErpCode = skuDataList.stream()
                .map(SkuData::getErpCode)
                .collect(toList());
        // find Skus that already exist in DB
        List<Sku> existingSkuList = skuListMabangMapper.searchExistence(allSkuErpCode);
        // We map all existing Skus in DB with erpCode as key
        Map<String, Sku> existingSkusIDMap = existingSkuList.stream()
                .collect(
                        Collectors.toMap(
                                Sku::getErpCode, Function.identity()
                        )
                );

        ArrayList<SkuData> existingSkuDatas = new ArrayList<>();
        for (SkuData retrievedSkuData : skuDataList) {
            Sku skuInDatabase = existingSkusIDMap.get(retrievedSkuData.getErpCode());
            // the current SkuData's erpCode is in DB, so we add it to the list of existingSkuDatas
            if (skuInDatabase != null) {
                existingSkuDatas.add(retrievedSkuData);
            }
        }

        /* for skuDatas to update, update product names and sku status them to DB */
        try {
            if (!existingSkuDatas.isEmpty()) {
                log.info("{} skus to be updated.", existingSkuDatas.size());

                //update status, en_name, zh_name, weight of existing skus
                List<Sku> skusToUpdate = new ArrayList<>();
                for(SkuData skuData: existingSkuDatas) {
                    boolean isUpdated = false;
                    Sku s = existingSkuList.stream()
                            .filter(sku -> sku.getErpCode().equals(skuData.getErpCode()))
                            .findFirst()
                            .orElse(null);
                    assert s != null;
                    s.setUpdateBy("mabang api");
                    s.setUpdateTime(new Date());
                    if(!s.getStatus().equals(skuData.getStatusValue())) {
                        s.setStatus(skuData.getStatusValue());
                        isUpdated = true;
                    }
                    // Removing the customer code from the product CN name
                    if (!skuData.getNameEN().isEmpty()) {
                        Matcher enNameMatcher = enNamePattern.matcher(skuData.getNameEN());
                        if (enNameMatcher.matches() && !enNameMatcher.group(2).isEmpty()) {
                            if(!s.getEnName().equals(enNameMatcher.group(2))) {
                                s.setEnName(enNameMatcher.group(2));
                                isUpdated = true;
                            }
                        }
                        else {
                            if(!s.getEnName().equals(skuData.getNameEN())) {
                                s.setEnName(skuData.getNameEN());
                                isUpdated = true;
                            }
                        }
                    }
                    // Removing the customer code from the product CN name
                    if (!skuData.getNameCN().isEmpty()) {
                        Matcher cnNameMatcher = cnNamePattern.matcher(skuData.getNameCN());
                        if (cnNameMatcher.matches() && !cnNameMatcher.group(2).isEmpty()) {
                            if(!s.getZhName().equals(cnNameMatcher.group(2))) {
                                s.setZhName(cnNameMatcher.group(2));
                                isUpdated = true;
                            }
                        }
                        else {
                            if(!s.getZhName().equals(skuData.getNameCN())) {
                                s.setZhName(skuData.getNameCN());
                                isUpdated = true;
                            }
                        }
                    }
                    // updating isGift
                    if(!Objects.equals(s.getIsGift(), skuData.getIsGift())) {
                        s.setIsGift(skuData.getIsGift());
                        isUpdated = true;
                    }
                    if(isUpdated)
                        skusToUpdate.add(s);

                    // update sku_weight
//                    String remark = updateSkuWeight(s, skuData.getSaleRemark());
                    // TODO : disabled temporarily because we are not updating the weight on Mabang, thus creating conflicts
//                    if(!remark.isEmpty())
//                        updatedSkusRemarkMap.put(s, remark);
//                    else
                    updatedSkusRemarkMap.put(s, "");
                }
                if(!skusToUpdate.isEmpty())
                    skuService.updateBatchById(skusToUpdate);
                log.info("Updated {} skus : {}.", skusToUpdate.size(), existingSkuDatas.stream().map(SkuData::getErpCode).collect(toList()));
            }
        } catch (RuntimeException e) {
            log.error(e.getLocalizedMessage());
        }
        return updatedSkusRemarkMap;
    }

    public void saveSkuPrices(List<SkuData> newSkus) {
        List<SkuPrice> l = new ArrayList<>();
        for(SkuData skuData : newSkus) {
            SkuPrice sp = new SkuPrice();
            sp.setCreateBy("mabang api");
            sp.setPrice(skuData.getSalePrice());
            sp.setSkuId( skuListMabangMapper.searchSkuId(skuData.getErpCode()));
            try {
                sp.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(skuData.getCreatedTime()));
            } catch (ParseException e) {
                sp.setDate(new Date());
            }
            l.add(sp);
        }
        skuPriceService.saveBatch(l);
    }

    /**
     * Creates SkuDeclaredValue objects from Skudatas and insert them into DB
     * @param skuDataList
     */
    public void saveSkuDeclaredValues(List<SkuData> skuDataList) {
        Calendar cal = Calendar.getInstance();
        //set time to 00:00:00
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date date = cal.getTime();

        List<SkuDeclaredValue> l = new ArrayList<>();
        for(SkuData skuData : skuDataList) {
            SkuDeclaredValue sdv = new SkuDeclaredValue();
            sdv.setCreateBy("mabang api");
            sdv.setDeclaredValue(skuData.getDeclareValue());
            sdv.setSkuId(skuListMabangMapper.searchSkuId(skuData.getErpCode()));
            sdv.setEffectiveDate(date);
            l.add(sdv);
        }
        skuDeclaredValueService.saveBatch(l);
    }

    /**
     *  Parse the product code from Sku erpCode
     * @param skuDataList List of Sku erpCodes in format XXXXXXXX-XX
     * @return productCodeList in format XXXXXXXX
     * @throws SkuListRequestErrorException
     */
    public List<String> parseSkuListToProductCodeList(List<SkuData> skuDataList) throws SkuListRequestErrorException {
        List<String> productCodeList = new ArrayList<>();
        Pattern p = Pattern.compile("^(.+)-[a-zA-Z]{0,5}$");
        for(SkuData sku : skuDataList) {
            Matcher m = p.matcher(sku.getErpCode());
            if(m.matches()) {
                productCodeList.add(m.group(1));
            }
            else {
                productCodeList.add(sku.getErpCode());
            }
        }
        return productCodeList;
    }

    /**
     *
     * @param erpCode Sku erpCode in format XXXXXXXX-XX
     * @return
     * @throws SkuListRequestErrorException
     */
    public String parseSkuToProduct(String erpCode) throws SkuListRequestErrorException {

        Pattern p = Pattern.compile("^(.+)-[a-zA-Z]{0,5}$");
        Matcher m = p.matcher(erpCode);
        if(m.matches()) {
            return m.group(1);
        }
        else {
            return erpCode;
        }
    }

    /**
     *  Creates sku objects from Skudatas
     * @param skuDataList
     * @return
     */
    public Map<Sku, String> createSkus(List<SkuData> skuDataList) {
        final String electroMagSensitiveAttributeId = skuListMabangMapper.searchSensitiveAttributeId("Electro-magnetic");
        final String electricSensitiveAttributeId = skuListMabangMapper.searchSensitiveAttributeId("Electronic/Electric");
        final String normalSensitiveAttributeId = skuListMabangMapper.searchSensitiveAttributeId("Normal goods");
        Map<Sku, String> skuMap = new HashMap<>();
        for(SkuData skuData : skuDataList) {
            Sku s = new Sku();
            s.setId(UUID.randomUUID().toString());
            s.setErpCode(skuData.getErpCode());
            s.setCreateBy("mabang api");
            if (!skuData.getNameEN().isEmpty()) {
                Matcher enNameMatcher = enNamePattern.matcher(skuData.getNameEN());
                if (enNameMatcher.matches() && !enNameMatcher.group(2).isEmpty()) {
                    s.setEnName(enNameMatcher.group(2));
                }
                else {
                    s.setEnName(skuData.getNameEN());
                }
            }
            // Removing the customer code from the product CN name
            if (!skuData.getNameCN().isEmpty()) {
                Matcher cnNameMatcher = cnNamePattern.matcher(skuData.getNameCN());
                if (cnNameMatcher.matches() && !cnNameMatcher.group(2).isEmpty()) {
                    s.setZhName(cnNameMatcher.group(2));
                }
                else {
                    s.setZhName(skuData.getNameCN());
                }
            }
            if(skuData.getStockPicture().equals("") || skuData.getStockPicture() == null) {
                s.setImageSource(skuData.getSalePicture());
            }
            else {
                s.setImageSource(skuData.getStockPicture());
            }
            s.setIsGift(skuData.getIsGift());
            // hasBattery : 1 = yes ; 2 = no
            if (skuData.getHasBattery() == 1) {
                // magnetic : 1 = yes ; 2 = no
                if (skuData.getMagnetic() == 1)
                    s.setSensitiveAttributeId(electroMagSensitiveAttributeId);
                else
                    s.setSensitiveAttributeId(electricSensitiveAttributeId);
            } else {
                // magnetic : 1 = yes ; 2 = no
                if (skuData.getMagnetic() == 1)
                    s.setSensitiveAttributeId(electroMagSensitiveAttributeId);
                else
                    s.setSensitiveAttributeId(normalSensitiveAttributeId);
            }
            skuService.save(s);
            // we are going to set the weight of the product
            String remark = createSkuWeight(s, skuData.getSaleRemark());
            skuMap.put(s, remark);
        }
        return skuMap;
    }
    public String createSkuWeight(Sku sku, String salesRemark) {
        String remark = "";
        SkuWeight sw = new SkuWeight();
        sw.setSkuId(sku.getId());
        sw.setEffectiveDate(new Date());
        Matcher saleRemarkMatcher = saleRemarkPattern.matcher(salesRemark);
        if(saleRemarkMatcher.matches() && !saleRemarkMatcher.group(1).isEmpty()) {
            String saleRemark = saleRemarkMatcher.group(1);
            int weight = (int) ceil(Double.parseDouble(saleRemark));
            sw.setWeight(weight);
            if(!saleRemarkMatcher.group(2).isEmpty()) {
                remark = saleRemarkMatcher.group(2);
            }
        }
        skuWeightService.save(sw);
        return remark;
    }
    public String updateSkuWeight(Sku sku, String salesRemark) {
        boolean isUpdated = false;
        String remark = "";
        Matcher saleRemarkMatcher = saleRemarkPattern.matcher(salesRemark);

        if(saleRemarkMatcher.matches() && !saleRemarkMatcher.group(1).isEmpty()) {
            SkuWeight oldSkuWeight = skuWeightService.getBySkuId(sku.getId());
            String saleRemark = saleRemarkMatcher.group(1);
            int weight = (int) ceil(Double.parseDouble(saleRemark));
            if(oldSkuWeight.getWeight() != weight) {
                SkuWeight newSkuWeight = new SkuWeight();
                newSkuWeight.setWeight(weight);
                newSkuWeight.setSkuId(sku.getId());
                newSkuWeight.setEffectiveDate(new Date());
                skuWeightService.save(newSkuWeight);
                isUpdated = true;
            }
            if(!saleRemarkMatcher.group(2).isEmpty()) {
                if(isUpdated)
                    remark = saleRemarkMatcher.group(2);
            }
        }
        return remark;
    }
    public ResponsesWithMsg publishSkuToMabang(List<SkuOrderPage> skuList) {
        ResponsesWithMsg responses = new ResponsesWithMsg();
        List<SkuData> skuDataList = skuList.stream()
                .map(this::SkuOrderPageToSkuData)
                .collect(toList());
        List<SkuAddRequestBody> requestBodies = new ArrayList<>();
        for(SkuData skuData : skuDataList) {
            requestBodies.add(new SkuAddRequestBody(skuData));
        }
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);

        List<CompletableFuture<SkuAddResponse>> futures = requestBodies.stream()
                .map(requestBody -> CompletableFuture.supplyAsync(() -> {
                    try {
                        SkuAddRequest request = new SkuAddRequest(requestBody);
                        return request.send();
                    } catch (Exception e) {
                        log.error("Error publishing sku {} to mabang : {}", requestBody.getStockSku(), e.getMessage());
                        return new SkuAddResponse(Response.Code.ERROR, null, null, requestBody.getStockSku(), e.getMessage());
                    }
                }, executor))
                .collect(toList());
        List<SkuAddResponse> results = futures.stream().map(CompletableFuture::join).collect(toList());
        long successCount = results.stream().filter(SkuAddResponse::success).count();
        log.info("{}/{} skus published successfully.", successCount, skuDataList.size());
        results.forEach(response -> {
            if(response.success()) {
                responses.addSuccess(response.getStockSku());
            } else {
                responses.addFailure(response.getStockSku(), response.getMessage());
            }
        });
        return responses;
    }

    /**
     * Upsert skus to Mabang, max 50 skus
     * @param erpCodes
     * @return
     */
    @Transactional
    @Override
    public Map<Sku, String> skuSyncUpsert(List<String> erpCodes) {
        Map<Sku, String> newSkusNeedTreatmentMap = new HashMap<>();
        List<SkuData> skusFromMabang = new ArrayList<>();
        List<List<String>> skusPartition = Lists.partition(erpCodes, 50);
        for(List<String> skuPartition : skusPartition) {
            SkuListRequestBody body = new SkuListRequestBody();
            body.setStockSkuList(String.join(",", skuPartition));
            SkuListRawStream rawStream = new SkuListRawStream(body);
            SkuListStream stream = new SkuListStream(rawStream);
            List<SkuData> partialSkusFromMabang = stream.all();
            if(!partialSkusFromMabang.isEmpty()) {
                skusFromMabang.addAll(partialSkusFromMabang);
            }
        }

        if (!skusFromMabang.isEmpty()) {
            // we save the skuDatas in DB
            // and store skus that need manual treatment
            Map<Sku, String> newSkusMap = new HashMap<>(saveSkuFromMabang(skusFromMabang));
            newSkusNeedTreatmentMap = new HashMap<>(newSkusMap);
            Map<Sku, String> finalNewSkusNeedTreatmentMap = newSkusNeedTreatmentMap;
            newSkusMap.forEach((k, v) -> {
                if (v.isEmpty()) {
                    finalNewSkusNeedTreatmentMap.remove(k);
                }
            });
            // mongo sync after transaction
            for(Sku sku : newSkusMap.keySet()) {
                try {
                    skuMongoService.migrateOneSku(sku);
                } catch (Exception e) {
                    log.error("Error while migrating skuId: {}", sku.getId());
                    log.error(e.getMessage());
                }
            }
        }
        return newSkusNeedTreatmentMap;
    }

    public SkuData SkuOrderPageToSkuData(SkuOrderPage skuOrderPage) {
        SensitiveAttribute sensitiveAttribute = sensitiveAttributeService.getById(skuOrderPage.getSensitiveAttribute());
        if(sensitiveAttribute == null) {
            sensitiveAttribute = sensitiveAttributeService.getByZhName(skuOrderPage.getSensitiveAttribute());
        }
        SkuData skuData = new SkuData();
        if(skuOrderPage.getId() != null)
            skuData.setVirtualSkus(skuOrderPage.getId());
        skuData.setErpCode(skuOrderPage.getErpCode());
        skuData.setNameCN(skuOrderPage.getZhName());
        skuData.setNameEN(skuOrderPage.getEnName());
        skuData.setDeclareNameZh(skuOrderPage.getDeclareName());
        skuData.setDeclareNameEn(skuOrderPage.getDeclareEname());
        skuData.setSalePrice(skuOrderPage.getSkuPrice());
        skuData.setDeclareValue(skuOrderPage.getDeclaredValue());
        skuData.setWarehouseName(DEFAULT_WAREHOUSE_NAME);
        List<Label> labels = new ArrayList<>();
        for(String labelName : skuOrderPage.getLabelData().split(",")) {
            Label label = new Label();
            label.setName(labelName);
            labels.add(label);
        }
        skuData.setLabelData(labels.toArray(new Label[0]));
        if(skuOrderPage.getWeight() != null)
            skuData.setSaleRemark(skuOrderPage.getWeight().toString());
        skuData.setHasBattery(sensitiveAttribute.getHasBattery());
        skuData.setMagnetic(sensitiveAttribute.getMagnetic());
        skuData.setPowder(sensitiveAttribute.getPowder());
        skuData.setIsPaste(sensitiveAttribute.getIsPaste());
        skuData.setNoLiquidCosmetic(sensitiveAttribute.getNoLiquidCosmetic());
        skuData.setIsFlammable(sensitiveAttribute.getIsFlammable());
        skuData.setIsKnife(sensitiveAttribute.getIsKnife());
        skuData.setIsGift(skuOrderPage.getIsGift());
        skuData.setSupplier(skuOrderPage.getSupplier());
        skuData.setSupplierLink(skuOrderPage.getSupplierLink());
        skuData.setSalePicture(skuOrderPage.getImageSource());
        return skuData;
    }

    public SkuOrderPage SkuDataToSkuOrderPage(SkuData skuData) {
        SkuOrderPage sop = new SkuOrderPage();
        SensitiveAttribute sensitiveAttribute = new SensitiveAttribute();
        sensitiveAttribute.setHasBattery(skuData.getHasBattery());
        sensitiveAttribute.setPowder(skuData.getPowder());
        sensitiveAttribute.setIsFlammable(skuData.getIsFlammable());
        sensitiveAttribute.setIsKnife(skuData.getIsKnife());
        sensitiveAttribute.setIsPaste(skuData.getIsPaste());
        sensitiveAttribute.setMagnetic(skuData.getMagnetic());
        sensitiveAttribute.setNoLiquidCosmetic(skuData.getNoLiquidCosmetic());
        String sensitiveAttributeName = sensitiveAttributeService.getNameByAttributes(sensitiveAttribute);
        sop.setSensitiveAttribute(sensitiveAttributeName);
        sop.setId(String.valueOf(skuData.getStockSkuId()));
        sop.setErpCode(skuData.getErpCode());
        sop.setZhName(skuData.getNameCN());
        sop.setEnName(skuData.getNameEN());
        sop.setDeclaredValue(skuData.getDeclareValue());
        sop.setSkuPrice(skuData.getSalePrice());
        if(skuData.getWarehouse() != null)
            sop.setWarehouse(skuData.getWarehouse()[0].getWarehouseName());
        if(skuData.getLabelData() != null) {
            StringBuilder labelDataString = new StringBuilder();
            for(Label labelData : skuData.getLabelData()) {
                labelDataString.append(labelData.getName()).append(",");
            }
            sop.setLabelData(labelDataString.toString());
        }
        sop.setImageSource(skuData.getSalePicture());
        sop.setIsGift(skuData.getIsGift());
        sop.setSupplier(skuData.getSupplier());
        sop.setSupplierLink(skuData.getSupplierLink());
        sop.setStatus(skuData.getStatusValue());
        return sop;
    }

    /**
     * Call a routine to replace SKU codes (from MabangAPI)
     * by SKU IDs in platform_order_content table after creating new SKUs
     */
    @Override
    public void updateSkuId() {
        OnlCgformFieldMapper onlCgformFieldMapper = SpringContextUtils.getBean(OnlCgformFieldMapper.class);
        Map<String, Object> params = new HashMap<>();
        String sql = "UPDATE platform_order_content SET sku_id = skuErpToId(sku_id) WHERE sku_id NOT LIKE '1%'";
        params.put("execute_sql_string", sql);
        onlCgformFieldMapper.executeUpdatetSQL(params);
    }
    @Override
    public void mabangSkuStockUpdate(List<String> skuList) {
        StringBuilder skus = new StringBuilder();
        List<SkuStockData> updateList = new ArrayList<>();
        List<Sku> skuToUpdate = new ArrayList<>();
        int count = 1;
        for(int i = 1; i <= skuList.size(); i++) {
            if(i%100 != 1)
                skus.append(",");
            skus.append(skuList.get(i - 1));
            if(i%100 == 0) {
                SkuStockRequestBody body = (new SkuStockRequestBody())
                        .setStockSkus(skus.toString())
                        .setTotal(skuList.size());
                log.info("Sending request for page {}/{}.", count++, body.getTotalPages());

                SkuStockRawStream rawStream = new SkuStockRawStream(body);
                SkuStockStream stream = new SkuStockStream(rawStream);
                updateList.addAll(stream.all());
                skus = new StringBuilder();
            }
        }
        if(skus.length() != 0) {
            SkuStockRequestBody body = (new SkuStockRequestBody())
                    .setStockSkus(skus.toString())
                    .setTotal(skuList.size());
            SkuStockRawStream rawStream = new SkuStockRawStream(body);
            SkuStockStream stream = new SkuStockStream(rawStream);
            updateList.addAll(stream.all());
        }
        updateList.forEach(skuStockData -> {
            Sku sku = skuService.getByErpCode(skuStockData.getStockSku());
            Integer availableAmount = skuStockData.getWarehouseStock("SZBA宝安仓").getStockQuantity();
            Integer purchasingAmount = skuStockData.getWarehouseStock("SZBA宝安仓").getShippingQuantity();
            if(sku.getAvailableAmount().equals(availableAmount) && sku.getPurchasingAmount().equals(purchasingAmount)) {
                return;
            }
            sku.setAvailableAmount(availableAmount);
            sku.setPurchasingAmount(purchasingAmount);
            sku.setUpdateBy("mabang api");
            sku.setUpdateTime(new Date());
            skuToUpdate.add(sku);
        });
        if(skuToUpdate.isEmpty()) {
            return;
        }
        log.info("Updating stock for {} skus.", skuToUpdate.size());
        skuService.updateBatchStockByIds(skuToUpdate);
        for(Sku sku : skuToUpdate) {
            skuMongoService.updateStock(sku);
        }
    }

    @Override
    public ResponsesWithMsg mabangSkuWeightUpdate(List<SkuWeight> skuWeights) {
        ResponsesWithMsg responses = new ResponsesWithMsg();
        List<String> skuIds = skuWeights.stream()
                .map(SkuWeight::getSkuId)
                .collect(toList());
        List<Sku> skus = skuService.listByIds(skuIds);
        // Map skuWeights by skuId, if skuId already in map, keep the skuWeight with the latest effective date
        Map<String, SkuWeight> skuWeightMappedById = skuWeights.stream()
                .collect(Collectors.toMap(SkuWeight::getSkuId, Function.identity(), (skuWeight1, skuWeight2) -> {
                    if(skuWeight1.getEffectiveDate().after(skuWeight2.getEffectiveDate())) {
                        log.info("The weight [{}] for sku [{}] is more recent.", skuWeight1.getWeight() ,skuWeight1.getSkuId());
                        return skuWeight1;
                    }
                    log.info("The weight [{}] for sku [{}] is more recent.", skuWeight2.getWeight() ,skuWeight2.getSkuId());
                    return skuWeight2;
                }));
        List<Sku> skusToUpdate = new ArrayList<>();
        for(Sku sku : skus) {
            SkuWeight latestSkuWeight = skuWeightService.getBySkuId(sku.getId());
            if(latestSkuWeight == null) {
                skusToUpdate.add(sku);
                continue;
            }
            Date latestEffectiveDate = latestSkuWeight.getEffectiveDate();
            Date effectiveDateToImport = skuWeightMappedById.get(sku.getId()).getEffectiveDate();
            if(latestEffectiveDate.after(effectiveDateToImport)) {
                log.info("Sku {} has a more recent weight in DB. Therefore won't be imported to Mabang", sku.getErpCode());
                responses.addSuccess(sku.getErpCode());
                continue;
            }
            skusToUpdate.add(sku);
        }
        Map<String, String> remarkMappedByErpCode = new HashMap<>();
        List<List<String>> skusPartition = Lists.partition(skusToUpdate.stream().map(Sku::getErpCode).collect(toList()), 50);
        for(List<String> skuPartition : skusPartition) {
            SkuListRequestBody body = new SkuListRequestBody();
            body.setStockSkuList(String.join(",", skuPartition));
            SkuListRawStream rawStream = new SkuListRawStream(body);
            SkuUpdateListStream stream = new SkuUpdateListStream(rawStream);
            List<SkuData> skusFromMabang = stream.all();
            log.info("{} skus to be updated.", skusFromMabang.size());
            if (skusFromMabang.isEmpty()) {
                continue;
            }
            skusFromMabang.stream()
                    .filter(skuData -> skuData.getSaleRemark() != null)
                    .forEach(skuData -> {
                        String erpCode = skuData.getErpCode();
                        String remark = skuData.getSaleRemark();
                        remarkMappedByErpCode.put(erpCode, remark);
                    });
        }

        List<SkuData> skuDataList = skuWeights.stream()
                .map(skuWeight -> {
                    Sku sku = skusToUpdate.stream()
                            .filter(s -> s.getId().equals(skuWeight.getSkuId()))
                            .findFirst()
                            .orElse(null);
                    Sku skuInDb = skus.stream()
                            .filter(s -> s.getId().equals(skuWeight.getSkuId()))
                            .findFirst()
                            .orElse(null);
                    if(null == skuInDb) {
                        log.error("Sku not found : {}", skuWeight.getSkuId());
                        responses.addFailure(skuWeight.getSkuId(), "Sku not found");
                        return null;
                    }
                    if(null == sku) {
                        return null;
                    }
                    SkuData skuData = new SkuData();
                    skuData.setErpCode(sku.getErpCode());
                    if(remarkMappedByErpCode.containsKey(sku.getErpCode())) {
                        StringBuilder remark = new StringBuilder();
                        remark.append(skuWeight.getWeight());
                        Matcher saleRemarkMatcher = saleRemarkPattern.matcher(remarkMappedByErpCode.get(sku.getErpCode()));
                        if(saleRemarkMatcher.matches() && !saleRemarkMatcher.group(2).isEmpty()) {
                            log.info("Sku {} has remark from Mabang : {}", sku.getErpCode(), saleRemarkMatcher.group(2));
                            String saleRemarkNotWeight = saleRemarkMatcher.group(2);
                            remark.append(saleRemarkNotWeight);
                        }
                        skuData.setSaleRemark(remark.toString());
                    }
                    else {
                        skuData.setSaleRemark(String.valueOf(skuWeight.getWeight()));
                    }
                    skuData.setWeight(skuWeight.getWeight());
                    return skuData;
                })
                .filter(Objects::nonNull)
                .collect(toList());

        ExecutorService executor = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS, MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);
        List<CompletableFuture<SkuChangeResponse>> futures = skuDataList.stream()
                .map(skuData -> CompletableFuture.supplyAsync(() -> {
                    try {
                        SkuChangeRequestBody body = new SkuChangeRequestBody(skuData);
                        SkuChangeRequest request = new SkuChangeRequest(body);
                        return request.send();
                    } catch (Exception e) {
                        log.error("Error updating weight for sku {} : {}", skuData.getErpCode(), e.getMessage());
                        return new SkuChangeResponse(Response.Code.ERROR, null, null, skuData.getErpCode());
                    }
                }, executor))
                .collect(toList());
        List<SkuChangeResponse> results = futures.stream().map(CompletableFuture::join).collect(toList());
        long successCount = results.stream().filter(SkuChangeResponse::success).count();
        log.info("{}/{} skus updated successfully.", successCount, skuDataList.size());
        results.forEach(response -> {
            if(response.success()) {
                responses.addSuccess(response.getStockSku(), "Mabang");
            }
            else {
                responses.addFailure(response.getStockSku(), "Mabang");
            }
        });
        return responses;
    }

    @Override
    public List<SkuData> fetchUnpairedSkus(List<String> stockSkuList) {
        List<List<String>> skusPartition = Lists.partition(new ArrayList<>(stockSkuList), 50);
        List<SkuData> skuDataList = new ArrayList<>();
        for(List<String> skuPartition : skusPartition) {
            SkuListRequestBody body = new SkuListRequestBody();
            body.setShowWarehouse(1);
            body.setShowLabel(1);
            body.setStockSkuList(String.join(",", skuPartition));
            SkuListRawStream rawStream = new SkuListRawStream(body);
            UnpairedSkuListStream stream = new UnpairedSkuListStream(rawStream);
            skuDataList.addAll(stream.all());
        }
        return skuDataList;
    }

    @Override
    public List<SkuOrderPage> unpairedSkus(String shopId, Integer pageNo, Integer pageSize, String column, String order) {
        int offset = (pageNo - 1) * pageSize;
        List<String> stockSkuList = skuService.fetchUnpairedSkus(shopId, offset, pageSize, column, order);
        if(stockSkuList.isEmpty()){
            return new ArrayList<>();
        }
        List<SkuData> skuDataList = fetchUnpairedSkus(stockSkuList);
        return skuDataList.stream()
                .map(this::SkuDataToSkuOrderPage)
                .collect(toList());
    }

    @Override
    public int countUnpairedSkus(String shopId) {
        return skuService.countUnpairedSkus(shopId);
    }
}
