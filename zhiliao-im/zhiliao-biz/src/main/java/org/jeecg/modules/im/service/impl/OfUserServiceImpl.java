package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.OfUser;
import org.jeecg.modules.im.entity.Tag;
import org.jeecg.modules.im.entity.query_helper.QTag;
import org.jeecg.modules.im.mapper.OfUserMapper;
import org.jeecg.modules.im.mapper.TagMapper;
import org.jeecg.modules.im.service.FriendService;
import org.jeecg.modules.im.service.OfUserService;
import org.jeecg.modules.im.service.TagService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * openfire用户
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Service
@Slf4j
public class OfUserServiceImpl extends BaseServiceImpl<OfUserMapper, OfUser> implements OfUserService {

    @Autowired
    private OfUserMapper ofUserMapper;

    @Override
    public OfUser findByUsername(String username) {
        return ofUserMapper.findByUsername(username);
    }

}
