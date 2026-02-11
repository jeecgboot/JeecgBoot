package org.jeecg.modules.business.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.business.domain.api.mabang.getOrderLogisticsLabel.ShippingLabel;
import org.jeecg.modules.business.domain.api.wia.Parcel;
import org.jeecg.modules.business.domain.api.wia.WiaResponse;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.service.IApiService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: WIA API
 * @Date: 2022-11-08
 * @Version: V1.0
 */
@Api(tags = "WIA API")
@RestController
@RequestMapping("/wia")
@Slf4j
public class WiaTraceController {

    @Autowired
    private IApiService apiService;
    @Autowired
    private IPlatformOrderService platformOrderService;

    /**
     * 分页列表查询
     *
     * @param trackingNumbers 物流跟踪号
     * @param req
     * @return
     */
    @AutoLog(value = "包裹-查询轨迹")
    @ApiOperation(value = "包裹-查询轨迹", notes = "包裹-查询轨迹")
    @GetMapping(value = "/prod")
    public WiaResponse getTraces(@RequestParam(name = "nums") List<String> trackingNumbers, HttpServletRequest req) {
        List<Parcel> parcels = apiService.searchByTrackingNumbers(trackingNumbers);
        WiaResponse wiaResponse = new WiaResponse();
        wiaResponse.setSuccess(!parcels.isEmpty());
        wiaResponse.setParcels(parcels);
        return wiaResponse;
    }

    /**
     * 回调地址，接受马帮订单的物流面单
     * @return
     */
    @AutoLog(value = "物流面单回调")
    @ApiOperation(value = "物流面单回调", notes = "物流面单回调")
    @GetMapping(value = "/shippingLabelCallback")
    public void getShippingLabel(@RequestParam(name = "b10_10") JSONObject b10x10, @RequestParam(name = "a4") JSONObject a4,
                                 @RequestParam(name = "platformOrderId") String platformOrderId,
                                 @RequestParam(name = "trackNumber") String trackingNumber, HttpServletRequest req) {
        log.info("Received shipping label callback request, starting processing.");
        if (platformOrderId == null) {
            log.error("platformOrderId is null, aborting.");
            return;
        }
        PlatformOrder platformOrder = platformOrderService.selectByPlatformOrderId(platformOrderId);
        if (platformOrder == null) {
            log.error("Platform order {} couldn't be found.", platformOrderId);
        } else {
            ShippingLabel label = new ShippingLabel(b10x10);
            // Fall back to A4 label if 10x10 is empty
            if (label.isEmpty()) {
                label = new ShippingLabel(a4);
                // Abort if still empty
                if (label.isEmpty()) {
                    log.error("No shipping label found for platformOrderId {}", platformOrderId);
                    return;
                }
            }
            if (platformOrder.getTrackingNumber() != null &&  platformOrder.getTrackingNumber().equalsIgnoreCase(trackingNumber)) {
                platformOrder.setShippingLabelUrl(label.getAvailableLabelUrl());
                platformOrderService.saveOrUpdate(platformOrder);
                log.info("Saved shipping label URL for platform order {}", platformOrderId);
            } else {
                log.error("Tracking number in database doesn't match with the one from label, aborting.");
            }
        }
    }
}
