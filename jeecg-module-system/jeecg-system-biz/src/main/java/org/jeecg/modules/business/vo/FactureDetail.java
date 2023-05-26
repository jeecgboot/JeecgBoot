package org.jeecg.modules.business.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

@Data
@TableName("detail_de_facture")
public class FactureDetail {
    @TableField("Boutique")
    private final String boutique;

    @TableField("`N° de Mabang`")
    private final String mabangNum;

    @TableField("`N° de commande`")
    private final String commandeNum;

    @TableField("`N° de suivi`")
    private final String suiviNum;

    @TableField("`Date de commande`")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final String commandeDate;

    @TableField("`Date d'expédition`")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final String expeditionDate;

    @TableField("`Nom de client`")
    private final String clientName;

    @TableField("`Pays`")
    private final String country;

    @TableField("`Code postal`")
    private final String postalCode;

    @TableField("`SKU`")
    private final String sku;

    @TableField("`Nom produits`")
    private final String productName;

    @TableField("`Quantité`")
    private final String quantity;

    @TableField("`Frais d'achat`")
    private final BigDecimal purchaseFee;

    @TableField("`Frais de FRET`")
    private final BigDecimal fretFee;

    @TableField("`Frais de livraison`")
    private final BigDecimal livraisonFee;

    @TableField("`Frais de service`")
    private final BigDecimal serviceFee;

    @TableField("`Frais de préparation`")
    private final BigDecimal pickingFee;

    @TableField("`Frais de matériel d'emballage`")
    private final BigDecimal packagingMaterialFee;

    @TableField("`TVA`")
    private final BigDecimal TVA;

    @TableField("`N° de facture`")
    private final String factureNum;
}
