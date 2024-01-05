package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionMapper extends BaseMapper<Transaction> {
    List<Transaction> list();

    List<Transaction> listByClientId(@Param("clientId") String clientId);
    List<Transaction> listByClientIdAndCurrency(@Param("clientId") String clientId, @Param("currency") String currency);
}
