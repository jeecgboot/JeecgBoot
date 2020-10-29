package com.crj.java.task.front.common.system.base.service.impl;

import com.crj.java.task.front.common.system.base.entity.TaskEntity;
import com.crj.java.task.front.common.system.base.service.TaskService;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: ServiceImpl基类
 * @Author: Crj
 * @Date: 2019-4-21 8:13
 * @Version: 1.0
 */
@Slf4j
public class TaskServiceImpl<M extends BaseMapper<T>, T extends TaskEntity> extends ServiceImpl<M, T> implements TaskService<T> {

}
