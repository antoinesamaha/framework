package com.foc.web.modules.business;

import com.foc.business.printing.PrnLayout;
import com.foc.business.printing.gui.PrintingAction;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.StreamResource;

public class PrnLayout_BrowserWindowOpenerStreamResource extends StreamResource {

	private PrintingAction printingAction = null;
	private PrnLayout      prnLayout      = null;
	private boolean        wordDoc        = false;
	private String         outputFileNameWithoutExtension = null; 
	
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
	
	@Override
	public DownloadStream getStream() {
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
	
}