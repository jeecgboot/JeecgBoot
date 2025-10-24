<#include "../components/header.ftl">
<tr>
    <td style="padding:35px 0;">Cher(e) ${client.firstName} ${client.surname},</td>
</tr>
<tr>
    <td style="padding:0 0 35px 0;">Vous trouverez en pièce-jointe votre inventaire du ${.now?string('dd-MM-yyyy')}</td>
</tr>
<#include "../components/footer.ftl">