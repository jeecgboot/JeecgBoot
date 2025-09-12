<#include "components/header.ftl">

<tr>
        <td style="padding:35px 0;">您好，</td>
</tr>

<tr>
        <td style="padding:0 0 35px 0;">
                系统检测到以下店铺的订单因「买家自选物流」已被自动作废。
        </td>
</tr>

<tr>
        <table style="border: 1px solid #bbb; border-collapse: collapse; text-align: center; width: 100%;">
                <thead>
                <tr>
                        <th style="border: 1px solid #bbb;">店铺</th>
                        <th style="border: 1px solid #bbb;">物流方式</th>
                        <th style="border: 1px solid #bbb;">作废订单数</th>
                        <th style="border: 1px solid #bbb;">时间</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                        <td style="border: 1px solid #bbb;">${shopCodes?join(", ")}</td>
                        <td style="border: 1px solid #bbb;">${shippingServices?join(", ")}</td>
                        <td style="border: 1px solid #bbb;">${orderCount}</td>
                        <td style="border: 1px solid #bbb;">${time}</td>
                </tr>
                </tbody>
        </table>
</tr>

<tr>
        <td style="padding:35px 0;">
                订单号列表如下：<br/>
                <ul style="text-align:left;">
                        <#list orders as order>
                                <li>${order}</li>
                        </#list>
                </ul>
        </td>
</tr>

<#include "components/footer.ftl">
