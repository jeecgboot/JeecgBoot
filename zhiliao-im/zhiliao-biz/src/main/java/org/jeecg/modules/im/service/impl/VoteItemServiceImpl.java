package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.im.entity.VoteItem;
import org.jeecg.modules.im.mapper.VoteItemMapper;
import org.jeecg.modules.im.service.VoteItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群聊投票项 服务实现类
 * </p>
 *
 * @author junko
 * @since 2022-05-04
 */
@Service
public class VoteItemServiceImpl extends BaseServiceImpl<VoteItemMapper, VoteItem> implements VoteItemService {

}
