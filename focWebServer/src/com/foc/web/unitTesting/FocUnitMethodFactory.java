package com.foc.web.unitTesting;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import com.foc.Globals;
import com.foc.web.unitTesting.methods.FUploadFile_UnitMethod;

public class FocUnitMethodFactory {

  private Map<String, IFUnitMethod> methodMap = null;
  private StringBuilder stringBuilder = null;
  
  private FocUnitMethodFactory() {

    methodMap = new HashMap<String, IFUnitMethod>();

    methodMap.put(FXMLUnit.TAG_UPLOAD, new FUploadFile_UnitMethod());
    
    methodMap.put(FXMLUnit.TAG_LOGOUT, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	String nextTest = attributes.getValue(FXMLUnit.ATT_CALL_TEST_COMPOSED);
      	nextTest = nextTest.replace(">", ".");
        command.logout(nextTest);
      }
    });

    methodMap.put(FXMLUnit.TAG_NAVIGATE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.navigate();
      }
    });

    methodMap.put(FXMLUnit.TAG_LOGIN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.login();
      }
    });

    methodMap.put(FXMLUnit.TAG_HOME, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.home();
      }
    });

    methodMap.put(FXMLUnit.TAG_ADMIN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.admin();
      }
    });

    methodMap.put(FXMLUnit.TAG_APPLY, new IFUnitMethod() {
      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
     		command.validationApply(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_SAVE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.validationSave(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_SIGN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.sign();
      }
    });
    
    methodMap.put(FXMLUnit.TAG_DEEP_APPLY, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.validationDeepApply();
      }
    });

    methodMap.put(FXMLUnit.TAG_DISCARD, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.validationDiscard(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_PRINT, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.validationPrint();
      }
    });

    methodMap.put(FXMLUnit.TAG_FULL_SCREEN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.validationFullScreen();
      }
    });

    methodMap.put(FXMLUnit.TAG_ATTACH_IMAGE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.validationAttachImage();
      }
    });

    methodMap.put(FXMLUnit.TAG_GO_BACK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.validationBack();
      }
    });

    methodMap.put(FXMLUnit.TAG_MORE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.validationMore(attributes.getValue(FXMLUnit.ATT_MORE_CAPTION));
      }
    });

    methodMap.put(FXMLUnit.TAG_APPROVE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.validationApprove();
      }
    });

    methodMap.put(FXMLUnit.TAG_SELECT_VIEW, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.view_Select(attributes.getValue(FXMLUnit.ATT_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_VIEW_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_GEAR_COMPONENT_ADD, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.gearComponentAdd(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_GEAR_COMPONENT_OPEN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.gearComponentOpen(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_NAVIGATE_TO, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.navigateTo(attributes.getValue(FXMLUnit.ATT_MENU_CODE));
      }
    });

    methodMap.put(FXMLUnit.TAG_NAVIGATE_TO_ADMIN_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.navigateToAdminAndDoubleClick(attributes.getValue(FXMLUnit.ATT_MENU_CODE));
      }
    });

    methodMap.put(FXMLUnit.TAG_NAVIGATE_TO_DOUBLE_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.navigateToAndDoubleClick(attributes.getValue(FXMLUnit.ATT_MENU_CODE));
      }
    });

    methodMap.put(FXMLUnit.TAG_SELECT_ITEM_IN_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	String ancestorRefString = attributes.getValue(FXMLUnit.ATT_ANCESTOR);
      	int ancestor = 0;
      	if(ancestorRefString != null){
      		ancestor = Integer.valueOf(ancestorRefString);
      	}
        command.selectItemInTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_PROPERTY_NAME), attributes.getValue(FXMLUnit.ATT_PROPERTY_VALUE), attributes.getValue(FXMLUnit.ATT_VARIABLE), ancestor);
      }
    });

    methodMap.put(FXMLUnit.TAG_ADD_ITEM_IN_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.addItemInTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_VARIABLE));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_OPEN_ITEM_IN_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.openItemInTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_DELETE_ITEM_IN_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.deleteItemInTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_TABLE_RIGHT_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.table_RightClick(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_MENU_CAPTION));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_ENABLED, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	if(value != null){// && !value.startsWith("$F{")
      		command.componentAssertEnabled(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), Boolean.valueOf(value));
      	}else{
      		command.getLogger().addFailure("Faild Assert value Null");
      	}
      }
    });

    methodMap.put(FXMLUnit.TAG_SET_COMPONENT_VALUE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	if(value != null && !value.startsWith("$F{")){
      		command.setComponentValue(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), value, false);
      	}
      }
    });
    
    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_VALUE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	String componentValue = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	
      	if(componentValue != null && !componentValue.startsWith("$F{")){
      		command.setComponentValue(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), componentValue, true);
      	}
      }
    });
    
    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_EDITABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	command.AssertComponentEditable(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_SET_COMPONENT_VALUE_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.setComponentValueInTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE), attributes.getValue(FXMLUnit.ATT_TABLE_FIELD), attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE), attributes.getValue(FXMLUnit.ATT_PRIORITY_TO_CAPTION_PROPERTY));
      }
    });

    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_ENABLED_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.assertComponentEnabledInTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE), attributes.getValue(FXMLUnit.ATT_TABLE_FIELD), Boolean.getBoolean(attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE)));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_VALUE_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.assertComponentValueInTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE), attributes.getValue(FXMLUnit.ATT_TABLE_FIELD), attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE));
      }
    });

    methodMap.put(FXMLUnit.TAG_SELECT_CELL_FOR_FORMULA, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.selectComponentInTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE), attributes.getValue(FXMLUnit.ATT_TABLE_FIELD));
      }
    });

    methodMap.put(FXMLUnit.TAG_APPLY_FORMULA, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.applyTheFormula(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_REPLACE_ACTIVE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.checkReplaceCheckBoxForTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_VALUE));
      }
    });
    

    methodMap.put(FXMLUnit.TAG_GET_COMPONENT_VALUE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.getComponentValue(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), attributes.getValue(FXMLUnit.ATT_VARIABLE));
      }
    });

    methodMap.put(FXMLUnit.TAG_BUTTON_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	command.buttonClick(attributes.getValue(FXMLUnit.ATT_NAME));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_POPUP_BUTTON_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	command.popupButtonClick(attributes.getValue(FXMLUnit.ATT_NAME), attributes.getValue(FXMLUnit.ATT_MENU_CODE));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_NOTIFICATION_EXPECTED, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	FocUnitDictionary.getInstance().expectedNotification_Set(attributes.getValue(FXMLUnit.ATT_NOTIFICATION_MESSAGE), attributes.getValue(FXMLUnit.ATT_NOTIFICATION_DESCRIPTION), attributes.getValue(FXMLUnit.ATT_NOTIFICATION_TYPE));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_BANNER_ADD_LINE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.bannerAddNewLine(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_BANNER_DELETE_LINE_IDX, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.bannerDeleteLineByIndex(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_INDEX));
      }
    });

    methodMap.put(FXMLUnit.TAG_BANNER_DELETE_LINE_REF, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.bannerDeleteLineByReference(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE));
      }
    });

    methodMap.put(FXMLUnit.TAG_SET_VALUE_BANNER_IDX, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	if(!value.startsWith("$F{")){
      		command.setComponentValueInBannerByIndex(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_INDEX), attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), value, false);
      	}
      }
    });

    methodMap.put(FXMLUnit.TAG_SET_VALUE_BANNER_REF, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	if(!value.startsWith("$F{")){      	
      		command.setComponentValueInBannerByReference(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE), attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), value);
      	}
      }
    });

    methodMap.put(FXMLUnit.TAG_GET_VALUE_BANNER_REF, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.getComponentValueInBanner(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE), attributes.getValue(FXMLUnit.ATT_VARIABLE));
      }
    });

    methodMap.put(FXMLUnit.TAG_CALL, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	command.callTest();      	
      }
    });

    methodMap.put(FXMLUnit.TAG_CALL_FOR, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
      	String variable   = attributes.getValue(FXMLUnit.ATT_VARIABLE);
        String startIndex = attributes.getValue(FXMLUnit.ATT_START_INDEX);
        String endIndex   = attributes.getValue(FXMLUnit.ATT_END_INDEX);
        String withMemoryLog_Strg = attributes.getValue(FXMLUnit.ATT_WITH_MEMORY_LOG);

        int startAt  = -1;
        int endAt    = -1; 
        
        if(startIndex != null && !startIndex.isEmpty()){
        	startAt  = Integer.valueOf(startIndex);
        }

        if(endIndex != null && !endIndex.isEmpty()){
        	endAt  = Integer.valueOf(endIndex);
        }
        
        boolean withMemoryLog = false;
        if(withMemoryLog_Strg != null && !withMemoryLog_Strg.isEmpty() && withMemoryLog_Strg.equalsIgnoreCase("true")){
        	withMemoryLog = true;
        }

        if(startAt < 0){
        	command.getLogger().addFailure("No start index < 0");
        }else if(endAt < 0){
        	command.getLogger().addFailure("No end index < 0");
        }else if(endAt < startAt){
        	command.getLogger().addFailure("end index < start index (+"+endAt+" < "+startAt+"+)");
        }else{
        	StringBuilder stringBuilder = null;
	        for(int i=startAt; i<=endAt; i++){
	        	if(withMemoryLog){
	        		stringBuilder = logMemory(i);
	        	}
	        	String currentVariableName  = variable+i;
	        	Object currentVariableValue = FocUnitDictionary.getInstance().getXMLVariables().get(currentVariableName);
	        	if(currentVariableValue == null){
	        		command.getLogger().addFailure("Index out of bounds : "+i+" "+currentVariableName+" for "+variable);
	        	}
	        	FocUnitDictionary.getInstance().getXMLVariables().put(variable, currentVariableValue);
	        	command.callTest();
	        }
	        if(stringBuilder != null){
	        	try{
		        	File logFile = new File("C://Users//user//Desktop//memory Log File.txt");
		        	FileWriter fileWriter = new FileWriter(logFile);
		        	fileWriter.write(stringBuilder.toString());
		        	fileWriter.flush();
		        	fileWriter.close();
	        	}catch(Exception ex){
	        		Globals.logException(ex);
	        	}
	        }
        }
      }
    });
    
    methodMap.put(FXMLUnit.TAG_VARIABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
        command.setVariable(attributes.getValue(FXMLUnit.ATT_VARIABLE), attributes.getValue(FXMLUnit.ATT_VALUE));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_DRAG_AND_DROP, new IFUnitMethod() {
			
			@Override
			public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
				command.dragAndDropHighLevel(attributes.getValue(FXMLUnit.ATT_DRAG_AND_DROP_SOURCE), attributes.getValue(FXMLUnit.ATT_DND_SRC_OBJ_PROP_NAME), attributes.getValue(FXMLUnit.ATT_DND_SRC_OBJ_PROP_VALUE), attributes.getValue(FXMLUnit.ATT_DRAG_AND_DROP_TARGET), attributes.getValue(FXMLUnit.ATT_DND_TRG_OBJ_PROP_NAME), attributes.getValue(FXMLUnit.ATT_DND_TRG_OBJ_PROP_VALUE));
			}
		});

    methodMap.put(FXMLUnit.TAG_COLLAPSE_ALL, new IFUnitMethod() {
			
			@Override
			public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
				String boolStr = attributes.getValue(FXMLUnit.ATT_COLLAPSE);
				boolean bool = boolStr != null ? Boolean.valueOf(boolStr) : true;
				command.collapseAll(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), bool);//hussein
			}
		});
		
		methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_VALUE_FOR_EACH, new IFUnitMethod() {
				
			@Override
			public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
				String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
	      if(!value.startsWith("$F{")){
	      	command.setComponentValueInBannerByIndex(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_INDEX), attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), value, true);//hussein
	      }
			}
		});
		
		methodMap.put(FXMLUnit.TAG_INIT_SAAS_ACCOUNT, new IFUnitMethod() {
			
			@Override
			public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) {
				command.initialiseSaaSAccount();
			}
		});
		
  }

  public void dispose() {
    instance = null;
    methodMap = null;
  }

  public void putMethod(String methodName, IFUnitMethod method) {
    methodMap.put(methodName, method);
  }

  public IFUnitMethod getMethodByName(String methodName) {
    return methodMap.get(methodName);
  }

  public int getMethodCount() {
    return methodMap.size();
  }

  // ----------------------------------------------------------
  // Instance
  // ----------------------------------------------------------
  private static FocUnitMethodFactory instance = null;

  public static FocUnitMethodFactory getInstance() {
    if (instance == null)
      instance = new FocUnitMethodFactory();
    return instance;
  }
  
  private StringBuilder logMemory(int index){
  	if(stringBuilder == null){
  		stringBuilder = new StringBuilder();
  	}
    System.gc();
    String memoryLog = Globals.logMemory("Memory Size: ");

    stringBuilder.append("Index: " + index);
    stringBuilder.append("\n");
    stringBuilder.append(memoryLog);
    stringBuilder.append("\n");
    stringBuilder.append("\n");
    
    return stringBuilder;
  }
}
