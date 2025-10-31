<#include "header.ftl">

<h3>New Invoice Summary</h3>
<p>Dear ${clientName},</p>
<p>Your invoices have been generated automatically. Please review the details below:</p>

<table style="border-collapse: collapse; width: 100%;">
    <thead>
    <tr style="background-color:#f7f7f7;">
        <th style="border:1px solid #bbb;padding:8px;">Invoice No.</th>
        <th style="border:1px solid #bbb;padding:8px;">Shop</th>
        <th style="border:1px solid #bbb;padding:8px;">Amount (${currency})</th>
        <th style="border:1px solid #bbb;padding:8px;">Status</th>
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
                    Approved
                <#else>
                    <span style="color:red; font-weight:bold;">Pending (Top-up Required)</span>
                </#if>
            </td>
        </tr>
    </#list>
    </tbody>
</table>

<p>
    Your current balance:
    <#if currentBalance?number < 0>
        <strong style="color:red;">${currentBalance?string["#,##0.00"]} ${currency}</strong>
    <#else>
        <strong>${currentBalance?string["#,##0.00"]} ${currency}</strong>
    </#if>
</p>

<#if hasDebt>
    <p style="color:red; font-weight:bold;">
        One of your invoices is pending payment. Please top up your account and upload the payment proof to continue processing.
    </p>
</#if>

<p>View details here: <a href="${clientInvoiceLink}">${clientInvoiceLink}</a></p>

<#include "footer.ftl">
