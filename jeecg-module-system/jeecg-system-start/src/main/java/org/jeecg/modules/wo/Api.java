package org.jeecg.modules.wo;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.wo.menu.entity.WoMenu;
import org.jeecg.modules.wo.menu.service.IWoMenuService;
import org.jeecg.modules.wo.order.entity.WoOrder;
import org.jeecg.modules.wo.order.entity.WoOrderDetail;
import org.jeecg.modules.wo.order.service.IWoOrderDetailService;
import org.jeecg.modules.wo.order.service.IWoOrderService;
import org.jeecg.modules.wo.order.vo.WoOrderPage;
import org.jeecg.modules.wo.queue.entity.WoQueueRecord;
import org.jeecg.modules.wo.queue.service.IWoQueueRecordService;
import org.jeecg.modules.wo.store.entity.WoStore;
import org.jeecg.modules.wo.store.service.IWoStoreService;
import org.jeecg.modules.wo.store.service.impl.WoStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@io.swagger.annotations.Api(tags="小程序点餐排队API")
@RestController
@RequestMapping("/api")
public class Api {

    @Autowired
    private IWoStoreService woStoreService;

    @Autowired
    private IWoOrderService woOrderService;

    @Autowired
    private IWoOrderDetailService woOrderDetailService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IWoQueueRecordService woQueueRecordService;

    @Autowired
    private IWoMenuService menuService;


    @ApiOperation(value="获取全部门店", notes="获取全部门店")
    @GetMapping(value = "/listAllStore")
    public Result<List<WoStore>> listAllStore() {
        QueryWrapper<WoStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<WoStore> list = woStoreService.list(queryWrapper);
        return Result.OK(list);
    }


    @ApiOperation(value="通过用户获取附件门店", notes="通过用户获取附件门店")
    @GetMapping(value = "/getNearybyStore")
    public Result<WoStore> getNearybyStore(@RequestParam String lat, @RequestParam String lon) {
        return null;
    }

    @ApiOperation(value="根据门店获取全部分类", notes="根据门店获取全部分类")
    @GetMapping(value = "/listAllCategory")
    public Result<List<String>> listAllCategory() {
        return null;
    }


    @ApiOperation(value="根据门店获取带分类菜品", notes="根据门店获取带分类菜品")
    @GetMapping(value = "/{storeId}/listMenus")
    public Result<List<WoMenu>> listMenus(@PathVariable Integer storeId) {
        return null;
    }


    @ApiOperation(value="通过用户订单列表", notes="通过用户订单列表")
    @GetMapping(value = "/{openId}/listOrders")
    public Result<List<WoOrder>> listOrders(@PathVariable String openId) {
        return null;
    }

    @ApiOperation(value="用户下单", notes="用户下单")
    @PostMapping(value = "/createOrder")
    @Transactional
    public Result<?> createOrder(@RequestBody WoOrder woOrder) {
        List<WoOrderDetail> details = woOrder.getDetails();
        QueryWrapper<WoMenu> queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", woOrder.getStoreId());
        List<WoMenu> menuList = menuService.list(queryWrapper);
        Map<Integer, Double> idPriceDict = menuList.stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getPrice(), (x, y) -> y));
        Double amount = details.stream().map(t -> t.getNum() * (idPriceDict.get(t.getMenuId()))).collect(Collectors.summingDouble(t -> t));
        woOrder.setOrderSeq(woOrder.generateSeq());
        woOrder.setStatus(1);
        woOrder.setPaymentAmount(amount);
        boolean save = woOrderService.save(woOrder);

        if(save) {
            details.forEach(item -> {
                item.setOrderId(woOrder.getId());
            });
            woOrderDetailService.saveBatch(details);
            return Result.ok("ok");
        }else {
            return Result.error("fail");
        }
    }

    @GetMapping(value = "/testCreateOrder")
    public Result<?> testCreateOrder(@RequestParam Integer memberId, @RequestParam Integer storeId) {
        Integer table = 1;
        DateTime date1 = DateUtil.parseDateTime("2023-04-20 10:00:00");

        QueryWrapper<WoMenu> queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", storeId);
        List<WoMenu> menuList = menuService.list(queryWrapper);

        for( ; table <= 2; table ++) {
            WoOrder order = new WoOrder();
            order.setMemberId(memberId);
            order.setStoreId(storeId);
            order.setTableNum(table);
            int m = new Random().nextInt(10);
            date1 = new DateTime(date1.getTime() + m*60*1000);
            order.setCreateTime(date1);

            List<WoOrderDetail> details = new ArrayList<>();
            int dSize = new Random().nextInt(4);
            dSize = dSize < 1 ? 1 : dSize;
            for(;dSize>0;dSize--) {
                WoOrderDetail d = new WoOrderDetail();
                WoMenu menu = menuList.get(menuList.size() - dSize);
                d.setMenuId(menu.getId());
                d.setTaste(menu.getTaste());
                d.setNum(1);
                details.add(d);
            }
            order.setDetails(details);
            createOrder(order);
        }

        return Result.ok();
    }


    @ApiOperation(value="获取订单详情", notes="获取订单详情")
    @GetMapping(value = "/{orderId}/detail")
    public Result<List<WoOrderPage>> listOrders(@PathVariable Integer orderId) {
        return null;
    }

    @ApiOperation(value="用户排队", notes="用户排队")
    @PostMapping(value = "/queue")
    public synchronized Result<?> queue(@RequestBody WoQueueRecord queueRecord) {
        Integer storeId = queueRecord.getStoreId();
        Integer maxQueueNum = jdbcTemplate.queryForObject("select ifnull(max(code), 0) as code from wo_queue_record where store_id=?", Integer.class, storeId);
        queueRecord.setCode(maxQueueNum++);
        queueRecord.setDelFlag(0);
        woQueueRecordService.save(queueRecord);
        return null;
    }


    @ApiOperation(value="获取门店排队结果", notes="获取门店排队结果")
    @GetMapping(value = "/{storeId}/detail")
    public Result<List<WoQueueRecord>> listQueueList(@PathVariable Integer storeId) {
        return null;
    }








    

}
