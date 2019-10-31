package org.jeecg.config.oss;

import org.jeecg.config.oss.OSSAutoConfiguration.OSSConfigurationImportSelector;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Object Storage Service auto configuration.
 */
@Configuration
@EnableConfigurationProperties(OSSProperties.class)
@Import(OSSConfigurationImportSelector.class)
public class OSSAutoConfiguration {

	/**
	 * {@link ImportSelector} to add {@link OSSType} configuration classes.
	 */
	static class OSSConfigurationImportSelector implements ImportSelector {

		@Override
		public String[] selectImports(AnnotationMetadata importingClassMetadata) {
			OSSType[] types = OSSType.values();
			String[] imports = new String[types.length];
			for (int i = 0; i < types.length; i++) {
				imports[i] = OSSConfigurations.getConfigurationClass(types[i]);
			}
			return imports;
		}

	}

}
