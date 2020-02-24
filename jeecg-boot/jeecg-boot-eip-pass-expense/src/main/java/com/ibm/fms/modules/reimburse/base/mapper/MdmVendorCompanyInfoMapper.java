package com.ibm.fms.modules.reimburse.base.mapper;

import java.util.List;
import com.ibm.fms.modules.reimburse.base.entity.MdmVendorCompanyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 主数据供应商归属组织
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
public interface MdmVendorCompanyInfoMapper extends BaseMapper<MdmVendorCompanyInfo> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<MdmVendorCompanyInfo> selectByMainId(@Param("mainId") String mainId);

}
