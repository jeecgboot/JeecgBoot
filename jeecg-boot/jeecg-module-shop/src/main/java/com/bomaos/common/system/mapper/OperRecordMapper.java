package com.bomaos.common.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.system.entity.OperRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 操作日志Mapper接口
 * Created by Panyoujie on 2018-12-24 16:10
 */
public interface OperRecordMapper extends BaseMapper<OperRecord> {

    /**
     * 分页查询
     */
    List<OperRecord> listPage(@Param("page") PageParam<OperRecord> page);

    /**
     * 查询全部
     */
    List<OperRecord> listAll(@Param("page") Map<String, Object> page);

}
