package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Inquiry;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.IInquiryService;
import org.jeecg.modules.business.service.ISecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Inquiry")
@RestController
@RequestMapping("/inquiry")
@Slf4j
public class InquiryController extends JeecgController<Inquiry, IInquiryService> {
    @Autowired
    private IInquiryService inquiryService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IClientService clientService;

    @ApiOperation(value = "Inquiry list", notes = "Inquiry list")
    @GetMapping(value = "/list")
    public Result<IPage<Inquiry>> queryPageList(Inquiry inquiry,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
        String inquiryCountryFilter = inquiry == null ? null : inquiry.getCountryId();
        if (inquiry != null && StringUtils.isNotBlank(inquiryCountryFilter)) {
            inquiry.setCountryId(null);
        }
        if (!securityService.checkIsEmployee()) {
            Client client = clientService.getCurrentClient();
            if (client == null || StringUtils.isBlank(client.getId())) {
                return Result.error(403, "Access denied");
            }
            inquiry.setClientId(client.getId());
        }
        Map<String, String[]> queryParams = new HashMap<>(req.getParameterMap());
        queryParams.remove("countryId");
        queryParams.remove("countryIds");
        QueryWrapper<Inquiry> queryWrapper = QueryGenerator.initQueryWrapper(inquiry, queryParams);
        if (StringUtils.isNotBlank(inquiryCountryFilter)) {
            String normalizedCountry = inquiryService.normalizeCountryValue(inquiryCountryFilter);
            queryWrapper.and(wrapper -> wrapper
                    .apply("FIND_IN_SET({0}, REPLACE(REPLACE(country_id, ', ', ','), ' ,', ','))", normalizedCountry)
                    .or().apply("FIND_IN_SET((SELECT name_en FROM country WHERE id = {0}), REPLACE(REPLACE(country_id, ', ', ','), ' ,', ','))", normalizedCountry)
                    .or().apply("FIND_IN_SET((SELECT special_name FROM country WHERE id = {0}), REPLACE(REPLACE(country_id, ', ', ','), ' ,', ','))", normalizedCountry)
                    .or().apply("FIND_IN_SET((SELECT name_zh FROM country WHERE id = {0}), REPLACE(REPLACE(country_id, ', ', ','), ' ,', ','))", normalizedCountry)
                    .or().apply("FIND_IN_SET((SELECT code FROM country WHERE id = {0}), REPLACE(REPLACE(country_id, ', ', ','), ' ,', ','))", normalizedCountry));
        }
        queryWrapper.orderByDesc("create_time");
        Page<Inquiry> page = new Page<>(pageNo, pageSize);
        IPage<Inquiry> pageList = inquiryService.page(page, queryWrapper);
        if (pageList != null && pageList.getRecords() != null) {
            pageList.getRecords().forEach(inquiryService::prepareInquiryForView);
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "Inquiry add")
    @ApiOperation(value = "Inquiry add", notes = "Inquiry add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Inquiry inquiry) {
        if (!securityService.checkIsEmployee()) {
            Client client = clientService.getCurrentClient();
            if (client == null || StringUtils.isBlank(client.getId())) {
                return Result.error(403, "Access denied");
            }
            inquiry.setClientId(client.getId());
        }
        try {
            inquiryService.createInquiry(inquiry);
            return Result.OK("Added successfully");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @AutoLog(value = "Inquiry edit")
    @ApiOperation(value = "Inquiry edit", notes = "Inquiry edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody Inquiry inquiry) {
        Result<String> accessError = checkInquiryAccess(inquiry.getId());
        if (accessError != null) {
            return accessError;
        }
        if (!securityService.checkIsEmployee()) {
            Client client = clientService.getCurrentClient();
            if (client == null || StringUtils.isBlank(client.getId())) {
                return Result.error(403, "Access denied");
            }
            inquiry.setClientId(client.getId());
        }
        try {
            inquiryService.updateInquiry(inquiry);
            return Result.OK("Edited successfully");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @AutoLog(value = "Inquiry delete")
    @ApiOperation(value = "Inquiry delete", notes = "Inquiry delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id") String id) {
        Result<String> accessError = checkInquiryAccess(id);
        if (accessError != null) {
            return accessError;
        }
        inquiryService.removeById(id);
        return Result.OK("Deleted successfully");
    }

    @AutoLog(value = "Inquiry batch delete")
    @ApiOperation(value = "Inquiry batch delete", notes = "Inquiry batch delete")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids") String ids) {
        for (String id : ids.split(",")) {
            Result<String> accessError = checkInquiryAccess(id);
            if (accessError != null) {
                return accessError;
            }
        }
        inquiryService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted successfully");
    }

    @ApiOperation(value = "Inquiry query by id", notes = "Inquiry query by id")
    @GetMapping(value = "/queryById")
    public Result<Inquiry> queryById(@RequestParam(name = "id") String id) {
        Result<String> accessError = checkInquiryAccess(id);
        if (accessError != null) {
            return Result.error(accessError.getCode(), accessError.getMessage());
        }
        Inquiry inquiry = inquiryService.getById(id);
        if (inquiry == null) {
            return Result.error("Record not found");
        }
        return Result.OK(inquiryService.prepareInquiryForView(inquiry));
    }

    private Result<String> checkInquiryAccess(String id) {
        if (securityService.checkIsEmployee()) {
            return null;
        }
        Client client = clientService.getCurrentClient();
        if (client == null || StringUtils.isBlank(client.getId())) {
            return Result.error(403, "Access denied");
        }
        Inquiry inquiry = inquiryService.getById(id);
        if (inquiry == null) {
            return Result.error("Record not found");
        }
        if (!client.getId().equals(inquiry.getClientId())) {
            return Result.error(403, "Access denied");
        }
        return null;
    }
}
