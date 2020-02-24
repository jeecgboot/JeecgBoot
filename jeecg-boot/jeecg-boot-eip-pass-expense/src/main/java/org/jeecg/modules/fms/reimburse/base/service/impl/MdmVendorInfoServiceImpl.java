package org.jeecg.modules.fms.reimburse.base.service.impl;

import java.io.Serializable;
import java.util.Collection;

import org.jeecg.modules.fms.reimburse.base.entity.MdmVendorInfo;
import org.jeecg.modules.fms.reimburse.base.mapper.MdmVendorAccountInfoMapper;
import org.jeecg.modules.fms.reimburse.base.mapper.MdmVendorCompanyInfoMapper;
import org.jeecg.modules.fms.reimburse.base.mapper.MdmVendorContactsInfoMapper;
import org.jeecg.modules.fms.reimburse.base.mapper.MdmVendorInfoMapper;
import org.jeecg.modules.fms.reimburse.base.service.IMdmVendorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 主数据供应商信息
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Service
@DS("reimbursement")
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
