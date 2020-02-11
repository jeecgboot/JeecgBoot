package org.jeecg.modules.shza.mafk.service.impl;

import org.jeecg.modules.shza.mafk.entity.ZzMafkPerson;
import org.jeecg.modules.shza.mafk.mapper.ZzMafkPersonMapper;
import org.jeecg.modules.shza.mafk.service.IZzMafkPersonService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 受害人
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class ZzMafkPersonServiceImpl extends ServiceImpl<ZzMafkPersonMapper, ZzMafkPerson> implements IZzMafkPersonService {
	
	@Autowired
	private ZzMafkPersonMapper zzMafkPersonShrMapper;
	
	@Override
	public List<ZzMafkPerson> selectByMainId(String mainId, int type) {
		return zzMafkPersonShrMapper.selectByMainId(mainId, type);
	}
}
