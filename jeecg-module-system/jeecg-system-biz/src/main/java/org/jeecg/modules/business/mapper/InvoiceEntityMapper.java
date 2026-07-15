package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.InvoiceEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceEntityMapper extends BaseMapper<InvoiceEntity> {

    boolean deleteByMainId(@Param("mainId") String mainId);

    List<InvoiceEntity> selectByMainId(@Param("mainId") String mainId);

    List<InvoiceEntity> selectActiveByClientId(@Param("clientId") String clientId);

    void clearDefaultByClientId(@Param("clientId") String clientId);
}
