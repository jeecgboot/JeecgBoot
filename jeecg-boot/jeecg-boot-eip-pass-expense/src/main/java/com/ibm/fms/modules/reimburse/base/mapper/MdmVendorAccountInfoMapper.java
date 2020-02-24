package com.ibm.fms.modules.reimburse.base.mapper;

import java.util.List;
import com.ibm.fms.modules.reimburse.base.entity.MdmVendorAccountInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 主数据供应商银行账户
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
public interface MdmVendorAccountInfoMapper extends BaseMapper<MdmVendorAccountInfo> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<MdmVendorAccountInfo> selectByMainId(@Param("mainId") String mainId);

}
