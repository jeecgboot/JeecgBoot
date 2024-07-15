package com.vone.mq.service;

import com.vone.mq.dao.*;
import com.vone.mq.dto.CommonRes;
import com.vone.mq.dto.PageRes;
import com.vone.mq.entity.PayGoods;
import com.vone.mq.entity.PayOrder;
import com.vone.mq.entity.PayQrcode;
import com.vone.mq.entity.Setting;
import com.vone.mq.utils.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import javax.persistence.criteria.Predicate;
import java.text.NumberFormat;
import java.util.*;

@Slf4j
@Service
public class AdminService {

    @Autowired
    private SettingDao settingDao;
    @Autowired
    private PayGoodsDao payGoodsDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private TmpPriceDao tmpPriceDao;
    @Autowired
    private PayQrcodeDao payQrcodeDao;

    @Autowired
    private WebService webService;

    public CommonRes login(String user,String pass){
        String password = "";
        Map<String,Object> userMap = new HashMap<>();
        Map<String,Object> map = settingDao.getUserInfo(user);
        userMap.putAll(map);
        if (userMap.isEmpty()) {
            return ResUtil.error("账号【"+user+"】不存在");
        }

        password = (String) userMap.get("password");
        Boolean status = (Boolean) userMap.get("status");
        if (!status) {
            return ResUtil.error("当前用户已冻结");
        }
        String salt = (String) userMap.get("salt");
        log.info(user,pass,salt,PasswordUtil.encrypt(user, pass, salt));
        pass = PasswordUtil.encrypt(user, pass, salt);
        if (!pass.equals(password)) {
            return ResUtil.error("账号或密码不正确");
        }
        userMap.remove("status");
        userMap.remove("salt");
        userMap.remove("password");
        return ResUtil.success(userMap);
    }

    @SneakyThrows
    public CommonRes regist(String user,String pass){
        Map<String,Object> map = settingDao.getUserInfo(user);
        if (!map.isEmpty()) {
            log.info("{}",map);
            return ResUtil.error("账号【"+user+"】已存在");
        }
        String salt = PasswordUtil.randomGen(8);
        String password = PasswordUtil.encrypt(user,pass,salt);
        settingDao.regist(user,password,salt);
        CommonRes res = ResUtil.success();
        res.setMsg("注册成功");
        return res;
    }


    public CommonRes saveSetting(Setting setting){
        settingDao.save(setting);
        return ResUtil.success();
    }

    public CommonRes getSettings(String username){
        Setting query = new Setting();
        query.setUsername(username);
        Example<Setting> example = Example.of(query);
        Optional<Setting> settings = settingDao.findOne(example);
        if (!settings.isEmpty()) {
            return ResUtil.success(settings.get());
        }
        return ResUtil.success(null);
    }

    public PageRes getOrders(String username,Integer page, Integer limit, Integer type, Integer state){

        Pageable pageable = PageRequest.of(page-1, limit, Sort.Direction.DESC, "id");

        Specification<PayOrder> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.in(root.get("username")).value(username).value("guest"));
            if (type!=null) {
                list.add(cb.equal(root.get("type").as(int.class), type));
            }

