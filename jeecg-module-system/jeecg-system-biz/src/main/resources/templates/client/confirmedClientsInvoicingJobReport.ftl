<#include "../components/header.ftl">
<tr>
    <td style="padding:35px 0;">Cher(e) ${client.firstName} ${client.surname},</td>
</tr>
<tr>
    <td style="padding:0 0 35px 0;">Une ou plusieurs commandes n'ont pas pu être facturées en raison d'un solde insuffisant :</td>
</tr>
<tr>
    <#if errors?size = 0>
        <p>No error</p>
    <#elseif chronologicalOrder=="1">
        <p>Les commandes à partir du numéro ${errors[0].platformOrderNumber} - ${errors[0].orderTime?datetime?string('dd-MM-yyyy')} n'ont pas été facturées.</p>
    <#else>
        <table style="border: 1px solid #bbb; text-align: center;width: 100%">
            <thead>
            <tr>
                <th>Numéro de commande</th>
                <th>Date de commande</th>
            </tr>
            </thead>
            <tbody>
            <#list errors as error>
                <tr>
                    <td>${error.platformOrderNumber}</td>
                    <td>${error.orderTime?datetime?string('dd-MM-yyyy')}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>
    <#if skipped??>
        <table style="border: 1px solid #bbb; text-align: center;width: 100%">
            <thead>
            <tr>
                <th>Numéro de commande</th>
                <th>Date de commande</th>
            </tr>
            </thead>
            <tbody>
            <#list skipped as error>
                <tr>
                    <td>${error.platformOrderNumber}</td>
                    <td>${error.orderTime?datetime?string('dd-MM-yyyy')}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>
</tr>
<tr>
    <td style="padding:0 0 35px 0;">Pour continuer à utiliser nos services, nous vous invitons à recharger votre compte.</td>
</tr>
<tr>
    <td style="padding:0 0 35px 0;">Pour toute information complémentaire nous vous invitons à vous rapprocher de votre conseiller.</td>
</tr>
<#include "../components/footer.ftl">