package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 企业申报基础表
 * @Author: jeecg-boot
 * @Date: 2020-06-02
 * @Version: V1.0
 */
public interface ICompanyApplyService extends IService<CompanyApply> {

    boolean saveByBase(String companyId,String id,String status, String oldId,String fromTable);

    /**
     * @Description: 根据id和fromtable查询
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/9
     */
    CompanyApply findByNewId(String id, String fromTable);
}
