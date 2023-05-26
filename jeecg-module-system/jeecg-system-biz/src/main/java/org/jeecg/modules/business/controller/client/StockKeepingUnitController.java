package org.jeecg.modules.business.controller.client;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.vo.SkuChannelHistory;
import org.jeecg.modules.business.vo.UserSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "SKU client")
@RestController
@RequestMapping("/business/sku/client")
@Slf4j
public class StockKeepingUnitController {
    @Autowired
    private ISkuService skuService;

    @GetMapping(value = "/userSku")
    public Result<List<UserSku>> skuByUser() {
        List<UserSku> res = skuService.findSkuForCurrentUser();
        return Result.OK(res);
    }

    @GetMapping(value = "/channelCountryHistory")
    public Result<?> skuChannelCountryHistory(@RequestParam String clientId, @RequestParam String countryCode) {

        List<String> skuIds = skuService.findSkuForUser(clientId)
                .stream()
                .map(ClientSku::getSkuId)
                .collect(Collectors.toList());
        List<SkuChannelHistory> res;
        try {
            res = skuService.findHistoryBySkuIdsAndCountryCode(skuIds, countryCode);
        } catch (UserException e) {
            return Result.error(e.getLocalizedMessage());
        }

        return Result.OK(res);
    }
}
