package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.Tag;
import org.jeecg.modules.im.entity.query_helper.QTag;
import org.jeecg.modules.im.mapper.TagMapper;
import org.jeecg.modules.im.service.FriendService;
import org.jeecg.modules.im.service.TagService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户的标签 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Service
@Slf4j
public class TagServiceImpl extends BaseServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Resource
    private FriendService friendService;
    @Override
    public List<Tag> findAllOfUser(Integer userId) {
        return tagMapper.findAllOfUser(userId);
    }

    @Override
    public Tag findByNameOfUser(Tag tag) {
        return tagMapper.findByNameOfUser(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> create(String name,String friendIds) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setUserId(getCurrentUserId());
        if(findByNameOfUser(tag)!=null){
            return fail("已存在标签["+tag.getName()+"]");
        }
        if(isEmpty(friendIds)){
            return fail("至少选择一个好友");
        }
        try {
            Friend friend;
            int count = 0;
            for (String friendId : StringUtils.split(friendIds, ",")) {
                friend=friendService.findByIdOfUser(Integer.parseInt(friendId),getCurrentUserId());
                if(friend==null||!friend.getStatus().equals(Friend.Status.Friend.getCode())){
                    throw new BusinessException("请选择好友");
                }
                count++;
            }
            tag.setFriendIds(friendIds);
            tag.setCount(count);
            tag.setTsCreate(getTs());
            if (!save(tag)) {
                return fail("创建标签失败");
            }
            return success("标签创建成功",findAllOfUser(getCurrentUserId()));
        }catch (Exception e){
            log.error("创建标签并关联好友异常：tag={},friendIds={},e={}", tag,friendIds, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    /**
     * 更新标签，更新好友的标签
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> update(Tag tag) {
        Tag temp = getById(tag.getId());
        if(temp==null||!temp.getUserId().equals(getCurrentUserId())){
            return fail("标签不存在");
        }
        temp = findByNameOfUser(tag);
        //如果新名称已存在且不是当前标签
        if(temp!=null&&!temp.getId().equals(tag.getId())){
            return fail("已存在标签["+tag.getName()+"]");
        }
        if(isEmpty(tag.getFriendIds())){
            return fail("至少选择一个好友");
        }
        try {
            StringBuilder friendIds = new StringBuilder(",");
            Friend friend;
            int count = 0;
            QTag q = new QTag();
            for (String friendId : StringUtils.split(tag.getFriendIds(), ",")) {
                if(isEmpty(friendId)){
                    continue;
                }
                friendIds.append(friendId).append(",");
                friend=friendService.findByIdOfUser(Integer.parseInt(friendId),getCurrentUserId());
                if(friend==null||!friend.getStatus().equals(Friend.Status.Friend.getCode())){
                    throw new BusinessException("请选择好友");
                }
                //好友标签更新
                q.setUserId(getCurrentUserId());
                q.setFriendId(String.format(",%s,",friendId));
                friend.setTagIds(tagListToIds(findByFriendIdOfUser(q)));
                count++;
            }
            tag.setFriendIds(friendIds.toString());
            tag.setCount(count);
            if (!updateById(tag)) {
                return fail("更新标签失败");
            }
            return success("标签已更新");
        }catch (Exception e){
            log.error("更新标签异常：tag={},e={}", tag, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    /**
     * 删除标签，更新标签中好友的标签
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> del(String id) {
        Tag tag = getById(id);
        if(tag==null||!tag.getUserId().equals(getCurrentUserId())){
            return fail("标签不存在");
        }
        try{
            if(!removeById(id)){
                return fail("删除失败");
            }
            if(!isEmpty(tag.getFriendIds())){
                Friend friend;
                QTag q = new QTag();
                for (String friendId : StringUtils.split(tag.getFriendIds(), ",")) {
                    if(isEmpty(friendId)){
                        continue;
                    }
                    friend = friendService.getById(friendId);
                    //好友标签更新
                    q.setUserId(getCurrentUserId());
                    q.setFriendId(String.format(",%s,",friendId));
                    friend.setTagIds(tagListToIds(findByFriendIdOfUser(q)));
                    friendService.updateById(friend);
                }
            }
            return success();
        }catch (Exception e){
            log.error("删除标签异常：tag={},e={}", tag, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    @Override
    public List<Tag> findByFriendIdOfUser(QTag q) {
        return tagMapper.findByFriendIdOfUser(q);
    }

    @Override
    public String tagListToIds(List<Tag> tags) {
        if(tags==null||tags.isEmpty()){
            return "";
        }
        StringBuilder ids = new StringBuilder(",");
        for (Tag tag : tags) {
            ids.append(tag.getId()).append(",");
        }
        return ids.toString();
    }
}
