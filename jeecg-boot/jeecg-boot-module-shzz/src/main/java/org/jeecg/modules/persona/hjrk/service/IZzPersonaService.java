package org.jeecg.modules.persona.hjrk.service;

import org.jeecg.modules.persona.hjrk.entity.ZzPersona;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 综治人口
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
public interface IZzPersonaService extends IService<ZzPersona> {

	public List<ZzPersona> selectByMainId(String mainId);
}
