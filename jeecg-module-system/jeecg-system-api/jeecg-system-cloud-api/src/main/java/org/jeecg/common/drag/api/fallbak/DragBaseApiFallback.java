package org.jeecg.common.drag.api.fallbak;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.drag.api.IDragBaseApi;

/**
 * IDragBaseApi fallback
 * @author LSQ
 * @date 2023/01/09
 */
@Slf4j
public class DragBaseApiFallback implements IDragBaseApi {

    @Setter
    private Throwable cause;


    @Override
    public void deleteDragPage(String id) {}

    @Override
    public String copyDragPage(String id) {
        return null;
    }
}
