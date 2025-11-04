<#include "components/header.ftl">

<tr>
    <td style="padding:35px 0;">您好，</td>
</tr>

<tr>
    <td style="padding:0 0 35px 0;">
        系统检测到以下店铺的订单因「指定平台发货仓库」已被自动作废。
    </td>
</tr>

<tr>
    <table style="border: 1px solid #bbb; border-collapse: collapse; text-align: center; width: 100%;">
        <thead>
        <tr>
            <th style="border: 1px solid #bbb;">店铺</th>
            <th style="border: 1px solid #bbb;">订单号</th>
            <th style="border: 1px solid #bbb;">指定平台发货仓库</th>
        </tr>
        </thead>
        <tbody>
        <#list infos as info>
            <tr>
                <td style="border: 1px solid #bbb;">${info.left}</td>
                <td style="border: 1px solid #bbb;">${info.middle}</td>
                <td style="border: 1px solid #bbb;">${info.right}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</tr>
<#include "components/footer.ftl">
