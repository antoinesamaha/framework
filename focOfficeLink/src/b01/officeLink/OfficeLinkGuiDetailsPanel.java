package b01.officeLink;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.gui.FGButton;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class OfficeLinkGuiDetailsPanel extends FPanel{
	
	protected OfficeLink officeLink = null; 
	
	public OfficeLinkGuiDetailsPanel(FocObject obj, int viewID){
		officeLink = (OfficeLink) obj;
		
		add(obj, OfficeLinkDesc.FLD_DESCRIPTION, 0, 0);
		
		FGTextField textFld = (FGTextField) add(obj, OfficeLinkDesc.FLD_FILE_INPUT_PATH, 0, 1);
		textFld.setEditable(false);
		FGButton selectFileButton = new FGButton("Browse...");
		add(selectFileButton, 2, 1);
		selectFileButton.addActionListener(new FileSelectionButtonListener(true));
		
		textFld = (FGTextField) add(obj, OfficeLinkDesc.FLD_FILE_OUTPUT_PATH, 0, 2);
		textFld.setEditable(false);
		selectFileButton = new FGButton("Browse...");
		add(selectFileButton, 2, 2);
		selectFileButton.addActionListener(new FileSelectionButtonListener(false));
		
		FValidationPanel vPanel = showValidationPanel(true);
		if(vPanel != null){
			vPanel.addSubject(obj);
		}
	}
	
	public class FileSelectionButtonListener implements ActionListener{
		boolean input = false;
		
		public FileSelectionButtonListener(boolean input){
			this.input = input;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
		  String filePath = officeLink.getSelectedPath(OfficeLink.defaultFilePath, "", OfficeLink.defaultFileType, JFileChooser.FILES_ONLY);
		  if (filePath != null){
		    if (filePath.endsWith(OfficeLink.PREFIX_EXCEL_07)|| filePath.endsWith(OfficeLink.PREFIX_EXCEL_XP)||filePath.endsWith(OfficeLink.PREFIX_WORD_07)||filePath.endsWith(OfficeLink.PREFIX_WORD_XP)){
		      //officeLink.setPropertyString(OfficeLinkDesc.FLD_DESC_STORAGE_NAME, storageName);
		    	if(input){
		    		officeLink.setPropertyString(OfficeLinkDesc.FLD_FILE_INPUT_PATH, filePath);
		    	}else{
		    		officeLink.setPropertyString(OfficeLinkDesc.FLD_FILE_OUTPUT_PATH, filePath);
		    	}
		    }else{
		      Globals.getDisplayManager().popupMessage("File Must be Word or Excel document");
		    }
		  }
		}
	}
}
