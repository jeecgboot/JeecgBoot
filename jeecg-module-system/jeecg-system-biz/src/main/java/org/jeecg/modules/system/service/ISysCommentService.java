package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysComment;
import org.jeecg.modules.system.vo.SysCommentFileVo;
import org.jeecg.modules.system.vo.SysCommentVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 系统评论回复表
 * @Author: jeecg-boot
 * @Date: 2022-07-19
 * @Version: V1.0
 */
public interface ISysCommentService extends IService<SysComment> {


    /**
     * 保存评论 返回评论ID
     *
     * @param sysComment
     */
    String saveOne(SysComment sysComment);

    /**
     * 删除
     *
     * @param id
     */
    void deleteOne(String id);

    /**
     * 根据表名和数据id查询表单评论及文件信息
     *
     * @param sysComment
     * @return
     */
    List<SysCommentVO> queryFormCommentInfo(SysComment sysComment);


    /**
     * 保存文件+评论
     *
     * @param req
     */
    void saveOneFileComment(HttpServletRequest req);


    /**
     * 查询当前表单的文件列表
     *
     * @param tableName
     * @param formDataId
     * @return
     */
    List<SysCommentFileVo> queryFormFileList(String tableName, String formDataId);
    /**
     * app端 保存文件+评论
     *
     * @param request
     */
    void appSaveOneFileComment(HttpServletRequest request);
}
