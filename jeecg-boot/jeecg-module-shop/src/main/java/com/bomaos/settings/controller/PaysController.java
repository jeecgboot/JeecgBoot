package com.bomaos.settings.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bomaos.common.core.annotation.OperLog;
import com.bomaos.common.core.web.*;
import com.bomaos.reception.common.PaysEnmu;
import com.bomaos.settings.entity.Pays;
import com.bomaos.settings.service.PaysService;
import com.bomaos.settings.vo.PaysVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付配置管理
 * Created by Panyoujie on 2021-03-29 11:06:11
 */
@Controller
@RequestMapping("/settings/pays")
public class PaysController extends BaseController {

    @Autowired
    private PaysService paysService;

    @RequiresPermissions("settings:pays:view")
    @RequestMapping()
    public String view() {
        return "settings/pays.html";
    }

    /**
     * 分页查询支付配置
     */
    @RequiresPermissions("settings:pays:list")
    @OperLog(value = "支付配置管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<PaysVo> page(HttpServletRequest request) {
        PageParam<Pays> pageParam = new PageParam<>(request);
        pageParam.addOrderDesc("created_at");

        List<Pays> paysList = paysService.page(pageParam, pageParam.getWrapper()).getRecords();
        List<PaysVo> collect = paysList.stream().map((pays) -> {
            PaysVo paysVo = new PaysVo();
            BeanUtils.copyProperties(pays, paysVo);
            JSONObject configs = JSONObject.parseObject(pays.getConfig());

            switch (Objects.requireNonNull(PaysEnmu.getByValue(pays.getDriver()))) {
                case MQPAY_ALIPAY:
                case MQPAY_WXPAY:
                    paysVo.setKey(configs.get("key").toString());
                    paysVo.setCreateUrl(configs.get("create_url").toString());
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                case EPAY_ALIPAY:
                case EPAY_QQPAY:
                case EPAY_WXPAY:
                    paysVo.setAppid(configs.get("pid").toString());
                    paysVo.setKey(configs.get("key").toString());
                    paysVo.setCreateUrl(configs.get("create_url").toString());
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                case BUDPAY_ALIPAY:
                case BUDPAY_WECHAT:
                    paysVo.setAppid(configs.get("pid").toString());
                    paysVo.setKey(configs.get("key").toString());
                    paysVo.setCreateUrl(configs.get("create_url").toString());
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                case YUNGOUOS_ALIPAY:
                case YUNGOUOS_WXPAY:
                    paysVo.setAppid(configs.get("mchId").toString());
                    paysVo.setKey(configs.get("key").toString());
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                case XUNHUPAY_ALIPAY:
                case XUNHUPAY_WXPAY:
                    paysVo.setAppid(configs.get("appid").toString());
                    paysVo.setKey(configs.get("appsecret").toString());
                    paysVo.setCreateUrl(configs.containsKey("create_url") ? configs.get("create_url").toString() : "");
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                case PAYJS_ALIPAY:
                case PAYJS_WXPAY:
                    paysVo.setAppid(configs.get("mchId").toString());
                    paysVo.setKey(configs.get("key").toString());
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                case WXPAY:
                    paysVo.setAppid(configs.get("appId").toString());
                    paysVo.setMchid(configs.get("mchId").toString());
                    paysVo.setKey(configs.get("key").toString());
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                case WXPAU_H5:
                    paysVo.setAppid(configs.get("appId").toString());
                    paysVo.setMchid(configs.get("mchId").toString());
                    paysVo.setKey(configs.get("key").toString());
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                case ALIPAY:
                case ALIPAY_PC:
                    paysVo.setAppid(configs.get("app_id").toString());
                    paysVo.setKey(configs.get("private_key").toString());
                    paysVo.setMpKey(configs.get("alipay_public_key").toString());
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                case PAYPAL:
                    paysVo.setMchid(configs.get("clientId").toString());
                    paysVo.setKey(configs.get("clientSecret").toString());
                    paysVo.setNotifyUrl(configs.get("return_url").toString());
                    break;
                case EPUSDT:
                    paysVo.setKey(configs.get("key").toString());
                    paysVo.setCreateUrl(configs.get("create_url").toString());
                    paysVo.setNotifyUrl(configs.get("notify_url").toString());
                    break;
                default:
                    break;
            }

            return paysVo;
        }).collect(Collectors.toList());
        return new PageResult<>(collect, pageParam.getTotal());
    }

