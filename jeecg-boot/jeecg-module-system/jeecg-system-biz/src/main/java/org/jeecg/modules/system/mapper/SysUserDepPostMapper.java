package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysUserDepPost;

import java.util.List;

/**
 * @Description: 部门岗位用户关联表 Mapper
 * @author: wangshuai
 * @date: 2025/9/5 12:01
 */
public interface SysUserDepPostMapper extends BaseMapper<SysUserDepPost> {

    /**
     * 通过用户id查询部门岗位用户
     *
     * @param userId
     * @return
     */
    @Select("select dep_id from sys_user_dep_post where user_id = #{userId}")
    List<String> getDepPostByUserId(@Param("userId") String userId);
}
