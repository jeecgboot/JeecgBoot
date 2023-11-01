package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.query_helper.QNotice;
import org.jeecg.modules.im.mapper.NoticeMapper;
import org.jeecg.modules.im.mapper.NoticeMapper;
import org.jeecg.modules.im.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 公告 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Override
    public IPage<Notice> pagination(MyPage<Notice> page, QNotice q) {
        return noticeMapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(Notice notice) {
        if(notice.getId()==null){
            notice.setTsCreate(getTs());
            if(!save(notice)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(notice)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public Notice findById(String id) {
        return noticeMapper.selectById(id);
    }
}
