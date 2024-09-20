package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysGatewayRoute;

import java.util.List;

/**
 * @Description: gateway路由管理
 * @Author: jeecg-boot
 * @Date:   2020-05-26
 * @Version: V1.0
 */
public interface SysGatewayRouteMapper extends BaseMapper<SysGatewayRoute> {
    /**
     * 还原逻辑删除
     * @param ids
     */
    int revertLogicDeleted(@Param("ids") List<String> ids);

    /**
     *彻底删除
     * @param ids
     */
    int deleteLogicDeleted(@Param("ids") List<String> ids);

    /**
     * 查询删除的列表
     * @return
     */
    @Select("select * from sys_gateway_route where del_flag = 1")
    List<SysGatewayRoute> queryDeleteList();
}
