package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.Quotation;
import org.jeecg.modules.business.mapper.ClientSalespersonMapper;
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
import java.util.List;

@Api(tags = "Inquiry quotation")
@RestController
@RequestMapping("/quotation")
@Slf4j
public class QuotationController {
    private static final String STATUS_INQUIRY = "0";
    private static final String STATUS_QUOTED = "1";

    @Autowired
    private IQuotationService quotationService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private ClientSalespersonMapper clientSalespersonMapper;

    @ApiOperation("Inquiry list")
    @GetMapping("/inquiry/list")
    public Result<IPage<Quotation>> inquiryList(
            Quotation q,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<String> clientScopeError = quotationService.applyClientScope(q);
        if (clientScopeError != null) {
            return Result.error(clientScopeError.getCode(), clientScopeError.getMessage());
        }
        q.setStatus(STATUS_INQUIRY);
        return Result.OK(quotationService.pageByStatus(new Page<>(pageNo, pageSize), q));
    }

    @ApiOperation("Inquiry add")
    @PostMapping("/inquiry/add")
    public Result<String> inquiryAdd(@RequestBody Quotation q) {
        if (StringUtils.isBlank(q.getInquiryLink())) return Result.error("inquiryLink cannot be empty");
        if (StringUtils.isBlank(q.getInquiryCountry())) return Result.error("inquiryCountry cannot be empty");
        if (q.getExpectedSales() == null) return Result.error("expectedSales cannot be empty");
        Result<String> clientScopeError = quotationService.applyClientScope(q);
        if (clientScopeError != null) {
            return Result.error(clientScopeError.getCode(), clientScopeError.getMessage());
        }
        quotationService.normalizeCountryFields(q);
        quotationService.fillInquirySalesByClient(q);
        q.setStatus(STATUS_INQUIRY);
        quotationService.save(q);
        return Result.OK("Added successfully");
    }

    @ApiOperation("Inquiry edit")
    @RequestMapping(value = "/inquiry/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> inquiryEdit(@RequestBody Quotation q) {
        if (StringUtils.isBlank(q.getId())) return Result.error("id cannot be empty");
        Result<String> ownershipError = quotationService.checkInquiryOwnership(q.getId());
        if (ownershipError != null) {
            return Result.error(ownershipError.getCode(), ownershipError.getMessage());
        }
        int rows = quotationService.updateInquiryFields(q);
        return rows > 0 ? Result.OK("Edited successfully") : Result.error("Edit failed: record not found or not in inquiry status");
    }

    @ApiOperation("Inquiry delete")
    @DeleteMapping("/inquiry/delete")
    public Result<String> inquiryDelete(@RequestParam("id") String id) {
        if (StringUtils.isBlank(id)) return Result.error("id cannot be empty");
        Result<String> ownershipError = quotationService.checkInquiryOwnership(id);
        if (ownershipError != null) {
            return Result.error(ownershipError.getCode(), ownershipError.getMessage());
        }
        Quotation q = quotationService.getByIdAndStatus(id, STATUS_INQUIRY);
        if (q == null) return Result.error("Record not found or not in inquiry status");
        boolean ok = quotationService.removeById(id);
        return ok ? Result.OK("Deleted successfully") : Result.error("Delete failed");
    }

    @ApiOperation("Inquiry query by id")
    @GetMapping("/inquiry/queryById")
    public Result<Quotation> inquiryQueryById(@RequestParam("id") String id) {
        Result<String> ownershipError = quotationService.checkInquiryOwnership(id);
        if (ownershipError != null) {
            return Result.error(ownershipError.getCode(), ownershipError.getMessage());
        }
        Quotation q = quotationService.getByIdAndStatus(id, STATUS_INQUIRY);
        return q != null ? Result.OK(q) : Result.error("Record not found or not in inquiry status");
    }

    @ApiOperation("Get salespersons by client")
    @GetMapping("/clientSalespersons")
    public Result<List<String>> getClientSalespersons(@RequestParam("clientId") String clientId) {
        if (StringUtils.isBlank(clientId)) return Result.error("clientId cannot be empty");
        Quotation q = new Quotation();
        q.setInquiryClient(clientId);
        Result<String> clientScopeError = quotationService.applyClientScope(q);
        if (clientScopeError != null) {
            return Result.error(clientScopeError.getCode(), clientScopeError.getMessage());
        }
        return Result.OK(clientSalespersonMapper.getSalespersonIdsByClientId(q.getInquiryClient()));
    }

