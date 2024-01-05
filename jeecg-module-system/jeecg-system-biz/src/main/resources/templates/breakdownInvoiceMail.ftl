<#include "components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher collègue,</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Vous trouverez en pièce-jointe une archive de l'ensemble des factures générées :</td>
    </tr>
    <tr>
        <td style="padding:10px 0;"><b>Erreurs :</b>
            <#if errors?size = 0>
                No error
            </#if>
        </td>
    </tr>
    <#list errors as error>
        <tr>
            <td style="padding:10px 0;">
                Error: ${error.invoiceEntity} - ${error.errorMsg}
            </td>
        </tr>
    </#list>
<#include "components/footer.ftl">