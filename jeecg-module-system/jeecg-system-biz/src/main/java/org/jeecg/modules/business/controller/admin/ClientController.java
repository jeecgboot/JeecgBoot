package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.IClientSkuService;
import org.jeecg.modules.business.service.IShopService;
import org.jeecg.modules.business.vo.ClientPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 客户
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
@Api(tags = "客户")
@RestController
@RequestMapping("/client/client")
@Slf4j
public class ClientController {
    private final IClientService clientService;

    private final IShopService shopService;

    private final IClientSkuService clientSkuService;

    @Autowired
    public ClientController(IClientService clientService, IShopService shopService, IClientSkuService clientSkuService) {
        this.clientService = clientService;
        this.shopService = shopService;
        this.clientSkuService = clientSkuService;
    }


    /**
     * 分页列表查询
     *
     * @param client
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "客户-分页列表查询")
    @ApiOperation(value = "客户-分页列表查询", notes = "客户-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Client client,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Client> queryWrapper = QueryGenerator.initQueryWrapper(client, req.getParameterMap());
        Page<Client> page = new Page<Client>(pageNo, pageSize);
        IPage<Client> pageList = clientService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param clientPage
     * @return
     */
    @AutoLog(value = "客户-添加")
    @ApiOperation(value = "客户-添加", notes = "客户-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ClientPage clientPage) {
        Client client = new Client();
        BeanUtils.copyProperties(clientPage, client);
        clientService.saveMain(client, clientPage.getShopList(), clientPage.getClientSkuList());
        return Result.OK("添加成功！");
    }

    /**
     * List of all clients
     * @return list of all clients
     */
    @GetMapping(value = "/all")
    public Result<List<Client>> all(){
        List<Client> list = clientService.list().stream()
                .filter(client -> client.getActive().equals("1"))
                .sorted(Comparator.comparing(Client::getInternalCode))
                .collect(Collectors.toList());
        return Result.OK(list);
    }

    /**
     * 编辑
     *
     * @param clientPage
     * @return
     */
    @AutoLog(value = "客户-编辑")
    @ApiOperation(value = "客户-编辑", notes = "客户-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ClientPage clientPage) {
        Client client = new Client();
        BeanUtils.copyProperties(clientPage, client);
        Client clientEntity = clientService.getById(client.getId());
        if (clientEntity == null) {
            return Result.error("未找到对应数据");
        }
        clientService.updateMain(client, clientPage.getShopList(), clientPage.getClientSkuList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客户-通过id删除")
    @ApiOperation(value = "客户-通过id删除", notes = "客户-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        clientService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "客户-批量删除")
    @ApiOperation(value = "客户-批量删除", notes = "客户-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.clientService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客户-通过id查询")
    @ApiOperation(value = "客户-通过id查询", notes = "客户-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Client client = clientService.getById(id);
        if (client == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(client);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "店铺-通过主表ID查询")
    @ApiOperation(value = "店铺-通过主表ID查询", notes = "店铺-通过主表ID查询")
    @GetMapping(value = "/queryShopByMainId")
    public Result<?> queryShopListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<Shop> shopList = shopService.selectByMainId(id);
        IPage<Shop> page = new Page<>();
        page.setRecords(shopList);
        page.setTotal(shopList.size());
        return Result.OK(page);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客户名下SKU-通过主表ID查询")
    @ApiOperation(value = "客户名下SKU-通过主表ID查询", notes = "客户名下SKU-通过主表ID查询")
    @GetMapping(value = "/queryClientSkuByMainId")
    public Result<?> queryClientSkuListByMainId(@RequestParam(name = "id", required = true) String id) {
        log.info(id);
        List<ClientSku> clientSkuList = clientSkuService.selectByMainId(id);
        log.info(clientSkuList.toString());
        IPage<ClientSku> page = new Page<>();
        page.setRecords(clientSkuList);
        page.setTotal(clientSkuList.size());
        return Result.OK(page);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param client
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Client client) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<Client> queryWrapper = QueryGenerator.initQueryWrapper(client, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<Client> queryList = clientService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<Client> clientList = new ArrayList<Client>();
        if (oConvertUtils.isEmpty(selections)) {
            clientList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            clientList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<ClientPage> pageList = new ArrayList<ClientPage>();
        for (Client main : clientList) {
            ClientPage vo = new ClientPage();
            BeanUtils.copyProperties(main, vo);
            List<Shop> shopList = shopService.selectByMainId(main.getId());
            vo.setShopList(shopList);
            List<ClientSku> clientSkuList = clientSkuService.selectByMainId(main.getId());
            vo.setClientSkuList(clientSkuList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "客户列表");
        mv.addObject(NormalExcelConstants.CLASS, ClientPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客户数据", "导出人:" + sysUser.getRealname(), "客户"));
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
                List<ClientPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ClientPage.class, params);
                for (ClientPage page : list) {
                    Client po = new Client();
                    BeanUtils.copyProperties(page, po);
                    clientService.saveMain(po, page.getShopList(), page.getClientSkuList());
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

}
