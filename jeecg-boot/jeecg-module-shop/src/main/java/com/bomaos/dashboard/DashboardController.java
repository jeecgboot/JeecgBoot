package com.bomaos.dashboard;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bomaos.common.core.enmu.*;
import com.bomaos.common.core.utils.DateUtil;
import com.bomaos.common.core.web.BaseController;
import com.bomaos.orders.entity.Orders;
import com.bomaos.orders.service.OrdersService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private OrdersService ordersService;

    @RequiresPermissions("dashboard:user:view")
    @RequestMapping("/workplace")
    public String view(Model model) throws ParseException {

        /**
         * 今日订单
         */
        Map<String, Object> orderList = getOrderList(ordersService);
        Integer count = (Integer) orderList.get("count");// 今日成功订单数量
        BigDecimal money = (BigDecimal) orderList.get("money"); // 今日成功金额
        model.addAttribute("count", count);
        model.addAttribute("money", money);

        /**
         * 昨日订单
         */
        Map<String, Object> yesterDayOrder = getYesterDayOrder(ordersService);
        Integer YesterDayCount = (Integer) yesterDayOrder.get("YesterDayCount");// 昨天成功订单数量
        BigDecimal YesterDayMoney = (BigDecimal) yesterDayOrder.get("YesterDayMoney"); // 昨天成功金额
        model.addAttribute("YesterDayCount", YesterDayCount);
        model.addAttribute("YesterDayMoney", YesterDayMoney);

        /**
         * 近七天订单
         */
        Map<String, Object> sevenDaysOrder = getSevenDaysOrder(ordersService);
        Integer SevenDaysCount = (Integer) sevenDaysOrder.get("SevenDaysCount");// 近七天成功订单数量
        BigDecimal SevenDaysMoney = (BigDecimal) sevenDaysOrder.get("SevenDaysMoney"); // 近七天成功金额
        model.addAttribute("SevenDaysCount", SevenDaysCount);
        model.addAttribute("SevenDaysMoney", SevenDaysMoney);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        List<String> dayList = new ArrayList<>(); // 天
        List<BigDecimal> wxpayList = new ArrayList<>(); // 每天微信的总金额
        List<BigDecimal> alipayList = new ArrayList<>(); // 每天支付宝的总金额
        List<BigDecimal> paypalList = new ArrayList<>(); // 每天贝宝的总金额
        List<BigDecimal> qqpayList = new ArrayList<>(); // 每天qq钱包的总金额
        List<BigDecimal> usdtList = new ArrayList<>(); // 每天usdt的总金额

        Integer wxpayAll = 0; // 七天微信的交易量
        Integer alipayAll = 0; // 七天支付宝的交易量
        Integer paypalAll = 0; // 七天贝宝的交易量
        Integer qqpayAll = 0; // 七天贝宝的交易量
        Integer usdtAll = 0; // 七天贝宝的交易量

        for (int i = 0; i < 7; i++) {
            Date startDayTime = DateStrUtil.getStartDayTime(-+i);
            Date endDayTime = DateStrUtil.getEndDayTime(-+i);
            Map<String, BigDecimal> timeDayList = getTimeDayList(startDayTime, endDayTime, ordersService);
            String day = simpleDateFormat.format(DateUtil.getStartDayTime(-+i));
            BigDecimal wxpay = timeDayList.get("wxpay");
            BigDecimal alipay = timeDayList.get("alipay");
            BigDecimal paypal = timeDayList.get("paypal");
            BigDecimal qqpay = timeDayList.get("qqpay");
            BigDecimal usdt = timeDayList.get("usdt");

            Map<String, Integer> timeDayCount = getTimeDayCount(startDayTime, endDayTime, ordersService);
            Integer wxpay1 = timeDayCount.get("wxpay");
            Integer alipay1 = timeDayCount.get("alipay");
            Integer paypal1 = timeDayCount.get("paypal");
            Integer qqpay1 = timeDayCount.get("qqpay");
            Integer usdt1 = timeDayCount.get("usdt");

            wxpayAll += wxpay1;
            alipayAll += alipay1;
            paypalAll += paypal1;
            qqpayAll += qqpay1;
            usdtAll += usdt1;

            wxpayList.add(wxpay);
            alipayList.add(alipay);
            paypalList.add(paypal);
            qqpayList.add(qqpay);
            usdtList.add(usdt);
            dayList.add(day);
        }

        List<Map> mapList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("value", wxpayAll.toString());
        map.put("name", "微信");
        mapList.add(map);

        Map<String, String> map1 = new HashMap<>();
        map1.put("value", alipayAll.toString());
        map1.put("name", "支付宝");
        mapList.add(map1);

        Map<String, String> map2 = new HashMap<>();
        map2.put("value", paypalAll.toString());
        map2.put("name", "Paypal");
        mapList.add(map2);

        Map<String, String> map3 = new HashMap<>();
        map3.put("value", qqpayAll.toString());
        map3.put("name", "QQ钱包 ");
        mapList.add(map3);

        Map<String, String> map4 = new HashMap<>();
        map4.put("value", usdtAll.toString());
        map4.put("name", "USDT ");
        mapList.add(map4);

        List<Orders> ordersList = ordersService.list(new QueryWrapper<Orders>().ge("status", 1));
        BigDecimal total_amount = new BigDecimal(0.00); // 获取今天成功交易的订单交易额
        for (Orders orders : ordersList) {
            total_amount = total_amount.add(new BigDecimal(orders.getMoney().toString())); // 统计今天的交易额
        }

        model.addAttribute("mapList", JSON.toJSONString(mapList));
        model.addAttribute("dayList", JSON.toJSONString(dayList));
        model.addAttribute("wxpayList", JSON.toJSONString(wxpayList));
        model.addAttribute("alipayList", JSON.toJSONString(alipayList));
        model.addAttribute("paypalList", JSON.toJSONString(paypalList));
        model.addAttribute("qqpayList", JSON.toJSONString(qqpayList));
        model.addAttribute("usdtList", JSON.toJSONString(usdtList));
        model.addAttribute("total_amount", total_amount.toString());
        model.addAttribute("total_number", ordersService.count(Wrappers.<Orders>lambdaQuery().eq(Orders::getStatus, 1)));

        model.addAttribute("user", getLoginUser());
        return "dashboard/workplace.html";
    }

    /**
     * 获取今天的交易订单
     *
     * @return
     */
    public static Map<String, Object> getOrderList(OrdersService ordersService) {
        QueryWrapper queryWrapper = getQueryWrapper(DateStrUtil.getDayBegin(), DateStrUtil.getDayEnd());
        queryWrapper.ge("status", 1);
        List<Orders> orderList = ordersService.list(queryWrapper);
        Integer count = 0; // 获取今天成功交易的订单数量
        BigDecimal money = new BigDecimal(0.00); // 获取今天成功交易的订单交易额
        for (Orders orders : orderList) {
            money = money.add(new BigDecimal(orders.getMoney().toString())); // 统计今天的交易额
            count++; // 统计成功交易的订单数量
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("money", money.setScale(2, BigDecimal.ROUND_HALF_DOWN));

        return map;
    }

    /**
     * 获取昨天的交易订单
     *
     * @return
     */
    public static Map<String, Object> getYesterDayOrder(OrdersService ordersService) {
        QueryWrapper queryWrapper = getQueryWrapper(DateStrUtil.getStartDayTime(-1), DateStrUtil.getEndDayTime(-1));
        queryWrapper.ge("status", 1);

        List<Orders> orderList = ordersService.list(queryWrapper);
        Integer count = 0; // 获取昨天成功交易的订单数量
        BigDecimal money = new BigDecimal(0.00); // 获取今天成功交易的订单交易额
        for (Orders orders : orderList) {
            money = money.add(new BigDecimal(orders.getMoney().toString())); // 统计今天的交易额
            count++; // 统计昨天成功交易的订单数量
        }

        Map<String, Object> map = new HashMap<>();
        map.put("YesterDayCount", count);
        map.put("YesterDayMoney", money.setScale(2, BigDecimal.ROUND_HALF_DOWN));

        return map;
    }

    /**
     * 获取昨天的交易订单
     *
     * @return
     */
    public static Map<String, Object> getSevenDaysOrder(OrdersService ordersService) {

        QueryWrapper queryWrapper = getQueryWrapper(DateStrUtil.getStartDayTime(-7), DateStrUtil.getEndDayTime(-0));
        queryWrapper.ge("status", 1);

        List<Orders> orderList = ordersService.list(queryWrapper);

        Integer count = 0; // 获取近七天成功交易的订单数量
        BigDecimal money = new BigDecimal(0.00); // 获取今天成功交易的订单交易额
        for (Orders orders : orderList) {
            money = money.add(new BigDecimal(orders.getMoney().toString())); // 统计今天的交易额
            count++; // 统计近七天成功交易的订单数量
        }

        Map<String, Object> map = new HashMap<>();
        map.put("SevenDaysCount", count);
        map.put("SevenDaysMoney", money.setScale(2, BigDecimal.ROUND_HALF_DOWN));

        return map;
    }

    public static Map<String, BigDecimal> getTimeDayList(Date StartTime, Date EndTime, OrdersService ordersService) {
        QueryWrapper queryWrapper = getQueryWrapper(StartTime, EndTime);
        queryWrapper.ge("status", 1);

        //查询当天所有支付记录
        List<Orders> ordersList = ordersService.list(queryWrapper);
        BigDecimal bigWxpay = new BigDecimal(0.00);
        BigDecimal bigAlipay = new BigDecimal(0.00);
        BigDecimal bigPaypal = new BigDecimal(0.00);
        BigDecimal bigQQPay = new BigDecimal(0.00);
        BigDecimal bigUSDT = new BigDecimal(0.00);
        for (Orders orders : ordersList) {
            if (Wxpay.getByValue(orders.getPayType())) { // 微信
                bigWxpay = bigWxpay.add(new BigDecimal(orders.getMoney().toString())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            } else if (Alipay.getByValue(orders.getPayType())) { // 支付宝
                bigAlipay = bigAlipay.add(new BigDecimal(orders.getMoney().toString())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            } else if (Paypal.getByValue(orders.getPayType())) {
                bigPaypal = bigPaypal.add(new BigDecimal(orders.getMoney().toString())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            } else if (QQPay.getByValue(orders.getPayType())) {
                bigQQPay = bigQQPay.add(new BigDecimal(orders.getMoney().toString())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            } else if (USDT.getByValue(orders.getPayType())) {
                bigUSDT = bigUSDT.add(new BigDecimal(orders.getMoney().toString())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            }
        }

        Map<String, BigDecimal> map = new HashMap<>();
        map.put("wxpay", bigWxpay);
        map.put("alipay", bigAlipay);
        map.put("paypal", bigPaypal);
        map.put("qqpay", bigQQPay);
        map.put("usdt", bigUSDT);
        return map;
    }

    public static Map<String, Integer> getTimeDayCount(Date StartTime, Date EndTime, OrdersService ordersService) {
        QueryWrapper queryWrapper = getQueryWrapper(StartTime, EndTime);
        queryWrapper.ge("status", 1);

        //查询当天所有支付记录
        List<Orders> ordersList = ordersService.list(queryWrapper);
        Integer wxpay = 0;
        Integer alipay = 0;
        Integer paypal = 0;
        Integer qqpay = 0;
        Integer usdt = 0;
        for (Orders orders : ordersList) {
            if (Wxpay.getByValue(orders.getPayType())) { // 微信
                wxpay++;
            } else if (Alipay.getByValue(orders.getPayType())) { // 支付宝
                alipay++;
            } else if (Paypal.getByValue(orders.getPayType())) {
                paypal++;
            } else if (QQPay.getByValue(orders.getPayType())) {
                qqpay++;
            } else if (USDT.getByValue(orders.getPayType())) {
                usdt++;
            }
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("wxpay", wxpay);
        map.put("alipay", alipay);
        map.put("paypal", paypal);
        map.put("qqpay", qqpay);
        map.put("usdt", usdt);
        return map;
    }

    /**
     * 根据时间查询今天的数据
     *
     * @param StartTime 今天开始的时间
     * @param EndTime   今天结束的时间
     * @return queryWrapper
     */
    public static QueryWrapper getQueryWrapper(Date StartTime, Date EndTime) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String DayStartTime = formatter.format(StartTime);
        String DayEndTime = formatter.format(EndTime);

        //查询条件为时间范围
        queryWrapper.apply("UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + DayStartTime + "')");
        queryWrapper.apply("UNIX_TIMESTAMP(create_time) < UNIX_TIMESTAMP('" + DayEndTime + "')");

        return queryWrapper;
    }

}
