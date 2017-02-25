package com.foc;

import java.lang.reflect.Method;

import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;

public class ClassFocDescDeclaration implements IFocDescDeclaration {
	
	private Class     cls            = null;
	private FocDesc   focDescription = null;
	private FocModule focModule      = null;
	
	public ClassFocDescDeclaration(FocModule focModule, Class cls){
		this.cls       = cls;
		this.focModule = focModule;
	}
	
	public FocDesc getFocDescription() {
		if(focDescription == null){
			focDescription = ClassFocDescDeclaration.getFocDescriptionForClass(cls);
			if(focDescription != null){
				String name = focDescription.getName();
				Globals.getApp().putIFocDescDeclaration(name, this);
				focDescription.setModule(focModule);
			}
			if(focDescription != null){
				focDescription.setModule(focModule);
			}
		}
		return focDescription;
		
    /*FocDesc focDesc = null;
    try {
      if (cls != null) {
        Class[] argsDeclare = null;
        Object[] args = null;
        Method methodGetFocDesc = null;
        try{
        	methodGetFocDesc = cls.getMethod("getInstance", argsDeclare);
        }catch(NoSuchMethodException e){
        	methodGetFocDesc = cls.getMethod("getFocDesc", argsDeclare);
        }
        if(methodGetFocDesc != null){
        	focDesc = (FocDesc) methodGetFocDesc.invoke(null, args);
        }
      }
    } catch (Exception e) {
    	Globals.logString("Exception while getting FocDesc for class : "+cls.getName());
      Globals.logException(e);
    }
    
    return focDesc;*/
	}
	
	@SuppressWarnings("unchecked")
	public static FocDesc getFocDescriptionForClass(Class cls){
    FocDesc focDesc = null;
    try {
      if (cls != null) {
        Class[] argsDeclare = null;
        Object[] args = null;
        Method methodGetFocDesc = null;
        try{
        	methodGetFocDesc = cls.getMethod("getInstance", argsDeclare);
        }catch(NoSuchMethodException e){
        	methodGetFocDesc = cls.getMethod("getFocDesc", argsDeclare);
        }
        if(methodGetFocDesc != null){
        	focDesc = (FocDesc) methodGetFocDesc.invoke(null, args);
        }
      }
    } catch (Exception e) {
      //Globals.getDisplayManager().popupMessage("getFocDescriptionForClass");
    	Globals.logString("Exception while getting FocDesc for class : "+cls.getName());
      Globals.logException(e);
    }
    return focDesc;
	}

	public int getPriority() {
		return IFocDescDeclaration.PRIORITY_FIRST;
	}

	@Override
	public FocModule getFocModule() {
		return focModule;
	}

	@Override
	public String getName() {
		return null;
	}
}
