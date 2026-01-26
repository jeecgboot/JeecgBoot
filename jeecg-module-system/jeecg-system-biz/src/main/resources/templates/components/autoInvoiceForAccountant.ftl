<#include "header.ftl">

<h3>自动开票汇总通知</h3>
<p>客户代码：${clientCode}</p>
<p>客户名称：${clientName}</p>
<p>
    <strong>当前余额:</strong>
    <#if currentBalance?replace(",", ".")?number < 0>
        <span style="color:red; font-weight:bold;">
            ${currentBalance?string["#,##0.00"]} ${currency}
        </span>
    <#else>
        ${currentBalance?string["#,##0.00"]} ${currency}
    </#if>
</p>
<#if invoices?has_content>
    <h4>成功开票</h4>
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
<#else>
    <p><i>本次未生成任何发票。</i></p>
</#if>
<#if failDetails?has_content>
    <h4 style="color:#d9534f;">未成功处理项</h4>

    <table style="border-collapse: collapse; width: 100%; font-size: 13px;">
        <thead>
        <tr style="background-color:#fff5f5;">
            <th style="border:1px solid #f1c0c0;padding:8px;">级别</th>
            <th style="border:1px solid #f1c0c0;padding:8px;">店铺</th>
            <th style="border:1px solid #f1c0c0;padding:8px;">阶段</th>
            <th style="border:1px solid #f1c0c0;padding:8px;">订单</th>
            <th style="border:1px solid #f1c0c0;padding:8px;">SKU</th>
            <th style="border:1px solid #f1c0c0;padding:8px;">原因</th>
        </tr>
        </thead>
        <tbody>
        <#list failDetails as fail>
            <tr>
                <td style="border:1px solid #f1c0c0;padding:8px;">
                    <#if fail.level??>${fail.level}<#else>-</#if>
                </td>
                <td style="border:1px solid #f1c0c0;padding:8px;">
                    <#if fail.shopName??>${fail.shopName}<#else><i>-</i></#if>
                </td>
                <td style="border:1px solid #f1c0c0;padding:8px;">
                    <#if fail.step??>${fail.step}<#else>-</#if>
                </td>
                <td style="border:1px solid #f1c0c0;padding:8px;">
                    <#if fail.platformOrderId??>${fail.platformOrderId}<#else>-</#if>
                </td>
                <td style="border:1px solid #f1c0c0;padding:8px;">
                    <#if fail.sku??>${fail.sku}<#else>-</#if>
                </td>
                <td style="border:1px solid #f1c0c0;padding:8px;">
                    ${fail.errorReason?if_exists}
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</#if>

<p>请登录系统后台审核采购：<br>
    <a href="${reviewLink}" target="_blank">${reviewLink}</a></p>

<#include "footer.ftl">
