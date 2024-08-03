package com.vmq.dao;

import com.vmq.entity.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface PayOrderDao  extends JpaRepository<PayOrder,Long>, JpaSpecificationExecutor {

    PayOrder findByPayId(String payId);
    PayOrder findByOrderId(String orderId);
    
    @Transactional
    @Modifying
    @Query(value = "update pay_order p set p.close_date=?3,p.state=-1 where username=?1 and p.create_date<?2 and p.state=0", nativeQuery = true)
    int setTimeout(String username,String timeout,String closeTime);

    @Transactional
    @Modifying
    @Query(value = "update pay_order p set p.state=?1 where p.id=?2", nativeQuery = true)
    int setState(int state,long id);

    /**
     * 查询关闭前的订单
     * @return
     */
    @Query(value = "select distinct username,type,really_price from pay_order where username=?1 and close_date between ?2-1 and ?3+1 ", nativeQuery = true)
    List<Map<String,Object>> findAllByCloseDate(String username, long beginTime, long endTime);

    @Query(value = "select * from pay_order where id = ?1", nativeQuery = true)
    PayOrder getById(Integer id);

    PayOrder findByReallyPriceAndStateAndType(double reallyPrice,int state,int type);

    PayOrder findByPayDate(Long payDate);

    @Query(value = "select count(*) from pay_order where create_date >= ?1 and create_date <= ?2 and username=?3", nativeQuery = true)
    int getTodayCount(String startDate,String endDate,String username);

    @Query(value = "select count(*) from pay_order where create_date >= ?1 and create_date <= ?2 and state = ?3 and username=?4", nativeQuery = true)
    int getTodayCount(String startDate,String endDate,int state,String username);

    @Query(value = "select sum(price) from pay_order where create_date >= ?1 and create_date <= ?2 and state = ?3 and username=?4", nativeQuery = true)
    double getTodayCountMoney(String startDate,String endDate,int state,String username);

    @Query(value = "select count(*) from pay_order", nativeQuery = true)
    int getCount();

    @Query(value = "select count(*) from pay_order where state = ?1 and username = ?2", nativeQuery = true)
    int getCount(int state,String username);

    @Query(value = "select sum(price) from pay_order where state = ?1 and username = ?2", nativeQuery = true)
    double getCountMoney(int state,String username);

    @Transactional
    @Modifying
    @Query(value = "delete from pay_order where username=?1 and state=?2", nativeQuery = true)
    int deleteByState(String username,int state);

    @Transactional
    @Modifying
    @Query(value = "delete from pay_order where username=?1 and create_date<?2", nativeQuery = true)
    int deleteByAfterCreateDate(String username,String date);

}