            if (state!=null) {
                list.add(cb.equal(root.get("state").as(int.class), state));
            }
            return cb.and(list.toArray(new Predicate[list.size()]));
        };
        Page<PayOrder> payOrders = payOrderDao.findAll(specification, pageable);

        PageRes list = PageRes.success(payOrders.getTotalElements(),payOrders.getContent());
        return list;
    }

    public PageRes getGoods(String username,Integer page, Integer limit, String name, Integer state){

        Pageable pageable = PageRequest.of(page-1, limit, Sort.Direction.DESC, "id");

        Specification<PayGoods> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(cb.in(root.get("username")).value(username).value("guest"));
            if (StringUtils.isNotBlank(name)) {
                list.add(cb.equal(root.get("name").as(String.class), name));
            }
            if (state!=null) {
                list.add(cb.equal(root.get("state").as(int.class), state));
            }
            return cb.and(list.toArray(new Predicate[list.size()]));
        };
        Page<PayGoods> goodsPage = payGoodsDao.findAll(specification, pageable);

        PageRes list = PageRes.success(goodsPage.getTotalElements(),goodsPage.getContent());
        return list;
    }


    public CommonRes setBd(Integer id,String username){
        PayOrder payOrder = payOrderDao.getById(id);
        if (payOrder==null){
            return ResUtil.error("订单不存在");
        }
        if (!validUserPermission(username,payOrder.getUsername())) {
            return ResUtil.error("抱歉，您无权操作其它商家订单");
        }
        Setting setting = webService.getDefaultSetting(username);
        String key = webService.getDefaultMd5Key(username);
        String p = "payId="+payOrder.getPayId()+"&orderId="+payOrder.getOrderId()+"&param="+payOrder.getParam()+"&type="+payOrder.getType()+"&price="+payOrder.getPrice()+"&reallyPrice="+payOrder.getReallyPrice();
        String sign = payOrder.getPayId()+payOrder.getParam()+payOrder.getType()+payOrder.getPrice()+payOrder.getReallyPrice()+key;
        p = p+"&sign="+md5(sign);

        String url = payOrder.getNotifyUrl();
        if (url==null || url.equals("")){
            url = setting.getNotifyUrl();
            if (url==null || url.equals("")){
                return ResUtil.error("您还未配置异步通知地址，请现在系统配置中配置");
            }
        }

        String res = HttpRequest.sendPost(url,p);

        if (res!=null && res.equals("success")){
            if (payOrder.getState()==0){
                tmpPriceDao.delprice(username,payOrder.getType()+"-"+payOrder.getReallyPrice());
            }
            payOrderDao.setState(1,payOrder.getId());
            return ResUtil.success();
        }else{
            return ResUtil.error(-2,res);
        }

    }

    public CommonRes addPayQrcode(PayQrcode payQrcode){
        if (payQrcode.getPayUrl()==null){
            return ResUtil.error();
        }
        if (payQrcode.getPrice()==0){
            return ResUtil.error();
        }
        if (payQrcode.getType()==0){
            return ResUtil.error();
        }
        payQrcodeDao.save(payQrcode);
        return ResUtil.success();
    }

    public CommonRes getMain(String username){
        Calendar currentDate = new GregorianCalendar();

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        Date tmp = currentDate.getTime();
        String startDate = String.valueOf(tmp.getTime());
        currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        tmp = currentDate.getTime();
        String endDate = String.valueOf(tmp.getTime());

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        int todayOrder = payOrderDao.getTodayCount(startDate,endDate,username);//当日总订单

        int todaySuccessOrder =  payOrderDao.getTodayCount(startDate,endDate,1,username);//当日成功订单
        int todaySuccessOrder2 =  payOrderDao.getTodayCount(startDate,endDate,2,username);//当日成功订单
        todaySuccessOrder += todaySuccessOrder2;

        int todayCloseOrder =  payOrderDao.getTodayCount(startDate,endDate,-1,username);//当日失败订单

        double todayMoney;
        double todayMoney2;
        try {
            todayMoney = payOrderDao.getTodayCountMoney(startDate,endDate,1,username);
        }catch (Exception e){
            todayMoney = 0;
        }
        try {
            todayMoney2 = payOrderDao.getTodayCountMoney(startDate,endDate,2,username);
        }catch (Exception e){
            todayMoney2 = 0;
        }

        todayMoney = Arith.add(todayMoney,todayMoney2);

        int countOrder = payOrderDao.getCount(1,username);
        double countMoney;
        double countMoney2;
        try {
            countMoney = payOrderDao.getCountMoney(1,username);
        }catch (Exception e){
            countMoney = 0;
        }
        try {
            countMoney2 = payOrderDao.getCountMoney(2,username);
        }catch (Exception e){
            countMoney2 = 0;
        }
        countMoney = Arith.add(countMoney,countMoney2);

        Map<String,String> map = new HashMap<>();
        map.put("todayOrder", String.valueOf(todayOrder));
        map.put("todaySuccessOrder", String.valueOf(todaySuccessOrder));
        map.put("todayCloseOrder", String.valueOf(todayCloseOrder));
        map.put("todayMoney", nf.format(todayMoney));
        map.put("countOrder", String.valueOf(countOrder));
        map.put("countMoney",nf.format(countMoney));

        return ResUtil.success(map);
    }

    public PageRes getPayQrcodes(String username, Integer page, Integer limit, Integer type){

        Pageable pageable = PageRequest.of(page-1, limit, Sort.Direction.DESC, "price");

        Specification<PayQrcode> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.equal(root.get("username").as(String.class), username));
            if (type!=null) {
                list.add(cb.equal(root.get("type").as(int.class), type));
            }

            return cb.and(list.toArray(new Predicate[list.size()]));
        };
        Page<PayQrcode> payQrcodes = payQrcodeDao.findAll(specification, pageable);

        PageRes list = PageRes.success(payQrcodes.getTotalElements(),payQrcodes.getContent());
        return list;
    }

    public CommonRes delPayQrcode(Long id,String username){
        PayQrcode payQrcode = payQrcodeDao.getById(id);
        if (payQrcode==null){
            return ResUtil.error("二维码不存在");
        }
        if (!validUserPermission(username,payQrcode.getUsername())) {
            return ResUtil.error("抱歉，您无权删除其他用户数据");
        }
        payQrcodeDao.deleteById(id);
        return ResUtil.success();
    }

    public CommonRes delOrder(Long id,String username){
        PayOrder payOrder = payOrderDao.findById(id).get();
        if (payOrder==null){
            return ResUtil.error("订单不存在");
        }
        if (!validUserPermission(username,payOrder.getUsername())) {
            return ResUtil.error("抱歉，您无权删除其它商家订单");
        }
        if (payOrder.getState()==0){
            tmpPriceDao.delprice(payOrder.getUsername(),payOrder.getType()+"-"+payOrder.getReallyPrice());
        }
        payOrderDao.deleteById(id);
        return ResUtil.success();
    }

    private static boolean validUserPermission(String username, String userdata) {
        if (StringUtils.isEmpty(userdata)) {
            return false;
        }
        return userdata.equals(username);
    }

    public CommonRes delGqOrder(String username){
        payOrderDao.deleteByState(username,-1);
        return ResUtil.success();
    }

    public CommonRes delLastOrder(String username){
        payOrderDao.deleteByAfterCreateDate(username,String.valueOf(new Date().getTime()-7*86400*1000));
        return ResUtil.success();
    }


    public static String md5(String text) {
        //加密后的字符串
        String encodeStr= DigestUtils.md5DigestAsHex(text.getBytes());
        return encodeStr;
    }
}
