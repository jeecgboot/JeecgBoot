package org.jeecg.common.constant;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.nonNull;

/**
 * @author Yan
 */
@Getter
@AllArgsConstructor
public enum GlobalVarResolver {

    SYS_ORG_CODE("sysOrgCode", (it) -> nonNull(it)
            ? it.getSysOrgCode()
            : ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getOrgCode()),

    SYS_MULTI_ORG_CODE("sysMultiOrgCode", (it) -> it.isOneDepart()
            ? it.getSysMultiOrgCode().get(0)
            : Joiner.on(",").join(it.getSysMultiOrgCode())),

    SYS_USER_CODE("sysUserCode", (it) -> nonNull(it)
            ? it.getSysUserCode()
            : ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername()),

    SYS_USER_ID("sysUserId", (it) -> ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getId()),

    SYS_USER_NAME("sysUserName", (it) -> nonNull(it)
            ? it.getSysUserName()
            : ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getRealname()),

    SYS_DATE("sysDate", (it) -> it.getSysDate()),

    SYS_TIME("sysTime", (it) -> it.getSysTime()),

    BPM_STATUS("bpmStatus", (it) -> "1"),
    ;

    private String key;

    private Function<SysUserCacheInfo, String> func;

    private static final Map<String, GlobalVarResolver> mappings;

    static {
        mappings = new HashMap<>();
        for (GlobalVarResolver value : GlobalVarResolver.values()) {
            mappings.put(value.getKey(), value);
        }
    }

    public static GlobalVarResolver resolve(String key) {
        return mappings.get(convert(key));
    }

    public boolean match(String key) {
        return this == resolve(key);
    }

    /**
     * 仅支持 sys_time 转 sysTime
     * @param key
     * @return
     */
    private static String convert(String key) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
    }
}
