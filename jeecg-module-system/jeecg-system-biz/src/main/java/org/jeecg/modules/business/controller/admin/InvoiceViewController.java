package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.Invoice;
import org.jeecg.modules.business.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "发票")
@RestController
@RequestMapping("/invoice")
@Slf4j
public class InvoiceViewController {
    @Autowired
    private InvoiceService invoiceService;
    @GetMapping(value = "/list")
    public Result<?> list(Invoice invoice, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Invoice> queryWrapper = QueryGenerator.initQueryWrapper(invoice, req.getParameterMap());
        Page<Invoice> page = new Page<>(pageNo, pageSize);
        IPage<Invoice> pageList = invoiceService.page(page, queryWrapper);
        return Result.ok(pageList);
    }
}
