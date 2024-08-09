package com.vmq.dao;

import com.vmq.entity.SysLog;
import com.vmq.entity.VmqSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface SysLogDao extends JpaRepository<SysLog,String> {


}
