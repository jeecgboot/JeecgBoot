package com.vmq.dao;

import com.vmq.entity.OtherSetting;
import com.vmq.entity.VmqSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface OtherSettingDao extends JpaRepository<OtherSetting,String> {

    @Query(value = "select s from OtherSetting s where s.username=?1")
    OtherSetting getSettingByUserName(String userName);

    OtherSetting findByAppIdAndType(String appId,String type);

}
