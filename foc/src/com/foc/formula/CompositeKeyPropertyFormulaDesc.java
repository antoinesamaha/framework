package com.foc.formula;

import com.foc.db.DBIndex;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FField;

public class CompositeKeyPropertyFormulaDesc extends PropertyFormulaDesc {
  
  public static final int FLD_VIEW_NAME         = 200;
  public static final int FLD_FILTER_CRITERIA   = 201;
  public static final int FLD_OBJECT_REFS       = 202;
  
  public CompositeKeyPropertyFormulaDesc(){
    super();
    setFocObjectClass(CompositeKeyPropertyFormula.class);
    setStorageName("FORMULA_PROP_COMP_KEY");
    
    FField focField = new FStringField("VIEWNAME", "View Name", FLD_VIEW_NAME, false, 30);
    addField(focField);
    
    focField = new FStringField("FILTERCRITERIA", "FilterCriteria", FLD_FILTER_CRITERIA, false, 30);
    addField(focField);
    
    focField = new FStringField("OBJECTREFS", "Object Refs", FLD_OBJECT_REFS, false, 30);
    addField(focField);
    
    DBIndex index = new DBIndex("FILTER_CRITERIA", this, false);
    index.addField(FLD_FILTER_CRITERIA);
    indexAdd(index);
  }
  
  private static FocDesc focDesc = null;
  public static FocDesc getInstance() {
    if (focDesc==null){
      focDesc = new CompositeKeyPropertyFormulaDesc();
    }
    return focDesc;
  }
}
