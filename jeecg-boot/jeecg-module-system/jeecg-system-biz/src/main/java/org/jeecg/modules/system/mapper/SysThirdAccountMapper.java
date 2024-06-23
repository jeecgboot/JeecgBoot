package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysThirdAccount;
import org.jeecg.modules.system.vo.thirdapp.JwUserDepartVo;

import java.util.List;

/**
 * @Description: 第三方登录账号表
 * @Author: jeecg-boot
 * @Date: 2020-11-17
 * @Version: V1.0
 */
public interface SysThirdAccountMapper extends BaseMapper<SysThirdAccount> {

    /**
     * 通过 sysUsername 集合批量查询
     *
     * @param sysUsernameArr username集合
     * @param thirdType       第三方类型
     * @return
     */
    List<SysThirdAccount> selectThirdIdsByUsername(@Param("sysUsernameArr") String[] sysUsernameArr, @Param("thirdType") String thirdType, @Param("tenantId") Integer tenantId);
    
    /**
     * 查询被绑定的用户
     * @param tenantId
     * @param thirdType
     * @return
     */
    List<JwUserDepartVo> getThirdUserBindByWechat(@Param("tenantId") int tenantId, @Param("thirdType") String thirdType);
}
