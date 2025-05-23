package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Invoice;
import org.jeecg.modules.business.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = "发票")
@RestController
@RequestMapping("/invoice")
@Slf4j
public class InvoiceViewController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private ISecurityService securityService;

    @GetMapping(value = "/list")
    public Result<?> list(Invoice invoice, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        boolean isEmployee = securityService.checkIsEmployee();
        Map<String, String[]> parameterMap = new HashMap<>(req.getParameterMap());
        for(String key : req.getParameterMap().keySet()) {
            if(key.equals("clientId") && !isEmployee) {
                parameterMap.remove("clientId");
            }
            if(key.equals("createBy") && !isEmployee) {
                parameterMap.remove("createBy");
            }
        }
        if(!isEmployee) {
            Client currentClient = clientService.getCurrentClient();
            if (currentClient == null) {
                return Result.error(HttpStatus.SC_UNAUTHORIZED, "Client is not registered as a user");
            }
            parameterMap.put("clientId", new String[]{currentClient.getId()});
        }
        QueryWrapper<Invoice> queryWrapper = QueryGenerator.initQueryWrapper(invoice, parameterMap);
        Page<Invoice> page = new Page<>(pageNo, pageSize);
        IPage<Invoice> pageList = invoiceService.page(page, queryWrapper);
        return Result.ok(pageList);
    }
    @DeleteMapping(value = "/cancelInvoice")
    public Result<?> cancelInvoice(@RequestParam("id") String id, @RequestParam("invoiceNumber") String invoiceNumber, @RequestParam("clientId") String clientId) {
        boolean isEmployee = securityService.checkIsEmployee();
        Client client = clientService.getById(clientId);
        Client currentClient;
        if(client == null) {
            log.error("Client {} not found", clientId);
            return Result.error(HttpStatus.SC_NOT_FOUND, "Client not found");
        }
        if (!isEmployee) {
            currentClient = clientService.getCurrentClient();
            if (currentClient == null) {
                log.error("Client is not registered as a user : {}", clientId);
                return Result.error(HttpStatus.SC_UNAUTHORIZED, "Client is not registered as a user");
            }
            if(!clientId.equals(currentClient.getId())) {
                log.error("Client {} is not authorized to download invoice detail for client {}", currentClient.getInternalCode(), client.getInternalCode());
                return Result.error(HttpStatus.SC_NOT_FOUND, "Invoice not found");
            }
        }
        log.info("Cancelling invoice number : {}", invoiceNumber);
        boolean invoiceCancelled = invoiceService.cancelInvoice(id, invoiceNumber, clientId, isEmployee);
        return Result.ok(invoiceCancelled ? "sys.api.invoiceCancelSuccess" : "sys.api.invoiceCancelSuccessFileDeleteFail");
    }
    @DeleteMapping(value="/cancelBatchInvoice")
    public Result<?> cancelBatchInvoice(@RequestBody List<Invoice> invoices) {
        boolean invoicesCancelled = invoiceService.cancelBatchInvoice(invoices);
        return Result.ok(invoicesCancelled ? "sys.api.invoiceCancelSuccess" : "sys.api.invoiceCancelSuccessFileDeleteFail");
    }
}
