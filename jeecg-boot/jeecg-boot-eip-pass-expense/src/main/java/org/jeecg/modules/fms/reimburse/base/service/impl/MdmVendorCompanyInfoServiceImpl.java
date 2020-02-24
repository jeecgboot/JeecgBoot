package org.jeecg.modules.fms.reimburse.base.service.impl;

import org.jeecg.modules.fms.reimburse.base.entity.MdmVendorCompanyInfo;
import org.jeecg.modules.fms.reimburse.base.mapper.MdmVendorCompanyInfoMapper;
import org.jeecg.modules.fms.reimburse.base.service.IMdmVendorCompanyInfoService;
import org.springframework.stereotype.Service;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 主数据供应商归属组织
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Service
@DS("reimbursement")
public class MdmVendorCompanyInfoServiceImpl extends ServiceImpl<MdmVendorCompanyInfoMapper, MdmVendorCompanyInfo> implements IMdmVendorCompanyInfoService {
	
	@Autowired
	private MdmVendorCompanyInfoMapper mdmVendorCompanyInfoMapper;
	
	@Override
	public List<MdmVendorCompanyInfo> selectByMainId(String mainId) {
		return mdmVendorCompanyInfoMapper.selectByMainId(mainId);
	}
}
