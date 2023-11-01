package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Contact;
import org.jeecg.modules.im.entity.Contact;
import org.jeecg.modules.im.entity.query_helper.QContact;
import org.jeecg.modules.im.mapper.ContactMapper;
import org.jeecg.modules.im.service.ContactService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 通讯录联系人 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-07
 */
@Service
public class ContactServiceImpl extends BaseServiceImpl<ContactMapper, Contact> implements ContactService {
    @Autowired
    private ContactMapper contactMapper;
    @Override
    public IPage<Contact> pagination(MyPage<Contact> page, QContact q) {
        return contactMapper.pagination(page,q);
    }
    @Override
    public List<Contact> findAllOfUser(Integer userId) {
        return contactMapper.findAllOfUser(userId);
    }

    @Override
    public Integer getCountOfUser(Integer userId) {
        LambdaQueryWrapper<Contact> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Contact::getUserId,userId);
        return (int)count(wrapper);
    }
}
