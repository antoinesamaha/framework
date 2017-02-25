package com.foc.vaadin.gui.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.foc.FocThreadLocal;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.menuBar.CSVColumnFormat;
import com.foc.vaadin.gui.components.menuBar.CSVFileFormat;
import com.foc.vaadin.gui.components.menuBar.IUploadReader;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVStatusLayout_MenuBar;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;

@SuppressWarnings("serial")
public class FVUploadLayout_Form extends FocXMLLayout {

//  private PipedInputStream  inputStream  = null;
  private Upload            upload       = null;
  private IUploadReader     uploadReader = null;
  private FVButton          closeButton  = null;

  private ByteArrayOutputStream outputStream = null;//IKHTIRA3
  private ByteArrayInputStream  inputStream = null; 
  
  @Override
  public void dispose() {
  	super.dispose();
  	if(closeButton != null){
  		closeButton.dispose();
  		closeButton = null;
  	}
  	inputStream = null;
  	upload = null;
  	uploadReader = null;
  }
  
  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
    super.init(window, xmlView, focData);
  }
  
  @Override
  protected void afterLayoutConstruction() {
    super.afterLayoutConstruction();
    uploadButton();
    /*
    FVStatusLayout_MenuBar statusBar = getValidationLayout() != null ? getValidationLayout().getStatusLayout(false) : null;
    if(statusBar != null){
    	statusBar.setVisible(false);
    }
    */
    //addComponent(getCloseButton(true));
  }
  
  public void setExplanationText(String text){
  	FVLabel label = (FVLabel) getComponentByName("_LABEL_EXPLANATION");
  	if(label != null){
  		label.setValue(text);
  	}
  }

  private HorizontalLayout newHorizontalLayout(String title, String explanation, String sample, boolean isTitle){
		HorizontalLayout colLay = new HorizontalLayout();
		
		FVLabel titleLbl = new FVLabel(title);
		if(isTitle) titleLbl.addStyleName("f14"); else titleLbl.addStyleName("f12");
		titleLbl.addStyleName("b");
		titleLbl.setWidth("200px");
		colLay.addComponent(titleLbl);
		
		FVLabel explanationLbl = new FVLabel(explanation);
		if(isTitle){
			explanationLbl.addStyleName("f14"); 
			titleLbl.addStyleName("b");
		}else{
			explanationLbl.addStyleName("f12");
		}
		explanationLbl.setWidth("400px");
		colLay.addComponent(explanationLbl);

		FVLabel sampleLbl = new FVLabel(sample);
		if(isTitle){
			sampleLbl.addStyleName("f14"); 
			sampleLbl.addStyleName("b");
		}else{
			sampleLbl.addStyleName("f12");
		}
		sampleLbl.setWidth("200px");
		colLay.addComponent(sampleLbl);

		return colLay;
  }
  
  public void fillFormattingIfSupported(CSVFileFormat csvFileFormat){
  	FVVerticalLayout vLay = (FVVerticalLayout) getComponentByName("_FORMAT");
  	if(csvFileFormat != null && vLay != null){
  		FVLine lineUp = new FVLine();
  		vLay.addComponent(lineUp);
  		
			FVLabel label = new FVLabel("Format:");
			vLay.addComponent(label);
			
			HorizontalLayout titleLay = newHorizontalLayout("Column", "Description", "Sample", true);
			vLay.addComponent(titleLay);
			
			for(int index = 0; index<csvFileFormat.getColumnCount(); index++){
				CSVColumnFormat colFormat = csvFileFormat.getColumnAt(index);

				HorizontalLayout colLay = newHorizontalLayout(colFormat.getTitle(), colFormat.getExplanation(), colFormat.getSample(), false);
				vLay.addComponent(colLay);
			}
  	}
  }
  
  private void uploadButton(){
    setUpload(new Upload(null, new UploadReceiver(FocWebServer.getInstance())));
    
    getUpload().addFinishedListener(new FinishedListener() {
			
			@Override
			public void uploadFinished(FinishedEvent event) {
				try{
					FVUploadLayout_Form.this.uploadFinished(event.getFilename());
				}catch(Exception e){
					Globals.logException(e);
				}
			}
		});
    
    getUpload().addFailedListener(new FailedListener() {
			
			@Override
			public void uploadFailed(FailedEvent event) {
				Globals.showNotification("File " + event.getFilename() + " failed to upload!", "", IFocEnvironment.TYPE_ERROR_MESSAGE);
				goBack(null);
			}
		});
    addComponent(getUpload());
  }

  public void uploadFinished(String fileName){
    if(getUploadReader() != null && outputStream != null){
    	ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    	getUploadReader().handleUploadStream(inputStream, fileName);
    }
		
		if(inputStream != null && !Globals.getApp().isUnitTest()) {
			Globals.showNotification("File " + fileName + " uploaded!", "", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
		}
		goBack(null);
  }
  
  /*
  private FVButton getCloseButton(boolean createIfNeeded){
  	if(closeButton == null && createIfNeeded){
  		closeButton = new FVButton("Close");
  		closeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					goBack(null);
				}
			});
  	}
  	return closeButton;
  }
  */
  
  public void setUploadReader(IUploadReader uploadReader){
    this.uploadReader = uploadReader;
  }
  
  public IUploadReader getUploadReader(){
    return uploadReader;
  }
  
  public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	public class UploadReceiver implements Upload.Receiver {
    private FocWebServer      webServer      = null;

    public UploadReceiver(FocWebServer webServer){
      this.webServer =  webServer;
    }
    
    public OutputStream receiveUpload(String filename, String mimeType) {

      	outputStream = new ByteArrayOutputStream();

        /* Trying to short Cut the Thread */
//        try {      	
//        inputStream = new PipedInputStream();
//        PipedOutputStream out = new PipedOutputStream(inputStream);
//        FocWebApplication webApplication = FocWebApplication.getInstanceForThread();
//        UploadThread uploadThread = new UploadThread(webServer, webApplication, filename);
//        uploadThread.start();
//      	return out;
//      } catch (IOException e) {
//        e.printStackTrace();
//        return null;
//      }
      	
        /* -  Trying to short Cut the Thread   -*/
        
        return outputStream;
    }
  }
  
  public class UploadThread extends Thread {
    private FocWebServer      webServer      = null;
    private String            fileName       = null;
    private FocWebApplication webApplication = null;
    
    public UploadThread(FocWebServer webServer, FocWebApplication webApplication, String fileName){
      this.webServer = webServer;
      this.fileName  = fileName;
      this.webApplication = webApplication;
    }
    
    public void run(){
      FocThreadLocal.setWebServer(webServer);
      FocWebApplication.setInstanceForThread(webApplication);
      
      if(getUploadReader() != null){
        getUploadReader().handleUploadStream(inputStream, fileName);
      }

      FocWebApplication.setInstanceForThread(null);      
      FocThreadLocal.unsetThreadLocal();
    }
  }

	public void setOutputStream_ForUnitTesting(ByteArrayOutputStream outputStream) {
		this.outputStream = outputStream;
	}
}
