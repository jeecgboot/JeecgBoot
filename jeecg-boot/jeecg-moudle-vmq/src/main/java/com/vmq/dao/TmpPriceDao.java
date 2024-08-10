package com.vmq.dao;

import com.vmq.entity.TmpPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TmpPriceDao  extends JpaRepository<TmpPrice,String> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO pay_price_tmp (id,username,pay_id,price) VALUES (TRUNCATE(unix_timestamp(now(3))*1000,0),?1,?2,?3)", nativeQuery = true)
    int checkPrice(String username,String payId,String price);

    @Transactional
    @Modifying
    @Query(value = "delete from pay_price_tmp where username=?1 and price = ?2", nativeQuery = true)
    int delprice(String username,String aDouble);

    @Transactional
    @Modifying
    @Query(value = "delete from pay_price_tmp where pay_id=?1", nativeQuery = true)
    int deleteByPayId(String payId);

    @Transactional
    @Modifying
    @Query(value = "delete from pay_price_tmp p where id<TRUNCATE(unix_timestamp(now(3)-1)*1000,0) and username=?1 and price not in(select concat(type,'-',really_price) from pay_order o where o.state=0 and o.username=p.username)", nativeQuery = true)
    int delpriceByUsername(String username);

    TmpPrice findByPayId(String payId);
}
