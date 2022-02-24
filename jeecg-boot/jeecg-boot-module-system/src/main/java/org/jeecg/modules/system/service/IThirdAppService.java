package org.jeecg.modules.system.service;

import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.modules.system.vo.thirdapp.SyncInfoVo;

import java.util.List;

/**
 * 第三方App对接
 */
public interface IThirdAppService {

    String getAccessToken();

    /**
     * 将本地部门同步到第三方App<br>
     * 同步方向：本地 --> 第三方APP
     * 同步逻辑：<br>
     * 1. 先判断是否同步过，有则修改，无则创建；<br>
     * 2. 本地没有但第三方App里有则删除第三方App里的。
     *
     * @return 成功返回true
     */
    SyncInfoVo syncLocalDepartmentToThirdApp(String ids);

    /**
     * 将第三方App部门同步到本地<br>
     * 同步方向：第三方APP --> 本地
     * 同步逻辑：<br>
     * 1. 先判断是否同步过，有则修改，无则创建；<br>
     * 2. 本地没有但第三方App里有则删除第三方App里的。
     *
     * @return 成功返回true
     */
    SyncInfoVo syncThirdAppDepartmentToLocal(String ids);

    /**
     * 将本地用户同步到第三方App<br>
     * 同步方向：本地 --> 第三方APP <br>
     * 同步逻辑：先判断是否同步过，有则修改、无则创建<br>
     * 注意：同步人员的状态，比如离职、禁用、逻辑删除等。
     * (特殊点：1、目前逻辑特意做的不删除用户，防止企业微信提前上线，用户已经存在，但是平台无此用户。
     *  企业微信支持禁用账号；钉钉不支持
     *  2、企业微信里面是手机号激活，只能用户自己改，不允许通过接口改)
     *
     * @return 成功返回空数组，失败返回错误信息
     */
    SyncInfoVo syncLocalUserToThirdApp(String ids);

    /**
     * 将第三方App用户同步到本地<br>
     * 同步方向：第三方APP --> 本地 <br>
     * 同步逻辑：先判断是否同步过，有则修改、无则创建<br>
     * 注意：同步人员的状态，比如离职、禁用、逻辑删除等。
     *
     * @return 成功返回空数组，失败返回错误信息
     */
    SyncInfoVo syncThirdAppUserToLocal();

    /**
     * 根据本地用户ID，删除第三方APP的用户
     *
     * @param userIdList 本地用户ID列表
     * @return 0表示成功，其他值表示失败
     */
    int removeThirdAppUser(List<String> userIdList);

    /**
     * 发送消息
     *
     * @param message
     * @param verifyConfig 是否验证配置（未启用的APP会拒绝发送）
     * @return
     */
    boolean sendMessage(MessageDTO message, boolean verifyConfig);

    boolean sendMessage(MessageDTO message);

}
