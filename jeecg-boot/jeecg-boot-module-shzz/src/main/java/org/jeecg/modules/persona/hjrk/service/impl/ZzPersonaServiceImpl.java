package org.jeecg.modules.persona.hjrk.service.impl;

import org.jeecg.modules.persona.hjrk.entity.ZzPersona;
import org.jeecg.modules.persona.hjrk.mapper.ZzPersonaMapper;
import org.jeecg.modules.persona.hjrk.service.IZzPersonaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 综治人口
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@Service
public class ZzPersonaServiceImpl extends ServiceImpl<ZzPersonaMapper, ZzPersona> implements IZzPersonaService {
	
	@Autowired
	private ZzPersonaMapper zzPersonaMapper;
	
	@Override
	public List<ZzPersona> selectByMainId(String mainId) {
		return zzPersonaMapper.selectByMainId(mainId);
	}
}
