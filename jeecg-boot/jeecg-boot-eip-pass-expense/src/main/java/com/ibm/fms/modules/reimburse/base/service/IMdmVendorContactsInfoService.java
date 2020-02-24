package com.ibm.fms.modules.reimburse.base.service;

import com.ibm.fms.modules.reimburse.base.entity.MdmVendorContactsInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 主数据供应商联系人
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
public interface IMdmVendorContactsInfoService extends IService<MdmVendorContactsInfo> {

	public List<MdmVendorContactsInfo> selectByMainId(String mainId);
}
