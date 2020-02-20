package org.jeecg.modules.persona.hjrk.service;

import org.jeecg.modules.persona.hjrk.entity.ZzPersona;
import org.jeecg.modules.persona.hjrk.entity.ZzPHjrk;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 户籍人口
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
public interface IZzPHjrkService extends IService<ZzPHjrk> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(ZzPHjrk zzPHjrk,List<ZzPersona> zzPersonaList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(ZzPHjrk zzPHjrk,List<ZzPersona> zzPersonaList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
