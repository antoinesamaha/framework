package com.foc.annotations.model;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface FocJoinField {
	public String sourceFieldName() default "";
	public String targetFieldName() default "";
	public String groupByFormula() default "";
	public String groupByFormulaAdditionalFields() default "";
}
