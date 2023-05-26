package org.jeecg.modules.business.domain.codeGeneration;

/**
 * Code generation rule.
 *
 * @param <T> the type of code
 */
public interface CodeGenerationRule<T> {

    /**
     * Generate next code from previous code, the previous code is allowed to be null,
     * implementation should handle this case.
     *
     * @param previous the previous code
     * @return the next code
     */
    T next(T previous);

    /**
     * Generated next code without input.
     *
     * @return the new code
     */
    T next();
}
