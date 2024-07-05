package com.vone.mq.dao;

import com.vone.mq.entity.PayOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PayOrderDao  extends JpaRepository<PayOrder,Long>, JpaSpecificationExecutor {

    PayOrder findByPayId(String payId);
    PayOrder findByOrderId(String orderId);
    
    @Transactional
    @Modifying
    @Query(value = "update pay_order p set p.close_date=?2,p.state=-1 where p.create_date<?1 and p.state=0", nativeQuery = true)
    int setTimeout(String timeout,String closeTime);

    @Transactional
    @Modifying
    @Query(value = "update pay_order p set p.state=?1 where p.id=?2", nativeQuery = true)
    int setState(int state,long id);

    List<PayOrder> findAllByCloseDate(Long closeDate);

    @Query(value = "select * from pay_order where id = ?1", nativeQuery = true)
    PayOrder getById(Integer id);

    PayOrder findByReallyPriceAndStateAndType(double reallyPrice,int state,int type);

    PayOrder findByPayDate(Long payDate);

    @Query(value = "select count(*) from pay_order where create_date >= ?1 and create_date <= ?2", nativeQuery = true)
    int getTodayCount(String startDate,String endDate);

    @Query(value = "select count(*) from pay_order where create_date >= ?1 and create_date <= ?2 and state = ?3", nativeQuery = true)
    int getTodayCount(String startDate,String endDate,int state);

    @Query(value = "select sum(price) from pay_order where create_date >= ?1 and create_date <= ?2 and state = ?3", nativeQuery = true)
    double getTodayCountMoney(String startDate,String endDate,int state);

    @Query(value = "select count(*) from pay_order", nativeQuery = true)
    int getCount();

    @Query(value = "select count(*) from pay_order where state = ?1", nativeQuery = true)
    int getCount(int state);

    @Query(value = "select sum(price) from pay_order where state = ?1", nativeQuery = true)
    double getCountMoney(int state);


    @Transactional
    int deleteByState(int state);


    @Transactional
    @Modifying
    @Query(value = "delete from pay_order where create_date<?1", nativeQuery = true)
    int deleteByAfterCreateDate(String date);

}
