package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.CompanyQualification;
import org.jeecg.modules.business.mapper.CompanyQualificationMapper;
import org.jeecg.modules.business.service.ICompanyQualificationService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.vo.QualificationBaseInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 企业资质
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanyQualificationServiceImpl extends ServiceImpl<CompanyQualificationMapper, CompanyQualification> implements ICompanyQualificationService {

    @Resource
    private CompanyQualificationMapper qualificationMapper;

    /**
     * 根据企业id查询正在生效的资质信息
     * @param companyId
     * @return
     */
    public Map<String, List<String>> getQualificationFiles(String companyId) {
        List<QualificationBaseInfo> companyQualifications = qualificationMapper.queryQualificationBaseInfo(companyId,Constant.status.NORMAL);

        //查询对应的 企业附件表
        Map<String,List<String>> result = new HashMap<>();
        companyQualifications.forEach(companyQualification -> {
            result.computeIfAbsent(companyQualification.getType(),k-> new ArrayList<>());
            result.get(companyQualification.getType()).add(companyQualification.getFilepath()+companyQualification.getFilename());
        });
        return result;
    }

}
