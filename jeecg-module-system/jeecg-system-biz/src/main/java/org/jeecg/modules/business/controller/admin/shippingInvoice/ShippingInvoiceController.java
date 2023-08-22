package org.jeecg.modules.business.controller.admin.shippingInvoice;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.ShippingInvoice;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.ShippingInvoicePage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 物流发票
 * @Author: jeecg-boot
 * @Date: 2022-12-20
 * @Version: V1.0
 */
@Api(tags = "物流发票")
@RestController
@RequestMapping("/generated/shippingInvoice")
@Slf4j
public class ShippingInvoiceController {
    @Autowired
    private IClientService clientService;
    @Autowired
    private IPlatformOrderContentService platformOrderContentService;
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private ISavRefundService savRefundService;
    @Autowired
    private IShippingInvoiceService shippingInvoiceService;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    private EmailService emailService;
    private static final String EXTENSION = ".xlsx";
    @Value("${jeecg.path.shippingInvoiceDir}")
    private String INVOICE_LOCATION;
    @Value("${jeecg.path.shippingInvoiceDetailDir}")
    private String INVOICE_DETAIL_LOCATION;
    @Value("${jeecg.path.shippingInvoicePdfDir}")
    private String INVOICE_PDF_LOCATION;
    @Value("${jeecg.path.shippingInvoiceDetailPdfDir}")
    private String INVOICE_DETAIL_PDF_LOCAION;

