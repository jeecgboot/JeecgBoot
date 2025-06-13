package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.Balance;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.ClientPage;
import org.jeecg.modules.business.vo.PlatformOrderOption;
import org.jeecg.modules.online.cgform.mapper.OnlCgformFieldMapper;
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

    private final IBalanceService balanceService;

    private final IPlatformOrderService platformOrderService;

    @Autowired
    public ClientController(IClientService clientService, IShopService shopService, IClientSkuService clientSkuService, IBalanceService balanceService, IPlatformOrderService platformOrderService) {
        this.clientService = clientService;
        this.shopService = shopService;
        this.clientSkuService = clientSkuService;
        this.balanceService = balanceService;
        this.platformOrderService = platformOrderService;
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
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ClientPage clientPage) {
        Client client = new Client();
        BeanUtils.copyProperties(clientPage, client);
        clientService.saveMain(client, clientPage.getShopList(), clientPage.getClientSkuList());
        String useBalance = clientPage.getUseBalance();
        log.info("useBalance:{}", useBalance);
        if ("1".equals(useBalance)) {
            balanceService.initBalance(client.getId());
        }
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
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ClientPage clientPage) {
        Client client = new Client();
        BeanUtils.copyProperties(clientPage, client);
        Client clientEntity = clientService.getById(client.getId());
        if (clientEntity == null) {
            return Result.error("未找到对应数据");
        }
        if (client.getUseBalance() != null && "1".equals(clientPage.getUseBalance())){
            // If useBalance is set to 1, initialize balance for the client
            balanceService.initBalance(client.getId());
        }
        clientService.updateMain(client, clientPage.getShopList(), clientPage.getClientSkuList());
        updateShopId();
        log.info("useBalance from clientPage: {}, useBalance updated for client: {}", clientPage.getUseBalance(), client.getUseBalance());
        log.info("Shop names replaced by new created shop IDs");
        return Result.OK("编辑成功!");
    }

    /**
     * Call a routine to replace shop names (from MabangAPI)
     * by shop IDs in platform_order table after creating a new shop
     */
    private static void updateShopId() {
        OnlCgformFieldMapper onlCgformFieldMapper = SpringContextUtils.getBean(OnlCgformFieldMapper.class);
        Map<String, Object> params = new HashMap<>();
        String sql = "UPDATE platform_order SET shop_id = shopErpToId(shop_id) WHERE shop_id NOT LIKE '1%'";
        params.put("execute_sql_string", sql);
        onlCgformFieldMapper.executeUpdatetSQL(params);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        // check if the client has any shops with orders
        List<String> deletedShopIds = new ArrayList<>();
        List<String> deletedBalanceIds;
        List<Shop> shopList = shopService.listByClient(id);
        if (shopList != null && !shopList.isEmpty()) {
            for (Shop shop : shopList) {
                Integer ordersAmount = platformOrderService.countOrdersByShop(shop.getId());
                log.info("checking shop: {}, ordersAmount: {}", shop.getName(), ordersAmount);
                if (ordersAmount != 0) {
                    return Result.error("客户的店铺" + shop.getName() + "存在订单，无法删除");
                }
            }
            deletedShopIds = shopList.stream().map(Shop::getId).collect(Collectors.toList());
            shopService.removeByIds(deletedShopIds);
        }
        // delete client balance
        List<Balance> balances = balanceService.list(
                new QueryWrapper<Balance>().eq("client_id", id)
        );
        deletedBalanceIds = balances.stream().map(Balance::getId).collect(Collectors.toList());
        balanceService.deleteBalanceByClientId(id);
        clientService.delMain(id);
        log.info("Deleted Client: {}, Deleted Shops: {}, Deleted Balances: {}",
                id, deletedShopIds, deletedBalanceIds);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> clientIds = Arrays.asList(ids.split(","));
        List<String> deletedClientIds = new ArrayList<>();

        for (String clientId : clientIds) {
            Client client = clientService.getById(clientId);
            if (client == null) {
                continue;
            }
            // check if the client has any shops with orders
            List<String> deletedShopIds = new ArrayList<>();
            List<String> deletedBalanceIds;
            List<Shop> shopList = shopService.listByClient(clientId);
            if (shopList != null && !shopList.isEmpty()) {
                for (Shop shop : shopList) {
                    Integer ordersAmount = platformOrderService.countOrdersByShop(shop.getId());
                    log.info("checking shop: {}, ordersAmount: {}", shop.getName(), ordersAmount);
                    if (ordersAmount > 0) {
                        return Result.error("客户的店铺" + shop.getName() + "存在订单，无法删除");
                    }
                }
                deletedShopIds = shopList.stream().map(Shop::getId).collect(Collectors.toList());
                shopService.removeByIds(deletedShopIds);
            }
            // delete client balance
            List<Balance> balances = balanceService.list(
                    new QueryWrapper<Balance>().eq("client_id", clientId)
            );
            deletedBalanceIds = balances.stream().map(Balance::getId).collect(Collectors.toList());
            balanceService.deleteBalanceByClientId(clientId);
            log.info("Deleted Client: {}, Deleted Shops: {}, Deleted Balances: {}",
                    clientId, deletedShopIds, deletedBalanceIds);
        }
        this.clientService.delBatchMain(clientIds);
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
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
