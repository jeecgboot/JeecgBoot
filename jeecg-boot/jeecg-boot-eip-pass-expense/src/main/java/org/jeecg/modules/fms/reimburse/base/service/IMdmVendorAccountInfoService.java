package org.jeecg.modules.fms.reimburse.base.service;

import java.util.List;

import org.jeecg.modules.fms.reimburse.base.entity.MdmVendorAccountInfo;
import org.jeecg.modules.fms.reimburse.base.vo.VendorAccntVO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 主数据供应商银行账户
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
public interface IMdmVendorAccountInfoService extends IService<MdmVendorAccountInfo> {

	public List<MdmVendorAccountInfo> selectByMainId(String mainId);
	
	public IPage<VendorAccntVO> selectPageVendorAccntByVendorcodeAndOrgcode(Page<VendorAccntVO> page,String vendorCode,String orgCode);
}
