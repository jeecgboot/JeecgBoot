package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.SkuData;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.SkuListRequestErrorException;
import org.jeecg.modules.business.entity.Product;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.mapper.SkuListMabangMapper;
import org.jeecg.modules.business.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final SkuListMabangMapper skuListMabangMapper;
    private final IProductService productService;
    private final ISkuService skuService;
    private final ISkuPriceService skuPriceService;
    private final ISkuDeclaredValueService skuDeclaredValueService;

    @Autowired
    public SkuListMabangServiceImpl(SkuListMabangMapper skuListMabangMapper, IProductService productService,
                                    ISkuService skuService, ISkuPriceService skuPriceService, ISkuDeclaredValueService skuDeclaredValueService) {
        this.skuListMabangMapper = skuListMabangMapper;
        this.productService = productService;
        this.skuService = skuService;
        this.skuPriceService = skuPriceService;
        this.skuDeclaredValueService = skuDeclaredValueService;
    }

    /**
     * Save skus to DB from mabang api.
     *
     * @param skuDataList skus to save.
     */
    @Override
    @Transactional
    public Map<Sku, String> saveSkuFromMabang(List<SkuData> skuDataList) {
        List<Sku> newSkus;
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
            if (newSkuDatas.size() != 0) {
                // we need to check if the product associated with the sku exists, for that we are going to parse the Sku erpCode into product code
                // check if the product code exists in DB, if not we create a new entry in DB and fill all the infos.
                // then we can finally add the new Sku, product has to be created first if it doesn't exist, since we need to fill productID in Sku table
                // we can now proceed to create new sku_declare_value associated with the new Sku and also sku_price
                Map<String, String> productNeedTreatment = saveProductFromMabang(newSkuDatas);
                int nbNewProducts = productNeedTreatment.size();
                log.info("{} products need manual treatment.", nbNewProducts);
                log.info("{} skus to be inserted.", newSkuDatas.size());

                //create a new sku for each sku data
                newSkus = createSkus(newSkuDatas);
                skuService.saveBatch(newSkus);

                // adding the additional information to List and pairing it to a sku with the associated product code
                List<String> productIdInMap = new ArrayList<>();
                for(Sku sku : newSkus) {
                    // checking if the sku parsed into productCode exists in the map and if information about the productCode has already been added
                    if(productNeedTreatment.containsKey(parseSkuToProduct(sku.getErpCode())) && !productIdInMap.contains(sku.getProductId())) {
                        newSkusMap.put(sku, productNeedTreatment.get(sku.getErpCode()));
                        productIdInMap.add(sku.getProductId());
                    }
                    else {
                        newSkusMap.put(sku, "");
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

    /**
     * Save products to DB from mabang api.
     *
     * @param skuDataList we save the new product code of the new skus
     * @return return a map with products with extra info that need manual review
     */
    public Map<String, String> saveProductFromMabang(List<SkuData> skuDataList) {
        List<String> allProductCodes = parseSkuListToProductCodeList(skuDataList);
        // we create a map with product as key and as value it may contain further information about the product that needs manual information treatment
        Map<Product, String> allProductsMap = createProductMap(skuDataList);

        List< Product> existingProduct = skuListMabangMapper.searchProductExistence(allProductCodes);
        Map<String, Product> existingProductsIDMap = existingProduct.stream()
                .collect(
                        Collectors.toMap(
                                Product::getCode, Function.identity()
                        )
                );
        // List is used for inserting products in DB
        List<Product> newProducts = new ArrayList<>();
        Map<String, String> productsWithInfoMap = new HashMap<>();
        for (Map.Entry<Product, String> entry : allProductsMap.entrySet()) {
            Product productInDB = existingProductsIDMap.get(entry.getKey().getCode());
            // the current product code is not in DB, so we add it to the list of newProducts
            if (productInDB == null) {
                newProducts.add(entry.getKey());
                // checking if the product needs manual info treatment
                if(!entry.getValue().equals("")) {
                    productsWithInfoMap.put(entry.getKey().getCode(), entry.getValue());
                }
            }
        }
        if(newProducts.size() > 0) {
            productService.saveBatch(newProducts);
        }
        return productsWithInfoMap;
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
     * Create a map of product as key and a string that contains additional information about the product.
     * @param skuDataList
     * @return returns a map of product with additional info if available
     */
    public Map<Product, String> createProductMap(List<SkuData> skuDataList) {
        Map<Product, String> productMap = new HashMap<>();

        final String electroMagSensitiveAttributeId = skuListMabangMapper.searchSensitiveAttributeId("Electro-magnetic");
        final String electricSensitiveAttributeId = skuListMabangMapper.searchSensitiveAttributeId("Electronic/Electric");
        final String normalSensitiveAttributeId = skuListMabangMapper.searchSensitiveAttributeId("Normal goods");
        // In NameCN field on top of the product name we also get the customer code in the beginning of the string : "XX Description of the product"
        final Pattern cnNamePattern = Pattern.compile("^([a-zA-Z]{2,5})\\s(.*)$");
        // IN saleRemark sometimes not only the product weight provided, we can get extra information such as service_fee (eg : "15每件服务费0.2")
        final Pattern saleRemarkPattern = Pattern.compile("^([0-9]*)(.*)$");
        // we are stocking product codes, so we don't get duplicates which causes error
        List<String> productCode = new ArrayList<>();

        for(SkuData sku : skuDataList) {
            String code = parseSkuToProduct(sku.getErpCode());
            // checking for duplicated entries
            if(!productCode.contains(code))
            {
                productCode.add(code);
                Product p = new Product();

                p.setCode(code);
                p.setCreateBy("mabang api");
                // Removing the customer code from the product CN name
                if (!sku.getNameCN().equals("") && sku.getNameCN()!= null) {
                    Matcher cnNameMatcher = cnNamePattern.matcher(sku.getNameCN());
                    if (cnNameMatcher.matches() && !cnNameMatcher.group(2).equals("")) {
                        p.setZhName(cnNameMatcher.group(2));
                    }
                    else {
                        p.setZhName(sku.getNameCN());
                    }
                }
                p.setEnName(sku.getNameEN());
                // hasBattery : 1 = yes ; 2 = no
                if (sku.getHasBattery() == 1) {
                    // magnetic : 1 = yes ; 2 = no
                    if (sku.getMagnetic() == 1)
                        p.setSensitiveAttributeId(electroMagSensitiveAttributeId);
                    else
                        p.setSensitiveAttributeId(electricSensitiveAttributeId);
                } else {
                    // magnetic : 1 = yes ; 2 = no
                    if (sku.getMagnetic() == 1)
                        p.setSensitiveAttributeId(electroMagSensitiveAttributeId);
                    else
                        p.setSensitiveAttributeId(normalSensitiveAttributeId);
                }
                p.setInvoiceName(sku.getNameEN());
                if (sku.getSaleRemark() != null && !sku.getSaleRemark().equals("")) {
                    Matcher saleRemarkMatcher = saleRemarkPattern.matcher(sku.getSaleRemark());
                    if(saleRemarkMatcher.matches() && !saleRemarkMatcher.group(1).equals("")) {
                        String saleRemark = saleRemarkMatcher.group(1);
                        int weight = (int) ceil(Double.parseDouble(saleRemark));
                        p.setWeight(weight);
                        if(!saleRemarkMatcher.group(2).equals("")) {
                            productMap.put(p, saleRemarkMatcher.group(2));
                        }
                        else
                            productMap.put(p, "");
                    }

                } else {
                    productMap.put(p, "");
                }
            }
        }
        return productMap;
    }

    /**
     *  Creates sku objects from Skudatas
     * @param skuDataList
     * @return
     */
    public List<Sku> createSkus(List<SkuData> skuDataList) {
        List<Sku> skuList = new ArrayList<>();
        for(SkuData skuData : skuDataList) {
            Sku s = new Sku();
            String productId = skuListMabangMapper.searchProductId(parseSkuToProduct(skuData.getErpCode()));
            s.setProductId(productId);
            s.setErpCode(skuData.getErpCode());
            s.setCreateBy("mabang api");
            if(skuData.getStockPicture().equals("") || skuData.getStockPicture() == null) {
                s.setImageSource(skuData.getSalePicture());
            }
            else {
                s.setImageSource(skuData.getStockPicture());
            }
            skuList.add(s);
        }
        return skuList;
    }
}
