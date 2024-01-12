<#include "../components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher Collègue</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Un ou plusieurs SKU n'ont pas pu être attribué à un client :</td>
    </tr>
    <tr>
        <td>
            <ul>
                <#if skus?size = 0 >
                    <li>Aucune erreur</li>
                <#else>
                    <#list skus as sku>
                        <li>${sku}</li>
                    </#list>
                </#if>
            </ul>
        </td>
    </tr>
<#include "../components/footer.ftl">