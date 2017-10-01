package com.foc.desc.parsers.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.foc.desc.field.FField;

public interface IFocFieldType<A extends Annotation> {
  public static final String TYPE_REF           = "REF";
  public static final String TYPE_CODE          = "CODE";
  public static final String TYPE_COMPANY       = "COMPANY";
  public static final String TYPE_EXTERNAL_CODE = "EXTERNAL_CODE";
  public static final String TYPE_DATE          = "DATE";
  public static final String TYPE_DESCRIPTION   = "DESCRIPTION";
  public static final String TYPE_NAME          = "NAME";
  public static final String TYPE_SITE          = "SITE";
  public static final String TYPE_ORDER         = "ORDER";
  public static final String TYPE_NOT_COMPLETED = "NOT_COMPLETED";
  public static final String TYPE_IS_SYSTEM     = "IS_SYSTEM";
  public static final String TYPE_TREE          = "TREE";
  
  //Field Types
  public static final String TYPE_STRING          = "String";
  public static final String TYPE_INTEGER         = "Integer";
  public static final String TYPE_LONG            = "Long";
  public static final String TYPE_BOOLEAN         = "Boolean";
  public static final String TYPE_DOUBLE          = "Double";
  public static final String TYPE_DATE_FIELD      = "Date";
  public static final String TYPE_TIME_FIELD      = "Time";
  public static final String TYPE_MULTIPLE_CHOICE = "MultipleChoice";
  public static final String TYPE_MULTIPLE_CHOICE_STRING = "MultipleChoiceString";
  public static final String TYPE_BLOB            = "Blob";
  public static final String TYPE_BLOB_MEDIUM     = "BlobMedium";
  public static final String TYPE_IMAGE           = "Image";
  public static final String TYPE_FILE            = "File";
  public static final String TYPE_FOREIGN_ENTITY  = "ForeignEntity";
  
	public String getTypeName();
	public FField newFField(Class focObjClass, Field f, A a);
}
