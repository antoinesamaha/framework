package com.foc.annotations.model;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FocField {
	public String type() default "";
	public String name() default "";
	public String dbName() default "";
	public int size() default 0;
	public int decimal() default 0;
	public boolean mandatory() default false;
	public FocChoice[] choices() default {}; 
}
