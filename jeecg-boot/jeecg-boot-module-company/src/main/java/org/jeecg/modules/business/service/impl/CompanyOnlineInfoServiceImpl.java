package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyCleanProduct;
import org.jeecg.modules.business.entity.CompanyOnlineInfo;
import org.jeecg.modules.business.mapper.CompanyOnlineInfoMapper;
import org.jeecg.modules.business.service.ICompanyOnlineInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 在线监控验收信息
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
@Service
public class CompanyOnlineInfoServiceImpl extends ServiceImpl<CompanyOnlineInfoMapper, CompanyOnlineInfo> implements ICompanyOnlineInfoService {

    /**
     * @Description:根据企业id查询数量
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/3
     */
    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyOnlineInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyOnlineInfo::getCompanyId, companyId);
        return this.count(queryWrapper);
    }
}
