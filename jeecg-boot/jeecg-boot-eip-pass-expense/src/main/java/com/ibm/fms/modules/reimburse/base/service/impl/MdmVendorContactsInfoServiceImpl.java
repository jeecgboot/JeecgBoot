package com.ibm.fms.modules.reimburse.base.service.impl;

import com.ibm.fms.modules.reimburse.base.entity.MdmVendorContactsInfo;
import com.ibm.fms.modules.reimburse.base.mapper.MdmVendorContactsInfoMapper;
import com.ibm.fms.modules.reimburse.base.service.IMdmVendorContactsInfoService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 主数据供应商联系人
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Service
public class MdmVendorContactsInfoServiceImpl extends ServiceImpl<MdmVendorContactsInfoMapper, MdmVendorContactsInfo> implements IMdmVendorContactsInfoService {
	
	@Autowired
	private MdmVendorContactsInfoMapper mdmVendorContactsInfoMapper;
	
	@Override
	public List<MdmVendorContactsInfo> selectByMainId(String mainId) {
		return mdmVendorContactsInfoMapper.selectByMainId(mainId);
	}
}
