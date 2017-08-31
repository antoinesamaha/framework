package com.foc.annotations.model;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface FocEntity {
	public String name() default "";
	public String dbResident() default "true";
	public boolean tree() default false;
}
