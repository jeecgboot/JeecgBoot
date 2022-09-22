package org.jeecg.modules.message.enums;

import org.jeecg.common.system.annotation.EnumDict;
import org.jeecg.common.system.vo.DictModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息跳转【vue3】
 * @Author taoYan
 * @Date 2022/8/19 20:41
 **/
@EnumDict("messageHref")
public enum Vue3MessageHrefEnum {

    /**
     * 流程催办
     */
    BPM("bpm", "/task/myHandleTaskInfo"),

    /**
     * 节点通知
     */
    BPM_TASK("bpm_task", "/task/myHandleTaskInfo"),

    /**
     * 邮件消息
     */
    EMAIL("email", "/eoa/email");
    
    String busType;
    
    String path;

    Vue3MessageHrefEnum(String busType, String path) {
        this.busType = busType;
        this.path = path;
    }

    public String getBusType() {
        return busType;
    }

    public String getPath() {
        return path;
    }

    /**
     * 获取字典数据
     * @return
     */
    public static List<DictModel> getDictList(){
        List<DictModel> list = new ArrayList<>();
        DictModel dictModel = null;
        for(Vue3MessageHrefEnum e: Vue3MessageHrefEnum.values()){
            dictModel = new DictModel();
            dictModel.setValue(e.getBusType());
            dictModel.setText(e.getPath());
            list.add(dictModel);
        }
        return list;
    }
    
}
