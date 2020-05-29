package org.jeecg.modules.business.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import org.jeecg.modules.business.entity.CompanySysuser;
import org.jeecg.modules.business.mapper.CompanyBaseinfoMapper;
import org.jeecg.modules.business.mapper.CompanySysuserMapper;
import org.jeecg.modules.business.service.ICompanySysuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 企业人员信息
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */



@Service
public class CompanySysuserServiceImpl extends ServiceImpl<CompanySysuserMapper, CompanySysuser> implements ICompanySysuserService {

    @Resource
    CompanyBaseinfoMapper companyBaseinfoMapper;

    /**
     *  根据借据号查找 公司用户中间表
     * @param userid
     * @return
     */
    public List<CompanySysuser> list(String userid) {
        return this.list(new QueryWrapper<CompanySysuser>().lambda().eq(CompanySysuser::getSysUserid,userid));
    }
    /**
     *  根据借据号删除 公司用户中间表
     * @param userid
     * @return
     */
    public  boolean removeByUserId(String userid){
        return this.remove(new QueryWrapper<CompanySysuser>().lambda().eq(CompanySysuser::getSysUserid,userid));
    }


    public  void save(String userid, String companys) {
        //插入新的费用账户信息表
        if(!StrUtil.isEmpty(companys)){
            //根据   查询  companyID
            List<CompanySysuser> CompanySysuser = new ArrayList<>();
            //组装  companySysuser
            Arrays.asList(companys.split(",")).forEach(companyId -> {
                CompanySysuser companySysuser = new CompanySysuser();
                companySysuser.setCompanyId(companyId);
                companySysuser.setSysUserid(userid);
                CompanySysuser.add(companySysuser);
            });
            this.saveBatch(CompanySysuser);
        }
    }
}
