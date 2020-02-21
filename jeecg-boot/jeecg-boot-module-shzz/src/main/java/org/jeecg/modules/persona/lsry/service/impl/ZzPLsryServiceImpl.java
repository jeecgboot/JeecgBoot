package org.jeecg.modules.persona.lsry.service.impl;

import org.jeecg.modules.persona.lsry.entity.ZzPLsry;
import org.jeecg.modules.persona.lsry.entity.ZzPLsryPersona;
import org.jeecg.modules.persona.lsry.mapper.ZzPLsryPersonaMapper;
import org.jeecg.modules.persona.lsry.mapper.ZzPLsryMapper;
import org.jeecg.modules.persona.lsry.service.IZzPLsryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 留守人员
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Service
public class ZzPLsryServiceImpl extends ServiceImpl<ZzPLsryMapper, ZzPLsry> implements IZzPLsryService {

	@Autowired
	private ZzPLsryMapper zzPLsryMapper;
	@Autowired
	private ZzPLsryPersonaMapper zzPLsryPersonaMapper;
	
	@Override
	@Transactional
	public void saveMain(ZzPLsry zzPLsry, List<ZzPLsryPersona> zzPLsryPersonaList) {
		zzPLsryMapper.insert(zzPLsry);
		if(zzPLsryPersonaList!=null && zzPLsryPersonaList.size()>0) {
			for(ZzPLsryPersona entity:zzPLsryPersonaList) {
				//外键设置
				entity.setZzPLsryId(zzPLsry.getId());
				zzPLsryPersonaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(ZzPLsry zzPLsry,List<ZzPLsryPersona> zzPLsryPersonaList) {
		zzPLsryMapper.updateById(zzPLsry);
		
		//1.先删除子表数据
		zzPLsryPersonaMapper.deleteByMainId(zzPLsry.getId());
		
		//2.子表数据重新插入
		if(zzPLsryPersonaList!=null && zzPLsryPersonaList.size()>0) {
			for(ZzPLsryPersona entity:zzPLsryPersonaList) {
				//外键设置
				entity.setZzPLsryId(zzPLsry.getId());
				zzPLsryPersonaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		zzPLsryPersonaMapper.deleteByMainId(id);
		zzPLsryMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			zzPLsryPersonaMapper.deleteByMainId(id.toString());
			zzPLsryMapper.deleteById(id);
		}
	}
	
}
