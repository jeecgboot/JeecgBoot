package com.ibm.fms.modules.reimburse.base.service.impl;

import com.ibm.fms.modules.reimburse.base.entity.MdmVendorInfo;
import com.ibm.fms.modules.reimburse.base.entity.MdmVendorCompanyInfo;
import com.ibm.fms.modules.reimburse.base.entity.MdmVendorAccountInfo;
import com.ibm.fms.modules.reimburse.base.entity.MdmVendorContactsInfo;
import com.ibm.fms.modules.reimburse.base.mapper.MdmVendorCompanyInfoMapper;
import com.ibm.fms.modules.reimburse.base.mapper.MdmVendorAccountInfoMapper;
import com.ibm.fms.modules.reimburse.base.mapper.MdmVendorContactsInfoMapper;
import com.ibm.fms.modules.reimburse.base.mapper.MdmVendorInfoMapper;
import com.ibm.fms.modules.reimburse.base.service.IMdmVendorInfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 主数据供应商信息
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Service
public class MdmVendorInfoServiceImpl extends ServiceImpl<MdmVendorInfoMapper, MdmVendorInfo> implements IMdmVendorInfoService {

	@Autowired
	private MdmVendorInfoMapper mdmVendorInfoMapper;
	@Autowired
	private MdmVendorCompanyInfoMapper mdmVendorCompanyInfoMapper;
	@Autowired
	private MdmVendorAccountInfoMapper mdmVendorAccountInfoMapper;
	@Autowired
	private MdmVendorContactsInfoMapper mdmVendorContactsInfoMapper;
	
	@Override
	@Transactional
	public void delMain(String id) {
		mdmVendorCompanyInfoMapper.deleteByMainId(id);
		mdmVendorAccountInfoMapper.deleteByMainId(id);
		mdmVendorContactsInfoMapper.deleteByMainId(id);
		mdmVendorInfoMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			mdmVendorCompanyInfoMapper.deleteByMainId(id.toString());
			mdmVendorAccountInfoMapper.deleteByMainId(id.toString());
			mdmVendorContactsInfoMapper.deleteByMainId(id.toString());
			mdmVendorInfoMapper.deleteById(id);
		}
	}
	
}
