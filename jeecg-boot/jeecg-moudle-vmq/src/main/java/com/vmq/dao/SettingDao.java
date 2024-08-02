package com.vmq.dao;

import com.vmq.entity.VmqSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface SettingDao extends JpaRepository<VmqSetting,String> {

    @Query(value = "select u.username,u.realname,u.password,u.salt,u.status from sys_user u where u.del_flag=0 and u.username=?1", nativeQuery = true)
    Map<String, Object> getUserInfo(String username);

    @Query(value = "select s from VmqSetting s where s.username=?1")
    VmqSetting getSettingByUserName(String userName);

    @Transactional
    @Modifying
    @Query(value = "insert into sys_user(id, username, realname, password, email, salt, status, del_flag) values (TRUNCATE(unix_timestamp(now(3))*1000,0),?1,?1,?2,?3,?4,true,false)",nativeQuery = true)
    void regist(String user, String password, String email, String salt);
}
