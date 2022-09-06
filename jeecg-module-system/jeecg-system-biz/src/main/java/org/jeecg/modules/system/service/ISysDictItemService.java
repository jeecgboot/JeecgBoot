package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysDictItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface ISysDictItemService extends IService<SysDictItem> {

    /**
     * 通过字典id查询字典项
     * @param mainId 字典id
     * @return
     */
    public List<SysDictItem> selectItemsByMainId(String mainId);
}
