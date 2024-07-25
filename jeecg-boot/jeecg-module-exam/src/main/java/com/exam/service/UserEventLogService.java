package com.exam.service;

import com.exam.domain.UserEventLog;
import com.exam.viewmodel.admin.user.UserEventPageRequestVM;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserEventLogService extends BaseService<UserEventLog> {

    List<UserEventLog> getUserEventLogByUserId(Integer id);

    PageInfo<UserEventLog> page(UserEventPageRequestVM requestVM);

    List<Integer> selectMothCount();
}
