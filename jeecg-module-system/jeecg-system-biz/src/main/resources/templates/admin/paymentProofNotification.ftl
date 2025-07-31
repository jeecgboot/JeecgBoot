<#include "../components/header.ftl">

<tr>
    <td style="padding:35px 0;">亲爱的同事：</td>
</tr>

<tr>
    <td style="padding:0 0 35px 0;">
        有客户上传了付款截图，请您前往WIA-APP查看并处理。
    </td>
</tr>

<tr>
    <table style="border: 1px solid #bbb; border-collapse: collapse; text-align: center; width: 100%;">
        <thead>
        <tr>
            <th style="border: 1px solid #bbb;">客户账号</th>
            <th style="border: 1px solid #bbb;">发票号</th>
            <th style="border: 1px solid #bbb;">上传时间</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td style="border: 1px solid #bbb;">${client}</td>
            <td style="border: 1px solid #bbb;">${invoiceNumber}</td>
            <td style="border: 1px solid #bbb;">${uploadTime}</td>
        </tr>
        </tbody>
    </table>
</tr>

<tr>
    <td style="padding:35px 0;">
        请点击下方链接前往审核页面 <br/>
        <a href="${reviewLink}" target="_blank">${reviewLink}</a>
    </td>
</tr>

<#include "../components/footer.ftl">
