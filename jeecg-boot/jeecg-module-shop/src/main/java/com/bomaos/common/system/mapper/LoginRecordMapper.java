package com.bomaos.common.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.system.entity.LoginRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 登录日志Mapper接口
 * Created by Panyoujie on 2018-12-24 16:10
 */
public interface LoginRecordMapper extends BaseMapper<LoginRecord> {

    /**
     * 分页查询
     */
    List<LoginRecord> listPage(@Param("page") PageParam<LoginRecord> page);

    /**
     * 查询全部
     */
    List<LoginRecord> listAll(@Param("page") Map<String, Object> page);

}
