package com.foc.web.modules.business;


import java.io.InputStream;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.business.config.BusinessConfig;
import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailDesc;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.business.printing.PrnContext;
import com.foc.business.printing.PrnLayout;
import com.foc.business.printing.PrnLayoutDefinition;
import com.foc.business.printing.PrnLayoutDefinitionDesc;
import com.foc.business.printing.gui.PrintingAction;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVUploadLayout_Form;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.components.menuBar.CSVFileFormat;
import com.foc.vaadin.gui.components.menuBar.IUploadReader;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class PrnLayout_Table extends FocXMLLayout{

	private PrintingAction printingAction = null;
	
	@Override
	public void dispose() {
		super.dispose();
		if(printingAction != null){
//			printingAction.disposeLauncherContent();
			printingAction.dispose();
			printingAction = null;
		}
	}
	
	private FVCheckBox getPrintLogoCheckBox(){
		return (FVCheckBox) getComponentByName("PRINT_LOGO");
	}
	
	private FocList getPrnLayoutList(){
		return getFocList();
	}
	
	private FVTableWrapperLayout getTableWrapperLayout(){
		return (FVTableWrapperLayout) getComponentByName("PRN_LAYOUT_TABLE");
	}
	
	private TableTreeDelegate getTableTreeDelegate(){
		return getTableWrapperLayout() != null ? getTableWrapperLayout().getTableTreeDelegate() : null; 
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
		if(getPrintLogoCheckBox() != null){
			getPrintLogoCheckBox().setValue(true);
		}
		
		adjustAddButtonVisibilityDependingOnGroupRights();
		TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
		if(tableTreeDelegate != null){

			tableTreeDelegate.getTable().addGeneratedColumn("PRINT_WORD_BUTTON", new Table.ColumnGenerator() {
				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					Button button = new Button("MS Word");
					if(getFocList() != null && itemId != null){
						BrowserWindowOpener browserWindowOpener = new BrowserWindowOpener(new BrowserWindowOpenerStreamResource(itemId, true));
						browserWindowOpener.extend(button);
					}
					return button;
				}
			});
			tableTreeDelegate.getTable().setColumnHeader("PRINT_WORD_BUTTON", "MS Word");
			
			tableTreeDelegate.getTable().addGeneratedColumn("PRINT_BUTTON", new Table.ColumnGenerator() {
				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					Button button = new Button("PDF");
					if(getFocList() != null && itemId != null){
						BrowserWindowOpener browserWindowOpener = new BrowserWindowOpener(new BrowserWindowOpenerStreamResource(itemId));
						browserWindowOpener.extend(button);
					}
					return button;
				}
			});
			tableTreeDelegate.getTable().setColumnHeader("PRINT_BUTTON", "PDF");
			
			tableTreeDelegate.getTable().addGeneratedColumn("SEND_EMAIL_BUTTON", new Table.ColumnGenerator() {
				
				@Override
				public Object generateCell(Table source, final Object itemId, Object columnId) {
					FVButton button = new FVButton("Send Email");
					button.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							
							PrintingAction printingAction = getPrintingAction();
							if(getFocList() != null && itemId != null && printingAction != null){
								PrnLayout prnLayout = (PrnLayout) getFocList().searchByReference((Long) itemId);
								if(prnLayout != null){
									boolean withLogo = false; 
											
									if(getPrintLogoCheckBox() != null){
									  withLogo = getPrintLogoCheckBox().getValue();
									}
									
									FocNotificationEmailTemplate template = (FocNotificationEmailTemplate) BusinessConfig.getInstance().getGeneralEmailTemplate();
									FocNotificationEmail email = new FocNotificationEmail(template, printingAction.getObjectToPrint());
									
									prnLayout.attachToEmail(email, printingAction, withLogo);
									
									Globals.popup(email, false, FocNotificationEmailDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT);
								}
							}
						}
					});
					return button;
				}
			});
			tableTreeDelegate.getTable().setColumnHeader("SEND_EMAIL_BUTTON", "Send Email");
		}
		
	}
	
	private void adjustAddButtonVisibilityDependingOnGroupRights() {
		if(getTableWrapperLayout() != null
			&& Globals.getApp() != null 
			&& Globals.getApp().getUser_ForThisSession() != null
			&& Globals.getApp().getUser_ForThisSession().getGroup() != null){
			boolean addEnabled = Globals.getApp().getUser_ForThisSession().getGroup().allowReportCreation();
			getTableTreeDelegate().setAddEnabled(addEnabled);
			getTableTreeDelegate().refreshEditable();
			if(!addEnabled){
				getTableTreeDelegate().removePopupMenu(TableTreeDelegate.ACTION_ADD);
			}
		}
	}
	
  public PrintingAction getPrintingAction() {
		return printingAction;
	}

	public void setPrintingAction_AndBecomeOwner(PrintingAction printingAction) {
		this.printingAction = printingAction;
	}
	
	public PrnContext getContext(){
		PrnContext context = null;
		if(getFocList() != null && getFocList().getAnyItem() != null){
			PrnLayout lay = (PrnLayout)getFocList().getAnyItem();
			if(lay != null){
				context = lay.getContext();
			}
		}
		return context;
	}
	
	public String getContextName(){
		return getContext() != null ? getContext().getDBName() : null;
	}
	
  @Override
  public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
    XMLViewKey key = new XMLViewKey(BusinessEssentialsWebModule.STORAGE_UPLOAD_LAYOUT, XMLViewKey.TYPE_FORM);

    FVUploadLayout_Form uploadLayout = (FVUploadLayout_Form) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, null);
    uploadLayout.setUploadReader(new FocExcelUploadThenEdit_UploadReader(table));
    StringBuffer explanation = new StringBuffer();
    explanation.append("Click the 'Choose File' button bellow to choose the Excel file that contains your original BOQ.<br>");
    explanation.append("Please follow these constraints for the import to go smooth:<br>");
    explanation.append("<ol>");
    explanation.append("<li>The First Sheet only will be imported. Other sheets are simply ignored and kept unchanged.</li>");
    explanation.append("<li>On the First line indicate the column names. To allow an easy and automatic corresondance with EVERPRO fields, use these column names:");
    {
	    explanation.append("<ul>");
	    explanation.append("<li><b>ID (or Code): </b>BOQ ITem ID or Code. This is best imported in the EXTERNAL_CODE in EVERPRO</li>");
	    explanation.append("<li><b>Desc (or Description): </b>Item description</li>");
	    explanation.append("<li><b>Quantity (or Qty): </b>Quantity</li>");
	    explanation.append("<li><b>UP (or Unit Price or U.P): </b>Unit rate to be filled by EVERPRO</li>");
	    explanation.append("</ul>");
    }
    explanation.append("<li>Choose any 2 unused empty excel columns and name them in the first line:");
    {
	    explanation.append("<ul>");
	    explanation.append("<li><b>Ref: </b>This is filled automatically by EVERPRO and makes a maping to the EVERPRO node.</li>");
	    explanation.append("<li><b>Level: </b>Fill this column yourself. It should contain the level of the node in the WBS 1,2,3... Bare in mind that it is not the serial number of the node it is only the level. This column should be filled only on the lines that are to be imported. Decorative useless excel lines that do not explicitly belong to the WBS (BOQ) are not to be imported.</li>");
      explanation.append("</ul>");
	  }
    explanation.append("<li>If the file is very big in memory try to lighten it by deleting unecessary sheets and images.</li>");
    explanation.append("</ol>");

    uploadLayout.setExplanationText(explanation.toString());
				
    uploadLayout.popupInDialog("Upload BOQ Excel file", "700px", "550px");
    return null;
  }

  @SuppressWarnings("unused")
	private class FocExcelUploadThenEdit_UploadReader implements IUploadReader {
  	
  	private ITableTree table = null;
  	
  	public FocExcelUploadThenEdit_UploadReader(ITableTree table){
  		this.table = table;
  	}
  	
  	private void dispose(){
  		table = null;
  	}
  	
  	public void handleUploadStream(InputStream inputStream, String fileName) {
  		boolean isXlsxFile = fileName != null && fileName.toLowerCase().endsWith(".jasper");
  		
  		String context = getContextName();
  		
  		FocList focListLayoutDefinition = PrnLayoutDefinitionDesc.getInstance().getFocList();
      if(getFocList() != null && isXlsxFile && context != null) {
      	focListLayoutDefinition.loadIfNotLoadedFromDB();
      	PrnLayoutDefinition prnLayDef = (PrnLayoutDefinition) focListLayoutDefinition.newEmptyItem();
      	prnLayDef.setName(fileName);
      	prnLayDef.setFileName(fileName);
      	prnLayDef.setContextDBName(context);
        prnLayDef.validate(true);
        focListLayoutDefinition.validate(true);
        
        prnLayDef.setJasperReport(inputStream);

        XMLViewKey key = new XMLViewKey(PrnLayoutDefinitionDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM);
        ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, prnLayDef);
        getMainWindow().changeCentralPanelContent(centralPanel, true);
      }else{
      	Globals.showNotification("Uploaded Excel file format not supported please save as '.xlsx' and try again.", "File Format Error", IFocEnvironment.TYPE_WARNING_MESSAGE);
      }
  	}

		public String getExplanation() {
			return null;
		}

		public CSVFileFormat getFileFormat() {
			return null;
		}
  };
  
	private class BrowserWindowOpenerStreamResource extends StreamResource{

		private Object selectedItemId = null;
		private boolean wordDoc = false;
		
		public BrowserWindowOpenerStreamResource(Object selectedItemId) {
			this(selectedItemId, false);
		}
		
		public BrowserWindowOpenerStreamResource(Object selectedItemId, boolean wordDoc) {
			super(null, "printnig_"+System.currentTimeMillis()+ (wordDoc ? ".docx" : ".pdf"));
			this.selectedItemId = selectedItemId;
			this.wordDoc = wordDoc;
			setCacheTime(1);
		}
		
		public void dispose(){
			selectedItemId = null;
		}
		
		@Override
		public DownloadStream getStream() {
			DownloadStream downloadStream = null;
			if(getFocList() != null && selectedItemId != null && getPrintingAction() != null && getPrintingAction().getLauncher() != null){
				PrnLayout prnLayout = (PrnLayout) getFocList().searchByReference((Long) selectedItemId);
				if(prnLayout != null){
					
					if(getPrintLogoCheckBox() != null){
					  boolean withLogo = getPrintLogoCheckBox().getValue();
					  getPrintingAction().getLauncher().setWithLogo(withLogo);
					}
					
					byte[] bytes = null;
					if(wordDoc){
						bytes = getPrintingAction().getLauncher().printWordDocument(prnLayout);
					}else{
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
		
	}
}
