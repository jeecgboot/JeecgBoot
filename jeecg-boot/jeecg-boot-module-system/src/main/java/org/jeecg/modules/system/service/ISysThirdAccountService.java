package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysThirdAccount;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.ThirdLoginModel;

import java.util.List;

/**
 * @Description: 第三方登录账号表
 * @Author: jeecg-boot
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface ISysThirdAccountService extends IService<SysThirdAccount> {
    /**更新第三方账户信息*/
    void updateThirdUserId(SysUser sysUser,String thirdUserUuid);
    /**创建第三方用户*/
    SysUser createUser(String phone, String thirdUserUuid);

    /** 根据本地userId查询数据 */
    SysThirdAccount getOneBySysUserId(String sysUserId, String thirdType);
    /** 根据第三方userId查询数据 */
    SysThirdAccount getOneByThirdUserId(String thirdUserId, String thirdType);

    /**
     * 通过 sysUsername 集合批量查询
     *
     * @param sysUsernameArr username集合
     * @param thirdType      第三方类型
     * @return
     */
    List<SysThirdAccount> listThirdUserIdByUsername(String[] sysUsernameArr, String thirdType);

    /**
     * 创建新用户
     *
     * @param tlm 第三方登录信息
     */
    SysThirdAccount saveThirdUser(ThirdLoginModel tlm);

}
