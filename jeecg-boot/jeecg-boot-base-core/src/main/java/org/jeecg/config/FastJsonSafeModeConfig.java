package org.jeecg.config;

import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Fastjson 安全模式配置
 * <p>
 * 全局开启 SafeMode，禁用 autoType 功能，防止 Fastjson 反序列化漏洞（RCE）。
 * 开启后，所有通过 JSON.parseObject / JSON.parseArray 的调用将拒绝解析 @type 字段，
 * 从而阻止攻击者利用恶意类进行远程代码执行。
 * </p>
 *
 * @see <a href="https://github.com/jeecgboot/JeecgBoot/issues/9433">#9433</a>
 * @see <a href="https://github.com/jeecgboot/JeecgBoot/issues/9436">#9436</a>
 * @see <a href="https://github.com/jeecgboot/JeecgBoot/issues/9439">#9439</a>
 */
@Slf4j
@Configuration
public class FastJsonSafeModeConfig {

    @PostConstruct
    public void enableSafeMode() {
        ParserConfig.getGlobalInstance().setSafeMode(true);
        log.info("Fastjson SafeMode 已全局开启，autoType 已禁用");
    }
}
