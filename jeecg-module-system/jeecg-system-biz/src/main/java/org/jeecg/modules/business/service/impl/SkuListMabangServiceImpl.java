package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.SkuData;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.SkuListRequestErrorException;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.entity.SkuWeight;
import org.jeecg.modules.business.mapper.SkuListMabangMapper;
import org.jeecg.modules.business.service.*;
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
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    Environment env;

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
                skuService.saveBatch(newSkus);

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
        // return only skus that need manual treatment
        Map<Sku, String> skusNeedTreatmentMap = newSkusMap;
        newSkusMap.forEach((k, v) -> {
            if (v.isEmpty()) {
                skusNeedTreatmentMap.remove(k);
            }
        });
        return skusNeedTreatmentMap;
    }

    @Override
    @Transactional
    public Map<Sku, String> updateSkusFromMabang(List<SkuData> skuDataList) {
        Map<Sku, String> updateSkusNeedTreatmentMap = new HashMap<>();
        if (skuDataList.isEmpty()) {
            return updateSkusNeedTreatmentMap;
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
                    String remark = updateSkuWeight(s, skuData.getSaleRemark());
                    if(!remark.isEmpty())
                        updateSkusNeedTreatmentMap.put(s, remark);
                }
                if(!skusToUpdate.isEmpty())
                    skuService.updateBatchById(skusToUpdate);
                log.info("Updated {} skus : {}.", skusToUpdate.size(), existingSkuDatas.stream().map(SkuData::getErpCode).collect(toList()));
            }
        } catch (RuntimeException e) {
            log.error(e.getLocalizedMessage());
        }
        return updateSkusNeedTreatmentMap;
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
        sw.setEffective_date(new Date());
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
                oldSkuWeight.setWeight(weight);
                skuWeightService.updateById(oldSkuWeight);
                isUpdated = true;
            }
            if(!saleRemarkMatcher.group(2).isEmpty()) {
                if(isUpdated)
                    remark = saleRemarkMatcher.group(2);
            }
        }
        return remark;
    }
}
