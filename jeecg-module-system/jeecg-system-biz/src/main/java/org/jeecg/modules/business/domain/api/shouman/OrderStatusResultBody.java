package org.jeecg.modules.business.domain.api.shouman;

import lombok.Data;

@Data
public class OrderStatusResultBody {

    private String tailTrackingNumber;
    private String submissionCarrierName;
    private String carrierName;
    private String expressNo;
    private String trackingUrl;
    private String carrierCode;
    private String weight;
    private String submissionCarrierCode;

    //0.未绑定-订单商品未与系统产品进行绑定
    //1.待处理-订单商品已经绑定需要修改商品备注或下美工制图单（定制商品需要手动下美工单）
    //2.制图中-订单处于美工设计订单稿件中
    //3.备货中-订单处于待进行生产状态（系统会定时将处于备货中的订单自动生成生产订单）
    //4.生产中-订单处理生产环节中
    //5.已配货-订单商品已经生产完成并完成产品检查打包
    //6.发货-该订单已经发货
    //7.售后-生产出的订单有质量问题需要进行售后操作（订单处于发货状态下才可发起售后单）
    //8.取消-来源平台订单取消,首曼系统进行取消
    //9.等待-订单有未确认的信息需要再次确认，等待生产
    //10.退款-来源平台订单退款，取消生产
    private Integer orderStatus;

    private Integer urgent;
}