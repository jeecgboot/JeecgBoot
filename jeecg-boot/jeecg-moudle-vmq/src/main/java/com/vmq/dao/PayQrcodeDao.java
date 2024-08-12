package com.vmq.dao;

import com.vmq.entity.PayQrcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PayQrcodeDao  extends JpaRepository<PayQrcode,Long>, JpaSpecificationExecutor {

    PayQrcode findByUsernameAndPriceAndType(String username,double price,int type);

    @Query(value = "select s from PayQrcode s where s.username=?1 and s.price=?2 and s.type=?3")
    List<PayQrcode> findByPriceAndType(String username,double price,int type);

    List<PayQrcode> findByUsernameAndType(String username, int type);
}
