<#include "../components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher(e) ${firstname} ${lastname},</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Vous recevez cet e-mail car votre solde sur la plateforme de WIA App est actuellement de <b>${balance} ${currency}</b></td>
    </tr>
    <#if clientCategory != "vip">
        <tr>
            <td style="padding:0 0 35px 0;">Pour continuer à utiliser nos services, nous vous invitons à recharger votre compte.</td>
        </tr>
    </#if>
    <tr>
        <td style="padding:0 0 35px 0;">Pour toute information complémentaire nous vous invitons à vous rapprocher de votre conseiller.</td>
    </tr>
<#include "../components/footer.ftl">