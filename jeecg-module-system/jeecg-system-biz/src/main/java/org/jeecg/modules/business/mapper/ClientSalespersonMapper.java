package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.ClientSalesperson;

import java.util.List;

public interface ClientSalespersonMapper extends BaseMapper<ClientSalesperson> {

    void deleteByClientId(@Param("clientId") String clientId);

    List<String> getSalespersonIdsByClientId(@Param("clientId") String clientId);

    List<String> getActiveClientIdsBySalespersonId(@Param("salespersonId") String salespersonId);

    List<ClientSalesperson> selectByClientIds(@Param("clientIds") List<String> clientIds);
}