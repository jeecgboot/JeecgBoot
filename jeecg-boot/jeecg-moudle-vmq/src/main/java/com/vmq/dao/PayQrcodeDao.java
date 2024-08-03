package com.vmq.dao;

import com.vmq.entity.PayQrcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PayQrcodeDao  extends JpaRepository<PayQrcode,Long>, JpaSpecificationExecutor {

    PayQrcode findByUsernameAndPriceAndType(String username,double price,int type);

    List<PayQrcode> findByUsernameAndType(String username, int type);
}
