package org.jeecg.config.oss;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

/**
 * General OSS condition used with all OSS configuration classes.
 */
public class OSSCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String sourceClass = "";
		if (metadata instanceof ClassMetadata) {
			sourceClass = ((ClassMetadata) metadata).getClassName();
		}
		ConditionMessage.Builder message = ConditionMessage.forCondition("OSS", sourceClass);
		Environment environment = context.getEnvironment();
		try {
			BindResult<OSSType> specified = Binder.get(environment).bind("oss.type", OSSType.class);
			if (!specified.isBound()) {
				return ConditionOutcome.match(message.because("automatic OSS type"));
			}
			OSSType required = OSSConfigurations.getType(((AnnotationMetadata) metadata).getClassName());
			if (specified.get() == required) {
				return ConditionOutcome.match(message.because(specified.get() + " OSS type"));
			}
		}
		catch (BindException ex) {
		}
		return ConditionOutcome.noMatch(message.because("unknown OSS type"));
	}

}
