package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysTableWhiteList;

import java.util.Map;

/**
 * @Description: 系统表白名单
 * @Author: jeecg-boot
 * @Date: 2023-09-12
 * @Version: V1.0
 */
public interface ISysTableWhiteListService extends IService<SysTableWhiteList> {

    /**
     * 新增
     *
     * @param sysTableWhiteList
     * @return
     */
    boolean add(SysTableWhiteList sysTableWhiteList);

    /**
     * 编辑
     *
     * @param sysTableWhiteList
     * @return
     */
    boolean edit(SysTableWhiteList sysTableWhiteList);

    /**
     * 通过id删除，可批量删除
     *
     * @param ids 多个使用逗号分割
     * @return
     */
    boolean deleteByIds(String ids);

    /**
     * 自动添加到数据库中
     *
     * @param tableName
     * @param fieldName
     * @return
     */
    SysTableWhiteList autoAdd(String tableName, String fieldName);

    /**
     * 以map的方式获取所有数据
     * key=tableName，value=fieldName
     *
     * @return
     */
    Map<String, String> getAllConfigMap();

}
