package com.foc.business.department;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FCompanyField;
import com.foc.desc.field.FField;
import com.foc.gui.FPanel;
import com.foc.list.FocList;
import com.foc.menu.FAbstractMenuAction;
import com.foc.menu.FMenuItem;
import com.foc.menu.FMenuList;

public class DepartmentDesc extends FocDesc{

  public static final int FLD_NAME           = FField.FLD_NAME;
  public static final int FLD_DESCRIPTION    = FField.FLD_DESCRIPTION;
  public static final int FLD_END_DEPARTMENT = 1;
  
  public static final String DB_NAME = "ACC_DEPARTMENT";
  
  public static final String FNAME_END_DEPARTMENT = "END_DEPARTMENT";
  
  public DepartmentDesc() {
    super(Department.class, FocDesc.DB_RESIDENT, DB_NAME, true);

    addReferenceField();
    setWithObjectTree();
    
    addNameField();
    addDescriptionField();
    
    FCompanyField compField = new FCompanyField(true, true);
    addField(compField);
    
    FBoolField bFld = new FBoolField(FNAME_END_DEPARTMENT, "End|Department", FLD_END_DEPARTMENT, false);
    addField(bFld);
  }
  
  // ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE LIST
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  
  public static FocList getList(int mode) {
  	return getInstance().getFocList(mode);
  }
  
  public FocList newFocList(){
  	return new DepartmentList(false);
  }

  public static Department getDepartmentByName(String name) {
  	FocList list = getList(FocList.LOAD_IF_NEEDED);
  	return (Department) list.searchByPropertyStringValue(DepartmentDesc.FLD_NAME, name);
  }

  @SuppressWarnings("serial")
	public static FMenuItem addMenuItem(FMenuList list){
		FMenuItem menuItem = new FMenuItem("Department", 'D', new FAbstractMenuAction(DepartmentDesc.getInstance(), true) {
			@Override
			public FPanel generatePanel() {
				DepartmentGuiTreePanel treePanel = new DepartmentGuiTreePanel(null, FocObject.DEFAULT_VIEW_ID);
				return treePanel;
			}
		});
		list.addMenu(menuItem);
		return menuItem;
  }
  
  // ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  
  public static DepartmentDesc getInstance() {
  	return (DepartmentDesc) getInstance(DB_NAME, DepartmentDesc.class);
  }
}
