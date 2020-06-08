package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.CompanyAcceptance;
import org.jeecg.modules.business.entity.CompanyApply;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.CompanyBaseinfo;

/**
 * @Description: 企业申报基础表
 * @Author: jeecg-boot
 * @Date: 2020-06-02
 * @Version: V1.0
 */
public interface ICompanyApplyService extends IService<CompanyApply> {

    boolean saveByBase(CompanyBaseinfo companyBaseinfo, String oldId);

    /**保存竣工验收信息申报记录
     * @Description:
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/5
     */
    boolean saveByBase(CompanyAcceptance companyAcceptance);
}
