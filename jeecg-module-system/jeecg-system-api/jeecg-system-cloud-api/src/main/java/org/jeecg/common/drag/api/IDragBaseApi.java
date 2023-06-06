package org.jeecg.common.drag.api;

import org.jeecg.common.constant.ServiceNameConstants;
import org.jeecg.common.drag.api.fallbak.DragBaseApiFallback;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 仪表盘API接口
 *
 * @author lsq
 * @date 2023/01/09
 */
@Component
@FeignClient(contextId = "dragBaseRemoteApi", value = ServiceNameConstants.SERVICE_SYSTEM, fallbackFactory = DragBaseApiFallback.class)
@ConditionalOnMissingClass("org.jeecg.modules.drag.service.impl.OnlDragBaseApiImpl")
public interface IDragBaseApi {

    /**
     * 通过id赋值仪表盘数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/drag/api/copyDragPage")
    String copyDragPage( @RequestParam("id") String id);

    /**
     * 删除表单
     * @param id
     */
    @DeleteMapping(value = "/drag/api/deleteDragPage")
    void deleteDragPage(@RequestParam("id") String id);

}
