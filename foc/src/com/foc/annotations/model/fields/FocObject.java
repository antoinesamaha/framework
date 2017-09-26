package com.foc.annotations.model.fields;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FocObject {
	public String dbName() default "";
	public boolean mandatory() default false;
	public boolean cascade() default false;
	public boolean detach() default false;
	public boolean saveOnebyOne() default false;
	public String table() default "";
	public boolean cachedList() default true;
	public boolean allowNull() default true;
	public String listFilterProperty() default "";
	public String listFilterValue() default "";
}
