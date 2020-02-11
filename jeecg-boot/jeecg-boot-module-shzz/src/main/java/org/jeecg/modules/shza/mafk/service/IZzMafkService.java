package org.jeecg.modules.shza.mafk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.shza.mafk.entity.ZzMafk;
import org.jeecg.modules.shza.mafk.entity.ZzMafkPerson;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 命案主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IZzMafkService extends IService<ZzMafk> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(ZzMafk zzMafk, List<ZzMafkPerson> zzMafkPersonShrList, List<ZzMafkPerson> zzMafkPersonXyrList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(ZzMafk zzMafk, List<ZzMafkPerson> zzMafkPersonShrList, List<ZzMafkPerson> zzMafkPersonXyrList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
