package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.im.entity.Vote;
import org.jeecg.modules.im.mapper.VoteMapper;
import org.jeecg.modules.im.service.VoteService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群聊投票 服务实现类
 * </p>
 *
 * @author junko
 * @since 2022-05-04
 */
@Service
public class VoteServiceImpl extends BaseServiceImpl<VoteMapper, Vote> implements VoteService {

}
