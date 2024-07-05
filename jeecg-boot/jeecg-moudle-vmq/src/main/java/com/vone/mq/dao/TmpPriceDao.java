package com.vone.mq.dao;

import com.vone.mq.entity.TmpPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TmpPriceDao  extends JpaRepository<TmpPrice,String> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO pay_price_tmp (price) VALUES (?1)", nativeQuery = true)
    int checkPrice(String price);


    @Transactional
    @Modifying
    @Query(value = "delete from pay_price_tmp where price = ?1", nativeQuery = true)
    int delprice(String aDouble);
}