    @ApiOperation("Quote list")
    @GetMapping("/quote/list")
    public Result<IPage<Quotation>> quoteList(
            Quotation q,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<String> clientScopeError = quotationService.applyClientScope(q);
        if (clientScopeError != null) {
            return Result.error(clientScopeError.getCode(), clientScopeError.getMessage());
        }
        return Result.OK(quotationService.pageByStatus(new Page<>(pageNo, pageSize), q));
    }

    @ApiOperation("Export customer visible quotes")
    @GetMapping("/quote/exportCustomerQuotes")
    public void exportCustomerQuotes(
            Quotation q,
            @RequestParam(name = "selections", required = false) String selections,
            HttpServletResponse response) throws IOException {
        quotationService.exportCustomerQuotes(q, selections, response);
    }

    @ApiOperation("Quote add")
    @PostMapping("/quote/add")
    public Result<String> quoteAdd(@RequestBody Quotation q) {
        if (StringUtils.isBlank(q.getId())) return Result.error("id cannot be empty");
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        Result<String> ownershipError = quotationService.checkInquiryOwnership(q.getId());
        if (ownershipError != null) {
            return Result.error(ownershipError.getCode(), ownershipError.getMessage());
        }
        int rows = quotationService.addQuoteBasedOnInquiry(q);
        return rows > 0 ? Result.OK("Quote added successfully") : Result.error("Add quote failed: record not found or not in inquiry status");
    }

    @ApiOperation("Quote edit")
    @RequestMapping(value = "/quote/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> quoteEdit(@RequestBody Quotation q) {
        if (StringUtils.isBlank(q.getId())) return Result.error("id cannot be empty");
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        Result<String> ownershipError = quotationService.checkInquiryOwnership(q.getId());
        if (ownershipError != null) {
            return Result.error(ownershipError.getCode(), ownershipError.getMessage());
        }
        int rows = quotationService.updateQuoteFields(q);
        return rows > 0 ? Result.OK("Edited successfully") : Result.error("Edit failed: record not found or not in quote status");
    }

    @ApiOperation("Quote query by id")
    @GetMapping("/quote/queryById")
    public Result<Quotation> quoteQueryById(@RequestParam("id") String id) {
        Result<String> ownershipError = quotationService.checkInquiryOwnership(id);
        if (ownershipError != null) {
            return Result.error(ownershipError.getCode(), ownershipError.getMessage());
        }
        Quotation q = quotationService.getByIdAndStatus(id, STATUS_QUOTED);
        return q != null ? Result.OK(q) : Result.error("Record not found or not in quote status");
    }

    @ApiOperation("Quote estimate")
    @PostMapping("/quote/estimate")
    public Result<Quotation> estimate(@RequestBody Quotation q) {
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        return Result.OK(quotationService.estimateQuote(q));
    }

    @ApiOperation("Quote revoke")
    @PostMapping("/quote/revoke")
    public Result<String> revokeQuote(
            @RequestParam(value = "id", required = false) String id,
            @RequestBody(required = false) java.util.Map<String, Object> body
    ) {
        if (!securityService.checkIsEmployee()) return Result.error(403, "Access denied");
        if (StringUtils.isNotBlank(id)) {
            Result<String> ownershipError = quotationService.checkInquiryOwnership(id);
            if (ownershipError != null) {
                return Result.error(ownershipError.getCode(), ownershipError.getMessage());
            }
            int rows = quotationService.revokeQuoteById(id);
            return rows > 0 ? Result.OK("Quote revoked successfully") : Result.error("Revoke failed: record not found or not in quote status");
        }
        if (body != null && body.get("ids") != null) {
            @SuppressWarnings("unchecked")
            java.util.List<String> ids = (java.util.List<String>) body.get("ids");
            for (String quoteId : ids) {
                Result<String> ownershipError = quotationService.checkInquiryOwnership(quoteId);
                if (ownershipError != null) {
                    return Result.error(ownershipError.getCode(), ownershipError.getMessage());
                }
            }
            int rows = quotationService.revokeQuoteBatch(ids);
            return rows > 0 ? Result.OK("Quotes revoked successfully") : Result.error("Revoke failed: no records found or not in quote status");
        }
        return Result.error("id or ids cannot be empty");
    }
}
