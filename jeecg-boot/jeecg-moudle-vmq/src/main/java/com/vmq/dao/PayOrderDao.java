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

    @Query("select o from PayOrder o where o.username=?1 and o.reallyPrice=?2 and o.state=?3 and o.type=?4 and o.param!='static'")
    PayOrder findByUsernameAndReallyPriceAndStateAndType(String username, double reallyPrice,int state,int type);

    @Query("select o from PayOrder o where o.username=?1 and o.reallyPrice=?2 and o.type=?3 and o.createDate<?4 and o.payDate>?5 and o.param!='static'")
    List<PayOrder> getRecentPaidOrder(String username, double reallyPrice,int type,long createTime,long payDate);

    PayOrder findByUsernameAndPayDate(String username, Long payDate);

    @Query(value = "select count(*) from pay_order where create_date >= ?1 and create_date <= ?2 and username=?3", nativeQuery = true)
    int getTodayCount(String startDate,String endDate,String username);

    @Query(value = "select count(*) from pay_order where create_date >= ?1 and create_date <= ?2 and state = ?3 and username=?4", nativeQuery = true)
    int getTodayCount(String startDate,String endDate,int state,String username);

    @Query(value = "select sum(price) from pay_order where create_date >= ?1 and create_date <= ?2 and state = ?3 and username=?4", nativeQuery = true)
    double getTodayCountMoney(String startDate,String endDate,int state,String username);

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

    PayOrder findByUsernameAndOrderId(String username, String outTradeNo);

    @Query("select o from PayOrder o where o.username=?1 and o.state=0 and o.param='static'")
    List<PayOrder> getUnPaidStaticOrder(String username);

    @Query(value = "select pay_id from pay_order o where o.state=0 and o.param='static' and o.username=?1 and o.type=?2 order by o.create_date limit 1",nativeQuery = true)
    String getFirstUnPaidStaticOrder(String username, Integer type);

    List<PayOrder> findByOrderIdIn(String[] payId);
}
