package org.jeecg.modules.persona.lsry.service.impl;

import org.jeecg.modules.persona.lsry.entity.ZzPLsryPersona;
import org.jeecg.modules.persona.lsry.mapper.ZzPLsryPersonaMapper;
import org.jeecg.modules.persona.lsry.service.IZzPLsryPersonaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 留守人员附表
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Service
public class ZzPLsryPersonaServiceImpl extends ServiceImpl<ZzPLsryPersonaMapper, ZzPLsryPersona> implements IZzPLsryPersonaService {
	
	@Autowired
	private ZzPLsryPersonaMapper zzPLsryPersonaMapper;
	
	@Override
	public List<ZzPLsryPersona> selectByMainId(String mainId) {
		return zzPLsryPersonaMapper.selectByMainId(mainId);
	}
}
