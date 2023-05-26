package org.jeecg.modules.business.controller.admin;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.service.CountryService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags="国家")
@RestController
@RequestMapping("/country")
@Slf4j
public class CountryController {
    private CountryService countryService;
    private IPlatformOrderService platformOrderService;

    @Autowired
    public CountryController(CountryService countryService, IPlatformOrderService platformOrderService) {
        this.countryService = countryService;
        this.platformOrderService = platformOrderService;
    }

    @GetMapping(value = "/activeList")
    public Result<?> getActiveList() {
        List<Country> countryList = countryService.getActiveCountries();
        System.out.println(countryList);
        return Result.OK(countryList);
    }
}
