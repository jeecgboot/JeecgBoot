package org.jeecg.modules.shza.mafk.service;

import org.jeecg.modules.shza.mafk.entity.ZzMafkPerson;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 受害人
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IZzMafkPersonService extends IService<ZzMafkPerson> {

	public List<ZzMafkPerson> selectByMainId(String mainId, int type);
}
