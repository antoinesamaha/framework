package com.foc.property.validators;

import com.foc.desc.FocObject;
import com.foc.desc.field.FFieldPath;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.tree.FNode;

/**
 * @author 01Barmaja
 */
public class CodeNamePropertyValidator implements FPropertyValidator{
  
  //private int FLD_CODE = 0;
  //private int FLD_NAME = 0;
  
  private FFieldPath codePath = null;
  private FFieldPath namePath = null;
  
  public CodeNamePropertyValidator( FFieldPath codePath, FFieldPath namePath ){
    this.codePath = codePath;
    this.namePath = namePath;
  }
  
  public CodeNamePropertyValidator( int FLD_CODE, int FLD_NAME ){
    this(FFieldPath.newFieldPath(FLD_CODE), FFieldPath.newFieldPath(FLD_NAME));
  }
  
  public void dispose(){
    codePath = null;
    namePath = null;
  }
  
  public boolean validateProperty(FProperty property){
    
    FocObject focObj = (property != null ? property.getFocObject() : null);
    if(focObj != null ){
      
      FProperty codeProperty = codePath.getPropertyFromObject(focObj);
      FProperty nameProperty = namePath.getPropertyFromObject(focObj);
      
      if( codeProperty != null && nameProperty != null ){
        String codeString = codeProperty.getString();
        String nameString = nameProperty.getString();
        if( codeString != null && nameString != null ){
          
          String code = codeString;
          String nodeName = nameString;
          if( code != null && nodeName != null && !nodeName.equalsIgnoreCase("root") && (code.equals("") || code.startsWith(FNode.NEW_ITEM))){
            if( focObj.getFatherSubject() instanceof FocList ){
              FocList list = (FocList)focObj.getFatherSubject();
              int FLD_CODE = codeProperty.getFocField().getID();
              FocObject resultFocObj = list.searchByPropertyStringValue(FLD_CODE, nodeName);
              int key = 0;
              while( resultFocObj != null ){
                resultFocObj = list.searchByPropertyStringValue(FLD_CODE, nodeName+"("+(++key)+")");
              }
              String keyString = key == 0 ? "" : "("+key+")";
              focObj.setPropertyString(FLD_CODE, nodeName+keyString);
            }
          }
        }
      }
    }
    
    return true;
  }
  
  
}
