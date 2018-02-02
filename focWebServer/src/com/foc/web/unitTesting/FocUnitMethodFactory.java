package com.foc.web.unitTesting;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import com.foc.Globals;
import com.foc.access.FocLogger;
import com.foc.web.unitTesting.methods.FUploadFile_UnitMethod;

public class FocUnitMethodFactory {

  private Map<String, IFUnitMethod> methodMap = null;
  
  private FocUnitMethodFactory() {

    methodMap = new HashMap<String, IFUnitMethod>();

    methodMap.put(FXMLUnit.TAG_UPLOAD, new FUploadFile_UnitMethod());
    
    methodMap.put(FXMLUnit.TAG_LOGOUT, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String nextTest = attributes.getValue(FXMLUnit.ATT_CALL_TEST_COMPOSED);
      	if(nextTest != null){
      		nextTest = nextTest.replace(">", ".");
      		FocUnitDictionary.getInstance().setNextTestExist(true);
      	}
        command.logout(nextTest);
      }
    });

    methodMap.put(FXMLUnit.TAG_NAVIGATE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickNavigate();
      }
    });

    methodMap.put(FXMLUnit.TAG_LOGIN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.login();
      }
    });

    methodMap.put(FXMLUnit.TAG_HOME, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickHome();
      }
    });

    methodMap.put(FXMLUnit.TAG_ADMIN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickAdmin();
      }
    });

    methodMap.put(FXMLUnit.TAG_APPLY, new IFUnitMethod() {
      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
     		command.button_ClickApply();
      }
    });
    
    methodMap.put(FXMLUnit.TAG_SAVE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickSave();
      }
    });
    
    methodMap.put(FXMLUnit.TAG_PAUSE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	FocUnitDictionary.getInstance().pause();
      }
    });

    methodMap.put(FXMLUnit.TAG_BREAKPOINT, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	int breakpoint = 3;
      	breakpoint++;
      }
    });
    
    methodMap.put(FXMLUnit.TAG_SIGN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.sign();
      }
    });
    
    methodMap.put(FXMLUnit.TAG_DEEP_APPLY, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickApplyRecursive();
      }
    });

    methodMap.put(FXMLUnit.TAG_DISCARD, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickDiscard(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_PRINT, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickPrint();
      }
    });

    methodMap.put(FXMLUnit.TAG_FULL_SCREEN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickFullScreen();
      }
    });

    methodMap.put(FXMLUnit.TAG_ATTACH_IMAGE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickAttachment();
      }
    });

    methodMap.put(FXMLUnit.TAG_GO_BACK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.button_ClickBack();
      }
    });

    methodMap.put(FXMLUnit.TAG_MORE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.moreMenu_Select(attributes.getValue(FXMLUnit.ATT_MORE_CAPTION));
      }
    });

    methodMap.put(FXMLUnit.TAG_APPROVE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.status_Approve();
      }
    });

    methodMap.put(FXMLUnit.TAG_SELECT_VIEW, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.view_Select(attributes.getValue(FXMLUnit.ATT_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_VIEW_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_GEAR_COMPONENT_ADD, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.gearComponentAdd(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_GEAR_COMPONENT_OPEN, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.gearComponentOpen(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_NAVIGATE_TO, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.menu_Highlight(attributes.getValue(FXMLUnit.ATT_MENU_CODE));
      }
    });

    methodMap.put(FXMLUnit.TAG_NAVIGATE_TO_ADMIN_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.menuAdmin_Click(attributes.getValue(FXMLUnit.ATT_MENU_CODE));
      }
    });

    methodMap.put(FXMLUnit.TAG_NAVIGATE_TO_DOUBLE_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.menu_Click(attributes.getValue(FXMLUnit.ATT_MENU_CODE));
      }
    });

    methodMap.put(FXMLUnit.TAG_SELECT_ITEM_IN_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String ancestorRefString = attributes.getValue(FXMLUnit.ATT_ANCESTOR);
      	int ancestor = 0;
      	if(ancestorRefString != null){
      		ancestor = Integer.valueOf(ancestorRefString);
      	}
        command.table_Select(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_PROPERTY_NAME), attributes.getValue(FXMLUnit.ATT_PROPERTY_VALUE), attributes.getValue(FXMLUnit.ATT_VARIABLE), ancestor);
      }
    });

    methodMap.put(FXMLUnit.TAG_ADD_ITEM_IN_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.table_Add(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_VARIABLE));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_OPEN_ITEM_IN_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.table_Open(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_DELETE_ITEM_IN_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.table_Delete(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_TABLE_RIGHT_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.table_RightClick(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_MENU_CAPTION));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_ENABLED, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	if(value != null){// && !value.startsWith("$F{")
      		command.component_AssertEnabled(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), Boolean.valueOf(value));
      	}else{
      		command.getLogger().addFailure("Faild Assert value Null");
      	}
      }
    });

    methodMap.put(FXMLUnit.TAG_SET_COMPONENT_VALUE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	if(value != null && !value.startsWith("$F{")){
      		command.component_SetValue(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), value, false);
      	}
      }
    });
    
    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_VALUE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String componentValue = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	
      	if(componentValue != null && !componentValue.startsWith("$F{")){
      		command.component_SetValue(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), componentValue, true);
      	}
      }
    });
    
    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_EDITABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	command.component_AssertEditable(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_SET_COMPONENT_VALUE_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String longRef = attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE);
      	long ref = Long.valueOf(longRef);
        command.componentInTable_SetValue(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), ref, attributes.getValue(FXMLUnit.ATT_TABLE_FIELD), attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE), attributes.getValue(FXMLUnit.ATT_PRIORITY_TO_CAPTION_PROPERTY));
      }
    });

    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_ENABLED_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String longRef = attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE);
      	long ref = Long.valueOf(longRef);
        command.componentInTable_AssertEnabled(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), ref, attributes.getValue(FXMLUnit.ATT_TABLE_FIELD), Boolean.getBoolean(attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE)));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_VALUE_TABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String longRef = attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE);
      	long ref = Long.valueOf(longRef);
        command.componentInTable_AssertValue(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), ref, attributes.getValue(FXMLUnit.ATT_TABLE_FIELD), attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE));
      }
    });

    methodMap.put(FXMLUnit.TAG_SELECT_CELL_FOR_FORMULA, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String longRef = attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE);
      	long ref = Long.valueOf(longRef);
        command.componentInTable_Select(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), ref, attributes.getValue(FXMLUnit.ATT_TABLE_FIELD));
      }
    });

    methodMap.put(FXMLUnit.TAG_APPLY_FORMULA, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.applyTheFormula(attributes.getValue(FXMLUnit.ATT_TABLE_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_REPLACE_ACTIVE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.checkReplaceCheckBoxForTable(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), attributes.getValue(FXMLUnit.ATT_VALUE));
      }
    });
    

    methodMap.put(FXMLUnit.TAG_GET_COMPONENT_VALUE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.component_GetValue(attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), attributes.getValue(FXMLUnit.ATT_VARIABLE));
      }
    });

    methodMap.put(FXMLUnit.TAG_BUTTON_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	command.button_Click(attributes.getValue(FXMLUnit.ATT_NAME));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_POPUP_BUTTON_CLICK, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	command.popupButtonClick(attributes.getValue(FXMLUnit.ATT_NAME), attributes.getValue(FXMLUnit.ATT_MENU_CODE));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_NOTIFICATION_EXPECTED, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	FocUnitDictionary.getInstance().expectedNotification_Set(attributes.getValue(FXMLUnit.ATT_NOTIFICATION_MESSAGE), attributes.getValue(FXMLUnit.ATT_NOTIFICATION_DESCRIPTION), attributes.getValue(FXMLUnit.ATT_NOTIFICATION_TYPE));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_BANNER_ADD_LINE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.banner_Add(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME));
      }
    });

    methodMap.put(FXMLUnit.TAG_BANNER_DELETE_LINE_IDX, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String index = attributes.getValue(FXMLUnit.ATT_INDEX);
      	int idx = index != null ? Integer.parseInt(index) : -1 ;
        command.banner_DeleteLineByIndex(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), idx);
      }
    });

    methodMap.put(FXMLUnit.TAG_BANNER_DELETE_LINE_REF, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String refString = attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE);
      	long ref = Long.valueOf(refString);
      	
        command.banner_DeleteLineByReference(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), ref);
      }
    });

    methodMap.put(FXMLUnit.TAG_SET_VALUE_BANNER_IDX, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	if(!value.startsWith("$F{")){
      		command.componentInBanner_SetValueByIndex(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_INDEX), attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), value, false);
      	}
      }
    });

    methodMap.put(FXMLUnit.TAG_SET_VALUE_BANNER_REF, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
      	if(!value.startsWith("$F{")){      	
      		command.componentInBanner_SetValueByReference(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_OBJECT_REFERENCE), attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), value);
      	}
      }
    });

    methodMap.put(FXMLUnit.TAG_GET_VALUE_BANNER_REF, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.componentInBanner_GetValue(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE), attributes.getValue(FXMLUnit.ATT_VARIABLE));
      }
    });

    methodMap.put(FXMLUnit.TAG_CALL, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
      	command.callTest();      	
      }
    });

    methodMap.put(FXMLUnit.TAG_CALL_FOR, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
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

      	PrintStream perfLogFile = null;
        if(withMemoryLog_Strg != null && !withMemoryLog_Strg.isEmpty() && withMemoryLog_Strg.equalsIgnoreCase("true")){
      		try{
      			perfLogFile = !Globals.logFile_CheckLogDir() ? new PrintStream(Globals.logFile_GetFileName("PERF_", "log")) : null;
					}catch (FileNotFoundException e){
						Globals.logException(e);
					}        	
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
	        	if(perfLogFile != null){
	        		stringBuilder = logMemory(i);
	        	}
	        	String currentVariableName  = variable+i;
	        	Object currentVariableValue = FocUnitDictionary.getInstance().getXMLVariables().get(currentVariableName);
	        	if(currentVariableValue == null){
//	        		command.getLogger().addFailure("Index out of bounds : "+i+" "+currentVariableName+" for "+variable);
	        	}
	        	FocUnitDictionary.getInstance().getXMLVariables().put(variable, currentVariableValue);
	        	command.callTest();
	        	if(stringBuilder != null && perfLogFile != null){
	        		perfLogFile.println(stringBuilder.toString());
	        		perfLogFile.flush();
	        	}
	        }
        }
        
      	if(perfLogFile != null){
      		if(!FocLogger.getInstance().isHasFailure()){
	      		FocLogger.getInstance().dispose();
	      		perfLogFile.println("Log Cleared because no failure");
	      		StringBuilder stringBuilder = logMemory(999);
	      		perfLogFile.println(stringBuilder.toString());
	      		perfLogFile.flush();
      		}else{
	      		perfLogFile.println("Could not clear Log because of failure");
      		}
      		
      		perfLogFile.close();
      		perfLogFile = null;
      	}
      }
    });
    
    methodMap.put(FXMLUnit.TAG_VARIABLE, new IFUnitMethod() {

      @Override
      public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
        command.variable_Set(attributes.getValue(FXMLUnit.ATT_VARIABLE), attributes.getValue(FXMLUnit.ATT_VALUE));
      }
    });
    
    methodMap.put(FXMLUnit.TAG_DRAG_AND_DROP, new IFUnitMethod() {
			
			@Override
			public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
				command.dragAndDropHighLevel(attributes.getValue(FXMLUnit.ATT_DRAG_AND_DROP_SOURCE), attributes.getValue(FXMLUnit.ATT_DND_SRC_OBJ_PROP_NAME), attributes.getValue(FXMLUnit.ATT_DND_SRC_OBJ_PROP_VALUE), attributes.getValue(FXMLUnit.ATT_DRAG_AND_DROP_TARGET), attributes.getValue(FXMLUnit.ATT_DND_TRG_OBJ_PROP_NAME), attributes.getValue(FXMLUnit.ATT_DND_TRG_OBJ_PROP_VALUE));
			}
		});

    methodMap.put(FXMLUnit.TAG_COLLAPSE_ALL, new IFUnitMethod() {
			
			@Override
			public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
				String boolStr = attributes.getValue(FXMLUnit.ATT_COLLAPSE);
				boolean bool = boolStr != null ? Boolean.valueOf(boolStr) : true;
				command.tree_CollapseAll(attributes.getValue(FXMLUnit.ATT_TABLE_NAME), bool);//hussein
			}
		});
		
		methodMap.put(FXMLUnit.TAG_ASSERT_COMPONENT_VALUE_FOR_EACH, new IFUnitMethod() {
				
			@Override
			public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
				String value = attributes.getValue(FXMLUnit.ATT_COMPONENT_VALUE);
	      if(!value.startsWith("$F{")){
	      	command.componentInBanner_SetValueByIndex(attributes.getValue(FXMLUnit.ATT_BANNER_LAYOUT_NAME), attributes.getValue(FXMLUnit.ATT_INDEX), attributes.getValue(FXMLUnit.ATT_COMPONENT_NAME), value, true);//hussein
	      }
			}
		});
		
		methodMap.put(FXMLUnit.TAG_INIT_SAAS_ACCOUNT, new IFUnitMethod() {
			
			@Override
			public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
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
  	StringBuilder stringBuilder = new StringBuilder();
  	
    System.gc();
    Globals.logMemory("");
    
    System.gc();
    Globals.logMemory("");

    System.gc();
    String memoryLog = Globals.logMemory("Memory Size: ");
  	
    stringBuilder.append("Index: " + index+" -> ");
    stringBuilder.append(memoryLog);
    
    return stringBuilder;
  }
}
