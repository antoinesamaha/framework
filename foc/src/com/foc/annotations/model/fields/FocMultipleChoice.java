package com.foc.annotations.model.fields;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.foc.annotations.model.FocChoice;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FocMultipleChoice {
	public int size() default 10;
	public boolean mandatory() default false;
	public FocChoice[] choices() default {};
	public boolean dbResident() default true;
}
