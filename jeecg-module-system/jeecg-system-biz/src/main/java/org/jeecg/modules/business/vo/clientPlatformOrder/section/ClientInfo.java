package org.jeecg.modules.business.vo.clientPlatformOrder.section;

import lombok.Data;
import org.jeecg.modules.business.entity.Client;

/**
 * Client information section in the order detail page when confirming orders
 */
@Data
public class ClientInfo {
    /**
     * 姓
     */
    private final String surname;
    /**
     * 名
     */
    private final String firstName;
    /**
     * 发票实体
     */
    private final String invoiceEntity;
    /**
     * 邮箱
     */
    private final String email;
    /**
     * 电话
     */
    private final String phone;
    /**
     * 门牌号码
     */
    private final String streetNumber;
    /**
     * 街道名
     */
    private final String streetName;
    /**
     * 邮编
     */
    private final String postcode;
    /**
     * 城市
     */
    private final String city;
    /**
     * 国家
     */
    private final String country;
    /**
     * 公司识别码类型
     */
    private final String companyIdType;
    /**
     * 公司识别码数值
     */
    private final String companyIdValue;

    public ClientInfo(String surname, String firstName, String invoiceEntity, String email, String phone,
                      String streetNumber, String streetName, String postcode, String city, String country,
                      String companyIdType, String companyIdValue) {
        this.surname = surname;
        this.firstName = firstName;
        this.invoiceEntity = invoiceEntity;
        this.email = email;
        this.phone = phone;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.companyIdType = companyIdType;
        this.companyIdValue = companyIdValue;
    }

    public ClientInfo(Client c) {
        this(c.getSurname(), c.getFirstName(), c.getInvoiceEntity(), c.getEmail(),
                c.getPhone(), c.getStreetNumber(),
                c.getStreetName(), c.getPostcode(), c.getCity(),
                c.getCountry(), c.getCompanyIdType(), c.getCompanyIdValue());
    }
}
