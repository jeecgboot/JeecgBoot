package org.jeecg.modules.business.domain.api.shouman;

import java.util.Map;

public interface RequestBody {

    /**
     * API Path
     *
     * @return path
     */
    String path();

    Map<String, Object> parameters();

}
