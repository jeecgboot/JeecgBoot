package org.jeecg.modules.business.controller.admin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.controller.admin.shippingInvoice.InvoiceDatas;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Credit;
import org.jeecg.modules.business.mapper.ExchangeRatesMapper;
import org.jeecg.modules.business.service.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.CreditPage;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.jeecg.modules.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.jeecg.modules.business.entity.Balance.OperationType;
 /**
 * @Description: credit
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Api(tags="credit")
@RestController
@RequestMapping("/credit")
@Slf4j
public class CreditController extends JeecgController<Credit, ICreditService> {
	@Autowired
	private IBalanceService balanceService;
	@Autowired
	private IClientService clientService;
	@Autowired
	private ICreditService creditService;
	@Autowired
	 private ICurrencyService currencyService;
	 @Autowired
	 private ISecurityService securityService;
     @Autowired
     private PlatformOrderShippingInvoiceService platformOrderShippingInvoiceService;
	 @Autowired
	 private InvoiceService invoiceService;
     @Autowired
     private ExchangeRatesMapper exchangeRatesMapper;

	 private final SimpleDateFormat CREATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 /**
	 * 分页列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value="credit-分页列表查询", notes="credit-分页列表查询")
	@GetMapping(value = "/list")
	 public Result<IPage<CreditPage>> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												@RequestParam(name="pageSize", defaultValue="50") Integer pageSize,
												@RequestParam(name="clientId", required = false) String clientId,
												@RequestParam(name="invoiceNumber", required = false) String invoiceNumber,
												@RequestParam(name="currencyId", required = false) String currencyId) {
		List<CreditPage> creditList = creditService.listWithFilters(clientId, invoiceNumber, currencyId, pageNo, pageSize);
		int total = creditService.countAllWithFilters(clientId, invoiceNumber, currencyId);
		IPage<CreditPage> page = new Page<>();
		page.setRecords(creditList);
		page.setTotal(total);
		page.setSize(pageSize);
		page.setCurrent(pageNo);
		return Result.OK(page);
	}
	
	/**
	 *   添加
	 *
	 * @param credit
	 * @return
	 */
	@AutoLog(value = "credit-添加")
	@ApiOperation(value="credit-添加", notes="credit-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Credit credit) throws RuntimeException {
		if(!securityService.checkIsEmployee()) {
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			log.error("User {}, tried to access /credit/add but is not authorized.", loginUser.getUsername());
			return Result.error(HttpStatus.SC_FORBIDDEN,"Access denied");
		}
		if(credit.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			return Result.error(HttpStatus.SC_BAD_REQUEST, "Credit amount cannot be negative.");
		}
		try {
			Response<String, String> addCreditResponse = creditService.addCredit(credit);
			if (addCreditResponse.getStatus() == HttpStatus.SC_INTERNAL_SERVER_ERROR || addCreditResponse.getStatus() == HttpStatus.SC_NOT_FOUND) {
				return Result.error(addCreditResponse.getStatus(), addCreditResponse.getError());
			}
			String creditId = addCreditResponse.getData();
			Response<InvoiceMetaData, String> makeInvoiceResponse = creditService.makeInvoice(creditId);
			return Result.OK(makeInvoiceResponse.getData());
		} catch(RuntimeException | IOException e) {
			return Result.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	/**
	 *  编辑
	 *
	 * @param credit
	 * @return
	 */
	@AutoLog(value = "credit-编辑")
	@ApiOperation(value="credit-编辑", notes="credit-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody Credit credit) throws Exception {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(!securityService.checkIsEmployee()) {
			log.error("User {}, tried to access /credit/edit but is not authorized.", loginUser.getUsername());
			return Result.error(HttpStatus.SC_FORBIDDEN,"Access denied");
		}
		log.info("editing credit");
		Credit lastCredit = creditService.getLastCredit(credit.getClientId(), credit.getCurrencyId());
		Credit creditToEdit = creditService.getById(credit.getId());
		Calendar lastCreditDate = Calendar.getInstance();
		lastCreditDate.setTime(lastCredit.getCreateTime());
		Calendar creditDate = Calendar.getInstance();
		creditDate.setTime(creditToEdit.getCreateTime());

		// if not the same day
		if(lastCreditDate.get(Calendar.DAY_OF_YEAR) != creditDate.get(Calendar.DAY_OF_YEAR)) {
			log.error("Credit can only be edited on the same day it was created.");
			return Result.error(HttpStatus.SC_FORBIDDEN,"Credit can only be edited on the same day it was created.");
		}

		boolean isAmountUpdated = true;
		// if the last credit is not the one being edited, then the amount cannot be edited
		if(!lastCredit.getId().equals(credit.getId())) {
			isAmountUpdated = false;
			if(creditToEdit.getAmount().compareTo(credit.getAmount()) != 0) {
				log.error("Credit amount cannot be edited, unless it's the last record.");
				return Result.error(HttpStatus.SC_CONFLICT,"Credit amount cannot be edited, unless it's the last record.");
			}
		}
		try {
			creditService.updateCredit(credit, isAmountUpdated, loginUser.getUsername());
		} catch (Exception e) {
			return Result.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
		log.info("credit edited successfully !");
		InvoiceMetaData invoiceMetaData = creditService.generateInvoiceExcel(credit.getInvoiceNumber());
		return Result.OK("sys.api.entryEditSuccess", invoiceMetaData);
	}
	
	/**
	 *   通过id删除
	 *   To cancel a credit, we change the status to 2 (cancelled) and then we create a cancellation credit entry with status 3 (cancellation) with the negative amount of the credit to cancel.
	 *	 We then update the balance of the client with the amount of the cancellation credit.
	 * @param id
	 * @return
	 */
	@AutoLog(value = "credit-通过id删除")
	@ApiOperation(value="credit-通过id删除", notes="credit-通过id删除")
	@Transactional
	@PutMapping(value = "/cancel")
	public Result<String> delete(@RequestParam(name="id") String id) {
		if(!securityService.checkIsEmployee()) {
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			log.error("User {}, tried to access /credit/cancel but is not authorized.", loginUser.getUsername());
			return Result.error(HttpStatus.SC_FORBIDDEN,"Access denied");
		}
		Credit credit = creditService.getById(id);
		if(credit == null) {
			return Result.error(404, "Credit not found");
		}
		Credit latestCredit = creditService.getLastCredit(credit.getClientId(), credit.getCurrencyId());
		if(!credit.getId().equals(latestCredit.getId())) {
			return Result.error(409, "Credit cannot be deleted, unless it's the last record.");
		}
		invoiceService.cancelInvoice(credit.getId(), credit.getInvoiceNumber(), credit.getClientId(), true);
		return Result.OK("sys.api.entryDeleteSuccess");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "credit-批量删除")
	@ApiOperation(value="credit-批量删除", notes="credit-批量删除")
	@Transactional
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids") String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		creditService.removeByIds(idList);
		balanceService.deleteBatchBalance(idList,OperationType.Credit.name());
		return Result.OK("sys.api.entryBatchDeleteSuccess");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="credit-通过id查询", notes="credit-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Credit> queryById(@RequestParam(name="id") String id) {
		Credit credit = creditService.getById(id);
		if(credit==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(credit);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param credit
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Credit credit) {
        return super.exportXls(request, credit, Credit.class, "credit");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Credit.class);
    }

	@GetMapping(value = "downloadInvoice")
	 public ResponseEntity<?> download(@RequestParam(name="invoiceNumber") String invoiceNumber) throws UserException, IOException {
		boolean isEmployee = securityService.checkIsEmployee();
		Client client;
		if (!isEmployee) {
			client = clientService.getCurrentClient();
			if (client == null) {
				log.error("Couldn't find the client for the invoice number : {}", invoiceNumber);
				return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
						.contentType(MediaType.TEXT_PLAIN)
						.body("");
			}
			Client invoiceClient = clientService.getClientFromInvoice(invoiceNumber);
			if (invoiceClient == null || !invoiceClient.getId().equals(client.getId())) {
				log.error("Client {} is trying to download invoice {} which doesn't belong to him.", client.getInternalCode(), invoiceNumber);
				return ResponseEntity.status(HttpStatus.SC_FORBIDDEN)
						.contentType(MediaType.TEXT_PLAIN)
						.body("You are not allowed to download this invoice.");
			}
		}
		String filename = platformOrderShippingInvoiceService.getInvoiceList(invoiceNumber, "invoice");
		if(!filename.equals("Error")) {
			File file = new File(filename);

			log.info("Filename : {}", file);

			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");

			Path path = Paths.get(file.getAbsolutePath());

			log.info("Absolute Path : {} \nLength : {}", path, file.length());
			ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

			return ResponseEntity.ok()
					.headers(header)
					.contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(resource);
		}
		else {
            log.error("Couldn't find the credit invoice file for : {}", invoiceNumber);
			return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
					.contentType(MediaType.TEXT_PLAIN)
					.body("Couldn't find the invoice file for : " + invoiceNumber);
		}
	}

	@GetMapping(value = "/getClient")
	public Result<Client> getClient(@RequestParam(name="invoiceNumber") String invoiceNumber) {
		Client client = clientService.getClientFromCredit(invoiceNumber);
		if(client == null) {
			return Result.error(HttpStatus.SC_NOT_FOUND, "Client not found");
		}
		return Result.OK(client);
	}
	@GetMapping(value = "/invoiceData")
	public Result<?> getInvoiceData(@RequestParam(name="invoiceNumber") String invoiceNumber) {
		InvoiceDatas invoiceDatas = new InvoiceDatas();
		Credit credit = creditService.getByInvoiceNumber(invoiceNumber);
		if(credit == null) {
			return Result.error(HttpStatus.SC_NOT_FOUND, "Credit not found");
		}
		String invoiceCurrency = currencyService.getCodeById(credit.getCurrencyId());
		if(!invoiceCurrency.equals("EUR")) {
			BigDecimal exchangeRate = exchangeRatesMapper.getExchangeRateFromDate("EUR", invoiceCurrency, CREATE_TIME_FORMAT.format(credit.getCreateTime()));
			invoiceDatas.setFinalAmountEur(credit.getAmount().divide(exchangeRate, 2, RoundingMode.CEILING));
			invoiceDatas.setFinalAmount(credit.getAmount());
		}
		else {
			invoiceDatas.setFinalAmountEur(credit.getAmount());
		}
		invoiceDatas.setInvoiceNumber(credit.getInvoiceNumber());
		invoiceDatas.setDescription(credit.getDescription());
		invoiceDatas.setStatus(credit.getStatus());
		return Result.OK(invoiceDatas);
	}
}