    @Autowired
    Environment env;
    /**
     * 分页列表查询
     *
     * @param shippingInvoice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "物流发票-分页列表查询")
    @ApiOperation(value = "物流发票-分页列表查询", notes = "物流发票-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ShippingInvoice shippingInvoice,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ShippingInvoice> queryWrapper = QueryGenerator.initQueryWrapper(shippingInvoice, req.getParameterMap());
        Page<ShippingInvoice> page = new Page<ShippingInvoice>(pageNo, pageSize);
        IPage<ShippingInvoice> pageList = shippingInvoiceService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param shippingInvoicePage
     * @return
     */
    @AutoLog(value = "物流发票-添加")
    @ApiOperation(value = "物流发票-添加", notes = "物流发票-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ShippingInvoicePage shippingInvoicePage) {
        ShippingInvoice shippingInvoice = new ShippingInvoice();
        BeanUtils.copyProperties(shippingInvoicePage, shippingInvoice);
        shippingInvoiceService.saveMain(shippingInvoice);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param shippingInvoicePage
     * @return
     */
    @AutoLog(value = "物流发票-编辑")
    @ApiOperation(value = "物流发票-编辑", notes = "物流发票-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ShippingInvoicePage shippingInvoicePage) {
        ShippingInvoice shippingInvoice = new ShippingInvoice();
        BeanUtils.copyProperties(shippingInvoicePage, shippingInvoice);
        ShippingInvoice shippingInvoiceEntity = shippingInvoiceService.getById(shippingInvoice.getId());
        if (shippingInvoiceEntity == null) {
            return Result.error("未找到对应数据");
        }
        shippingInvoiceService.updateMain(shippingInvoice);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物流发票-通过id删除")
    @ApiOperation(value = "物流发票-通过id删除", notes = "物流发票-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        shippingInvoiceService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "物流发票-批量删除")
    @ApiOperation(value = "物流发票-批量删除", notes = "物流发票-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.shippingInvoiceService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物流发票-通过id查询")
    @ApiOperation(value = "物流发票-通过id查询", notes = "物流发票-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ShippingInvoice shippingInvoice = shippingInvoiceService.getById(id);
        if (shippingInvoice == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(shippingInvoice);

    }


    /**
     * 导出excel
     *
     * @param request
     * @param shippingInvoice
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShippingInvoice shippingInvoice) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<ShippingInvoice> queryWrapper = QueryGenerator.initQueryWrapper(shippingInvoice, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<ShippingInvoice> queryList = shippingInvoiceService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<ShippingInvoice> shippingInvoiceList = new ArrayList<ShippingInvoice>();
        if (oConvertUtils.isEmpty(selections)) {
            shippingInvoiceList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            shippingInvoiceList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<ShippingInvoicePage> pageList = new ArrayList<ShippingInvoicePage>();
        for (ShippingInvoice main : shippingInvoiceList) {
            ShippingInvoicePage vo = new ShippingInvoicePage();
            BeanUtils.copyProperties(main, vo);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "物流发票列表");
        mv.addObject(NormalExcelConstants.CLASS, ShippingInvoicePage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物流发票数据", "导出人:" + sysUser.getRealname(), "物流发票"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ShippingInvoicePage> list = ExcelImportUtil.importExcel(file.getInputStream(), ShippingInvoicePage.class, params);
                for (ShippingInvoicePage page : list) {
                    ShippingInvoice po = new ShippingInvoice();
                    BeanUtils.copyProperties(page, po);
                    shippingInvoiceService.saveMain(po);
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

    /** Finds the absolute path of invoice file by recursively walking the directory and it's subdirectories
     *
     * @param dirPath
     * @param invoiceNumber
     * @return List of paths for the file but should only find one result
     */
    public List<Path> getPath(String dirPath, String invoiceNumber) {
        List<Path> pathList = new ArrayList<>();
        //Recursively list all files
        //The walk() method returns a Stream by walking the file tree beginning with a given starting file/directory in a depth-first manner.
        try (Stream<Path> stream = Files.walk(Paths.get(dirPath))) {
            pathList = stream.map(Path::normalize)
                    .filter(Files::isRegularFile) // directories, hidden files and files without extension are not included
                    .filter(path -> path.getFileName().toString().contains(invoiceNumber))
                    .filter(path -> path.getFileName().toString().endsWith(EXTENSION))
                    .collect(Collectors.toList());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    /** Finds the absolute path of invoice file by recursively walking the directory and it's subdirectories
     *
     * @param dirPath
     * @param invoiceNumber
     * @return List of paths for the file but should only find one result
     */
    public List<Path> getPath(String dirPath, String invoiceNumber, String invoiceEntity) {
        List<Path> pathList = new ArrayList<>();
        //Recursively list all files
        //The walk() method returns a Stream by walking the file tree beginning with a given starting file/directory in a depth-first manner.
        try (Stream<Path> stream = Files.walk(Paths.get(dirPath))) {
            pathList = stream.map(Path::normalize)
                    .filter(Files::isRegularFile) // directories, hidden files and files without extension are not included
                    .filter(path -> path.getFileName().toString().contains(invoiceNumber))
                    .filter(path -> path.getFileName().toString().contains(invoiceEntity))
                    .filter(path -> path.getFileName().toString().endsWith(EXTENSION))
                    .collect(Collectors.toList());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    /**
     *  Finds the absolute path of invoice file and return the path
     * @param invoiceNumber
     * @param filetype if it's an invoice or invoice detail
     * @return String returns the path of the invoice file
     */
    public String getInvoiceList(String invoiceNumber, String filetype) {
        log.info("Invoice number : " + invoiceNumber);
        List<Path> pathList = new ArrayList<>();
        if(filetype.equals("invoice")) {
            log.info("File asked is of type invoice");
            pathList = getPath(INVOICE_LOCATION, invoiceNumber);
        }
        if(filetype.equals("detail")) {
            log.info("File asked is of type invoice detail");
            pathList = getPath(INVOICE_DETAIL_LOCATION, invoiceNumber);
        }
        if(pathList.isEmpty()) {
            log.error("NO INVOICE FILE FOUND : " + invoiceNumber);
            return "ERROR";
        }
        else {
            for (Path path : pathList) {
                log.info(path.toString());
            }
            return pathList.get(0).toString();
        }
    }

    /**
     * Downloads the invoice and returns it in form of bytearray
     * @param invoiceNumber the invoice we want to download
     * @param filetype invoice or invoice detail
     * @return ResponseEntity if success then returns bytearray of the file, else return ERROR 404
     * @throws IOException
     */
    @GetMapping(value = "/downloadCompleteInvoiceExcel")
    public ResponseEntity<?> download(@RequestParam("invoiceNumber") String invoiceNumber, @RequestParam("filetype") String filetype) throws IOException {
        String filename = getInvoiceList(invoiceNumber, filetype);
        if(!filename.equals("ERROR")) {
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
            log.error("Couldn't find the invoice file for : " + invoiceNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Couldn't find the invoice file for : " + invoiceNumber);
        }
    }

    public String convertToPdf(String invoiceNumber, String fileType) throws Exception {
        String excelFilePath = getInvoiceList(invoiceNumber, fileType);// (C:\PATH\filename.xlsx)

        if(!excelFilePath.equals("ERROR")) {
            String pdfFilePath= INVOICE_PDF_LOCATION + "/" + invoiceNumber + ".pdf";
            if(fileType.equals("invoice")){
                pdfFilePath = INVOICE_PDF_LOCATION + "/Invoice N°" + invoiceNumber + ".pdf";
            }
            if(fileType.equals("detail")) {
                pdfFilePath = INVOICE_DETAIL_PDF_LOCAION + "/Détail_calcul_de_facture_" + invoiceNumber + ".pdf";
            }

            Pattern p = Pattern.compile("^(.*)[\\/\\\\](.*)(\\.[a-z]+)"); //group(1): "C:\PATH" , group(2) : "filename", group(3): ".xlsx"
            Matcher m = p.matcher(excelFilePath);
            if (m.matches()) {
                pdfFilePath = INVOICE_PDF_LOCATION + "/" + m.group(2) + ".pdf";
            }
            // Créé un classeur pour charger le fichier Excel
            Workbook workbook = new Workbook(excelFilePath);
            // On enregistre le document au format PDF
            workbook.save(pdfFilePath, SaveFormat.PDF);
            return pdfFilePath;
        }
        return "ERROR";
    }
    /**
     *
     * @param invoiceNumber
     * @return
     */
    @GetMapping(value = "/downloadPdf")
    public ResponseEntity<?> downloadPdf(@RequestParam("invoiceNumber") String invoiceNumber) throws Exception {
        String pdfFilePath = convertToPdf(invoiceNumber, "invoice");
        if(!pdfFilePath.equals("ERROR")) {
            File file = new File(pdfFilePath);
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFilePath);
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            Path path = Paths.get(file.getAbsolutePath());

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Couldn't find the invoice file for : " + invoiceNumber);
    }

    @GetMapping(value = "/sendDetailsByEmail")
    public Result<?> sendDetailsByEmail(@RequestParam("invoiceNumber") String invoiceNumber,
                                        @RequestParam("invoiceID") String invoiceID,
                                        @RequestParam("email") String email,
                                        @RequestParam("invoiceEntity") String invoiceEntity) throws Exception {
        String filePath = getInvoiceList(invoiceNumber, "detail");
        String fileType = "Détails de facture";
        String subject = "Détails de facture N°" + invoiceNumber;
        Properties prop = emailService.getMailSender();
        Map <String, Object> templateModel = new HashMap<>();
        templateModel.put("fileType", fileType);
        templateModel.put("invoiceEntity", invoiceEntity);
        templateModel.put("invoiceID", invoiceID);
        templateModel.put("invoiceNumber", invoiceNumber);

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
            }
        });
        try {
            freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
            Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                    .getTemplate("invoiceDetailMail.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
            emailService.sendMessageWithAttachment(email, subject, htmlBody, filePath,session);
            return Result.OK("component.email.emailSent");
        }
        catch(Exception e) {
            return Result.error("An error occurred while trying to send an email.");
        }
    }

    /**
     * Fetches the shop owner's invoice entity or null via SQL
     * @param invoiceNumber Invoice number
     * @return if fetch successful returns invoice entity, else will return error
     */
    @GetMapping(value = "/getClient")
    public Result<?> getShopOwnerFromInvoice(@RequestParam("invoiceNumber") String invoiceNumber) {
        log.info("Invoice Number : " + invoiceNumber);
        Client client =  shippingInvoiceService.getShopOwnerFromInvoiceNumber(invoiceNumber);
        if(client == null) {
            log.error("Couldn't find shop owner from invoice number");
            return Result.error("Couldn't find shop owner from invoice number");
        }
        log.info("Found client for invoice {} : {}", invoiceNumber, client.fullName());
        return Result.OK(client);
    }

    /**
     * Deletes an invoice
     * @param invoiceNumber invoice number to cancel
     * @return if update successful
     */
    @PostMapping(value = "/cancelInvoice")
    public Result<?> cancelInvoice(@RequestParam("id") String id, @RequestParam("invoiceNumber") String invoiceNumber, @RequestParam("clientId") String clientId) {
        log.info("Cancelling invoice number : {}", invoiceNumber);
        platformOrderContentService.cancelInvoice(invoiceNumber);
        platformOrderService.cancelInvoice(invoiceNumber);
        savRefundService.cancelInvoice(invoiceNumber);
        shippingInvoiceService.delMain(id);
        log.info("Deleting invoice files ...");
        String invoiceEntity = clientService.getClientEntity(clientId);
        List<Path> invoicePathList = getPath(INVOICE_LOCATION, invoiceNumber, invoiceEntity);
        List<Path> detailPathList = getPath(INVOICE_DETAIL_LOCATION, invoiceNumber, invoiceEntity);
        boolean invoiceDeleted = false, detailDeleted = false;

        if(invoicePathList.isEmpty()) {
            log.error("FILE NOT FOUND : " + invoiceNumber);
        } else {
            for (Path path : invoicePathList) {
                log.info(path.toString());
            }
            try {
                File invoiceFile = new File(invoicePathList.get(0).toString());
                if(invoiceFile.delete()) {
                    log.info("Invoice file {} delete successful.", invoicePathList.get(0).toString());
                    invoiceDeleted = true;
                } else {
                    log.error("Invoice file delete fail.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(detailPathList.isEmpty()) {
            log.error("DETAIL FILE NOT FOUND : " + invoiceNumber);
        } else {
            for (Path path : detailPathList) {
                log.info(path.toString());
            }
            try {
                File detailFile = new File(detailPathList.get(0).toString());
                if(detailFile.delete()) {
                    log.info("Detail file {} delete successful.", detailPathList.get(0).toString());
                    detailDeleted = true;
                } else {
                    log.error("Detail file delete fail.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("Invoice files deleted.");
        return Result.ok("Invoice cancel successful." + (invoiceDeleted ? "" : " Failed to delete invoice file.") + (detailDeleted ? "" : " Failed to delete detail file."));
    }

    /**
     * Delete a batch of invoices
     * @param ids list of invoice ids
     * @param invoiceNumbers list of invoice numbers
     * @param clientIds list of clients
     * @return result
     */
    @PostMapping(value = "/cancelBatchInvoice")
    public Result<?> cancelBatchInvoice(@RequestParam("ids") List<String> ids, @RequestParam("invoiceNumbers") List<String> invoiceNumbers, @RequestParam("clientIds") List<String> clientIds) {

        log.info("Cancelling invoices : {}", invoiceNumbers);
        platformOrderContentService.cancelBatchInvoice(invoiceNumbers);
        platformOrderService.cancelBatchInvoice(invoiceNumbers);
        savRefundService.cancelBatchInvoice(invoiceNumbers);
        shippingInvoiceService.delBatchMain(ids);
        log.info("Deleting invoice files ...");

        for(int i = 0; i < ids.size(); i++) {
            String invoiceNumber = invoiceNumbers.get(i);
            String invoiceEntity = clientService.getClientEntity(clientIds.get(i));
            List<Path> invoicePathList = getPath(INVOICE_LOCATION, invoiceNumber, invoiceEntity);
            List<Path> detailPathList = getPath(INVOICE_DETAIL_LOCATION, invoiceNumber, invoiceEntity);

            if(invoicePathList.isEmpty()) {
                log.error("FILE NOT FOUND : " + invoiceNumber + ", " +  invoiceEntity);
            } else {
                for (Path path : invoicePathList) {
                    log.info(path.toString());
                }
                try {
                    File invoiceFile = new File(invoicePathList.get(0).toString());
                    if(invoiceFile.delete()) {
                        log.info("Invoice file {} delete successful.", invoicePathList.get(0).toString());
                    } else {
                        log.error("Invoice file delete fail.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(detailPathList.isEmpty()) {
                log.error("DETAIL FILE NOT FOUND : " + invoiceNumber + ", " +  invoiceEntity);
            } else {
                for (Path path : detailPathList) {
                    log.info(path.toString());
                }
                try {
                    File detailFile = new File(detailPathList.get(0).toString());
                    if(detailFile.delete()) {
                        log.info("Detail file {} delete successful.", detailPathList.get(0).toString());
                    } else {
                        log.error("Detail file {} delete fail.", detailPathList.get(0).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("End of invoice files deletion.");
        return Result.ok("Invoices cancellation finished.");
    }
}