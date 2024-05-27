<html>
<head>
    <meta charset="UTF-8">
</head>
<body style="font-family: 'Verdana'; background-color: #e9e9eb;">
<br/>
<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
    <tr>
        <td align="center" bgcolor="#0770cd" style="padding: 20px 0 20px 0;">
            <h1 style="color: #ffffff">WORK ORDER</h1>
        </td>
    </tr>
    <tr>
        <td bgcolor="#ffffff" style="padding: 10px 20px 10px 20px;">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="color: #626262">
                <tr>
                    <td style="color: #0a0a0a; padding: 5px 0px 5px 0px;">
                        Dear <b>${name}</b>,
                        <br/>
                        <br/>
                        <b>${approver} (${approverEmail})</b> just finish approved Work Order <b>${spkDescription}</b> with details in below:
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" style="padding: 20px 0px 0px 0px;">
                            <tr>
                                <td width="30%" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">SBU</td>
                                <td align="right" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">${sbuCode} - ${sbuDesc}</td>
                            </tr>
                            <tr>
                                <td width="30%" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">SPK ID</td>
                                <td align="right" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">${spkId}</td>
                            </tr>
                            <tr>
                                <td width="30%" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">Quotation Date</td>
                                <td align="right" style="color: #0a0a0a;" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">${approvedQuotationDate}</td>
                            </tr>
                            <tr>
                                <td width="30%" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">Amount</td>
                                <td align="right" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">${amount}</td>
                            </tr>
                            <tr>
                                <td width="30%" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">Store</td>
                                <td align="right" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">${storeId} - ${storeName}</td>
                            </tr>
                            <tr>
                                <td width="30%" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">Vendor</td>
                                <td align="right" style="color: #0a0a0a; padding: 5px 0px 5px 0px;">${vendorId} - ${vendorName}</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td align="center" cellspacing="0" cellpadding="0" style="padding: 15px 0px 0px 0px;">
                        <table cellspacing="10" cellpadding="10" width="100%">
                            <tr>
                                <td align="center" height="30" bgcolor="#f39c12" style="-webkit-border-radius: 5px; -moz-border-radius: 5px; border-radius: 5px; color: #ffffff; display: block;">
                                    <a href="${detailLink}" style="font-size:15px; font-weight: bold; font-family: Helvetica, Arial, sans-serif; text-decoration: none; line-height:30px; width:100%; display:inline-block"><span style="color: #FFFFFF">VIEW MORE DETAILS</span></a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <hr style="display: block; height: 1px; border: 0; border-top: 1px solid #ccc; margin: 1em 0; padding: 0;"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td style="font-size: medium;">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" >
                <tr>
                    <td align="left" width="100%" bgcolor="#ffffff" style="padding: 10px 20px 10px 20px; color: #626262; font-size: 14px; letter-spacing: 1.5px; line-height: 17px;">
                        Caution: <br/>
                        *) If the reader of this message is not the intended recipient, you are hereby notified that any unauthorized disclosure, dissemination, distribution, copying or taking of any action in reliance on the information herein is strictly prohibited. <br/>
                        **) If you have received this communication in error, please immediately notify ITSD@map.co.id and delete this message.
                    </td>
                </tr>
                <tr bgcolor="#ffffff" style="padding: 20px 20px 20px 20px;">
                    <td style="padding: 0px 20px 0px 20px;">
                        <hr style="display: block; height: 1px; border: 0; border-top: 1px solid #ccc; margin: 1em 0; padding: 0;"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="100%" bgcolor="#ffffff" style="padding: 10px 20px 10px 20px; color: #626262; font-size: 14px; letter-spacing: 1.5px; line-height: 17px;">
                        Note: <br/>
                        *) Do not reply this mail.
                    </td>
                </tr>
                <tr bgcolor="#ffffff" >
                    <td style="padding: 0px 20px 0px 20px;">
                        <hr style="display: block; height: 1px; border: 0; border-top: 1px solid #ccc; margin: 1em 0; padding: 0;"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="100%" style="padding: 20px 20px 20px 20px; font-size: 14px;" bgcolor="#ffffff">
                        <b>PT. Mitra Adiperkasa, Tbk.</b>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>