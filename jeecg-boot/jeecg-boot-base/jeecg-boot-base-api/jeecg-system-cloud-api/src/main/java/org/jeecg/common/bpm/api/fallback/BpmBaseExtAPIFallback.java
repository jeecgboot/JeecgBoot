package org.jeecg.common.bpm.api.fallback;

import java.util.List;
import java.util.Map;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.bpm.api.IBpmBaseExtAPI;
import org.jeecg.common.online.api.IOnlineBaseExtAPI;
import org.jeecg.common.system.vo.DictModel;

import com.alibaba.fastjson.JSONObject;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 进入fallback的方法 检查是否token未设置
 */
@Slf4j
public class BpmBaseExtAPIFallback implements IBpmBaseExtAPI {

    @Setter
    private Throwable cause;

    @Override
    public Result<String> startMutilProcess(String flowCode, String id, String formUrl, String formUrlMobile,
        String username, String jsonData) throws Exception {
        return null;
    }

    @Override
    public Result<String> startDesFormMutilProcess(String flowCode, String id, String formUrl, String formUrlMobile,
        String username, String jsonData) throws Exception {
        return null;
    }

    @Override
    public Result<String> saveMutilProcessDraft(String flowCode, String id, String formUrl, String formUrlMobile,
        String username, String jsonData) throws Exception {
        return null;
    }
}
