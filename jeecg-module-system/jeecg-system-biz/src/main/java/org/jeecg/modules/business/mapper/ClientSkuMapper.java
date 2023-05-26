package org.jeecg.modules.business.mapper;

import java.util.List;
import org.jeecg.modules.business.entity.ClientSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 客户名下SKU
 * @Author: jeecg-boot
 * @Date:   2021-04-02
 * @Version: V1.0
 */
@Repository
public interface ClientSkuMapper extends BaseMapper<ClientSku> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	List<ClientSku> selectByMainId(@Param("mainId") String mainId);
}
