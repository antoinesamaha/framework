package com.foc.annotations.model;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface FocFilterCondition {
	public String fieldPath() default "";
	public String prefix() default "";
	public String caption() default "";
	public String captionProperty() default "";
}
