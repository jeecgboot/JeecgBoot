package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.StickerSend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 贴纸项发送记录 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-12-13
 */
@Mapper
public interface StickerSendMapper extends BaseMapper<StickerSend> {

}
