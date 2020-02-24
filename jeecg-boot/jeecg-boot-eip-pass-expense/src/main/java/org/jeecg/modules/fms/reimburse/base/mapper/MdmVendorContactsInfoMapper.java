package org.jeecg.modules.fms.reimburse.base.mapper;

import java.util.List;
import org.jeecg.modules.fms.reimburse.base.entity.MdmVendorContactsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 主数据供应商联系人
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
public interface MdmVendorContactsInfoMapper extends BaseMapper<MdmVendorContactsInfo> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<MdmVendorContactsInfo> selectByMainId(@Param("mainId") String mainId);

}
