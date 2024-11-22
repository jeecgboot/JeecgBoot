package org.jeecg.modules.business.mapper;

import java.util.List;
import org.jeecg.modules.business.entity.Shop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.vo.ShopPage;
import org.springframework.stereotype.Repository;

/**
 * @Description: 店铺
 * @Author: jeecg-boot
 * @Date:   2021-04-02
 * @Version: V1.0
 */
@Repository
public interface ShopMapper extends BaseMapper<Shop> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<Shop> selectByMainId(@Param("mainId") String mainId);

    List<Shop> selectByClient(@Param("clientID") String clientID);

    List<String> selectShopIdByClient(@Param("clientID") String clientID);

    List<String> getShopIdsByClientAndType(@Param("clientIds") List<String> clientIds, @Param("type") String clientType);

    String getNameById(@Param("shopId") String shopId);

    String getCodeById(@Param("shopId") String shopId);

    List<ShopPage> listShopGroupedByClient();

    String getIdByCode(@Param("code") String erpCode);
}
