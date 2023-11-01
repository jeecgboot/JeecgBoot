package org.jeecg.modules.im.api;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.api.fallback.ApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jeecg-demo", fallbackFactory = ApiFallback.class)
public interface FeignApi {

    /**
     * 根据门店码查询公司
     * @param code
     * @return
     */
    @GetMapping(value = "/company/getByCode")
    Result<String> getByCode(@RequestParam(value = "code") String code);
}
