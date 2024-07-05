package com.vone.mq.dao;

import com.vone.mq.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingDao extends JpaRepository<Setting,String> {


}
