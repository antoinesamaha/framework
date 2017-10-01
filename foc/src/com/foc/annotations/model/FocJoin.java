package com.foc.annotations.model;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface FocJoin {
	public String table() default "";
	public String alias() default "";
	public String on() default "";
	public String type() default "";
	public String where() default "";
	public boolean isPrimaryKey() default false;
	public FocJoinField[] fields() default {};
}
