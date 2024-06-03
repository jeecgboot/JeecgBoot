<#include "components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher Client(e),</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Vous trouverez en pièce-jointe le fichier que vous nous avez demandé :</td>
    </tr>
    <tr>
        <td style="padding:10px 0;"><b>Type de fichier :</b> ${fileType}</td>
    </tr>
    <tr>
        <td style="padding:10px 0;"><b>Client :</b> ${invoiceEntity}</td>
    </tr>
    <tr>
        <td style="padding:10px 0;"><b>Numéro de facture :</b> <a href="http://app.wia-sourcing.com/business/admin/invoice/Invoice?invoice=${invoiceNumber}"> ${invoiceNumber} </a></td>
    </tr>
<#include "components/footer.ftl">