package org.jeecg.modules.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysComment;
import org.jeecg.modules.system.vo.SysCommentFileVo;
import org.jeecg.modules.system.vo.SysCommentVO;
import org.jeecg.modules.system.vo.UserAvatar;

import java.util.List;
import java.util.Set;

/**
 * @Description: 系统评论回复表
 * @Author: jeecg-boot
 * @Date: 2022-07-19
 * @Version: V1.0
 */
public interface SysCommentMapper extends BaseMapper<SysComment> {

    List<SysCommentVO> queryCommentList(@Param("tableName") String tableName, @Param("formDataId") String formDataId);

    /**
     * 根据表名和数据id查询表单文件
     *
     * @param tableName
     * @param formDataId
     * @return
     */
    List<SysCommentFileVo> queryFormFileList(@Param("tableName") String tableName, @Param("formDataId") String formDataId);

    /**
     * 根据用户名获取用户信息
     * @param idSet
     * @return
     */
    List<UserAvatar> queryUserAvatarList(@Param("idSet") Set<String> idSet);

}
