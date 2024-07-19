package com.vmq.dao;

import com.vmq.entity.PayGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PayGoodsDao extends JpaRepository<PayGoods,Long>, JpaSpecificationExecutor {

}
