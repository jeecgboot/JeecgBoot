package org.jeecg.modules.shza.mafk.service.impl;

import org.jeecg.enums.MaPersonTypeEnum;
import org.jeecg.modules.shza.mafk.entity.ZzMafk;
import org.jeecg.modules.shza.mafk.entity.ZzMafkPerson;
import org.jeecg.modules.shza.mafk.mapper.ZzMafkPersonMapper;
import org.jeecg.modules.shza.mafk.mapper.ZzMafkMapper;
import org.jeecg.modules.shza.mafk.service.IZzMafkService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 命案主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class ZzMafkServiceImpl extends ServiceImpl<ZzMafkMapper, ZzMafk> implements IZzMafkService {

	@Autowired
	private ZzMafkMapper zzMafkMapper;
	@Autowired
	private ZzMafkPersonMapper zzMafkPersonMapper;

	@Override
	@Transactional
	public void saveMain(ZzMafk zzMafk, List<ZzMafkPerson> zzMafkPersonShrList, List<ZzMafkPerson> zzMafkPersonXyrList) {
		zzMafkMapper.insert(zzMafk);
		if(zzMafkPersonShrList!=null && zzMafkPersonShrList.size()>0) {
			for(ZzMafkPerson entity:zzMafkPersonShrList) {
				//外键设置
				entity.setZzMafkId(zzMafk.getId());
				// 受害人
				entity.setType(MaPersonTypeEnum.SHR.getValue());
				zzMafkPersonMapper.insert(entity);
			}
		}
		if(zzMafkPersonXyrList!=null && zzMafkPersonXyrList.size()>0) {
			for(ZzMafkPerson entity:zzMafkPersonXyrList) {
				//外键设置
				entity.setZzMafkId(zzMafk.getId());
				// 嫌疑人
				entity.setType(MaPersonTypeEnum.XYR.getValue());
				zzMafkPersonMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(ZzMafk zzMafk, List<ZzMafkPerson> zzMafkPersonShrList, List<ZzMafkPerson> zzMafkPersonXyrList) {
		zzMafkMapper.updateById(zzMafk);
		
		//1.先删除子表数据
		zzMafkPersonMapper.deleteByMainId(zzMafk.getId());
		zzMafkPersonMapper.deleteByMainId(zzMafk.getId());
		
		//2.子表数据重新插入
		if(zzMafkPersonShrList!=null && zzMafkPersonShrList.size()>0) {
			for(ZzMafkPerson entity:zzMafkPersonShrList) {
				//外键设置
				entity.setZzMafkId(zzMafk.getId());
				// 受害人
				entity.setType(MaPersonTypeEnum.SHR.getValue());
				zzMafkPersonMapper.insert(entity);
			}
		}
		if(zzMafkPersonXyrList!=null && zzMafkPersonXyrList.size()>0) {
			for(ZzMafkPerson entity:zzMafkPersonXyrList) {
				//外键设置
				entity.setZzMafkId(zzMafk.getId());
				// 嫌疑人
				entity.setType(MaPersonTypeEnum.XYR.getValue());
				zzMafkPersonMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		zzMafkPersonMapper.deleteByMainId(id);
		zzMafkMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			zzMafkPersonMapper.deleteByMainId(id.toString());
			zzMafkMapper.deleteById(id);
		}
	}
	
}
