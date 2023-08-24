<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
        <table align="center" cellpadding="0" cellspacing="0" width="600" bgcolor="#FFF" style="font-family:Arial,Helvetica,sans-serif;text-align:center;table-layout:fixed;font-size: 16px;border: 1px solid #0B49A6">
            <tbody>
                <tr>
                    <td width="600" height="90" bgcolor="#0B49A6" valign="top" align="center" style="padding:20px 0;table-layout:fixed">
                        <a href="http://app.wia-sourcing.com/user/login">
                            <img src="https://wia-sourcing.com/wp-content/uploads/2022/10/Fichier-24Icons.png" alt="logo" width="360" style="width:100%;max-width:360px;">
                        </a>
                   </td>
               </tr>
                <tr>
                    <td align="left">
                        <table width="520" align="center" style="color:#000;">
                            <tbody>
                                <tr>
                                    <td style="padding:35px 0;">Cher collègue,</td>
                                </tr>
                                <tr>
                                    <td style="padding:0 0 35px 0;">Vous trouverez en pièce-jointe une archive de l'ensemble des factures générées :</td>
                                </tr>
                                <tr>
                                    <td style="padding:10px 0;"><b>Erreurs :</b>
                                        <#if errors?size = 0>
                                            No error
                                        </#if>
                                    </td>
                                </tr>
                                <#list errors as error>
                                    <tr>
                                        <td style="padding:10px 0;">
                                            Error: ${error.invoiceEntity} - ${error.errorMsg}
                                        </td>
                                    </tr>
                                </#list>
                                <tr>
                                 <td style="padding:35px 0 5px 0;">Merci d’utiliser nos services.</td>
                                </tr>
                                <tr>
                                    <td style="padding:5px 0;">Cordialement</td>
                                </tr>
                                <tr>
                                    <td style="padding:5px 0 35px 0;">L’équipe WIA Sourcing.</td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                    </tr>
                    <tr>
                    <td align="left" bgcolor="#0B49A6"  width="600">
                        <table align="center" width="520">
                            <tbody>
                                <tr>
                                    <td style="font-style: italic;padding: 20px 0;">Ce message a été envoyé automatiquement. Merci de ne pas répondre. Ce message et ainsi que toutes les pièces jointes sont confidentielles.</td>
                                </tr>
                                <tr>
                                    <td style="padding: 0 0 20px 0;">Si vous avez reçu ce message par erreur, merci de nous avertir immédiatement et de détruire ce message.</td>
                                </tr>
                                <tr>
                                    <td>Service client :</td>
                                </tr>
                                <tr>
                                    <td>Pour obtenir plus d’informations concernant nos services, veuillez nous contacter à l’adresse ci-dessous ou en visitant notre site web.</td>
                                </tr>
                            </tbody>
                        </table>
                        <table align="center" width="520" style="padding: 15px 0">
                            <tbody>
                                <tr>
                                    <td width="220" style="text-align:center;border-radius:2em;padding:20px 10px 20px 0;;" bgcolor="#EF5A1A"><a href="https://wia-sourcing.com/contactez-nous" style="color:white;text-decoration:none">Nous contacter</a></td>
                                    <td width="40"  ></td>
                                    <td width="220" style="text-align:center;border-radius:2em;padding:20px 0 20px 10px;" bgcolor="#EF5A1A"><a href="https://wia-sourcing.com/" style="color:white;text-decoration:none">Notre site web</a></td>
                                </tr>
                            </tbody>
                        </table>
                        <table align="center" width="520" style="padding: 0 0 35px 0;">
                            <tbody>
                                <tr>
                                    <td style="color:#EF5A1A;">WIA SOURCING</td>
                                </tr>
                                <tr>
                                    <td>© 2018/2023 par WIA Sourcing Agency.</td>
                                </tr>
                                <tr>
                                    <td>TOUS DROITS RÉSERVÉS©</td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
    </body>
</html>