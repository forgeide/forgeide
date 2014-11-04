package org.forgeide.qualifiers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Qualifier for injecting configuration values
 *
 * @author Shane Bryzak
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration
{
   @Nonbinding String key() default "";

   @Nonbinding boolean required() default false;

   @Nonbinding String defaultValue() default "";
}
