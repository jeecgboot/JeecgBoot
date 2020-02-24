package com.ibm.fms.modules.reimburse.base.service.impl;

import com.ibm.fms.modules.reimburse.base.entity.MdmVendorAccountInfo;
import com.ibm.fms.modules.reimburse.base.mapper.MdmVendorAccountInfoMapper;
import com.ibm.fms.modules.reimburse.base.service.IMdmVendorAccountInfoService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 主数据供应商银行账户
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Service
public class MdmVendorAccountInfoServiceImpl extends ServiceImpl<MdmVendorAccountInfoMapper, MdmVendorAccountInfo> implements IMdmVendorAccountInfoService {
	
	@Autowired
	private MdmVendorAccountInfoMapper mdmVendorAccountInfoMapper;
	
	@Override
	public List<MdmVendorAccountInfo> selectByMainId(String mainId) {
		return mdmVendorAccountInfoMapper.selectByMainId(mainId);
	}
}
