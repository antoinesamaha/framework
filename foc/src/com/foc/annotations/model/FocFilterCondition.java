package com.foc.annotations.model;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import com.foc.list.filter.FocListFilter;

@Retention(RUNTIME)
public @interface FocFilterCondition {
	public String fieldPath() default "";
	public String prefix() default "";
	public String caption() default "";
	public String captionProperty() default "";
	public int    level() default FocListFilter.LEVEL_DATABASE;
}
