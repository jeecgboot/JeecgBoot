package org.jeecg.modules.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Client;
import org.springframework.stereotype.Repository;

/**
 * @Description: 客户 登录账户 关系
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
@Repository
public interface ClientUserMapper {
    /**
     * Find client information for current system user
     *
     * @param userId the identifier of system user
     * @return client entity or null in case that current user is not a client
     */
    Client selectClientByUserId(@Param("userId") String userId);
}
