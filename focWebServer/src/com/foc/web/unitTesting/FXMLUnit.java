package com.foc.web.unitTesting;

public interface FXMLUnit {
  
  public static final String TAG_TEST                       = "Test";
  public static final String TAG_TEST_SUITE                 = "TestSuite";
  public static final String TAG_CALL                       = "Call";
  public static final String TAG_CALL_FOR                   = "CallFor";
  public static final String TAG_LOGOUT                     = "Logout";
  public static final String TAG_LOGIN                      = "Login";
  public static final String TAG_NAVIGATE                   = "Navigate";
  public static final String TAG_HOME                       = "Home";// <Home />
  public static final String TAG_ADMIN                      = "Admin";
  
  public static final String TAG_SIGN                       = "Sign";
  public static final String TAG_APPLY                      = "Apply";
  public static final String TAG_SAVE                       = "Save";
  public static final String TAG_PAUSE                      = "Pause";
  public static final String TAG_BREAKPOINT                 = "Breakpoint";
  public static final String TAG_DEEP_APPLY                 = "DeepApply";
  public static final String TAG_DISCARD                    = "Discard";
  public static final String TAG_PRINT                      = "Print";
  public static final String TAG_FULL_SCREEN                = "FullSreen";
  public static final String TAG_ATTACH_IMAGE               = "AttachImage";
  public static final String TAG_GO_BACK                    = "GoBack";
  public static final String TAG_MORE                       = "More";
  public static final String TAG_APPROVE                    = "Approve";
  public static final String TAG_BUTTON_CLICK               = "ButtonClick";// <ButtonClick name="[Button Name]" />
  public static final String TAG_POPUP_BUTTON_CLICK         = "PopupButtonClick";
  public static final String TAG_NOTIFICATION_EXPECTED      = "NotificationExpected";
  public static final String TAG_VARIABLE                   = "Variable";
  public static final String TAG_UPLOAD                     = "Upload";
  public static final String ATT_FILE_NAME                  = "filename";
  
  public static final String TAG_SELECT_VIEW                = "SelectView";
  public static final String ATT_LAYOUT_NAME                = "layoutName";
  public static final String ATT_VIEW_NAME                  = "viewName";
  
  /**
   * {@literal <Navigation_MenuSelect codeName="code from menu tree">}  
   */
  public static final String TAG_NAVIGATE_TO                  = "Navigation_MenuSelect";//<Navigation_MenuClick menuCode="COST_BY_UNDERLYING" />
  public static final String TAG_NAVIGATE_TO_ADMIN_CLICK      = "Admin_MenuClick";
  public static final String TAG_NAVIGATE_TO_DOUBLE_CLICK     = "Navigation_MenuClick";
  public static final String TAG_SELECT_ITEM_IN_TABLE         = "Table_SelectItem";
  public static final String TAG_ADD_ITEM_IN_TABLE            = "Table_AddItem";// <Table_AddItem tableName="_INSPECTION_TABLE" variable="LAST_ITEM_IN_TABLE" />
  public static final String TAG_OPEN_ITEM_IN_TABLE           = "Table_OpenItem";
  public static final String TAG_DELETE_ITEM_IN_TABLE         = "Table_DeleteItem";
  public static final String TAG_TABLE_RIGHT_CLICK            = "Table_RightClick";
  public static final String TAG_SET_COMPONENT_VALUE          = "Component_SetValue";// <Component_SetValue componentName="DEADLINE_DAYS" componentValue="30" />
  public static final String TAG_ASSERT_COMPONENT_VALUE       = "Component_AssertValue";
  public static final String TAG_ASSERT_COMPONENT_EDITABLE    = "Component_AssertEditable";
  public static final String TAG_ASSERT_COMPONENT_ENABLED     = "Component_AssertEnabled";
  public static final String TAG_SET_COMPONENT_VALUE_TABLE    = "Table_SetCellValue";
  public static final String TAG_ASSERT_COMPONENT_VALUE_TABLE = "Table_AssertCellValue";
  public static final String TAG_ASSERT_COMPONENT_ENABLED_TABLE = "Table_AssertCellEnabled";
  public static final String TAG_SELECT_CELL_FOR_FORMULA      = "Table_SelectCellForFormula";
  public static final String TAG_APPLY_FORMULA                = "Table_ApplyFormula";
  public static final String TAG_REPLACE_ACTIVE               = "Table_Replace";
  public static final String TAG_GET_COMPONENT_VALUE          = "Component_GetValue";
  public static final String TAG_GEAR_COMPONENT_ADD           = "Component_Gear_Add";
  public static final String TAG_GEAR_COMPONENT_OPEN          = "Component_Gear_Open";
  public static final String TAG_BANNER_ADD_LINE              = "BannerList_AddLine";
  public static final String TAG_BANNER_DELETE_LINE_IDX       = "BannerList_DeleteLine_Index";
  public static final String TAG_BANNER_DELETE_LINE_REF       = "BannerList_DeleteLine_Reference";
  public static final String TAG_SET_VALUE_BANNER_IDX         = "BannerList_SetComponentValue_Index";
  public static final String TAG_SET_VALUE_BANNER_REF         = "BannerList_SetComponentValue_Reference";
  public static final String TAG_GET_VALUE_BANNER_REF         = "BannerList_GetComponentValue_Reference";
  public static final String TAG_DRAG_AND_DROP                = "DragAndDrop";
  public static final String TAG_COLLAPSE_ALL                 = "Tree_CollapseAll";
  public static final String TAG_ASSERT_COMPONENT_VALUE_FOR_EACH = "ForEach_AssertValue";
  public static final String TAG_INIT_SAAS_ACCOUNT            = "InitSaaSAccount";

