package org.jeecg.config.oss;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.jeecg.config.oss.aliyun.AliYunOSSAutoConfiguration;
import org.jeecg.config.oss.tencent.QcCOSAutoConfiguration;
import org.springframework.util.Assert;


/**
 * Mappings between {@link OSSType} and {@code @Configuration}.
 */
final class OSSConfigurations {

	private static final Map<OSSType, Class<?>> MAPPINGS;

	static {
		Map<OSSType, Class<?>> mappings = new EnumMap<>(OSSType.class);
		mappings.put(OSSType.ALIYUN, AliYunOSSAutoConfiguration.class);
		mappings.put(OSSType.QC, QcCOSAutoConfiguration.class);
		MAPPINGS = Collections.unmodifiableMap(mappings);
	}

	private OSSConfigurations() {
	}

	public static String getConfigurationClass(OSSType ossType) {
		Class<?> configurationClass = MAPPINGS.get(ossType);
		Assert.state(configurationClass != null, () -> "Unknown OSS type " + ossType);
		return configurationClass.getName();
	}

	public static OSSType getType(String configurationClassName) {
		for (Map.Entry<OSSType, Class<?>> entry : MAPPINGS.entrySet()) {
			if (entry.getValue().getName().equals(configurationClassName)) {
				return entry.getKey();
			}
		}
		throw new IllegalStateException("Unknown configuration class " + configurationClassName);
	}

}
