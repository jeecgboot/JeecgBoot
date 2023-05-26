package org.jeecg.modules.business.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.business.domain.api.wia.Parcel;
import org.jeecg.modules.business.domain.api.wia.WiaResponse;
import org.jeecg.modules.business.service.IApiService;
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
}
