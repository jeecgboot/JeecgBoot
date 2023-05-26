package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.ClientSku;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 客户名下SKU
 * @Author: jeecg-boot
 * @Date:   2021-04-02
 * @Version: V1.0
 */
public interface IClientSkuService extends IService<ClientSku> {

	public List<ClientSku> selectByMainId(String mainId);
}
