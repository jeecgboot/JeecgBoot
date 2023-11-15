<#include "components/header.ftl">
    <tr>
        <td style="padding:35px 0;">Cher(s) collègue(s),</td>
    </tr>
    <tr>
        <td style="padding:0 0 35px 0;">Des erreurs se sont produites lors du déroulement de la tâche plannifiée suivante : <br/><b>${job} invoicing job</b></td>
    </tr>
    <tr>
        <td style="padding:10px 0;"><b>Erreurs :</b>
            <#if errors?size = 0>
                No error
            </#if>
        </td>
    </tr>
    <tr>
        <table style="border: 1px solid #bbb; border-collapse: collapse; text-align: center; width: 100%;">
            <thead>
            <tr>
                <th style="border: 1px solid #bbb;">Client</th>
                <th style="border: 1px solid #bbb;">Error Message</th>
            </tr>
            </thead>
            <tbody>
            <#list errors as error>
                <tr>
                    <td style="border: 1px solid #bbb;">${error.internalCode}</td>
                    <td style="border: 1px solid #bbb;">${error.errorMsg}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </tr>
<#include "components/footer.ftl">