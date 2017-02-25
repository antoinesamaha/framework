package com.foc.property;

import java.sql.Blob;

import com.foc.desc.FocObject;

public class FBlobLongProperty extends FBlobProperty{

  public FBlobLongProperty(FocObject focObj, int fieldID, Blob defaultValue) {
    super(focObj, fieldID, defaultValue);
  }
}
