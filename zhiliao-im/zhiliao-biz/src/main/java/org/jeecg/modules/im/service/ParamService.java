package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.Param;
import org.jeecg.modules.im.entity.query_helper.QParam;

import java.util.List;

/**
 * <p>
 * 参数 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
public interface ParamService extends IService<Param> {
    IPage<Param> pagination(MyPage<Param> page, QParam q);

    List<Param> findAll();

    String getByName(Param.Name name);

    String getByName(Param.Name name,String defaultValue);

    Result<Object> del(String ids);

    Result<Object> createOrUpdate(Param param);

}
