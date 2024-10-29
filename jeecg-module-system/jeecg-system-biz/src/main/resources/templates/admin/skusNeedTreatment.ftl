<#include "../components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher Collègue</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Un ou plusieurs SKU ont été <b>${operation}</b> et nécessitent une action de votre part :</td>
    </tr>
    <tr>
        <td>
            <ul>
                <#list skusMap as key, value>
                    <li>${key.erpCode} : ${value}</li>
                </#list>
            </ul>
        </td>
    </tr>
<#include "../components/footer.ftl">