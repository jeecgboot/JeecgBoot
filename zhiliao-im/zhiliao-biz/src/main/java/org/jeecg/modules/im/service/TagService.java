package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Tag;
import org.jeecg.modules.im.entity.query_helper.QTag;

import java.util.List;

/**
 * <p>
 * 用户的标签 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
public interface TagService extends IService<Tag> {

    List<Tag> findAllOfUser(Integer userId);

    /**
     * 查找用户指定名称的标签
     */
    Tag findByNameOfUser(Tag tag);

    /**
     * 创建标签并关联好友
     */
    Result<Object> create(String name, String friendIds);
    /**
     * 更新标签并关联好友
     */
    Result<Object> update(Tag tag);

    /**
     * 删除标签
     */
    Result<Object> del(String id);

    /**
     * 查询某个用户给某个好友设置的标签
     */
    List<Tag> findByFriendIdOfUser(QTag q);

    String tagListToIds(List<Tag> tags);
}
