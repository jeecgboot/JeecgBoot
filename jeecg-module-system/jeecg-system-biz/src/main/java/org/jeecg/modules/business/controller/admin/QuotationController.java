package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Inquiry;
import org.jeecg.modules.business.entity.Quotation;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.IInquiryService;
import org.jeecg.modules.business.service.IQuotationService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "Quotation")
@RestController
@RequestMapping("/quotation")
@Slf4j
public class QuotationController {

    @Autowired
    private IQuotationService quotationService;
    @Autowired
    private IInquiryService inquiryService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IClientService clientService;

    @ApiOperation("Quote list")
    @GetMapping("/list")
    public Result<IPage<Quotation>> quoteList(
            Quotation q,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (securityService.checkIsEmployee()) {
            return Result.OK(quotationService.pageByStatus(new Page<>(pageNo, pageSize), q));
        }
        Client client = clientService.getCurrentClient();
        if (client == null || StringUtils.isBlank(client.getId())) {
            return Result.error(403, "Access denied");
        }
        return Result.OK(quotationService.pageByStatusForClient(new Page<>(pageNo, pageSize), q, client.getId()));
    }

    @ApiOperation("Export customer visible quotes")
    @GetMapping("/exportCustomerQuotes")
    public void exportCustomerQuotes(
            Quotation q,
            @RequestParam(name = "selections", required = false) String selections,
            HttpServletResponse response) throws IOException {
        quotationService.exportCustomerQuotes(q, selections, response);
    }

    @ApiOperation("Create quote from inquiry")
    @PostMapping("/add")
    public Result<String> quoteAdd(@RequestBody Quotation q) {
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        if (q == null || StringUtils.isBlank(q.getInquiryId())) {
            return Result.error("inquiryId cannot be empty");
        }
        Inquiry inquiry = inquiryService.getById(q.getInquiryId());
        if (inquiry == null) {
            return Result.error("Inquiry not found");
        }
        if (StringUtils.isBlank(q.getCountry())) {
            q.setCountry(inquiryService.resolvePrimaryCountryId(inquiry));
        }
        try {
            quotationService.prepareDirectQuote(q);
            q.setInquiryId(inquiry.getId());
            quotationService.save(q);
            return Result.OK("Quote added successfully");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("Quote direct add")
    @PostMapping("/directAdd")
    public Result<String> quoteDirectAdd(@RequestBody Quotation q) {
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        try {
            if (q != null) {
                q.setInquiryId(null);
            }
            quotationService.prepareDirectQuote(q);
            quotationService.save(q);
            return Result.OK("Direct quote added successfully");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("Quote edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> quoteEdit(@RequestBody Quotation q) {
        if (StringUtils.isBlank(q.getId())) return Result.error("id cannot be empty");
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        int rows = quotationService.updateQuoteFields(q);
        return rows > 0 ? Result.OK("Edited successfully") : Result.error("Edit failed: record not found or quote already completed");
    }

    @ApiOperation("Quote query by id")
    @GetMapping("/queryById")
    public Result<Quotation> quoteQueryById(@RequestParam("id") String id) {
        Quotation q;
        if (securityService.checkIsEmployee()) {
            q = quotationService.getQuoteById(id);
        } else {
            Client client = clientService.getCurrentClient();
            if (client == null || StringUtils.isBlank(client.getId())) {
                return Result.error(403, "Access denied");
            }
            q = quotationService.getQuoteByIdForClient(id, client.getId());
        }
        return q != null ? Result.OK(q) : Result.error("Record not found");
    }

    @ApiOperation("Quote estimate")
    @PostMapping("/estimate")
    public Result<Quotation> estimate(@RequestBody Quotation q) {
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        return Result.OK(quotationService.estimateQuote(q));
    }

    @ApiOperation("Quote delete")
    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam("id") String id) {
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        if (StringUtils.isBlank(id)) {
            return Result.error("id cannot be empty");
        }
        boolean removed = quotationService.removeById(id);
        return removed ? Result.OK("Deleted successfully") : Result.error("Delete failed: record not found");
    }

    @ApiOperation("Quote batch delete")
    @DeleteMapping("/deleteBatch")
    public Result<String> deleteBatch(
            @RequestParam(value = "ids", required = false) String idsParam,
            @RequestBody(required = false) Map<String, Object> body) {
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        List<String> ids = resolveIds(idsParam, body);
        if (ids.isEmpty()) {
            return Result.error("ids cannot be empty");
        }
        boolean removed = quotationService.removeByIds(ids);
        return removed ? Result.OK("Deleted successfully") : Result.error("Delete failed: no records found");
    }

    @ApiOperation("Quote revoke")
    @PostMapping("/revoke")
    public Result<String> revokeQuote(
            @RequestParam(value = "id", required = false) String id,
            @RequestBody(required = false) Map<String, Object> body
    ) {
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        if (StringUtils.isNotBlank(id)) {
            int rows = quotationService.revokeQuoteById(id);
            return rows > 0 ? Result.OK("Quote revoked successfully") : Result.error("Revoke failed: record not found");
        }
        if (body != null && body.get("ids") != null) {
            @SuppressWarnings("unchecked")
            java.util.List<String> ids = (java.util.List<String>) body.get("ids");
            int rows = quotationService.revokeQuoteBatch(ids);
            return rows > 0 ? Result.OK("Quotes revoked successfully") : Result.error("Revoke failed: no records found");
        }
        return Result.error("id or ids cannot be empty");
    }

    private List<String> resolveIds(String idsParam, Map<String, Object> body) {
        if (StringUtils.isNotBlank(idsParam)) {
            return splitIds(idsParam);
        }
        if (body == null) {
            return Collections.emptyList();
        }
        Object idsValue = body.get("ids");
        if (idsValue instanceof String) {
            return splitIds((String) idsValue);
        }
        if (idsValue instanceof List<?>) {
            List<String> ids = new ArrayList<>();
            for (Object item : (List<?>) idsValue) {
                if (item != null && StringUtils.isNotBlank(String.valueOf(item))) {
                    ids.add(String.valueOf(item).trim());
                }
            }
            return ids;
        }
        return Collections.emptyList();
    }

    private List<String> splitIds(String ids) {
        if (StringUtils.isBlank(ids)) {
            return Collections.emptyList();
        }
        return Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }
}
