<#include "../components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher Collègue</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Un client vient de créer une facture, merci de vérifier les informations liées à la facture :</td>
    </tr>
    <tr>
        <td style="padding:10px 0;"><b>Type de facture :</b> ${invoiceType}</td>
    </tr>
    <tr>
        <td style="padding:10px 0;"><b>Client :</b> ${invoiceEntity}</td>
    </tr>
    <tr>
        <td style="padding:10px 0;"><b>Numéro de facture :</b> <a href="http://app.wia-sourcing.com/business/admin/invoice/Invoice?invoice=${invoiceNumber}"> ${invoiceNumber} </a></td>
    </tr>
<#include "../components/footer.ftl">