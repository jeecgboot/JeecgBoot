package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Contact;
import org.jeecg.modules.im.entity.query_helper.QContact;

import java.util.List;

/**
 * <p>
 * 通讯录联系人 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-02-07
 */
@Mapper
public interface ContactMapper extends BaseMapper<Contact> {
    MyPage<Contact> pagination(@Param("pg") MyPage<Contact> pg, @Param("q") QContact q);

    List<Contact> findAllOfUser(Integer userId);
}
