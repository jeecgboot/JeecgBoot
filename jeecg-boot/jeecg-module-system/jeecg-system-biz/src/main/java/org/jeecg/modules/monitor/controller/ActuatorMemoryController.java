package org.jeecg.modules.monitor.controller;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 内存健康检查
 * @author: chenrui
 */
@Slf4j
@RestController
@RequestMapping("/sys/actuator/memory")
public class ActuatorMemoryController {


    /**
     * 内存详情
     * @return
     * @throws Exception
     */
    @GetMapping("/info")
    public Result<?> getRedisInfo() throws Exception {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		JSONObject operatingSystemJson = JSONObject.parseObject(JSONObject.toJSONString(operatingSystemMXBean));
		long totalPhysicalMemory = operatingSystemJson.getLongValue("totalPhysicalMemorySize");
		long freePhysicalMemory = operatingSystemJson.getLongValue("freePhysicalMemorySize");
		long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
		Runtime runtime = Runtime.getRuntime();
		Map<String,Number> result = new HashMap<>();
		result.put("memory.physical.total", totalPhysicalMemory);
		result.put("memory.physical.used", freePhysicalMemory);
		result.put("memory.physical.free", usedPhysicalMemory);
		result.put("memory.physical.usage", NumberUtil.div(usedPhysicalMemory, totalPhysicalMemory));
		result.put("memory.runtime.total", runtime.totalMemory());
		result.put("memory.runtime.used", runtime.freeMemory());
		result.put("memory.runtime.max", runtime.totalMemory() - runtime.freeMemory());
		result.put("memory.runtime.free", runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory());
		result.put("memory.runtime.usage", NumberUtil.div(runtime.totalMemory() - runtime.freeMemory(), runtime.totalMemory()));
        return Result.ok(result);
    }

}
