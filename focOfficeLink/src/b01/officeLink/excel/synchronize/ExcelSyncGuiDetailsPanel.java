package b01.officeLink.excel.synchronize;

import java.awt.GridBagConstraints;
import java.io.File;

import javax.swing.SwingUtilities;

import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.gui.FGFileChooser;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.property.FString;

@SuppressWarnings("serial")
public class ExcelSyncGuiDetailsPanel extends FPanel {

	private ExcelSync configObject = null; 
	private FGFileChooser   fileChooser  = null;
		
	public ExcelSyncGuiDetailsPanel(FocObject obj, int viewID){
    super("Excel Synchronization Model", FPanel.FILL_BOTH);

    configObject = (ExcelSync) obj;
    
    if(configObject.isCreated()){
    	setFill(FPanel.FILL_HORIZONTAL);
    	FString       pathProperty = (FString)obj.getFocProperty(ExcelSyncDesc.FLD_FILE);
	    fileChooser  = new FGFileChooser(pathProperty, this, "Select the excel file to be binded to the project", FGFileChooser.MODE_FILES_ONLY, "xlsx", "Excel file with header line");
	    fileChooser.addExtensionFileFilter("xls", "Excel file with header line");

	    add(obj, FField.FLD_NAME, 0, 0);
	    add(fileChooser.getTextAndButtonPanel(), 0, 1, 3, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
	    
	    SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
			    fileChooser.popupFileChooser();
			    File file = new File(fileChooser.getPath());
			    if(configObject.getName().isEmpty() && file != null && !file.getName().isEmpty()){
			    	configObject.setName(file.getName());
			    }
				}
			});
    }else{
    	configObject.resetExcelFile();
	    ExcelColumnGuiBrowsePanel browsePanel = new ExcelColumnGuiBrowsePanel(configObject.getMapList(), FocObject.DEFAULT_VIEW_ID);
	    browsePanel.setFill(FPanel.FILL_BOTH);
	    
	    add(obj, FField.FLD_NAME, 0, 0);
	    //add(fileChooser.getTextAndButtonPanel(), 0, 1, 3, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
	    add(browsePanel, 0, 2, 3, 1);
    }
    
    FValidationPanel vPanel = showValidationPanel(true);
    vPanel.addSubject(configObject);
  }
	
	public void dispose(){
		super.dispose();
		configObject = null;
		fileChooser  = null;
	}
}
