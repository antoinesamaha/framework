package com.foc.annotations.model;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import com.foc.desc.parsers.xml.FXMLDesc;
import com.foc.desc.parsers.xml.XMLFocDescParser;

@Retention(RUNTIME)
public @interface FocGroupByField {
	public String name() default "";
}
