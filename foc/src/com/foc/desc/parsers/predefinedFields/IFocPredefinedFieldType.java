package com.foc.desc.parsers.predefinedFields;

import java.lang.annotation.Annotation;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public interface IFocPredefinedFieldType<A extends Annotation> {
  public static final String TYPE_CODE           = "CODE";
  public static final String TYPE_COMPANY        = "COMPANY";
  public static final String TYPE_EXTERNAL_CODE  = "EXTERNAL_CODE";
  public static final String TYPE_DATE           = "DATE";
  public static final String TYPE_DESCRIPTION    = "DESCRIPTION";
  public static final String TYPE_NAME           = "NAME";
  public static final String TYPE_SITE           = "SITE";
  public static final String TYPE_ORDER          = "ORDER";
  public static final String TYPE_NOT_COMPLETED  = "NOT_COMPLETED";
  public static final String TYPE_IS_SYSTEM      = "IS_SYSTEM";
  public static final String TYPE_TREE           = "TREE";
  public static final String TYPE_REVIEW_STATUS  = "REVIEW_STATUS";
  public static final String TYPE_REVIEW_COMMENT = "REVIEW_COMMENT";
  
	public String getTypeName();
	public FField newFField(FocDesc focDesc, A a);
}
