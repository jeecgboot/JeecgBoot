package org.jeecg.modules.fms.reimburse.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.fms.reimburse.base.entity.MdmVendorAccountInfo;
import org.jeecg.modules.fms.reimburse.base.vo.VendorAccntVO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 主数据供应商银行账户
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
public interface MdmVendorAccountInfoMapper extends BaseMapper<MdmVendorAccountInfo> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<MdmVendorAccountInfo> selectByMainId(@Param("mainId") String mainId);
	
	public List<VendorAccntVO> selectVendorAccntByVendorcodeAndOrgcode(Page<VendorAccntVO> page, @Param("vendorCode") String vendorCode,@Param("orgCode") String orgCode);

}
