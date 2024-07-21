package com.bomaos.common.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.common.system.entity.OperRecord;

import java.util.List;
import java.util.Map;

/**
 * 操作日志服务类
 * Created by Panyoujie on 2018-12-24 16:10
 */
public interface OperRecordService extends IService<OperRecord> {

    /**
     * 关联分页查询
     */
    PageResult<OperRecord> listPage(PageParam<OperRecord> page);

    /**
     * 关联查询所有
     */
    List<OperRecord> listAll(Map<String, Object> page);

    /**
     * 异步添加
     */
    void saveAsync(OperRecord operRecord);

}
