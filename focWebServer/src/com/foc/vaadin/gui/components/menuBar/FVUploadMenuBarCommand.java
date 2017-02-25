package com.foc.vaadin.gui.components.menuBar;

import com.vaadin.ui.MenuBar.MenuItem;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.FVUploadLayout_Form;
import com.foc.web.modules.business.BusinessEssentialsWebModule;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FVUploadMenuBarCommand extends FVMenuBarCommand {

  private IUploadReader uploadReader = null;
  
  public FVUploadMenuBarCommand(IUploadReader uploadReader) {
  	this(uploadReader, null);
  }
  
  public FVUploadMenuBarCommand(IUploadReader uploadReader, String explanationText) {
    this.uploadReader = uploadReader;
  }
  
  @Override
  public void menuSelected(MenuItem selectedItem) {
    FocCentralPanel centralWindow = new FocCentralPanel();
    centralWindow.fill();
    
    XMLViewKey key = new XMLViewKey(BusinessEssentialsWebModule.STORAGE_UPLOAD_LAYOUT, XMLViewKey.TYPE_FORM);

    FVUploadLayout_Form uploadLayout = (FVUploadLayout_Form) XMLViewDictionary.getInstance().newCentralPanel(centralWindow, key, null);
    uploadLayout.setUploadReader(uploadReader);
    String text = uploadReader.getExplanation() != null ? uploadReader.getExplanation() : "";
    uploadLayout.setExplanationText(text);
    
    CSVFileFormat format = uploadReader.getFileFormat();
    
    if(format != null && format.getColumnCount() > 0){
    	uploadLayout.fillFormattingIfSupported(format);
    }
    
    uploadLayout.setExplanationText(text);
    centralWindow.changeCentralPanelContent(uploadLayout, true);
    
    Window window = centralWindow.newWrapperWindow();
    window.setCaption("Upload");
    FocWebApplication.getInstanceForThread().addWindow(window);
  }

}