    /**
     * 查询全部支付配置
     */
    @RequiresPermissions("settings:pays:list")
    @OperLog(value = "支付配置管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Pays> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(paysService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询支付配置
     */
    @RequiresPermissions("settings:pays:list")
    @OperLog(value = "支付配置管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(paysService.getById(id));
    }

    /**
     * 添加支付配置
     */
    @RequiresPermissions("settings:pays:save")
    @OperLog(value = "支付配置管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Pays pays) {
        if (paysService.save(pays)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改支付配置
     */
    @RequiresPermissions("settings:pays:update")
    @OperLog(value = "支付配置管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(PaysVo paysVo) {
        Map<String, String> map = new HashMap<>();
        switch (Objects.requireNonNull(PaysEnmu.getByValue(paysVo.getDriver()))) {
            case MQPAY_ALIPAY:
            case MQPAY_WXPAY:
                map.put("key", paysVo.getKey());
                map.put("create_url", paysVo.getCreateUrl());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            case EPAY_ALIPAY:
            case EPAY_QQPAY:
            case EPAY_WXPAY:
                map.put("pid", paysVo.getAppid());
                map.put("key", paysVo.getKey());
                map.put("create_url", paysVo.getCreateUrl());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            case BUDPAY_ALIPAY:
            case BUDPAY_WECHAT:
                map.put("pid", paysVo.getAppid());
                map.put("key", paysVo.getKey());
                map.put("create_url", paysVo.getCreateUrl());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            case YUNGOUOS_ALIPAY:
            case YUNGOUOS_WXPAY:
                map.put("mchId", paysVo.getAppid());
                map.put("key", paysVo.getKey());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            case XUNHUPAY_ALIPAY:
            case XUNHUPAY_WXPAY:
                map.put("appid", paysVo.getAppid());
                map.put("appsecret", paysVo.getKey());
                map.put("create_url", paysVo.getCreateUrl());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            case PAYJS_ALIPAY:
            case PAYJS_WXPAY:
                map.put("mchId", paysVo.getAppid());
                map.put("key", paysVo.getKey());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            case WXPAY:
                map.put("appId", paysVo.getAppid());
                map.put("mchId", paysVo.getMchid());
                map.put("key", paysVo.getKey());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            case WXPAU_H5:
                map.put("appId", paysVo.getAppid());
                map.put("mchId", paysVo.getMchid());
                map.put("key", paysVo.getKey());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            case ALIPAY:
            case ALIPAY_PC:
                map.put("app_id", paysVo.getAppid());
                map.put("private_key", paysVo.getKey());
                map.put("alipay_public_key", paysVo.getMpKey());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            case PAYPAL:
                map.put("clientId", paysVo.getMchid());
                map.put("clientSecret", paysVo.getKey());
                map.put("return_url", paysVo.getNotifyUrl());
                break;
            case EPUSDT:
                map.put("key", paysVo.getKey());
                map.put("create_url", paysVo.getCreateUrl());
                map.put("notify_url", paysVo.getNotifyUrl());
                break;
            default:
                break;
        }
        String jsonString = JSON.toJSONString(map);
        Pays pays = new Pays();
        BeanUtils.copyProperties(paysVo, pays);
        pays.setConfig(jsonString);
        if (paysService.updateById(pays)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除支付配置
     */
    @RequiresPermissions("settings:pays:remove")
    @OperLog(value = "支付配置管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (paysService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加支付配置
     */
    @RequiresPermissions("settings:pays:save")
    @OperLog(value = "支付配置管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Pays> list) {
        if (paysService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改支付配置
     */
    @RequiresPermissions("settings:pays:update")
    @OperLog(value = "支付配置管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Pays> batchParam) {
        if (batchParam.update(paysService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除支付配置
     */
    @RequiresPermissions("settings:pays:remove")
    @OperLog(value = "支付配置管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (paysService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 修改商品状态
     */
    @OperLog(value = "分类列表管理", desc = "修改移动端状态", result = true)
    @RequiresPermissions("products:products:update")
    @ResponseBody
    @RequestMapping("/status/updateMobile")
    public JsonResult updateIsMobileStates(Integer id, Integer enabled) {
        if (enabled == null || (enabled != 0 && enabled != 1)) {
            return JsonResult.error("状态值不正确");
        }
        Pays pays = new Pays();
        pays.setId(id);
        pays.setIsMobile(enabled);
        if (paysService.updateById(pays)) {
            return JsonResult.ok("移动端支付开启成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 修改商品状态
     */
    @OperLog(value = "分类列表管理", desc = "修改电脑端状态", result = true)
    @RequiresPermissions("products:products:update")
    @ResponseBody
    @RequestMapping("/status/updatePc")
    public JsonResult updateIsPcStates(Integer id, Integer enabled) {
        if (enabled == null || (enabled != 0 && enabled != 1)) {
            return JsonResult.error("状态值不正确");
        }
        Pays pays = new Pays();
        pays.setId(id);
        pays.setIsPc(enabled);
        if (paysService.updateById(pays)) {
            return JsonResult.ok("电脑端支付开启成功");
        }
        return JsonResult.error("开启失败");
    }

    /**
     * 修改商品状态
     */
    @OperLog(value = "分类列表管理", desc = "修改手续费标签状态", result = true)
    @RequiresPermissions("products:products:update")
    @ResponseBody
    @RequestMapping("/handling/update")
    public JsonResult updateIsHandling(Integer id, Integer enabled) {
        if (enabled == null || (enabled != 0 && enabled != 1)) {
            return JsonResult.error("状态值不正确");
        }
        Pays pays = new Pays();
        pays.setId(id);
        pays.setIsHandlingFee(enabled);
        if (paysService.updateById(pays)) {
            return JsonResult.ok("手续费标签开启成功");
        }
        return JsonResult.error("开启失败");
    }

    /**
     * 修改支付配置
     */
    @RequiresPermissions("settings:pays:update")
    @OperLog(value = "支付配置管理", desc = "修改手续费", param = false, result = true)
    @ResponseBody
    @RequestMapping("/updateHandlingFee")
    public JsonResult updateSort(PaysVo paysVo) {
        Pays pays = new Pays();
        BeanUtils.copyProperties(paysVo, pays);
        pays.setHandlingFee(paysVo.getHandlingFee());

        if (paysService.updateById(pays)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }
}
