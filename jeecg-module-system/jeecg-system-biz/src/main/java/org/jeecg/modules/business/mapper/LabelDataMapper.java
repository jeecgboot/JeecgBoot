package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.LabelData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 自定义分类
 * @Author: jeecg-boot
 * @Date:   2025-02-06
 * @Version: V1.0
 */
@Repository
public interface LabelDataMapper extends BaseMapper<LabelData> {

}
