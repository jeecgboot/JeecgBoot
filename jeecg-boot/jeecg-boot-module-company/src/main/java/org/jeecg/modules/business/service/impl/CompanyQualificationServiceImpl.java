package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
     * @param companyId companyId
     * @return 是否成功
     */
    public Map<String, List<Map<String,String>>> getQualificationFiles(String companyId) {
        List<QualificationBaseInfo> companyQualifications = qualificationMapper.queryQualificationBaseInfo(companyId,Constant.status.NORMAL);

        //查询对应的 企业附件表
        Map<String, List<Map<String,String>>> result = new HashMap<>();
        companyQualifications.forEach(companyQualification -> {
            result.computeIfAbsent(companyQualification.getType(),k-> new ArrayList<>());
            Map<String,String> param = new HashMap<>();
            param.put("id",companyQualification.getId());
            param.put("url",companyQualification.getFilepath()+companyQualification.getFilename());
            result.get(companyQualification.getType()).add(param);
        });
        return result;
    }

    /**
     * 根据ids修改资质表
     * @param ids 主键
     * @param updateParams  更改的参数
     * @return 是否成功
     */
    @Override
    public Boolean updateQualificationFiles(List<String> ids, Map<String, Object> updateParams) {
        UpdateWrapper updateWrapper = new UpdateWrapper<CompanyQualification>().in("id",ids);
        updateParams.entrySet().forEach(entry->{
            updateWrapper.set(entry.getKey(),entry.getValue());
        });
        return this.update(updateWrapper);
    }

    @Override
    public Map<String, List<Map<String, String>>> compareQualification(String applyId) {

        //查询对应的 企业附件表
        Map<String, List<Map<String,String>>> result = new HashMap<>();
        bulidResult("add",qualificationMapper.queryAddQualification(applyId), result);
        bulidResult("delete",qualificationMapper.queryDeleteQualification(applyId), result);

        return result;
    }

    private void bulidResult(String key,List<QualificationBaseInfo> addQualifications, Map<String, List<Map<String, String>>> result) {
        addQualifications.forEach(companyQualification -> {
            result.computeIfAbsent(key,k-> new ArrayList<>());
            Map<String,String> param = new HashMap<>();
            param.put("id",companyQualification.getId());
            param.put("url",companyQualification.getFilepath()+companyQualification.getFilename());
            result.get(key).add(param);
        });
    }

}
