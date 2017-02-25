package com.foc.web.unitTesting;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.foc.dataDictionary.FocDataDictionary;
import com.foc.util.expression.FocExpression;
import com.foc.util.expression.IExpressionHandler;
import com.foc.web.util.FXMLAbstractAttributes;

public class FocUnitXMLAttributes extends FXMLAbstractAttributes {

  private FocUnitTest test = null;
  
  public FocUnitXMLAttributes() {
    super();
  }
  
  public FocUnitXMLAttributes(FocUnitTest test, Attributes attributes) {
    super(attributes);
    setTest(test);
  }
  
  public FocUnitXMLAttributes(Attributes attributes) {
    super(attributes);
  }

  public String getString(){
  	return getString(null);
  }
  
  public String getString(String[] excludeAttributes){
  	StringBuffer buff = new StringBuffer();
  	
  	for(int i=0; i<getLength(); i++){
  		boolean include = true; 
  		if(excludeAttributes != null){
  			for(int j=0; j<excludeAttributes.length && include; j++){
  				include = excludeAttributes[j] == null || getQName(i) == null || !excludeAttributes[j].equals(getQName(i));
  			}
  		}
  		if(include){
	  		buff.append("(");
	  		buff.append(getQName(i));
	  		buff.append(":");
	  		buff.append(getValue(i));
	  		buff.append(") ");
  		}
  	}
  	return buff.toString();
  }
  
  public FocUnitTest getTest() {
    return test;
  }

  public void setTest(FocUnitTest test) {
    this.test = test;
  }
  
  @Override
  protected String resolveValue(String value) {
    value = FocExpression.parseExpression(value, new IExpressionHandler() {
      
      @Override
      public String handleFieldOrParameter(String expression, char type, int startIndex, int endIndex, String fieldOfParameter, ArrayList<String> arguments) {
        if(getTest() != null){
          String result = null;
          if(type == 'F'){
            if(getTest().getCallerArguments() != null && fieldOfParameter != null){
              result = getTest().getCallerArguments().getValue(fieldOfParameter);
            }
          }else if(type == 'V'){
            if(fieldOfParameter != null){
              result = (String) getTest().getSuite().getDictionary().getXMLVariables().get(fieldOfParameter);
            }
          }else if(type == 'P'){
            if(fieldOfParameter != null){
            	result = (String) FocDataDictionary.getInstance().getValue(null, fieldOfParameter, null);
//              result = (String) getTest().getSuite().getDictionary().getXMLVariables().get(fieldOfParameter);
            }
          }          
          
          if(result != null) expression = result;
          else{
            getTest().getSuite().getDictionary().getLogger().addWarning("Could not interpret expression: " + expression + ". Expression will be returned intact.");
          }
        }
        
        else{
          FocUnitDictionary.getInstance().getLogger().addError("Test in FocUnitXMLAttribute is null.");
        }

        return expression;
      }

    });
    
    return value;
  }
}
