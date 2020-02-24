package org.jeecg.modules.fms.reimburse.base.service.impl;

import java.util.List;

import org.jeecg.modules.fms.reimburse.base.entity.MdmVendorAccountInfo;
import org.jeecg.modules.fms.reimburse.base.mapper.MdmVendorAccountInfoMapper;
import org.jeecg.modules.fms.reimburse.base.service.IMdmVendorAccountInfoService;
import org.jeecg.modules.fms.reimburse.base.vo.VendorAccntVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 主数据供应商银行账户
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Service
@DS("reimbursement")
public class MdmVendorAccountInfoServiceImpl extends ServiceImpl<MdmVendorAccountInfoMapper, MdmVendorAccountInfo> implements IMdmVendorAccountInfoService {
	
	@Autowired
	private MdmVendorAccountInfoMapper mdmVendorAccountInfoMapper;
	
	@Override
	public List<MdmVendorAccountInfo> selectByMainId(String mainId) {
		return mdmVendorAccountInfoMapper.selectByMainId(mainId);
	}
	
	public IPage<VendorAccntVO> selectPageVendorAccntByVendorcodeAndOrgcode(Page<VendorAccntVO> page,String vendorCode,String orgCode){
		
		return page.setRecords(mdmVendorAccountInfoMapper.selectVendorAccntByVendorcodeAndOrgcode(page, vendorCode, orgCode));
	}
}
