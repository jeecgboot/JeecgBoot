package org.jeecg.modules.system.mapper;

import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysDictItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    /**
     * 通过字典id查询字典项
     * @param mainId 字典id
     * @return
     */
    @Select("SELECT * FROM sys_dict_item WHERE DICT_ID = #{mainId} order by sort_order asc, item_value asc")
    public List<SysDictItem> selectItemsByMainId(String mainId);
}
