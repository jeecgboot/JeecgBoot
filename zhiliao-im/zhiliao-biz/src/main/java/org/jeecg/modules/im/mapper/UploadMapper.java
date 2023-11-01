package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Upload;
import org.jeecg.modules.im.entity.query_helper.QUpload;

/**
 * <p>
 * 上传记录 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Mapper
public interface UploadMapper extends BaseMapper<Upload> {
    MyPage<Upload> pagination(@Param("pg") MyPage<Upload> pg, @Param("q") QUpload q);

    Upload findByHref(String href);
}
