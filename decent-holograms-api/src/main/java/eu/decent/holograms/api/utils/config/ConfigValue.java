package eu.decent.holograms.api.utils.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for config values.
 *
 * @author d0by
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigValue {

    /**
     * The path to the config value.
     *
     * @return the path
     */
    String value() default "";

    /**
     * The minimum value of a number config value.
     *
     * @return the minimum value
     */
    double min() default Double.MIN_VALUE;

    /**
     * The maximum value of a number config value.
     *
     * @return the maximum value
     */
    double max() default Double.MAX_VALUE;

}