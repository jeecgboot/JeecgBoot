package com.xxl.job.admin.business.mapper;

import com.xxl.job.admin.business.model.XxlJobGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobGroupMapper {

    public List<XxlJobGroup> findAll();

    public List<XxlJobGroup> findByAddressType(@Param("addressType") int addressType);

    public int save(XxlJobGroup xxlJobGroup);

    public int update(XxlJobGroup xxlJobGroup);

    public int remove(@Param("id") int id);

    public XxlJobGroup load(@Param("id") int id);

    public List<XxlJobGroup> pageList(@Param("offset") int offset,
                                      @Param("pagesize") int pagesize,
                                      @Param("appname") String appname,
                                      @Param("title") String title);

    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("appname") String appname,
                             @Param("title") String title);

}
