package com.foc.desc.pojo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.FocField;
import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;
import com.foc.desc.field.FField;
import com.foc.desc.pojo.fields.FocFieldFactory;
import com.foc.desc.pojo.fields.IFocFieldType;
import com.foc.util.FocAnnotationUtil;

public class FocDescDeclaration_PojoBased implements IFocDescDeclaration {

	private FocModule            module      = null; 
	private Class<PojoFocDesc>   descClass   = null;
	private Class<PojoFocObject> objClass    = null;
	private String               name        = null;
	private String               storageName = null;
	private PojoFocDesc          focDesc     = null;
	
	public FocDescDeclaration_PojoBased(FocModule module, String name, String storageName, Class<PojoFocDesc> descClass, Class<PojoFocObject> objClass){
		this.name        = name;
		this.module      = module;
		this.descClass   = descClass;
		this.objClass    = objClass;
		this.storageName = storageName;
	}
	
	@Override
	public FocModule getFocModule() {
		return module;
	}

	@Override
	public int getPriority() {
		return IFocDescDeclaration.PRIORITY_FIRST;
	}

	public String getName() {
		return name;
	}

	@Override
	public FocDesc getFocDescription() {
		if(focDesc == null){
			focDesc = newFocDesc();
			PojoFocDesc pojoFocDesc = (PojoFocDesc) focDesc; 
			readAnnotations(pojoFocDesc);
			if(pojoFocDesc != null) pojoFocDesc.afterXMLParsing();
		}
    return focDesc;
	}
	
	private PojoFocDesc newFocDesc(){
	  try {
	    if (descClass != null) {
				FocEntity entity = (FocEntity) FocAnnotationUtil.findAnnotation(objClass, FocEntity.class);
				if(entity != null){			
		    	
		      Class[] clss = new Class[4];
		      Object[] args = new Object[4];
		      {
		      	clss[0] = Class.class;
		      	args[0] = objClass;
	
		      	clss[1] = boolean.class;
		      	args[1] = entity.dbResident();
		      	
		      	clss[2] = String.class;
		      	args[2] = storageName;
		      	
		      	clss[3] = boolean.class;
		      	args[3] = Boolean.FALSE;
		      }
		      Constructor<PojoFocDesc> methodGetFocDesc = null;
		      try{
		      	methodGetFocDesc = descClass.getConstructor(clss);
		      }catch(NoSuchMethodException e){
		      	Globals.logException(e);
		      }
		      if(methodGetFocDesc != null){
		      	focDesc = (PojoFocDesc) methodGetFocDesc.newInstance(args);
		      	focDesc.setName(name);
		      	
						Globals.getApp().putIFocDescDeclaration(name, this);
		      }
		    }
	    }
	  } catch (Exception e) {
	  	Globals.logString("Exception while getting FocDesc for class : "+descClass.getName());
	    Globals.logException(e);
	  }
	  return focDesc;
	}

	private void readAnnotations(PojoFocDesc focDesc){
		try{
      FocEntity entity = (FocEntity) FocAnnotationUtil.findAnnotation(objClass, FocEntity.class);
      if(entity.isTree()) focDesc.setWithObjectTree();
      
      Field[] fields = objClass.getFields();
      for(Field f : fields){
      	FocField fieldAnnotation = f.getAnnotation(FocField.class);
      	if(fieldAnnotation != null){
	      	String fieldTypeName = fieldAnnotation.type();
	      	IFocFieldType fieldType = FocFieldFactory.getInstance().get(fieldTypeName);
	      	FField focField = fieldType.newFField(null, f, fieldAnnotation);
	      	focField.setId(focDesc.nextFldID());
	      	focDesc.addField(focField);
//	      	Modifier.isStatic(f.getModifiers());
      	}
      }
    } catch (Exception e) {
      Globals.logString("Could not load file : " + focDesc.getClass().getName());
      Globals.logException(e);
    }
	}
}
