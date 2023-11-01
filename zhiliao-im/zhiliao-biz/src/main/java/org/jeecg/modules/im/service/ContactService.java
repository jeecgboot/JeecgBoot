package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Contact;
import org.jeecg.modules.im.entity.query_helper.QContact;

import java.util.List;

/**
 * <p>
 * 通讯录联系人 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-07
 */
public interface ContactService extends IService<Contact> {
    IPage<Contact> pagination(MyPage<Contact> page, QContact q);

    List<Contact> findAllOfUser(Integer userId);
    //查询用户的通讯录数
    Integer getCountOfUser(Integer userId);
}
