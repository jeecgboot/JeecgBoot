package org.jeecg.modules.business.domain.api.mabang;

import java.util.Map;

/**
 * This interface represent the requirement that data should follow to be carried on to the Mabang API
 */
public interface RequestBody {

    /**
     * Action name, specified by API.
     *
     * @return action name
     */
    String api();


    /**
     * Parameters that required by different action
     *
     * @return map between parameter name and its value
     */
    Map<String, Object> parameters();

}
