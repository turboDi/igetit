<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
</head>
<body>

<table cellspacing="0" cellpadding="0" id="ecxemail_table" style="border-collapse:collapse;width:98%;" border="0">
    <tbody>
    <tr>
        <td id="ecxemail_content" style="font-family:'lucida grande',tahoma,verdana,arial,sans-serif;font-size:12px;padding:0;background:#ffffff;">
            <table cellspacing="0" cellpadding="0" width="100%" border="0" style="border-collapse:collapse;width:100%;">
                <tbody>
                <tr>
                    <td style="font-size:11px;font-family:LucidaGrande,tahoma,verdana,arial,sans-serif;padding:0;background-color:#fff;border:none;">
                        <table cellspacing="0" cellpadding="0" width="100%" style="border-collapse:collapse;">
                            <tbody>
                            <tr>
                                <td style="padding:0;width:100%;">
                                    <table cellspacing="0" cellpadding="0" width="100%" bgcolor="#fff" style="border-collapse:collapse;width:100%;">
                                        <tbody>
                                        <tr>
                                            <td style="">
                                                <center>
                                                    <table cellspacing="0" cellpadding="0" style="border-collapse:collapse;">
                                                        <tbody>
                                                        <tr>
                                                            <td align="left" id="ecxheader_title" style="width:100%;line-height:0;">
                                                                <center>
                                                                    <table cellspacing="0" cellpadding="0" style="border-collapse:collapse;">
                                                                        <tbody>
                                                                        <tr>
                                                                            <td style="">
                                                                                <img src="${siteCfg.url}/img/logo.png" width="128" height="128" style="border:0;">
                                                                            </td>
                                                                        </tr>
                                                                        </tbody>
                                                                    </table>
                                                                </center>
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </center>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:0;width:100%;">
                                    <table cellspacing="0" cellpadding="0" width="100%" bgcolor="#ffffff" id="ecxtable_color" style="border-collapse:collapse;">
                                        <tbody>
                                        <tr>
                                            <td style="">
                                                <table cellspacing="0" cellpadding="0" width="100%" id="ecxemail_filler" style="border-collapse:collapse;">
                                                    <tbody>
                                                    <tr>
                                                        <td height="19" style="">&nbsp;</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                <center>
                                                    <table cellspacing="0" cellpadding="0" width="480px" style="border-collapse:collapse;max-width:480px;width:auto;">
                                                        <tbody>
                                                        <tr>
                                                            <td align="left" id="ecxbody_container" style="background-color:#ffffff;display:block;border:none;">
                                                                <table cellspacing="0" cellpadding="0" width="100%" style="border-collapse:collapse;">
                                                                    <tbody>
                                                                    <tr>
                                                                        <td style="">
                                                                            <table cellspacing="0" cellpadding="0" width="100%" style="border-collapse:collapse;">
                                                                                <tbody>
                                                                                <tr>
                                                                                    <td height="24px" id="ecxwelcome_header_wrapper" style="padding:20px 24px 0 24px;">
                                                                                        <span style="font-size:22px;line-height:32px;font-weight:500;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;" id="ecxwelcome_header">${message(code: 'email.verify.welcome.text', locale: locale)}, <span id="ecxheader_username" style="color:#681B74;text-decoration:none;font-size:22px;line-height:32px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;"> ${p.fullName}</span>!</span>
                                                                                    </td>
                                                                                </tr>
                                                                                </tbody>
                                                                            </table>
                                                                            <table cellspacing="0" cellpadding="0" width="100%" style="border-collapse:collapse;">
                                                                                <tbody>
                                                                                <tr>
                                                                                    <td height="20px" id="ecxwelcome_body_wrapper" style="padding:16px 24px 0 24px;">
                                                                                        <span style="font-size:16px;line-height:20px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;" id="ecxwelcome_body">${message(code: 'email.verify.confirm.text', locale: locale)}</span>
                                                                                    </td>
                                                                                </tr>
                                                                                </tbody>
                                                                            </table>
                                                                            <table cellspacing="0" cellpadding="0" width="100%" style="border-collapse:collapse;">
                                                                                <tbody>
                                                                                <tr>
                                                                                    <td height="17px" id="ecxwelcome_cta_wrapper" style="padding:30px 0 38px 0;">
                                                                                        <center>
                                                                                            <a id="ecxwelcome_cta" style="color:#681B74;text-decoration:none;border-radius:3px;border:1px solid #681B74;padding:10px 19px 12px 19px;font-size:17px;font-weight:500;white-space:nowrap;border-collapse:collapse;display:inline-block;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;" href="${createLink(absolute:'true', uri: "/account/verification?key=$key")}" target="_blank">${message(code: 'email.verify.confirm.button.text', locale: locale)}</a>
                                                                                        </center>
                                                                                    </td>
                                                                                </tr>
                                                                                </tbody>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </center>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:0;width:100%;">
                                    <table cellspacing="0" cellpadding="0" width="100%" style="border-collapse:collapse;width:100%;">
                                        <tbody>
                                        <tr>
                                            <td style="">
                                                <center>
                                                    <table cellspacing="0" cellpadding="0" width="100%" style="border-collapse:collapse;">
                                                        <tbody>
                                                        <tr>
                                                            <td style="">
                                                                <table cellspacing="0" cellpadding="0" width="100%" id="ecxinstagram_footer" border="0" style="border-collapse:collapse;">
                                                                    <tbody>
                                                                    <tr>
                                                                        <td style="font-size:12px;font-family:Helvetica Neue,Helvetica,Lucida Grande,tahoma,verdana,arial,sans-serif;padding:24px 0 24px 0;border-left:none;border-right:none;border-top:solid 1px #c9cbcd;border-bottom:none;color:#888888;font-weight:300;line-height:20px;text-align:center;">${message(code: 'email.contact.us.text', locale: locale)} <a href="mailto:${siteCfg.email}" style="color:#888888;text-decoration:underline;font-family:Helvetica Neue,Helvetica,Lucida Grande,tahoma,verdana,arial,sans-serif;font-weight:bold;" target="_blank">${message(code: 'email.contact.us.link.text', locale: locale)}</a>.</td>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </center>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>

</body>
</html>