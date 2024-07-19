package com.vmq.dao;

import com.vmq.entity.PayQrcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PayQrcodeDao  extends JpaRepository<PayQrcode,Long>, JpaSpecificationExecutor {

    PayQrcode findByPriceAndType(double price,int type);
}
