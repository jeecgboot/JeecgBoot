package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.im.entity.Votation;
import org.jeecg.modules.im.mapper.VotationMapper;
import org.jeecg.modules.im.service.VotationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 投票结果 服务实现类
 * </p>
 *
 * @author junko
 * @since 2022-05-04
 */
@Service
public class VotationServiceImpl extends BaseServiceImpl<VotationMapper, Votation> implements VotationService {

}
