package org.jeecg.modules.im.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.Collection;
import org.jeecg.modules.im.entity.query_helper.QCollection;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 收藏的消息 服务类
 * </p>
 *
 * @author junko
 * @since 2023-03-14
 */
@Service
public interface CollectionService extends IService<Collection> {
    IPage<Collection> pagination(MyPage<Collection> page, QCollection q);
    //分页查询，
    List<Collection> paginationApi(MyPage<Collection> page, QCollection q);
    Result<Object> del(String ids);

}
