package org.jeecg.modules.persona.hjrk.service.impl;

import org.jeecg.modules.persona.hjrk.entity.ZzPHjrk;
import org.jeecg.modules.persona.hjrk.entity.ZzPersona;
import org.jeecg.modules.persona.hjrk.mapper.ZzPersonaMapper;
import org.jeecg.modules.persona.hjrk.mapper.ZzPHjrkMapper;
import org.jeecg.modules.persona.hjrk.service.IZzPHjrkService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 户籍人口
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@Service
public class ZzPHjrkServiceImpl extends ServiceImpl<ZzPHjrkMapper, ZzPHjrk> implements IZzPHjrkService {

	@Autowired
	private ZzPHjrkMapper zzPHjrkMapper;
	@Autowired
	private ZzPersonaMapper zzPersonaMapper;
	
	@Override
	@Transactional
	public void saveMain(ZzPHjrk zzPHjrk, List<ZzPersona> zzPersonaList) {
		zzPHjrkMapper.insert(zzPHjrk);
		if(zzPersonaList!=null && zzPersonaList.size()>0) {
			for(ZzPersona entity:zzPersonaList) {
				//外键设置
				entity.setOutId(zzPHjrk.getId());
				zzPersonaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(ZzPHjrk zzPHjrk,List<ZzPersona> zzPersonaList) {
		zzPHjrkMapper.updateById(zzPHjrk);
		
		//1.先删除子表数据
		zzPersonaMapper.deleteByMainId(zzPHjrk.getId());
		
		//2.子表数据重新插入
		if(zzPersonaList!=null && zzPersonaList.size()>0) {
			for(ZzPersona entity:zzPersonaList) {
				//外键设置
				entity.setOutId(zzPHjrk.getId());
				zzPersonaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		zzPersonaMapper.deleteByMainId(id);
		zzPHjrkMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			zzPersonaMapper.deleteByMainId(id.toString());
			zzPHjrkMapper.deleteById(id);
		}
	}
	
}
