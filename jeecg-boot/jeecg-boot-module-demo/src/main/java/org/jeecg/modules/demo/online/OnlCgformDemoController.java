package org.jeecg.modules.demo.online;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Online表单开发 demo 示例
 *
 * @author sunjianlei
 * @date 2021-12-16
 */
@Slf4j
@RestController("onlCgformDemoController")
@RequestMapping("/demo/online/cgform")
public class OnlCgformDemoController {

    /**
     * Online表单 http 增强，list增强示例
     * @param params
     * @return
     */
    @PostMapping("/enhanceJavaListHttp")
    public Result<?> enhanceJavaListHttp(@RequestBody JSONObject params) {
        log.info(" --- params：" + params.toJSONString());
        JSONArray dataList = params.getJSONArray("dataList");
        List<DictModel> dict = virtualDictData();
        for (int i = 0; i < dataList.size(); i++) {
            JSONObject record = dataList.getJSONObject(i);
            String province = record.getString("province");
            if (province == null) {
                continue;
            }
            String text = dict.stream()
                    .filter(p -> province.equals(p.getValue()))
                    .map(DictModel::getText)
                    .findAny()
                    .orElse(province);
            record.put("province", text);
        }
        Result<?> res = Result.OK(dataList);
        res.setCode(1);
        return res;
    }

    /**
     * 模拟字典数据
     *
     * @return
     */
    private List<DictModel> virtualDictData() {
        List<DictModel> dict = new ArrayList<>();
        dict.add(new DictModel("bj", "北京"));
        dict.add(new DictModel("sd", "山东"));
        dict.add(new DictModel("ah", "安徽"));
        return dict;
    }


    /**
     * Online表单 http 增强，add、edit增强示例
     * @param params
     * @return
     */
    @PostMapping("/enhanceJavaHttp")
    public Result<?> enhanceJavaHttp(@RequestBody JSONObject params) {
        log.info(" --- params：" + params.toJSONString());
        String tableName = params.getString("tableName");
        JSONObject record = params.getJSONObject("record");
        /*
         * 业务场景一： 获取提交表单数据，进行其他业务关联操作
         * （比如：根据入库单，同步更改库存）
         */
        log.info(" --- tableName：" + tableName);
        log.info(" --- 行数据：" + record.toJSONString());
        /*
         * 业务场景二： 保存数据之前进行数据的校验
         * 直接返回错误状态即可
         */
        String phone = record.getString("phone");
        if (oConvertUtils.isEmpty(phone)) {
            return Result.error("手机号不能为空！");
        }
        /*
         * 业务场景三： 保存数据之对数据的处理
         * 直接操作 record 即可
         */
        record.put("phone", "010-" + phone);

        /* 其他业务场景自行实现 */

        // 返回场景一： 不对 record 做任何修改的情况下，可以直接返回 code，
        // 返回 0 = 丢弃当前数据
        // 返回 1 = 新增当前数据
        // 返回 2 = 修改当前数据 TODO（？）存疑
//		 return Result.OK(1);

        // 返回场景二： 需要对 record 做修改的情况下，需要返回一个JSONObject对象（或者Map也行）
        JSONObject res = new JSONObject();
        res.put("code", 1);
        // 将 record 返回以进行修改
        res.put("record", record);
        // TODO 不要 code 的概念
        return Result.OK(res);
    }

}
