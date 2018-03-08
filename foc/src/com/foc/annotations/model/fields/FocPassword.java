package com.foc.annotations.model.fields;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FocPassword {
	public int size() default 250;
	public boolean mandatory() default false;
	public boolean dbResident() default true;
}
