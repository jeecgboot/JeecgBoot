package com.ibm.fms.modules.reimburse.base.service;

import com.ibm.fms.modules.reimburse.base.entity.MdmVendorAccountInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 主数据供应商银行账户
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
public interface IMdmVendorAccountInfoService extends IService<MdmVendorAccountInfo> {

	public List<MdmVendorAccountInfo> selectByMainId(String mainId);
}
