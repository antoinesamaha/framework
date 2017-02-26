package b01.officeLink;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;
import com.foc.desc.FocObject;
import com.foc.gui.FListPanel;

import b01.officeLink.excel.ExcelRefillerInterface;
import b01.officeLink.excel.synchronize.ExcelColumnDesc;
import b01.officeLink.excel.synchronize.ExcelSyncDesc;

public class OfficeLinkModule extends FocModule {

  private static OfficeLinkModule module = null;  
  
	@Override
	public void declareFocObjectsOnce() {
    declareFocDescClass(OfficeLinkDesc.class);
    declareFocDescClass(ExcelSyncDesc.class);
    declareFocDescClass(ExcelColumnDesc.class);
  }

  public static OfficeLinkModule getInstance() {
    if(module == null) module = new OfficeLinkModule();
    return module;
  }

  public void export(FocObject obj, String docName, boolean allowModification){
  	export(obj, docName, allowModification, null);
  }
  
  public void export(FocObject obj, String docName, boolean allowModification, ExcelRefillerInterface excelRefiller){
    FocDesc desc            = obj.getThisFocDesc();
    String  descStorageName = desc.getStorageName();
    
    FListPanel fileTemplatesPanel = new OfficeLinkGuiBrowsePanel(OfficeLinkDesc.getList(/*FocList.FORCE_RELOAD,*/ descStorageName), 0, allowModification, descStorageName, obj, docName, excelRefiller);
    Globals.getDisplayManager().newInternalFrame(fileTemplatesPanel);
  }

  public void declare() {
    Globals.getApp().declareModule(this);
  }
}
