<#include "../components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher Collègue</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Voici la liste des commandes dont le status a été mis à jour à "Normal" :</td>
    </tr>
    <tr>
        <td style="padding:10px 0;">Nombre de commandes :<b> ${count}</b><br/>
            <ul>
                <#list orderIds as orderId>
                    <li>${orderId}</li>
                </#list>
            </ul>
        </td>
    </tr>
<#include "../components/footer.ftl">