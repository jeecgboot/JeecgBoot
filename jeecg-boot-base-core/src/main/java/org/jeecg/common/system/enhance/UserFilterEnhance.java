package org.jeecg.common.system.enhance;

import java.util.List;

/**
 * 用户增强
 */
public interface UserFilterEnhance {
    
    /**
     * 获取用户id
     * @param loginUserId 当前登录的用户id
     * 
     * @return List<String> 返回多个用户id
     */
    default List<String> getUserIds(String loginUserId) {
        return null;
    }
}
