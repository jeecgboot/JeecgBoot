package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.query_helper.QLink;
import org.jeecg.modules.im.mapper.LinkMapper;
import org.jeecg.modules.im.mapper.LinkMapper;
import org.jeecg.modules.im.service.LinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
@Service
public class LinkServiceImpl extends BaseServiceImpl<LinkMapper, Link> implements LinkService {
    @Autowired
    private LinkMapper linkMapper;
    @Override
    public IPage<Link> pagination(MyPage<Link> page, QLink q) {
        return linkMapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(Link link) {
        if(link.getId()==null){
            link.setTsCreate(getTs());
            if(!save(link)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(link)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public Link findById(String id) {
        return linkMapper.selectById(id);
    }

    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        linkMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }
}
