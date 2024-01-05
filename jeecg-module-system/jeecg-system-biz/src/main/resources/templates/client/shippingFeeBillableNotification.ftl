<#include "../components/header.ftl">
<tr>
    <td style="padding:35px 0;">Cher(e) ${fullName},</td>
</tr>
<tr>
    <td style="padding:0 0 20px 0;">Vous pouvez dès à présent facturer les frais de livraisons pour <span style="color:#0B49A6;font-weight:bold">${orderNumbers?size}</span> commande<#if orderNumbers?size &gt; 1>s</#if> :</td>
</tr>
<tr>
    <td>
        <ul>
            <#list orderNumbers as orderNumber>
                <li>${orderNumber}</li>
            </#list>
        </ul>
    </td>
</tr>
<tr>
    <td style="padding:0 0 35px 0;">Pour toute information complémentaire nous vous invitons à vous rapprocher de votre conseiller.</td>
</tr>
<#include "../components/footer.ftl">