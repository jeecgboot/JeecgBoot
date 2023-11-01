package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.entity.CallRecord;
import org.jeecg.modules.im.entity.ClientConfig;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.io.agora.media.RtcTokenBuilder2;
import org.jeecg.modules.im.service.CallRecordService;
import org.jeecg.modules.im.service.ClientConfigService;
import org.jeecg.modules.im.service.SysConfigService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 通话记录
 */
@RestController
@RequestMapping("/a/callRecord")
public class CallRecordCtrl extends BaseApiCtrl {
    @Resource
    private CallRecordService callRecordService;

    @Resource
    private SysConfigService sysConfigService;

    static int tokenExpirationInSeconds = 3600;
    static int privilegeExpirationInSeconds = 3600;

    @RequestMapping("/all")
    public Result<Object> all(){
        return success(callRecordService.findAll(getCurrentUserId()));
    }
    @RequestMapping("/getOne")
    public Result<Object> getOne(@RequestParam Integer recordId){
        Integer userId = getCurrentUserId();
        CallRecord record = callRecordService.getById(recordId);
        if(record==null||!record.getFromId().equals(getCurrentUserId())
        ||!record.getToId().equals(userId)){
            return fail();
        }
        return success(record);
    }
    /**
     * 发起通话
     */
    @PostMapping("/launch")
    public Result<Object> launch(@RequestParam int toId,@RequestParam boolean isVideo){
        SysConfig sysConfig = sysConfigService.get();

        int uid = getCurrentUserId();
        String channelId = uid+"-"+toId;

        CallRecord callRecord = new CallRecord();
        callRecord.setToId(toId);
        callRecord.setIsVideo(isVideo);
        callRecord.setFromId(uid);
        callRecord.setChannelId(channelId);
        callRecord.setStatus(CallRecord.Status.Waiting.getCode());
        callRecord.setTsCreate(getTs());

        Kv data = Kv.create();
        Result<Object> result = callRecordService.launch(callRecord);
        if(result.isSuccess()){
            data.set("record",result.getResult());
            data.set("channel", channelId);
            data.set("token", new RtcTokenBuilder2().buildTokenWithUid(
                    sysConfig.getAgoraAppId(),
                    sysConfig.getAgoraAppCertificate(),
                    channelId,
                    uid,
                    RtcTokenBuilder2.Role.ROLE_PUBLISHER,
                    tokenExpirationInSeconds,
                    privilegeExpirationInSeconds
            ));
            return success(data);
        }
        return result;
    }

    /**
     * 受邀方获取通话token
     * @param callRecordId
     * @return
     */
    @PostMapping("/getToken")
    public Result<Object> getToken(@RequestParam int callRecordId){
        SysConfig sysConfig = sysConfigService.get();

        int uid = getCurrentUserId();

        CallRecord callRecord = callRecordService.getById(callRecordId);
        if(callRecord==null||callRecord.getToId()!=uid){
            return fail("通话记录不存在");
        }

        return success(new RtcTokenBuilder2().buildTokenWithUid(
                sysConfig.getAgoraAppId(),
                sysConfig.getAgoraAppCertificate(),
                callRecord.getChannelId(),
                uid,
                RtcTokenBuilder2.Role.ROLE_SUBSCRIBER,
                tokenExpirationInSeconds,
                privilegeExpirationInSeconds
        ));
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return callRecordService.del(ids);
    }
}
