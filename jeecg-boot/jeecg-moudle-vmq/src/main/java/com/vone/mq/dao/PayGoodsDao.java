package com.vone.mq.dao;

import com.vone.mq.entity.PayGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PayGoodsDao extends JpaRepository<PayGoods,Long>, JpaSpecificationExecutor {

}
