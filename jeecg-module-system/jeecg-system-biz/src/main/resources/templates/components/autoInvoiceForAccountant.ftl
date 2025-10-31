<#include "header.ftl">

<h3>自动开票汇总通知</h3>
<p>客户代码：${clientCode}</p>
<p>客户名称：${clientName}</p>
<p>
    <strong>当前余额:</strong>
    <#if currentBalance?number < 0>
        <span style="color:red; font-weight:bold;">
            ${currentBalance?string["#,##0.00"]} ${currency}
        </span>
    <#else>
        ${currentBalance?string["#,##0.00"]} ${currency}
    </#if>
</p>
<table style="border-collapse: collapse; width: 100%;">
    <thead>
    <tr style="background-color:#f7f7f7;">
        <th style="border:1px solid #bbb;padding:8px;">发票号</th>
        <th style="border:1px solid #bbb;padding:8px;">店铺</th>
        <th style="border:1px solid #bbb;padding:8px;">金额 (${currency})</th>
        <th style="border:1px solid #bbb;padding:8px;">状态</th>
    </tr>
    </thead>
    <tbody>
    <#list invoices as inv>
        <tr>
            <td style="border:1px solid #bbb;padding:8px;">${inv.invoiceCode}</td>
            <td style="border:1px solid #bbb;padding:8px;">${inv.shopName}</td>
            <td style="border:1px solid #bbb;padding:8px;">${inv.amount}</td>
            <td style="border:1px solid #bbb;padding:8px;">
                <#if inv.approved>
                    审核通过
                <#else>
                    <span style="color:red; font-weight:bold;">待补款</span>
                </#if>
            </td>
        </tr>
    </#list>
    </tbody>
</table>

<p>请登录系统后台审核采购：<br>
    <a href="${reviewLink}" target="_blank">${reviewLink}</a></p>

<#include "footer.ftl">