  public static final String ATT_WITH_MEMORY_LOG              = "withMemoryLog";
  public static final String ATT_COLLAPSE                     = "collapse";
  public static final String ATT_BUTTON_NAME                  = "buttonName";
  public static final String ATT_NOTIFICATION_MESSAGE         = "notificationMessage";
  public static final String ATT_NOTIFICATION_TYPE            = "notificationType";
  public static final String ATT_NOTIFICATION_DESCRIPTION     = "notificationDescription";
  public static final String ATT_NAME                         = "name";
  public static final String ATT_MENU_CODE                    = "menuCode";
  public static final String ATT_MENU_CAPTION                 = "menuCaption";
  public static final String ATT_PROPERTY_NAME                = "propertyName";
  public static final String ATT_PROPERTY_VALUE               = "propertyValue";
  public static final String ATT_TABLE_NAME                   = "tableName";
  public static final String ATT_BANNER_LAYOUT_NAME           = "bannerName";
  public static final String ATT_TABLE_FIELD                  = "tableField";
  public static final String ATT_MORE_CAPTION                 = "moreCaption";
  public static final String ATT_COMPONENT_NAME               = "componentName";
  public static final String ATT_COMPONENT_VALUE              = "componentValue";
  public static final String ATT_STARTING_TEST                = "startingTest";
  public static final String ATT_CALL_SUIT                    = "suiteName";
  public static final String ATT_CALL_TEST                    = "testName";
  public static final String ATT_CALL_TEST_COMPOSED           = "test";
  public static final String ATT_VARIABLE                     = "variable";
  public static final String ATT_START_INDEX                  = "startIndex";
  public static final String ATT_END_INDEX                    = "endIndex";
  public static final String ATT_VALUE                        = "value";
  public static final String ATT_OBJECT_REFERENCE             = "objectReference";
  public static final String ATT_INDEX                        = "index";
  public static final String ATT_ANCESTOR                     = "ancestor";
  public static final String ATT_DRAG_AND_DROP_SOURCE         = "sourceComponent";
  public static final String ATT_DRAG_AND_DROP_TARGET         = "targetComponent";
  public static final String ATT_DND_SRC_OBJ_PROP_NAME        = "sourceObjectPropertyName";
  public static final String ATT_DND_SRC_OBJ_PROP_VALUE       = "sourceObjectPropertyValue";
  public static final String ATT_DND_TRG_OBJ_PROP_NAME        = "targetObjectPropertyName";
  public static final String ATT_DND_TRG_OBJ_PROP_VALUE       = "targetObjectPropertyValue";
  public static final String ATT_PRIORITY_TO_CAPTION_PROPERTY = "priorityToCaptionProperty";
}
