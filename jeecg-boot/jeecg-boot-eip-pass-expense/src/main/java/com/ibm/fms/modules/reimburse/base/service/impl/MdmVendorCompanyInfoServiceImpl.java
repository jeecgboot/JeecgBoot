package com.ibm.fms.modules.reimburse.base.service.impl;

import com.ibm.fms.modules.reimburse.base.entity.MdmVendorCompanyInfo;
import com.ibm.fms.modules.reimburse.base.mapper.MdmVendorCompanyInfoMapper;
import com.ibm.fms.modules.reimburse.base.service.IMdmVendorCompanyInfoService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 主数据供应商归属组织
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Service
public class MdmVendorCompanyInfoServiceImpl extends ServiceImpl<MdmVendorCompanyInfoMapper, MdmVendorCompanyInfo> implements IMdmVendorCompanyInfoService {
	
	@Autowired
	private MdmVendorCompanyInfoMapper mdmVendorCompanyInfoMapper;
	
	@Override
	public List<MdmVendorCompanyInfo> selectByMainId(String mainId) {
		return mdmVendorCompanyInfoMapper.selectByMainId(mainId);
	}
}
