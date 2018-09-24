package com.foc.web.modules.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.foc.ConfigInfo;
import com.foc.business.printing.PrnLayout;
import com.foc.business.printing.gui.PrintingAction;
import com.foc.util.Utils;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ApplicationConstants;

public class PrnLayout_BrowserWindowOpenerStreamResource extends StreamResource {

	private PrintingAction printingAction = null;
	private PrnLayout      prnLayout      = null;
	private boolean        wordDoc        = false;
	private String         outputFileNameWithoutExtension = null;
	private String         errorMessage   = null;
	
	public PrnLayout_BrowserWindowOpenerStreamResource(PrnLayout prnLayout, PrintingAction printingaction) {
		this(prnLayout, printingaction, false);
	}
	
	public PrnLayout_BrowserWindowOpenerStreamResource(PrnLayout prnLayout, PrintingAction printingAction, boolean wordDoc) {
//		super(null, "printnig_"+System.currentTimeMillis()+ (wordDoc ? ".docx" : ".pdf"));
		super(null, "printnig_"+System.currentTimeMillis());
		outputFileNameWithoutExtension = "printnig_"+System.currentTimeMillis();
		this.prnLayout = prnLayout;
		this.printingAction = printingAction;
		this.wordDoc = wordDoc;
		setCacheTime(1);
	}
	
	public void dispose(){
		printingAction = null;
		prnLayout = null;
	}
	
	protected boolean isWithLogo() {
		return true;
	}
	
	public PrintingAction getPrintingAction() {
		return printingAction;
	}
	
	protected void beforeGetStream() {
	}
	
	@Override
	public DownloadStream getStream() {
		beforeGetStream();
		DownloadStream downloadStream = null;
		if(getPrintingAction() != null && getPrintingAction().getLauncher() != null){
			if(prnLayout != null){
				
//				if(this.prnLayout_Table.getPrintLogoCheckBox() != null){
//				  boolean withLogo = this.prnLayout_Table.getPrintLogoCheckBox().getValue();
//				  this.prnLayout_Table.getPrintingAction().getLauncher().setWithLogo(withLogo);
//				}
				
			  getPrintingAction().getLauncher().setWithLogo(isWithLogo());
				getPrintingAction().setLaunchedAutomatically(false);
				
				byte[] bytes = null;
				if(wordDoc){
					if(outputFileNameWithoutExtension != null) setFilename(outputFileNameWithoutExtension+".docx");
					bytes = getPrintingAction().getLauncher().printWordDocument(prnLayout);
				}else{
					if(outputFileNameWithoutExtension != null) setFilename(outputFileNameWithoutExtension+".pdf");
					bytes = getPrintingAction().getLauncher().web_FillReport(prnLayout, prnLayout.getFileName());
				}
				if(bytes != null){
					if(wordDoc){
						setMIMEType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
					}else{
						setMIMEType("application/pdf");
					}
		  		setStreamSource(new FStreamSource(bytes));
				}
				downloadStream = super.getStream();
			}
		} else {
			StreamSource source = new StreamSource(){
				@Override
				public InputStream getStream() {
					try{
						return new ByteArrayInputStream(getErrorMessageAsHTML().getBytes("UTF-8"));
					}catch (UnsupportedEncodingException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			};
			// second, create a StreamResource and pass the previous StreamResource:
			StreamResource resource = new StreamResource(source, "file.html");
			resource.setMIMEType("text/html; charset=utf-8");
			downloadStream = ((ConnectorResource) resource).getStream();
		}
		return downloadStream;
	}

	public PrnLayout getPrnLayout() {
		return prnLayout;
	}

	public void setPrnLayout(PrnLayout prnLayout) {
		this.prnLayout = prnLayout;
	}

	public boolean isWordDoc() {
		return wordDoc;
	}

	public void setWordDoc(boolean wordDoc) {
		this.wordDoc = wordDoc;
	}

	public String getErrorMessageAsHTML() {
		String message = getErrorMessage();
		if(Utils.isStringEmpty(message)) {
			if(ConfigInfo.isArabic()) {
				message = "الطباعة غير ممكنة";
			}	else {
				message = "Could not print report";
			}
		}
		String htmlPrefix = "<div style=\"color:blue;font-family:Arial;font-size:36;position:fixed;float:left;top:50%;left: 50%;transform: translate(-50%, -50%);\" >";
		String htmlSuffix = "</div>";
		
		return htmlPrefix + message + htmlSuffix;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}