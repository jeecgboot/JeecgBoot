package org.jeecg.modules.persona.lsry.service;

import org.jeecg.modules.persona.lsry.entity.ZzPLsryPersona;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 留守人员附表
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
public interface IZzPLsryPersonaService extends IService<ZzPLsryPersona> {

	public List<ZzPLsryPersona> selectByMainId(String mainId);
}
