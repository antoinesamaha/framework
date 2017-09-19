package com.foc.util;

import java.lang.annotation.Annotation;

public class FocAnnotationUtil {
	
	public static Annotation findAnnotation(Class cls, Class annotationClass){
		if(cls != null && annotationClass != null){
			Annotation[] annArray = cls.getAnnotationsByType(annotationClass);
			if(annArray.length > 0){
				return annArray[0];
			}
		}
		return null;
	}
}
