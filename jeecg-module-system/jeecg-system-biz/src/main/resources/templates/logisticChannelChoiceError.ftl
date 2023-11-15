<#include "components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher(s) collègue(s),</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Des erreurs se sont produites lors de l'attribution d'une ligne de transport à une ou plusieurs commandes :</td>
    </tr>
    <tr>
        <td style="padding:10px 0;"><b>Erreurs :</b>
            <#if errors?size = 0>
                No error
            </#if>
        </td>
    </tr>
    <tr>
        <table style="border: 1px solid #bbb; text-align: center;">
            <thead>
                <tr>
                    <th>Shop</th>
                    <th>Order ID</th>
                    <th>Country</th>
                    <th>Sensitive Attribute</th>
                </tr>
            </thead>
            <tbody>
                <#list errors as error>
                    <tr>
                        <td>${error.shop}</td>
                        <td>${error.orderId}</td>
                        <td>${error.country}</td>
                        <td>${error.sensitiveAttribute}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </tr>
<#include "components/footer.ftl">