package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMsg;
import org.jeecg.modules.im.entity.query_helper.QMucMsg;

import java.util.List;

/**
 * <p>
 * 群聊消息 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
public interface MucMsgService extends IService<MucMsg> {
    IPage<MucMsg> pagination(MyPage<MucMsg> page, QMucMsg q);

    List<MucMsg> pageApi(QMucMsg q);
    //清空某个群的聊天记录
    Result<Object> clearAll(Integer mucId);
    //删除某人的群聊记录
    Result<Object> delByUserOfMuc(Integer userId,Integer mucId);
    //删除某人的所有群聊记录
    Result<Object> clearByUser(Integer userId);
    //删除某群指定的多条消息
    Result<Object> delByIds(Integer mucId,String msgIds);

}
