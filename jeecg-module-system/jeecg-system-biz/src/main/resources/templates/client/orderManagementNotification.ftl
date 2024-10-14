<#include "../components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher(e) ${firstname} ${lastname},</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Voici un récapitulatif des opérations que vous avez effectuées sur vos commandes :</td>
    </tr>
    <#if cancelSuccessCount??>
        <tr>
            <td style="padding:0 0 35px 0;">Demandes d'annulations de commande : <b>${cancelSuccessCount}</b> réussie(s) :
                <ul>
                    <#list cancelSuccesses as success>
                        <li>${success}</li>
                    </#list>
                </ul>
            </td>
        </tr>
        <#if cancelFailures?size gt 0 >
            <tr>
                <td style="padding:0 0 35px 0;">Demandes d'annulations de commande échouées :<br/>
                    <ul>
                        <#list cancelFailures as failure>
                            <li>${failure}</li>
                        </#list>
                    </ul>
                </td>
            </tr>
        </#if>
    </#if>
    <#if suspendSuccessCount??>
        <tr>
            <td style="padding:0 0 35px 0;">Demandes de suspension de commande : <b>${suspendSuccessCount}</b> réussie(s) :
                <ul>
                    <#list suspendSuccesses as success>
                        <li>${success}</li>
                    </#list>
                </ul>
            </td>
        </tr>
        <#if suspendFailures?size gt 0 >
            <tr>
                <td style="padding:0 0 35px 0;">Demandes de suspension de commande échouées :<br/>
                    <ul>
                        <#list suspendFailures as failure>
                            <li>${failure}</li>
                        </#list>
                    </ul>
                </td>
            </tr>
        </#if>
    </#if>
    <#if editSuccessCount??>
        <tr>
            <td style="padding:0 0 35px 0;">Demandes de modification d'informations de commande : <b>${editSuccessCount}</b> réussie(s) :
                <ul>
                    <#list editSuccesses as success>
                        <li>${success}</li>
                    </#list>
                </ul>
            </td>
        </tr>
        <#if editFailures?size gt 0 >
            <tr>
                <td style="padding:0 0 35px 0;">Demandes de modification de commande échouées :<br/>
                    <ul>
                        <#list editFailures as failure>
                            <li>${failure}</li>
                        </#list>
                    </ul>
                </td>
            </tr>
        </#if>
    </#if>
    <tr>
        <td style="padding:0 0 35px 0;">Pour toute information complémentaire nous vous invitons à vous rapprocher de votre conseiller.</td>
    </tr>
<#include "../components/footer.ftl">