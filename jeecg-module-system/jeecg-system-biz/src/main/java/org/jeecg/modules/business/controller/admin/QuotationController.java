package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.Quotation;
import org.jeecg.modules.business.service.IQuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "询单报价表")
@RestController
@RequestMapping("/quotation")
@Slf4j
public class QuotationController {
    private static final String STATUS_INQUIRY = "0";
    private static final String STATUS_QUOTED  = "1";
    @Autowired
    private IQuotationService quotationService;

    @ApiOperation("询单-分页列表")
    @GetMapping("/inquiry/list")
    public Result<IPage<Quotation>> inquiryList(
            Quotation q,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        q.setStatus(STATUS_INQUIRY);
        return Result.OK(quotationService.pageByStatus(new Page<>(pageNo, pageSize), q));
    }

    @ApiOperation("询单-新增（新建一行）")
    @PostMapping("/inquiry/add")
    public Result<String> inquiryAdd(@RequestBody Quotation q) {
        if (StringUtils.isBlank(q.getInquiryLink())) return Result.error("inquiryLink cannot be empty");
        if (StringUtils.isBlank(q.getInquiryCountry())) return Result.error("inquiryCountry cannot be empty");
        if (q.getExpectedSales() == null) return Result.error("expectedSales cannot be empty");
        if (StringUtils.isBlank(q.getCountry()) && StringUtils.isNotBlank(q.getInquiryCountry())) {
            q.setCountry(q.getInquiryCountry().split(",")[0]);
        }
        q.setStatus(STATUS_INQUIRY);
        quotationService.save(q);
        return Result.OK("Added successfully");
    }

    @RequestMapping(value="/inquiry/edit", method={RequestMethod.PUT, RequestMethod.POST})
    public Result<String> inquiryEdit(@RequestBody Quotation q) {
        if (StringUtils.isBlank(q.getId())) return Result.error("id cannot be empty");
        if (StringUtils.isBlank(q.getCountry()) && StringUtils.isNotBlank(q.getInquiryCountry())) {
            q.setCountry(q.getInquiryCountry().split(",")[0].trim());
        }
        int rows = quotationService.updateInquiryFields(q);
        return rows > 0 ? Result.OK("Edited successfully！") : Result.error("Edit failed: record not found or not in inquiry status");
    }

    @ApiOperation("询单-删除(仅询单状态)")
    @DeleteMapping("/inquiry/delete")
    public Result<String> inquiryDelete(@RequestParam("id") String id) {
        if (StringUtils.isBlank(id)) return Result.error("id cannot be empty");
        Quotation q = quotationService.getByIdAndStatus(id, STATUS_INQUIRY);
        if (q == null) return Result.error("Record not found or not in inquiry status");
        boolean ok = quotationService.removeById(id);
        return ok ? Result.OK("Deleted successfully") : Result.error("Delete failed");
    }

    @ApiOperation("询单-通过id查询")
    @GetMapping("/inquiry/queryById")
    public Result<Quotation> inquiryQueryById(@RequestParam("id") String id) {
        Quotation q = quotationService.getByIdAndStatus(id, STATUS_INQUIRY);
        return q != null ? Result.OK(q) : Result.error("Record not found or not in inquiry status");
    }

    // ======================
    // 报价（status=1）
    // ======================

    @ApiOperation("报价-分页列表")
    @GetMapping("/quote/list")
    public Result<IPage<Quotation>> quoteList(
            Quotation q,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        return Result.OK(quotationService.pageByStatus(new Page<>(pageNo, pageSize), q));
    }

    /**
     * 逻辑名叫“新增报价”，实际：基于原询单补全报价字段 + status 0->1（不会新增行）
     */
    @ApiOperation("报价-新增（基于询单补全并完成）")
    @PostMapping("/quote/add")
    public Result<String> quoteAdd(@RequestBody Quotation q) {
        if (StringUtils.isBlank(q.getId())) return Result.error("id cannot be empty");
        int rows = quotationService.addQuoteBasedOnInquiry(q);
        return rows > 0 ? Result.OK("Quote added successfully！") : Result.error("Add quote failed: record not found or not in inquiry status");
    }

    @ApiOperation("报价-编辑（只更新报价字段，不置空）")
    @RequestMapping(value="/quote/edit", method={RequestMethod.PUT, RequestMethod.POST})
    public Result<String> quoteEdit(@RequestBody Quotation q) {
        if (StringUtils.isBlank(q.getId())) return Result.error("id cannot be empty");
        int rows = quotationService.updateQuoteFields(q);
        return rows > 0 ? Result.OK("Edited successfully！") : Result.error("Edit failed: record not found or not in quote status");
    }

    @ApiOperation("报价-通过id查询")
    @GetMapping("/quote/queryById")
    public Result<Quotation> quoteQueryById(@RequestParam("id") String id) {
        Quotation q = quotationService.getByIdAndStatus(id, STATUS_QUOTED);
        return q != null ? Result.OK(q) : Result.error("Record not found or not in quote status");
    }

    @ApiOperation("报价-试算：计算重量/汇率/成本/售价/利润")
    @PostMapping("/quote/estimate")
    public Result<Quotation> estimate(@RequestBody Quotation q) {
        return Result.OK(quotationService.estimateQuote(q));
    }

    @ApiOperation("报价-撤销（支持单个/批量：回到询单状态 + 清空报价字段）")
    @PostMapping("/quote/revoke")
    public Result<String> revokeQuote(
            @RequestParam(value = "id", required = false) String id,
            @RequestBody(required = false) java.util.Map<String, Object> body
    ) {
        // for single revoke
        if (org.apache.commons.lang3.StringUtils.isNotBlank(id)) {
            int rows = quotationService.revokeQuoteById(id);
            return rows > 0 ? Result.OK("Quote revoked successfully") : Result.error("Revoke failed: record not found or not in quote status");
        }
        // batch revoke
        if (body != null && body.get("ids") != null) {
            @SuppressWarnings("unchecked")
            java.util.List<String> ids = (java.util.List<String>) body.get("ids");
            int rows = quotationService.revokeQuoteBatch(ids);
            return rows > 0 ? Result.OK("Quotes revoked successfully") : Result.error("Revoke failed: no records found or not in quote status");
        }
        return Result.error("id or ids cannot be empty");
    }



}
