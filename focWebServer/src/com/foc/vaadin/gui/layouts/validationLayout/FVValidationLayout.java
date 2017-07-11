package com.foc.vaadin.gui.layouts.validationLayout;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.OptionDialog;
import com.foc.access.AccessSubject;
import com.foc.access.FocDataMap;
import com.foc.admin.FocGroup;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserHistory;
import com.foc.admin.FocUserHistoryDesc;
import com.foc.admin.FocUserHistoryList;
import com.foc.admin.GroupXMLViewDesc;
import com.foc.business.notifier.DocMsg;
import com.foc.business.notifier.DocMsgDesc;
import com.foc.business.notifier.FocPageLink;
import com.foc.business.notifier.FocPageLinkDesc;
import com.foc.business.photoAlbum.PhotoAlbumAppGroup;
import com.foc.business.printing.PrnContext;
import com.foc.business.printing.PrnLayoutDesc;
import com.foc.business.printing.ReportFactory;
import com.foc.business.printing.gui.PrintingAction;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.business.workflow.map.WFMap;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.link.FocLinkOutRights;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.ASCII;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.components.menuBar.FVMenuBar;
import com.foc.vaadin.gui.components.menuBar.FVMenuBarCommand;
import com.foc.vaadin.gui.layouts.link.FVLinkLayout;
import com.foc.vaadin.gui.mswordGenerator.FocXmlMSWordParser;
import com.foc.vaadin.gui.pdfGenerator.FocXmlPDFParser;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.business.PrnLayout_Table;
import com.foc.web.modules.notifier.DocMsg_Form;
import com.foc.web.modules.photoAlbum.PhotoAlbumWebModule;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class FVValidationLayout extends HorizontalLayout {
  private FValidationSettings            validationSettings  = null;
	private ArrayList<IValidationListener> validationListeners = null;
	private ICentralPanel                  centralPanel        = null;
  
//  private FVHorizontalLayout buttonsLayout = null;
	/*
	private Button nextContextHelpButton     = null;
  private Button previousContextHelpButton = null;
  private Button exitContextHelpButton     = null;
  */
  
	private Button sendInternalEmailButton = null;
	private Button printAndExitButton      = null;
	private Button pdfGeneratorButton      = null;
	private Button attachImageButton       = null;
	private Button fullScreenButton        = null;
	private Button sendEmailButton         = null;
  private Button discardButton           = null;
  private Button deleteButton            = null;
  private Button discardLink             = null;
  private Button applyButton             = null;
  private Button printButton             = null;
  private Button saveButton              = null;
  private Button backButton              = null;

  private Embedded valo_SendInternalEmailEmbedded = null;
  private Embedded valo_PrintAndExitEmbedded      = null;
  private Embedded valo_PdfGeneratorEmbedded      = null;
  private Embedded valo_MSWordGeneratorEmbedded   = null;
  private Embedded valo_AttachImageEmbedded       = null;  
  private Embedded valo_FullScreenEmbedded        = null;  
  private Embedded valo_SendEmailEmbedded         = null;
  private Embedded valo_PDFPrintEmbedded          = null;
  private Embedded valo_DeleteEmbedded            = null;
  private Embedded valo_PrintEmbedded             = null;
  private Embedded valo_BackEmbedded              = null;
  private Button   valo_DiscardButton             = null;
  private Button   valo_ApplyButton               = null;  
  private Button   valo_SaveButton                = null;
  private FVCheckBox valo_NotCompletedYet         = null;

  private FocXmlPDFParser    focXmlPDFParser    = null;
  private FocXmlMSWordParser focXmlMSWordParser = null;
  private FVHelpButton       tipsButton      = null;
//  private FVStatusLayout_ComboBox statusLayout_ComboBox = null;
  private FVStatusLayout_MenuBar statusLayout_MenuBar = null;
  private FVStatusLayout     statusLayout    = null;
  private FVStageLayout      stageLayout     = null;
//  private FVStageLayout_ComboBox stageLayout_ComboBox = null;
  private FVStageLayout_MenuBar  stageLayout_MenuBar = null;
  private FVViewSelector_MenuBar viewSelector        = null;
  private FVLinkLayout           linkLayout          = null;
  
  private FVLabel titleLabel = null;
  
  private INavigationWindow focVaadinMainWindow = null;
  
  private FVMenuBar moreMenuBar = null;
  private boolean askForConfirmationForExit_Forced = false;
//  private HelpContextComponentFocusable helpContextComponentFocusable = null;
  
  private boolean helpOn = false;
  
  private boolean goingBackAfterDoneClicked = false;
  
  public FVValidationLayout(INavigationWindow focVaadinMainWindow, ICentralPanel centralPanel, FValidationSettings validationSettings, boolean showBackButton) {
  	super();
  	setMargin(false);
  	setSpacing(true);
  	setCaption(null);
//  	addStyleName("noPrint");
    this.validationSettings = validationSettings;
  	this.focVaadinMainWindow = focVaadinMainWindow;
  	this.centralPanel = centralPanel;
    setWidth("100%");
    setHeight("-1px");
    setStyleName("foc-validation");
    if(Globals.isValo()){
//    	We get problems when we apply this style
//    	in trees the scroll bar is hidden so we can't scroll over the tree 
//    	addStyleName("floatPosition");
    }
    
    if(Globals.isValo()){
    	addStyleName("foc-footerLayout");
    }
    addStyleName("noPrint");
    
  	initButtonsLayout(showBackButton);
    validationListeners = new ArrayList<IValidationListener>();
    addTransactionToRecentVisited();
  }
  
  public void dispose(){
  	if(validationListeners != null){
  		validationListeners.clear();
  		validationListeners = null;
  	}
  	
  	if(focXmlPDFParser != null){
  		focXmlPDFParser.dispose();
  		focXmlPDFParser = null;
  	}
  	
  	if(focXmlMSWordParser != null){
  		focXmlMSWordParser.dispose();
  		focXmlMSWordParser = null;
  	}
  	if(stageLayout_MenuBar != null){
  		stageLayout_MenuBar.dispose();
  		stageLayout_MenuBar = null;
  	}
//  	dispose_HelpContextComponentFocusable();
//	  buttonsLayout     = null;
	  discardButton     = null;
	  applyButton       = null;
	  saveButton        = null;
	  printButton       = null;
	  backButton        = null;
	  attachImageButton = null;
	  viewSelector      = null;
	  
	  titleLabel = null;
	  
  	focVaadinMainWindow = null;
  	validationSettings = null;
  	
//  	nextContextHelpButton = null;
//  	exitContextHelpButton = null;
//  	previousContextHelpButton = null;
  }
//  
//  public void dispose_HelpContextComponentFocusable(){
//  	if(helpContextComponentFocusable != null){
//  		helpContextComponentFocusable.dispose();
//  		helpContextComponentFocusable = null;
//  	}
//  }
//  
  public void adjustForSignatureSlideShow(){
  	if(titleLabel != null) removeComponent(titleLabel);
  	if(getApplyButton(false) != null){
  		setComponentAlignment(getApplyButton(false), Alignment.BOTTOM_LEFT);
  	}
  	if(getDiscardButton(false) != null){
  		setComponentAlignment(getDiscardButton(false), Alignment.BOTTOM_LEFT);
  	}
  }
  
  public boolean isRTL(){
  	return ConfigInfo.isGuiRTL();
  }
  
  public FVLabel addTitle(){
      titleLabel = new FVLabel(validationSettings == null || validationSettings.getTitle() == null ? "" : validationSettings.getTitle());
      titleLabel.addStyleName("foc-f16");
      titleLabel.addStyleName("foc-bold");
      titleLabel.addStyleName("foc-text-center");
      titleLabel.addStyleName("noPrint");
      titleLabel.setWidth("-1px");
      titleLabel.setHeight("-1px");
    	addComponent(titleLabel);
    	setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);
    	setExpandRatio(titleLabel, 1f);
    return titleLabel;
  }
  
  public Button getGoBackButton(boolean createIfNeeded){
  	if(backButton == null && createIfNeeded){
      backButton = new Button("");
  		if(validationSettings.getDiscardLink() != null && !validationSettings.getDiscardLink().isEmpty()){
  			backButton.setCaption(validationSettings.getDiscardLink());
  		}else{
  			backButton.setIcon(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_CANCEL));
  		}
  		
      backButton.setStyleName(BaseTheme.BUTTON_LINK);
      backButton.addClickListener(new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
          goBack();
        }
      });
  	}
  	return backButton;
  }

  public Button getPdfGeneratorButton(boolean createIfNeeded){
  	if(pdfGeneratorButton == null && createIfNeeded && ConfigInfo.isForDevelopment()){
  		pdfGeneratorButton = new Button();
  		pdfGeneratorButton.setStyleName(BaseTheme.BUTTON_LINK);
  		pdfGeneratorButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_ADOBE));
			
  		focXmlPDFParser = new FocXmlPDFParser(getCentralPanel(), getFocData());
  		focXmlMSWordParser = new FocXmlMSWordParser(getCentralPanel(), getFocData());
  		
  		pdfGeneratorButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
				}
			});
			BrowserWindowOpener opener = new BrowserWindowOpener(focXmlMSWordParser.getStreamResource());
			opener.extend(pdfGeneratorButton);
  	}
  	return pdfGeneratorButton;
  }

  public void poiAddPictureTest(){
  	try{
	    XWPFDocument document = new XWPFDocument();
	    XWPFParagraph paragraph = document.createParagraph();
	
	    XWPFRun run = paragraph.createRun();
	    int format;
	    
	    String imgFilePath = "C://Users//user//Pictures//forward.png";
	
	    format = XWPFDocument.PICTURE_TYPE_PNG;
	    run.setText(imgFilePath);
	    run.addBreak();
	    run.setText("This is where a picture would be.\r\nAnd here would be another image");
	    run.addBreak(BreakType.PAGE);
	
	    FileOutputStream out = new FileOutputStream("C://Users//user//Desktop//images.docx");
	    byte[] byts = new byte[1000000];
	    out.write(byts);
	    document.addPictureData(byts, format);
	    document.write(out);
	    out.close();
  	}catch(Exception e){
  		
  	}
  }
  
  private void createWordTableRows(){
  	try{
  		XWPFDocument document = new XWPFDocument();
  		XWPFTable tableOne = document.createTable();
  		XWPFTableRow row = null;
  		for(int i=0; i<10; i++){
  			row = tableOne.createRow();
  			
  			row.getCell(0).setText("Row Number: " + i);
  			
  		}
  		FileOutputStream outStream = new FileOutputStream("C://Users//user//Desktop//POI Word Doc Sample Table 1.docx");
      document.write(outStream);
      outStream.close();
  	}catch(Exception ex){
  		Globals.logException(ex);
  	}
  }
  
  private void createWordTable(){
  	try{
  		XWPFDocument document = new XWPFDocument();
  		 
      // New 2x2 table
      XWPFTable tableOne = document.createTable();
      XWPFTableRow tableOneRowOne = tableOne.getRow(0);
      tableOneRowOne.setRepeatHeader(true);
      
      tableOneRowOne.getCell(0).setText("Hello");
      tableOneRowOne.addNewTableCell().setText("World");
      
      XWPFTableRow tableOneRowTwo = tableOne.createRow();
       
      tableOneRowTwo.getCell(0).setText("This is");
      tableOneRowTwo.getCell(1).setText("a table");
            
      for(int i=1; i<50; i++){
      	tableOneRowTwo = tableOne.createRow();
        tableOneRowTwo.getCell(0).setText("row " + i + " tol 0 bla bla very long string");
        tableOneRowTwo.getCell(1).setText("row " + i + " col 1");
      }
      
      //Add a break between the tables
      document.createParagraph().createRun().addBreak();

      // New 3x3 table
      XWPFTable tableTwo = document.createTable();
      XWPFTableRow tableTwoRowOne = tableTwo.getRow(0);
      tableTwoRowOne.getCell(0).setText("col one, row one");
      tableTwoRowOne.addNewTableCell().setText("col two, row one");
      tableTwoRowOne.addNewTableCell().setText("col three, row one");
      
      XWPFTableRow tableTwoRowTwo = tableTwo.createRow();
      tableTwoRowTwo.getCell(0).setText("col one, row two");
      tableTwoRowTwo.getCell(1).setText("col two, row two");
      tableTwoRowTwo.getCell(2).setText("col three, row two");

      XWPFTableRow tableTwoRowThree = tableTwo.createRow();
      tableTwoRowThree.getCell(0).setText("col one, row three");
      tableTwoRowThree.getCell(1).setText("col two, row three");
      tableTwoRowThree.getCell(2).setText("col three, row three");

      FileOutputStream outStream = new FileOutputStream("C://Users//user//Desktop//POI Word Doc Sample Table 1.docx");

      document.write(outStream);
      outStream.close();
      
  	}catch(Exception e){
  		e.printStackTrace();
  	}
  }
  
  private void createWordDocument(){
		try{
			XWPFDocument doc = new XWPFDocument();

			fillWordDocument(doc);
      InputStream pic = new FileInputStream("C://Users/user//Desktop//adobe.png");
//      
      doc.addPictureData(pic, Document.PICTURE_TYPE_JPEG);
      
      FileOutputStream out = new FileOutputStream("C://temp//POIWord_1.docx");
      doc.write(out);
      out.close();
		}catch(Exception e){
			e.printStackTrace();
		}  	
  }

  public static void fillWordDocument(XWPFDocument doc){
		try{
      XWPFParagraph p1 = doc.createParagraph();
      p1.setAlignment(ParagraphAlignment.CENTER);
      p1.setBorderBottom(Borders.DOUBLE);
      p1.setBorderTop(Borders.DOUBLE);

      p1.setBorderRight(Borders.DOUBLE);
      p1.setBorderLeft(Borders.DOUBLE);
      p1.setBorderBetween(Borders.SINGLE);

      p1.setVerticalAlignment(TextAlignment.TOP);
      
      XWPFRun r1 = p1.createRun();
      r1.setBold(true);
      r1.setText("The quick brown fox");
      r1.setBold(true);
      r1.setFontFamily("Courier");
      r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
      r1.setTextPosition(100);

      XWPFParagraph p2 = doc.createParagraph();
      p2.setAlignment(ParagraphAlignment.RIGHT);
			
      //BORDERS
      p2.setBorderBottom(Borders.DOUBLE);
      p2.setBorderTop(Borders.DOUBLE);
      p2.setBorderRight(Borders.DOUBLE);
      p2.setBorderLeft(Borders.DOUBLE);
      p2.setBorderBetween(Borders.SINGLE);

      XWPFRun r2 = p2.createRun();
      r2.setText("jumped over the lazy dog");
      r2.setStrike(true);
      r2.setFontSize(20);

      XWPFRun r3 = p2.createRun();
      r3.setText("and went away");
      r3.setStrike(true);
      r3.setFontSize(20);
      r3.setSubscript(VerticalAlign.SUPERSCRIPT);
			
      XWPFParagraph p3 = doc.createParagraph();
      p3.setWordWrap(true);
      p3.setPageBreak(true);
              
      p3.setAlignment(ParagraphAlignment.BOTH);
      p3.setSpacingLineRule(LineSpacingRule.EXACT);

      p3.setIndentationFirstLine(600);
      
      XWPFRun r4 = p3.createRun();
      r4.setTextPosition(200);
      r4.setText("To be, or not to be: that is the question: "
              + "Whether 'tis nobler in the mind to suffer "
              + "The slings and arrows of outrageous fortune, "
              + "Or to take arms against a sea of troubles, "
              + "And by opposing end them? To die: to sleep; ");
      r4.addBreak(BreakType.PAGE);
      r4.setText("No more; and by a sleep to say we end "
              + "The heart-ache and the thousand natural shocks "
              + "That flesh is heir to, 'tis a consummation "
              + "Devoutly to be wish'd. To die, to sleep; "
              + "To sleep: perchance to dream: ay, there's the rub; "
              + ".......");
      r4.setItalic(true);
			
    //This would imply that this break shall be treated as a simple line break, and break the line after that word:

      XWPFRun r5 = p3.createRun();
      r5.setTextPosition(-10);
      r5.setText("For in that sleep of death what dreams may come");
      r5.addCarriageReturn();
      r5.setText("When we have shuffled off this mortal coil,"
              + "Must give us pause: there's the respect"
              + "That makes calamity of so long life;");
      r5.addBreak();
      r5.setText("For who would bear the whips and scorns of time,"
              + "The oppressor's wrong, the proud man's contumely,");
      
      r5.addBreak(BreakClear.ALL);
      r5.setText("The pangs of despised love, the law's delay,"
              + "The insolence of office and the spurns" + ".......");

		}catch(Exception e){
			e.printStackTrace();
		}  	
  }
  
  private FocDataDictionary newFocDataDictionary_ForPrinting(){
		FocDataDictionary dictionary = null;
		if(getCentralPanel() instanceof FocXMLLayout){
			FocXMLLayout layout = (FocXMLLayout) getCentralPanel();
			FocDataDictionary oldDictionary = layout.getFocDataDictionary(false);
			if(oldDictionary != null){
				dictionary = new FocDataDictionary();
				dictionary.copy(oldDictionary);
			}
		}
		return dictionary;
  }
  
  public Button getPrintButton(boolean createIfNeeded){
  	if(printButton == null && createIfNeeded){
	    // A button to open the printer-friendly page.
	    printButton = new Button("");
//Do not show tool tip text for this button because it will apear on the printout!!!	    
//	    printButton.setDescription("Print");
//------------------------------------------	    
		  printButton.setStyleName(BaseTheme.BUTTON_LINK);
		  printButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_PRINT));  		

		  printButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					printClickListener();
				}
			});
  	}
  	return printButton;
  }
  
  private void printClickListener(){
  	if(!Globals.getApp().checkSession()){					
			INavigationWindow nw = getNavigationWindow();
			saveAndRefreshWithoutGoBack();
			
			getCentralPanel().copyGuiToMemory();
			FocDataDictionary dictionary = newFocDataDictionary_ForPrinting();
			//----------
			IFocData focDataToPrint = getFocData();
			if(getCentralPanel() != null && getCentralPanel() instanceof FocXMLLayout){
				FocXMLLayout focXMLLayout = (FocXMLLayout) getCentralPanel();
				focDataToPrint = focXMLLayout.getFocDataToPrint() != null ? focXMLLayout.getFocDataToPrint() : getFocData();
			}
			FocWebApplication.getFocWebSession_Static().setPrintingData(dictionary, getCentralPanel().getXMLView().getXmlViewKey(), focDataToPrint, false);
			refreshPendingSignatureButtonCaption(nw);
  	}
  }
  
  public Button getPrintAndExitButton(boolean createIfNeeded){
  	if(printAndExitButton == null && createIfNeeded){
  		printAndExitButton = new Button("");
  		printAndExitButton.setDescription("Print and Exit");
  		printAndExitButton.setStyleName(BaseTheme.BUTTON_LINK);
  		printAndExitButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_PRINT_AND_EXIT));  		

		  printAndExitButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					printAndExitClickListener();
				}
			});
  	}
  	return printAndExitButton;
  }
  
  private void printAndExitClickListener(){
  	if(!Globals.getApp().checkSession()){					
			getCentralPanel().copyGuiToMemory();
			FocDataDictionary dictionary = newFocDataDictionary_ForPrinting();
			FocWebApplication.getFocWebSession_Static().setPrintingData(dictionary, getCentralPanel().getXMLView().getXmlViewKey(), getFocData(), true);
			apply();
		}
  }
  
  private void applyBrowserWindowOpenerToPrintButton(AbstractComponent printButton){
  	BrowserWindowOpener opener = null; 
  	if(validationSettings != null && validationSettings.avoidRowBreak()){
  		opener = new BrowserWindowOpener(PrintUI_Break.class);
  	}else{
	  	opener = new BrowserWindowOpener(PrintUI.class);
  	}
  	opener.setFeatures("height=700,width=900,resizable,titlebar=no");
    opener.extend(printButton);
  }
  
  private void navigateToPrintingForm(String printingView){
  	FVViewSelector_MenuBar viewSelector = getViewSelector(false);
  	viewSelector.setView_WithoutSavingSelection(printingView);
  }
  
  public Button getAttachButton(boolean createIfNeeded){
  	if(attachImageButton == null && createIfNeeded){
		  attachImageButton = new Button();
		  attachImageButton.setDescription("Upload Image");
		  attachImageButton.setStyleName(BaseTheme.BUTTON_LINK);
		  attachImageButton.setIcon(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_ATTACH));
		  attachImageButton.addClickListener(new Button.ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
		    	attachClickListener();
		    }
		  });
  	}
  	return attachImageButton;
  }
  
  private void attachClickListener(){
  	FocObject focObject = getFocObject();
  	if(focObject != null){
      if(focObject.hasRealReference()){
      	ICentralPanel centralPanel = PhotoAlbumWebModule.newAttachmentCentralPanel(getFocVaadinMainWindow(), focObject);
        getFocVaadinMainWindow().changeCentralPanelContent(centralPanel, true);
      }
    }
  }
  
  public Button getFullScreenButton(boolean createIfNeeded){
  	boolean inPopup = true;
  	if(focVaadinMainWindow == null || focVaadinMainWindow instanceof FocWebVaadinWindow){
  		inPopup = false;
  	}
  	if(fullScreenButton == null && createIfNeeded && !inPopup && !FocWebApplication.getInstanceForThread().isMobile() && ConfigInfo.showFullScreenButton()){
		  fullScreenButton = new Button();
		  fullScreenButton.setDescription("Full screen");
		  fullScreenButton.setStyleName(BaseTheme.BUTTON_LINK);
		  refreshFullScreenIcon();
		  fullScreenButton.addClickListener(new Button.ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
		    	fullScreenButtonClickListener();
		    }
		  });
  	}
  	return fullScreenButton;
  }
  
  private void fullScreenButtonClickListener(){
  	INavigationWindow comp = getNavigationWindow();//findAncestor(FocWebVaadinWindow.class);
  	if(comp instanceof FocWebVaadinWindow){
  		int format = ((FocWebVaadinWindow) comp).getFullScreenMode();
      
  		if(format == FocWebVaadinWindow.FORMAT_FULL_SCREEN){
  			((FocWebVaadinWindow) comp).setFullScreenMode(FocWebVaadinWindow.FORMAT_PORTRAIT);
  			
  			FocUser user = FocWebApplication.getFocUser();
  			if(user != null) user.saveFullScreenSettings(FocUserHistory.MODE_WINDOWED);
  		}else if(format == FocWebVaadinWindow.FORMAT_PORTRAIT){
  			((FocWebVaadinWindow) comp).setFullScreenMode(FocWebVaadinWindow.FORMAT_FULL_SCREEN);

  			FocUser user = FocWebApplication.getFocUser();
  			if(user != null) user.saveFullScreenSettings(FocUserHistory.MODE_FULLSCREEN);
  		}
  		refreshFullScreenIcon();
  	}
  }
  
  public void refreshFullScreenIcon(){
    if(getFocVaadinMainWindow() != null && getFocVaadinMainWindow() instanceof FocWebVaadinWindow){
      int format = ((FocWebVaadinWindow) getFocVaadinMainWindow()).getFullScreenMode();
      if(format == FocWebVaadinWindow.FORMAT_FULL_SCREEN){
      	if(Globals.isValo()){
      		if(valo_GetFullScreenEmbedded(false) != null){
      			valo_GetFullScreenEmbedded(false).setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_SMALL_SCREEN));
      		}
      	}else{
      		if(getFullScreenButton(false) != null){
      			getFullScreenButton(false).setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_SMALL_SCREEN));
      		}
      	}
      }else if(format == FocWebVaadinWindow.FORMAT_PORTRAIT){
      	if(Globals.isValo()){
      		if(valo_GetFullScreenEmbedded(false) != null){
      			valo_GetFullScreenEmbedded(false).setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_FULL_SCREEN));
      		}
      	}else{
      		if(getFullScreenButton(false) != null){
      			getFullScreenButton(false).setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_FULL_SCREEN));
      		}
      	}
      }
    }
  }
  
  public Button getApplyButton(boolean createIfNeeded){
    if(applyButton == null && createIfNeeded){
	    applyButton = new Button("");
	    applyButton.setStyleName(BaseTheme.BUTTON_LINK);
	    applyButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_APPLY));
	 	
	    applyButton.addClickListener(new Button.ClickListener() {
	      
	      @Override
	      public void buttonClick(ClickEvent event) {
	      	applyButtonClickListener();	      	
	      }
	    });
    }
    return applyButton;
  }
  
  public void applyButtonClickListener(){
  	INavigationWindow nw = getNavigationWindow();
  	Window parentWindowIfDialog = findAncestor(Window.class);
  	if(			FocWebApplication.getInstanceForThread() == null 
  			|| !FocWebApplication.getInstanceForThread().hasModalWindowOverIt(parentWindowIfDialog)){
	  	apply();
	  	refreshPendingSignatureButtonCaption(nw);
  	}
  }
  
	public Button getSaveButton(boolean createIfNeeded){
    if(saveButton == null && createIfNeeded){
    	if(Globals.isValo()){
    		saveButton = new Button(BaseTheme.BUTTON_LINK);	
    	}else{
    		saveButton = new Button();
      	saveButton.setStyleName(BaseTheme.BUTTON_LINK);
      	saveButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_SAVE));	
    	}
    	
      saveButton = new Button("");
//      saveButton.setDescription("Save changes and stay in this form");
      saveButton.setStyleName(BaseTheme.BUTTON_LINK);
      saveButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_SAVE));
      
      saveButton.addClickListener(new Button.ClickListener() {
        
        @Override
        public void buttonClick(ClickEvent event) {
        	saveButtonClickListener();
        }
      });
    }
    return saveButton;
  }
  
  private void saveButtonClickListener(){
  	if(!Globals.getApp().checkSession()){
    	INavigationWindow nw = getNavigationWindow();
    	Window parentWindowIfDialog = findAncestor(Window.class);
    	if(			FocWebApplication.getInstanceForThread() == null 
    			|| !FocWebApplication.getInstanceForThread().hasModalWindowOverIt(parentWindowIfDialog)){
	    	saveAndRefreshWithoutGoBack();
	    	refreshPendingSignatureButtonCaption(nw);
    	}
  	}
  }
    
  public boolean saveAndRefreshWithoutGoBack(){
  	boolean error = commit();

    //This refresh is important for example in WBS BKDN_TREE view. 
    //BEcause all new created nodes Would appear as small whilte lines after this save if we do not have this refresh line.
    //This is due to the fact that ref of new BKDN is changing after the save and thus without this refresh the ids of the lines
    //are not found and the properties are not found
    if(getCentralPanel() != null && getCentralPanel() instanceof FocXMLLayout){
    	FocXMLLayout centralPanel = (FocXMLLayout) getCentralPanel();
      centralPanel.refresh();
    }
    return error;
  }

  public void addTransactionToRecentVisited(){
	  FocObject focObject = getFocObject();
		if(focObject != null){
			FocUserHistoryList historyList = (FocUserHistoryList) FocUserHistoryDesc.getInstance().getFocList();
			if(historyList != null){
				historyList.addRecentTransaction(focObject);					
			}
		}
  }
  
  private boolean isObjectLocked(){
  	return isObjectLocked(getFocObject());
  }
  
  public boolean isObjectLocked(FocObject focObject){
  	boolean locked = false;
  	if(focObject != null){
  		locked = focObject.focObject_IsLocked();
  	}
  	return locked;
//  	boolean isFocList = getFocData() instanceof FocList;
//  	if(getFocData() instanceof FocDataMap){
//  		FocDataMap focDataMap = (FocDataMap) getFocData();
//  		isFocList = focDataMap.getMainFocData() instanceof FocList;
//  		isFocList = !isFocList && focDataMap.getMainFocData() instanceof FTree;
//  	}
//  	
//  	boolean allowModification = isFocList && focObject == null;
//  	if(!allowModification){
//  		allowModification = focObject.workflow_IsAllowDeletion();
//  	}
//		return allowModification;
	}
	
//	public boolean isAllowObjectModification(){
//		FocObject focObject = getFocObject();
//		boolean allowModification = 		(focObject != null && focObject.focObject_AllowModification()) 
//																|| !(getFocData() instanceof FocObject);//If we are in a BkdnTree or Table... We should not return not allowed!
//		return allowModification;
//	}
	
  public boolean commit(){
//  	if(valo_NotCompletedYet != null){
//  		valo_NotCompletedYet.copyGuiToMemory();
//  	}
//  	return commit(false);
//  }
//  
//  public boolean commit(boolean isFromPdfPrintnig){
    boolean error = false;
    try{
    	if(/*isFromPdfPrintnig || */!isObjectLocked()){
	    	addTransactionToRecentVisited();
	    	
	    	ArrayList<IValidationListener> cloneValidationListeners = new ArrayList<IValidationListener>();
	    	if(validationListeners != null){
		    	for(int i=0; i<validationListeners.size(); i++){
		    		cloneValidationListeners.add(validationListeners.get(i));	
		    	}
	    	}
	    	
	      for(int i=0; i<cloneValidationListeners.size(); i++){
	        error = error || cloneValidationListeners.get(i).validationCommit(FVValidationLayout.this);
	      }
	      for(int i=0; i<cloneValidationListeners.size(); i++){
	      	cloneValidationListeners.get(i).validationAfter(FVValidationLayout.this, !error);
	      }
	      
	      cloneValidationListeners.clear();
	      cloneValidationListeners = null;
    	}else{
    		error = true;
        Globals.showNotification("Modifications are not allowed.", "", IFocEnvironment.TYPE_WARNING_MESSAGE);
    	}
    }catch (Exception e){
      Globals.logException(e);
      if(ConfigInfo.isPopupExceptionDialog()){
      	Globals.showNotification("Could not save data.", e.getMessage(), IFocEnvironment.TYPE_ERROR_MESSAGE);
      }else{
      	Globals.logString("ERROR : Could not save data." + e.getMessage());      
      }
      error = false;
      // Ingnored, we'll let the Form handle the errors
    }
    return error;
  }

  private INavigationWindow getNavigationWindow(){
  	ICentralPanel centralPanel = getCentralPanel();
  	INavigationWindow iNavigationWindow = null;
  	if(centralPanel instanceof FocXMLLayout){
  		iNavigationWindow = ((FocXMLLayout)centralPanel).getMainWindow();
  	}
  	return iNavigationWindow;
  }
  
  public void refreshPendingSignatureButtonCaption(INavigationWindow iNavigationWindow){
  	if(iNavigationWindow == null){
  		iNavigationWindow = getNavigationWindow();
  	}
  	if(iNavigationWindow instanceof FocWebVaadinWindow){
  		FocWebVaadinWindow focWebVaadinWindow = (FocWebVaadinWindow)iNavigationWindow;
  		focWebVaadinWindow.resetPendingSignatureButtonCaption(null);
  	}
  }
  
  private Button newButton(String iconName){
    Button button = new Button("");
	
		if(validationSettings.getDiscardLink() != null && !validationSettings.getDiscardLink().isEmpty()){
			button.setCaption(validationSettings.getDiscardLink());
		}else{
			button.setIcon(FVIconFactory.getInstance().getFVIcon_Big(iconName));
		}
		if(!Globals.isValo()) button.setStyleName(BaseTheme.BUTTON_LINK);
  	return button;
  }
  
  public Button getDiscardButton(boolean createIfNeeded){
  	if(discardButton == null && createIfNeeded){
  		
  		
	    discardButton = new Button("");
  		
  		if(validationSettings.getDiscardLink() != null && !validationSettings.getDiscardLink().isEmpty()){
  	    discardButton.setCaption(validationSettings.getDiscardLink());
  		}else{
  	    discardButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_CANCEL));
  	    if(Globals.isValo()){
  	    	discardButton.addStyleName("noFocusHighlight");
  	    	discardButton.setWidth("32px"); 
  	    	discardButton.setHeight("32px");
  	    }
  		}
  		discardButton.setStyleName(BaseTheme.BUTTON_LINK);
  		
	    discardButton.addStyleName("noPrint");
	    discardButton.addClickListener(new Button.ClickListener() {
	      public void buttonClick(ClickEvent event) {
	      	discardButtonClickListener();
	      }
	    });
  	}    

  	return discardButton;
  }
  
  private void discardButtonClickListener(){
  	INavigationWindow nw = getNavigationWindow();
  	cancel();
  	refreshPendingSignatureButtonCaption(nw);
  }
  
  private Button getInternalEmailButton(boolean createIfNeeded){
  	if(sendInternalEmailButton == null && createIfNeeded){
  		sendInternalEmailButton = new Button("");
  		sendInternalEmailButton.setDescription("Send Internal Email");
  		sendInternalEmailButton.setStyleName(BaseTheme.BUTTON_LINK);
  		sendInternalEmailButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_EMAIL));
  		sendInternalEmailButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					sendInternalEmailClickListener();
				}
			});
  	}
  	return sendInternalEmailButton;
  }
  
  private void sendInternalEmailClickListener(){
  	if(!Globals.getApp().checkSession()){
			FocConstructor constructor = new FocConstructor(DocMsgDesc.getInstance(), null);
			DocMsg docMsg = (DocMsg) constructor.newItem();
			
			XMLViewKey xmlViewKey = new XMLViewKey(DocMsgDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM);
			DocMsg_Form docMsg_Form = (DocMsg_Form) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(getFocVaadinMainWindow(), xmlViewKey, docMsg);
			docMsg_Form.setPreviousXmlView(getCentralPanel().getXMLView());
			docMsg_Form.setPreviousFocData(getFocObject());
			docMsg_Form.parseXMLAndBuildGui();
			getFocVaadinMainWindow().changeCentralPanelContent(docMsg_Form, true);
		}
  }
  
  public Button getEmailButton(boolean createIfNeeded){
    if(sendEmailButton == null && createIfNeeded){
      sendEmailButton = new Button("");
      sendEmailButton.setDescription("Send Email");
      sendEmailButton.setStyleName(BaseTheme.BUTTON_LINK);
      sendEmailButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_EMAIL));
      sendEmailButton.addStyleName("noPrint");
      sendEmailButton.addClickListener(new Button.ClickListener() {
        
        @Override
        public void buttonClick(ClickEvent event) {
        	emailClickListener();
        }
      });
    }
    return sendEmailButton;
  }
  
  private void emailClickListener(){
	if(!Globals.getApp().checkSession()){
  	FocList focList = FocPageLinkDesc.getList(FocList.LOAD_IF_NEEDED);
  	FocPageLink focPageLink = (FocPageLink) focList.newEmptyItem();
  	String randomKeyStringForURL = getUrlKey(focList);
  	
	  FocObject focObj    = (getFocObject() != null && getFocObject() instanceof FocObject) ? (FocObject) getFocObject() : null;
		XMLViewKey xmlViewKey = getCentralPanel() != null ? getCentralPanel().getXMLView().getXmlViewKey() : null;
		String serialisation = getCentralPanel().getLinkSerialisation();      		
  	focPageLink.fill(focObj, xmlViewKey, serialisation, randomKeyStringForURL);
  	focPageLink.validate(true);
  	
  	String javaScript = "var win = window.open('mailto:?body=Hello,%20%0d%0a%20%0d%0aClick on this link or copy it in you internet browser to open the document:%20%0d%0a"+Page.getCurrent().getLocation()+randomKeyStringForURL+"%20%0d%0a%20%0d%0aRegards,', '_blank'); win.close();";
    Globals.logString("FVValidationLayout, Line: 897, mail line: " + Page.getCurrent().getLocation()+randomKeyStringForURL);
    JavaScript.getCurrent().execute(javaScript);
  	}
  }
  
  public String getUrlKey(FocList list){
  	String randomKeyStringForURL = ASCII.generateRandomString(50).trim();
  	
  	for(int i=0;i<list.size();i++){
  		FocPageLink focPageLink = (FocPageLink) list.getFocObject(i);
  		if(focPageLink !=null){
  			String key = focPageLink.getKey().trim();
  			if(key.equals(randomKeyStringForURL)){
  				randomKeyStringForURL = ASCII.generateRandomString(50);
  				i=0;
  			}
  		}
  	}
  	return randomKeyStringForURL;
  }
  
  public FVViewSelector_MenuBar getViewSelector(boolean createIfNeeded){
  	if(viewSelector == null && createIfNeeded){
  		viewSelector = new FVViewSelector_MenuBar(centralPanel);
  	}    

  	return viewSelector;
  }
  
  public FVLinkLayout getLinkLayout(boolean createIfNeeded){

  	FocGroup         group  = null;
  	FocLinkOutRights rights = null;
  	
		if(			Globals.getApp().getUser_ForThisSession() != null
				&& 	Globals.getApp().getUser_ForThisSession().getGroup() != null){
			group  = Globals.getApp().getUser_ForThisSession().getGroup();
			rights = Globals.getApp().getUser_ForThisSession().getGroup().getLinkOutRights();
		}
  	
  	//The link layout button only shows up if the user has the right to post.
  	if(linkLayout == null && createIfNeeded && rights != null){
  		FocDesc thisDesc = null;
  		if(getFocData() instanceof FocObject){
  			FocObject obj = (FocObject) getFocData();
  			thisDesc = obj.getThisFocDesc();
  		}
  		if(rights.hasRightsForTableDesc(thisDesc)){
  			linkLayout = new FVLinkLayout(null);
  			if(getFocData() != null){
  				linkLayout.setFocData(getFocData());
  			}
  		}
  	}

  	return linkLayout;
  }

  public void apply(){
  	if(!Globals.getApp().checkSession()){
  		FocCentralPanel focCentralPanel = ((AbstractComponent)getCentralPanel()).findAncestor(FocCentralPanel.class);
  		setGoingBackAfterDoneClicked(true);
	    if(!commit() && getCentralPanel() != null){
	    	//2017-06-29
	    	//This part is useful in the following case only:
	    	//1- From a table we open the Form as Popup
	    	//2- We edit this existing Row and we Apply.
	    	//In this case the refreshGuiForContainer is not called and thus
	    	//without these lines we will not see the impact of the changes on the 
	    	//initial table
	    	if(getCentralPanel() instanceof FocXMLLayout){
	    		FocXMLLayout layout = (FocXMLLayout) getCentralPanel();
	    		if(			layout.getTableTreeThatOpenedThisForm() != null 
	    				&& 	layout.getTableTreeThatOpenedThisForm().getFocDataWrapper() != null){
	    			layout.getTableTreeThatOpenedThisForm().getFocDataWrapper().refreshGuiForContainerChanges();
	    		}
	    	}
	    	//-------------------------------
	    	
	    	getCentralPanel().goBack(focCentralPanel);
	    }
  	}
  }

  public void cancel(){
  	if(!Globals.getApp().checkSession()){
			if(isAskForConfirmationForExit() && !isObjectLocked()){
				confirmBeforeExit();
			}else{
				cancel_ExecutionWithoutPrompt();
			}
  	}
  }
  
  public void cancel_ExecutionWithoutPrompt(){
  	if(validationListeners != null){
    	ArrayList<IValidationListener> cloneValidationListeners = new ArrayList<IValidationListener>();
    	for(int i=0; i<validationListeners.size(); i++){
    		cloneValidationListeners.add(validationListeners.get(i));	
    	}

	    for(int i=0; i<cloneValidationListeners.size(); i++){
	    	cloneValidationListeners.get(i).validationDiscard(FVValidationLayout.this);
	    }
	    for(int i=0; i<cloneValidationListeners.size(); i++){
	    	cloneValidationListeners.get(i).validationAfter(FVValidationLayout.this, false);
	    }
      
      cloneValidationListeners.clear();
      cloneValidationListeners = null;
  	}
  	
    goBack();
  }
  
  public boolean isWithStatus(){
  	return validationSettings == null || validationSettings.isWithStatus(); 
  }
  
  public FVStatusLayout_MenuBar getStatusLayout(boolean createIfNeeded){
  	if(			statusLayout_MenuBar == null && createIfNeeded && isWithStatus() 
  			&& 	Globals.getApp() != null && Globals.getApp().getUser_ForThisSession() != null && !Globals.getApp().getUser_ForThisSession().isGuest()){
  		
  		if(getCentralPanel() != null && getCentralPanel() instanceof FocXMLLayout){
  			FocXMLLayout xmlLayout = (FocXMLLayout) getCentralPanel();
  			FocObject focObject = xmlLayout.getFocObject();
  			if(focObject != null && focObject.status_hasStatus() /*&& !((FocObject)getCentralPanel().getFocData()).isCreated()*/){
  				statusLayout_MenuBar = new FVStatusLayout_MenuBar(xmlLayout, focObject);
  				
  				if(statusLayout_MenuBar.getRootMenuItem() != null && statusLayout_MenuBar.getRootMenuItem().getSize() > 0){//If the status bar is empty then do not show it
					  addComponent(statusLayout_MenuBar);
					  setComponentAlignment(statusLayout_MenuBar, Alignment.MIDDLE_LEFT);
  				}
  			}
  		}
  	}
  	return statusLayout_MenuBar;
  }
  
  
  public FVStageLayout_MenuBar getStageLayout(boolean createIfNeeded){
  	if(stageLayout_MenuBar == null && createIfNeeded && Globals.getApp() != null && Globals.getApp().getUser_ForThisSession() != null && !Globals.getApp().getUser_ForThisSession().isGuest()){
  		ICentralPanel centralPanel = getCentralPanel();
  		if(centralPanel != null && centralPanel instanceof FocXMLLayout){
  			FocXMLLayout xmlLayout = (FocXMLLayout) centralPanel;
  			FocObject focObject = xmlLayout.getFocObject();
	  		if(focObject != null){
	  			if(focObject.workflow_IsWorkflowSubject()){
	  	    	FocDesc focDesc = focObject.getThisFocDesc();
	  	    	if(focDesc != null && focDesc instanceof IWorkflowDesc){
	  	    		IWorkflowDesc iWorkflowDesc = (IWorkflowDesc) focDesc;
	  	    		WFMap map = WFTransactionConfigDesc.getMap_ForTransaction(iWorkflowDesc.iWorkflow_getDBTitle());
	  	    		if(map != null){
	    					stageLayout_MenuBar = new FVStageLayout_MenuBar(xmlLayout, focObject);
	    					addComponent(stageLayout_MenuBar);
	    				  setComponentAlignment(stageLayout_MenuBar, Alignment.MIDDLE_LEFT);
	  	    		}
	  	    	}
	  			}
	  		}
  		}
  	}
  	return stageLayout_MenuBar;
  }
  
  private void initButtonsLayout(boolean showBackButton) {
    if(validationSettings != null){
    	
      if (validationSettings.isWithPrint() && !FocWebApplication.getInstanceForThread().isMobile()) {
        
      	if(Globals.isValo()){
      		addComponent(valo_GetPrintEmbedded(true));
      		PrintingAction printingAction = newPrintingAction();
      		if(printingAction != null){
      			printingAction.dispose();
      			addComponent(valo_GetPDFPrintEmbedded(true));
      		}
      		applyBrowserWindowOpenerToPrintButton(valo_GetPrintEmbedded(false));
          setComponentAlignment(valo_GetPrintEmbedded(false), Alignment.BOTTOM_LEFT);
      	}else{
      		addComponent(getPrintButton(true));
      		applyBrowserWindowOpenerToPrintButton(getPrintButton(false));
          setComponentAlignment(getPrintButton(false), Alignment.BOTTOM_LEFT);
      	}
        
      	if(getFocData() != null && getFocData() instanceof FocObject && validationSettings.isWithPrintAndExit()){
	        if(Globals.isValo()){
	        	addComponent(valo_GetPrintEmbedded(true));
		        applyBrowserWindowOpenerToPrintButton(valo_GetPrintEmbedded(false));	        
		        setComponentAlignment(valo_GetPrintEmbedded(false), Alignment.BOTTOM_LEFT);
	        }else{
	        	addComponent(getPrintAndExitButton(true));
		        applyBrowserWindowOpenerToPrintButton(getPrintAndExitButton(false));	        
		        setComponentAlignment(getPrintAndExitButton(false), Alignment.BOTTOM_LEFT);	        	
	        }
        }
      }
      
      if (validationSettings.hasPDFGenerator()) {
      	if(Globals.isValo()){
      		if(valo_GetPdfGeneratorEmbedded(true) != null){
  	        addComponent(valo_GetPdfGeneratorEmbedded(false));
  	        setComponentAlignment(valo_GetPdfGeneratorEmbedded(false), Alignment.BOTTOM_LEFT);
        	}
      	}else{
      		if(getPdfGeneratorButton(true) != null){
  	        addComponent(getPdfGeneratorButton(true));
  	        setComponentAlignment(getPdfGeneratorButton(true), Alignment.BOTTOM_LEFT);
        	}      		
      	}
      }

      if (validationSettings.hasMSWordGenerator()) {
    		if(valo_GetMSWordGeneratorEmbedded(true) != null){
	        addComponent(valo_GetMSWordGeneratorEmbedded(false));
	        setComponentAlignment(valo_GetMSWordGeneratorEmbedded(false), Alignment.BOTTOM_LEFT);
      	}
      }

      if (validationSettings.isWithAttach() && isAttachementApplicable()) {
      	if(Globals.isValo()){
      		addComponent(valo_GetAttachEmbedded(true));
          setComponentAlignment(valo_GetAttachEmbedded(false), Alignment.BOTTOM_LEFT);
      	}else{
      		addComponent(getAttachButton(true));
          setComponentAlignment(getAttachButton(false), Alignment.BOTTOM_LEFT);      		
      	}
      }
      
      if (validationSettings.isWithEmail() && !FocWebApplication.getInstanceForThread().isMobile()){
      	if(Globals.isValo()){
      		addComponent(valo_GetEmailEmbedded(true));
          setComponentAlignment(valo_GetEmailEmbedded(false), Alignment.BOTTOM_LEFT);
      	}else{
      		addComponent(getEmailButton(true));
          setComponentAlignment(getEmailButton(false), Alignment.BOTTOM_LEFT);      		
      	}
      }
      
      //DO NOT DELETE THIS
      //UNDER DEVELOPMENT
      /*
      if (validationSettings.isWithInternalEmail() && !FocWebApplication.getInstanceForThread().isMobile()){
        addComponent(getInternalEmailButton(true));
        setComponentAlignment(getInternalEmailButton(false), Alignment.BOTTOM_LEFT);
      }
      */
    }
    
    if(Globals.isValo()){
    	Embedded embedded = valo_GetFullScreenEmbedded(true);
    	if(embedded != null){
		    addComponent(embedded);
		    setComponentAlignment(embedded, Alignment.BOTTOM_LEFT);
	    }
    }else{
	    Button fullScreenButton = getFullScreenButton(true);
	    if(fullScreenButton != null){
		    addComponent(fullScreenButton);
		    setComponentAlignment(fullScreenButton, Alignment.BOTTOM_LEFT);
	    }
  	}
    
    Component lastAdded = null;
    if (validationSettings.isWithViewSelector() 
    		&& !FocWebApplication.getInstanceForThread().isMobile() 
    		&& Globals.getApp() != null 
  			&& Globals.getApp().getUser_ForThisSession() != null 
  			&& Globals.getApp().getUser_ForThisSession().getGroup() != null
  			&& Globals.getApp().getUser_ForThisSession().getGroup().getViewsRight() < GroupXMLViewDesc.ALLOW_NOTHING){
    	
    	addComponent(getViewSelector(true));
		  lastAdded = getViewSelector(false);
		  setComponentAlignment(getViewSelector(false), Alignment.MIDDLE_LEFT);
    }
    
    if(getStatusLayout(true) != null){
    	lastAdded = (Component) getStatusLayout(false);
    }
    
    if(getStageLayout(true) != null){
    	
    	FocDesc focDesc = getFocObject().getThisFocDesc();
    	if(focDesc instanceof IWorkflowDesc){
    		IWorkflowDesc iWorkflowDesc = (IWorkflowDesc) focDesc;
    		WFMap map = WFTransactionConfigDesc.getMap_ForTransaction(iWorkflowDesc.iWorkflow_getDBTitle());
    		if(map != null){
	    		lastAdded = (Component) getStageLayout(false);
    		}
    	}
    }
    
    IValidationLayoutMoreMenuFiller filler = FVValidationMore.getInstance().getIValidationLayoutMoreMenuFiller();
    if(filler != null && (FocWebApplication.getFocUser() == null || !FocWebApplication.getFocUser().isGuest())){
      filler.addMenuItems(this);
      centralPanel.addMoreMenuItems(this);
    }
    
    MenuBar menuBar = getMenubar(false);
    if(menuBar != null && !FocWebApplication.getInstanceForThread().isMobile()){
      addComponent(menuBar);
      
      setComponentAlignment(menuBar, Alignment.MIDDLE_LEFT);
      lastAdded = menuBar;
    }
    
    if(getLinkLayout(true) != null){
    	addComponent(getLinkLayout(false));
    	setComponentAlignment(getLinkLayout(false), Alignment.BOTTOM_LEFT);
    }

//    addHelpButtons();
    addTitle();
    
    addApplyDiscardButtons(showBackButton);
  }
  
  /*
  private void addHelpButtons(){
  	Button button_PreviousContextHelp = getPreviousContextHelpButton(true);
  	addComponent(button_PreviousContextHelp);
  	setComponentAlignment(button_PreviousContextHelp, Alignment.MIDDLE_CENTER);
  	
  	Button button_ExitContextHelp = getExitContextHelpButton(true);
  	addComponent(button_ExitContextHelp);
  	setComponentAlignment(button_ExitContextHelp, Alignment.MIDDLE_CENTER);
  	setExpandRatio(button_ExitContextHelp, 1f);
  	
  	Button button_NextContextHelp = getNextContextHelpButton(true);
  	addComponent(button_NextContextHelp);
  	setComponentAlignment(button_NextContextHelp, Alignment.MIDDLE_CENTER);
  }
  
  public void toggleContextHelpButtonsVisibility(boolean showContextHelp){
  	helpOn = !helpOn;
  	dispose_HelpContextComponentFocusable();
  	
  	if(helpOn){
  		addStyleName("foc-validationHelpOn");
  	}else{
  		removeStyleName("foc-validationHelpOn");
  	}
  	
  	for(int i=0; i<getComponentCount(); i++){
  		getComponent(i).setVisible(!getComponent(i).isVisible());
  	}
  	
  	getPreviousContextHelpButton(false).setVisible(getPreviousContextHelpButton(false).isVisible());
  	getNextContextHelpButton(false).setVisible(getNextContextHelpButton(false).isVisible());
  	getExitContextHelpButton(false).setVisible(getExitContextHelpButton(false).isVisible());
  	
  	if(helpOn && showContextHelp){
  		getHelpContextComponentFocusable(true).showHelpAtIndex(0);
  	}
  }
  
  private Button getPreviousContextHelpButton(boolean createIfNeeded){
  	if(previousContextHelpButton == null && createIfNeeded){
  		previousContextHelpButton = new Button("< Previous Help Tip", new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					getHelpContextComponentFocusable(true).onButtonClickListener(false);					
				}
			});
  		previousContextHelpButton.setDescription("Previous Field Tip");
  		previousContextHelpButton.setVisible(false);
  	}
  	return previousContextHelpButton;
  }
  
  private Button getNextContextHelpButton(boolean createIfNeeded){
  	if(nextContextHelpButton == null && createIfNeeded){
  		nextContextHelpButton = new Button("Next Help Tip>", new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					getHelpContextComponentFocusable(true).onButtonClickListener(true);
				}
			});
  		nextContextHelpButton.setDescription("Next Field Tip");
  		nextContextHelpButton.setVisible(false);
  	}
  	return nextContextHelpButton;
  }
  
  private Button getExitContextHelpButton(boolean createIfNeeded){
  	if(exitContextHelpButton == null && createIfNeeded){
  		exitContextHelpButton = new Button("Exit Help Tip", new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					FocXMLLayout focXMLLayout = (FocXMLLayout) getCentralPanel();
					if(focXMLLayout != null && focXMLLayout.getMainWindow() != null && focXMLLayout.getMainWindow() instanceof FocWebVaadinWindow){
						FocWebVaadinWindow focWebVaadinWindow = (FocWebVaadinWindow) focXMLLayout.getMainWindow();
						if(focWebVaadinWindow.getHelpButton(false) != null){
							focWebVaadinWindow.getHelpButton(false).click();
						}
					}
				}
			});
  		exitContextHelpButton.setDescription("Exit Help Tip");
  		exitContextHelpButton.setVisible(false);
  	}
  	return exitContextHelpButton;
  }
  */
  
  public void addApplyDiscardButtons(boolean showBackButton){
    boolean showValidationAndSave = validationSettings != null && validationSettings.isWithApply();
    boolean showSave              = validationSettings != null && validationSettings.isWithSave();
    boolean showDiscard           = validationSettings != null && validationSettings.isWithDiscard();
    boolean showGoBack            = validationSettings == null && showBackButton;

  	ICentralPanel centralPanel = getCentralPanel();
  	if(centralPanel instanceof FocXMLLayout){
  		FocXMLLayout layout = (FocXMLLayout) centralPanel;
  		
			if(Globals.isValo()){
				Component comp = valo_GetNotCompletedYet(true);
				if(comp != null){
					addComponent(comp);
		    	setComponentAlignment(comp, Alignment.BOTTOM_LEFT);
				}
			}
  		
  		if(			layout.getTableTreeThatOpenedThisForm() != null 
  				&& 	layout.getTableTreeThatOpenedThisForm().getTableTreeDelegate() != null 
  				&&  layout.getTableTreeThatOpenedThisForm().getTableTreeDelegate().isDeleteEnabled() 
  				&&  getFocObject() != null){
  			if(Globals.isValo()){
					Embedded deleteButtton = valo_GetDeleteEmbedded(true);
	  			if(deleteButtton != null){
			    	addComponent(deleteButtton);
			    	setComponentAlignment(deleteButtton, Alignment.BOTTOM_LEFT);
	  			}
  			}else{
  				Button deleteButtton = getDeleteButton(true);
    			if(deleteButtton != null){
  		    	addComponent(deleteButtton);
  		    	setComponentAlignment(deleteButtton, Alignment.BOTTOM_LEFT);
    			}  				
  			}
  		}
  	}
    
    Component discardOrGoBackButton = null;
    
    if(showDiscard){
    	if(Globals.isValo()){
    		discardOrGoBackButton = valo_GetDiscardButton(true);
    	}else{
    		discardOrGoBackButton = getDiscardButton(true);    		
    	}
    } else if(showGoBack){
    	if(Globals.isValo()){
    		discardOrGoBackButton = valo_GetGoBackEmbedded(true);
    	}else{
    		discardOrGoBackButton = getGoBackButton(true);
    	}
    }

    if(discardOrGoBackButton != null){
    	addComponent(discardOrGoBackButton);
    	if(Globals.isValo()){
    		setComponentAlignment(discardOrGoBackButton, Alignment.MIDDLE_RIGHT);
    	}else{
    		setComponentAlignment(discardOrGoBackButton, Alignment.BOTTOM_RIGHT);
    	}
    		
	    if(titleLabel == null){
	    }
    }
    
    if (showValidationAndSave) {
    	if(showSave){
    		if(Globals.isValo()){
    			addComponent(valo_GetSaveButton(true));
  	    	setComponentAlignment(valo_GetSaveButton(false), Alignment.MIDDLE_LEFT);
    		}else{
    			addComponent(getSaveButton(true));
  	    	setComponentAlignment(getSaveButton(false), Alignment.BOTTOM_LEFT);    			
    		}
    	}

    	if(Globals.isValo()){
    		addComponent(valo_GetApplyButton(true));
      	setComponentAlignment(valo_GetApplyButton(false), Alignment.MIDDLE_LEFT);
    	}else{
    		addComponent(getApplyButton(true));
      	setComponentAlignment(getApplyButton(false), Alignment.BOTTOM_LEFT);    		
    	}
    }
  }
  
  public void hideApplyButtons(boolean hide){
  	Button button = valo_GetSaveButton(false);
  	if(button != null) button.setVisible(!hide);
  	button = valo_GetApplyButton(false);
  	if(button != null) button.setVisible(!hide);
  }
  
  public FocObject getFocObject(){
  	FocObject focObject = null;
  	
  	IFocData focData = getFocData();
  	if(focData instanceof FocObject){
  		focObject = (FocObject) focData;
  	}else	if(focData instanceof FocDataMap){
  		FocDataMap map = ((FocDataMap) focData);
  		focData = map.getMainFocData();
  		if(focData instanceof FocObject){
  			focObject = (FocObject) focData;
  		}
  	}
  	return focObject;
  }
  
  public FVMenuBar getMenubar(boolean createIfNeeded){
    if(moreMenuBar == null && createIfNeeded){
      moreMenuBar = new FVMenuBar(this);
      moreMenuBar.addItem("More", null);
      moreMenuBar.setStyleName("moreMenuBar");
      moreMenuBar.addStyleName("noPrint");
    }
    return moreMenuBar;
  }

  public void addMoreItem(String title, FVMenuBarCommand command){
  	addMoreItem(title, true, null, command);
  }
  
  public void addMoreItem(String title, boolean enabled, String description, FVMenuBarCommand command){
    if(command != null && title != null){
      FVMenuBar menuBar = getMenubar(true);
      command.setMenuBar(menuBar);
      if(menuBar.getItems() != null && menuBar.getItems().size() > 0){
        MenuItem moreMenu = menuBar.getItems().get(0);
        MenuItem newItem = moreMenu.addItem(title, command);
        newItem.setEnabled(enabled);
        if(description != null){
        	newItem.setDescription(description);
        }
      }
    }
  }
  
  public IFocData getFocData(){
    IFocData focData = getCentralPanel() != null ? getCentralPanel().getFocData() : null;
    return focData;
  }
  
  public boolean isAttachementApplicable(){
    boolean applicable = false;
    IFocData focData = getCentralPanel().getFocData();
    if(focData instanceof FocDataMap){
    	focData = ((FocDataMap) focData).getMainFocData();
    }
    if(focData != null && focData instanceof FocObject){
      applicable = true;
    }
    if(applicable){
	    PhotoAlbumAppGroup group = PhotoAlbumAppGroup.getCurrentAppGroup();
	    applicable = group != null && group.isAllowDownload();
    }
    return applicable; 
  }
  
	public void goBack(){
		//The condition on root prevents the navigation from going out of the main layout
		//when we click on "Back" of an internal layout 
		if(getCentralPanel() != null && getCentralPanel().isRootLayout()){
			getCentralPanel().goBack(null);
		}
	}
	
	public boolean isAskForConfirmationForExit() {
		IFocData focData = getFocData();
		boolean askForConfirmation = isAskForConfirmationForExit_Forced();
		if(!askForConfirmation && focData != null && focData instanceof AccessSubject && getValidationSettings().isWithApply()
				&& (getCentralPanel() == null || getCentralPanel().isRootLayout())){//If not root, internal we do not want to ask for confirmation
			askForConfirmation = ((AccessSubject) getFocData()).needValidationWithPropagation();
		}
    return askForConfirmation;
  }
	
	public void confirmBeforeExit(){
		OptionDialog dialog = new OptionDialog("Confirmation", "Do you want to confirm the changes you made?") {
			@Override
			public boolean executeOption(String optionName) {
				if(optionName != null){
					if(optionName.equals("SAVE_EXIT")){
						apply();
					}else if(optionName.equals("DISCARD")){
						cancel_ExecutionWithoutPrompt();
					}else if(optionName.equals("CANCEL")){
						
					}
				}
				return false;
			}
		};
		dialog.addOption("SAVE_EXIT", "Save & Exit");
		dialog.addOption("DISCARD", "Discard changes");
		dialog.addOption("CANCEL", "Cancel");
		dialog.setWidth("400px");
		dialog.setHeight("200px");
		Globals.popupDialog(dialog);
	}
	
  public INavigationWindow getFocVaadinMainWindow() {
  	FocWebApplication focWebApplication = FocWebApplication.getInstanceForThread(); 
  	return focWebApplication != null ? focWebApplication.getNavigationWindow() : null;
	}

	public void setFocVaadinMainWindow(FocWebVaadinWindow focVaadinMainWindow) {
		this.focVaadinMainWindow = focVaadinMainWindow;
	}
	
	public void addValidationListener(IValidationListener listener){
		if(validationListeners != null){
			validationListeners.add(listener);
		}
	}
	
	public void removeValidationListener(IValidationListener listener){
		if(validationListeners != null){
			validationListeners.remove(listener);
		}
	}

	public String getTitle() {
		return validationSettings != null ? validationSettings.getTitle() : "";
	}

	public void setTitle(String title) {
	  if(validationSettings != null){
	    validationSettings.setTitle(title);
	  }
		titleLabel.setValue(title);
	}

  public FValidationSettings getValidationSettings() {
    return validationSettings;
  }

  public void setValidationSettings(FValidationSettings validationSettings) {
    this.validationSettings = validationSettings;
  }

  public ICentralPanel getCentralPanel() {
    return centralPanel;
  }

  public Window getWindow(){
  	return findAncestor(Window.class);
  }
  
  public Button getDeleteButton(boolean createIfNeeded){
  	if(deleteButton == null && createIfNeeded){
			deleteButton = new Button();
			deleteButton.setStyleName(BaseTheme.BUTTON_LINK);
			if(FocWebApplication.getInstanceForThread().isMobile()){
				deleteButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_TRASH_WHITE));
			}else{
				deleteButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_TRASH_BLACK));
			}
			deleteButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					deleteButtonClickListener();
				}
			});
  	}
  	return deleteButton;
	}
  
  private void deleteButtonClickListener(){
  	FocObject focObj = getFocObject();
  	if(focObj != null){
  		StringBuffer message = focObj.checkDeletionWithMessage();
			if(message != null){
				Globals.showNotification("Cannot delete Item.", message.toString(), IFocEnvironment.TYPE_WARNING_MESSAGE);	
			}else{
		  	OptionDialog dialog = new OptionDialog("Confirm Deletion", "Are you sure you want to delete this item", getFocData()) {
					
					@Override
					public boolean executeOption(String optionName) {
						if(optionName.equals("DELETE")){
				    	ICentralPanel centralPanel = getCentralPanel();
				    	if(centralPanel instanceof FocXMLLayout){
				    		FocXMLLayout layout = (FocXMLLayout) centralPanel;
				    		if(layout.getTableTreeThatOpenedThisForm() != null){
			    				FocObject focObject = getFocObject();
			    				if(focObject != null){
				    				layout.getTableTreeThatOpenedThisForm().delete(focObject.getReference().getInteger());
				    				goBack();
			    				}
				    		}
				    	}
						}else if(optionName.equals("CANCEL")){
							
						}
						return false;
					}
				};
		  	dialog.addOption("DELETE", "Delete");
		  	dialog.addOption("CANCEL", "Cancel");
		  	dialog.setWidth("300px");
		  	dialog.setHeight("200px");
		  	dialog.popup();
			}
  	}
  }  	

  
	public boolean isAskForConfirmationForExit_Forced() {
		return askForConfirmationForExit_Forced;
	}

	public void setAskForConfirmationForExit_Forced(boolean isCustomDiscaaskForConfirmation) {
		this.askForConfirmationForExit_Forced = isCustomDiscaaskForConfirmation;
	}
	
	//-----------------------------------------------Valo Theme Embedded Component---------------------------------------------
	public Embedded valo_GetGoBackEmbedded(boolean createIfNeeded){
  	if(valo_BackEmbedded == null && createIfNeeded){
  		valo_BackEmbedded = new Embedded();
  		valo_BackEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		
  		if(validationSettings.getDiscardLink() != null && !validationSettings.getDiscardLink().isEmpty()){
  			backButton.setCaption(validationSettings.getDiscardLink());
  		}else{
  			backButton.setIcon(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_CANCEL));
  		}
  		
  		valo_BackEmbedded.addClickListener(new MouseEvents.ClickListener() {
				
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					goBack();					
				}
			});
  	}
  	return valo_BackEmbedded;
  }
  
  public Embedded valo_GetPdfGeneratorEmbedded(boolean createIfNeeded){
  	if(valo_PdfGeneratorEmbedded == null && createIfNeeded && ConfigInfo.isForDevelopment()){
  		valo_PdfGeneratorEmbedded = new Embedded();
  		valo_PdfGeneratorEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_PdfGeneratorEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_ADOBE));
			
  		focXmlPDFParser = new FocXmlPDFParser(getCentralPanel(), getFocData());
  		
  		valo_PdfGeneratorEmbedded.addClickListener(new MouseEvents.ClickListener() {
				
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				}
			});
  		
			BrowserWindowOpener opener = new BrowserWindowOpener(focXmlPDFParser.getStreamResource());
			opener.extend(valo_PdfGeneratorEmbedded);
  	}
  	return valo_PdfGeneratorEmbedded;
  }
  
  public Embedded valo_GetMSWordGeneratorEmbedded(boolean createIfNeeded){
  	if(valo_MSWordGeneratorEmbedded == null && createIfNeeded && ConfigInfo.isForDevelopment()){
  		valo_MSWordGeneratorEmbedded = new Embedded();
  		valo_MSWordGeneratorEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_MSWordGeneratorEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_WORD));
			
  		focXmlMSWordParser = new FocXmlMSWordParser(getCentralPanel(), getFocData());
  		
  		valo_MSWordGeneratorEmbedded.addClickListener(new MouseEvents.ClickListener() {
				
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				}
			});
  		
			BrowserWindowOpener opener = new BrowserWindowOpener(focXmlMSWordParser.getStreamResource());
			opener.extend(valo_MSWordGeneratorEmbedded);
  	}
  	return valo_MSWordGeneratorEmbedded;
  }
  
  private PrintingAction newPrintingAction(){
  	PrintingAction printingAction = null;
  	if(getCentralPanel() instanceof FocXMLLayout){
  		FocXMLLayout focXMLLayout = (FocXMLLayout) getCentralPanel();
  		printingAction = focXMLLayout.getPrintingAction();
  	}
  	return printingAction;
  }
  
  public Embedded valo_GetPDFPrintEmbedded(boolean createIfNeeded){
  	if(valo_PDFPrintEmbedded == null && createIfNeeded){
  		valo_PDFPrintEmbedded = new Embedded();
  		valo_PDFPrintEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_PDFPrintEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_ADOBE));
  		valo_PDFPrintEmbedded.setImmediate(true);
  		
  		valo_PDFPrintEmbedded.addClickListener(new MouseEvents.ClickListener() {
				
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					printJasperFireEvent();
				}
			});
  	}
  	return valo_PDFPrintEmbedded;
  }
  
  public void printJasperFireEvent(){
  	boolean requireCommit = !isObjectLocked();
  	if(requireCommit) requireCommit = valo_SaveButton != null || valo_ApplyButton != null;
  	
		if(!requireCommit || !commit()){//If the Object is locked the commit would return error and thus we will never be able to print locked objects.
			//This is why when locked we do not even call the commit and we proceed with the printing
//			PrintingAction printingAction = newPrintingAction();
//  		if(printingAction != null){
//  			printingAction.setObjectToPrint(getFocData());
//  			printingAction.initLauncher();
//  			popupPrintLayout_Table((FocXMLLayout) getCentralPanel(), getNavigationWindow(), printingAction);
//  		}
			if(getCentralPanel() instanceof FocXMLLayout){
				PrintingAction printingAction = newPrintingAction();
				popupPrintPdf_Table((FocXMLLayout) getCentralPanel(), getNavigationWindow(), printingAction, getFocData());
			}
		}
  }
  
  public static void popupPrintPdf_Table(FocXMLLayout previousLayout, INavigationWindow navigationWindow, PrintingAction printingAction, IFocData objectToPrint){
		if(printingAction != null){
			printingAction.setObjectToPrint(objectToPrint);
			printingAction.initLauncher();
			popupPrintLayout_Table(previousLayout, navigationWindow, printingAction);
		}
  }
  
  public static ICentralPanel popupPrintLayout_Table(FocXMLLayout previousLayout, INavigationWindow iNavigationWindow, PrintingAction printingAction){
  	PrnLayout_Table centralPanel = null;
  	if(iNavigationWindow != null && printingAction != null){
  		
	  	PrnContext prnContext = ReportFactory.getInstance().findContext(printingAction.getPrintingContext());
			if(prnContext != null){
				FocList layoutList = prnContext.getLayoutList();
				if(layoutList != null){
					layoutList.loadIfNotLoadedFromDB();
					
					XMLViewKey xmlViewKey = new XMLViewKey(PrnLayoutDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
					centralPanel = (PrnLayout_Table) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(iNavigationWindow, xmlViewKey, layoutList);
					centralPanel.setPrintingAction_AndBecomeOwner(printingAction);
					centralPanel.parseXMLAndBuildGui();
					iNavigationWindow.changeCentralPanelContent(centralPanel, true);
				}
			}
  	}
  	return centralPanel;
  }
  
  private class FVStreamSource implements StreamSource{
			
  	private byte[] bytes = null;
  	
		public FVStreamSource(byte[] bytes) {
			this.bytes = bytes;
		}
		
		public void dispsoe(){
			bytes = null;
		}
		
		public InputStream getStream() {
			return new ByteArrayInputStream(bytes);
		}
		
	}
  
  public Embedded valo_GetPrintEmbedded(boolean createIfNeeded){
  	if(valo_PrintEmbedded == null && createIfNeeded){
  		valo_PrintEmbedded = new Embedded();
  		valo_PrintEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_PrintEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_PRINT));
  		valo_PrintEmbedded.addClickListener(new MouseEvents.ClickListener() {

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					printClickListener();
				}
			});
  	}
  	return valo_PrintEmbedded;
  }
  
  public Embedded valo_GetPrintAndExitEmbedded(boolean createIfNeeded){
  	if(valo_PrintAndExitEmbedded == null){
  		valo_PrintAndExitEmbedded = new Embedded();
  		valo_PrintAndExitEmbedded.setDescription("Print and Exit");
  		valo_PrintAndExitEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_PrintAndExitEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_PRINT_AND_EXIT));
  		valo_PrintAndExitEmbedded.addClickListener(new MouseEvents.ClickListener() {

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					printAndExitClickListener();				
				}
			});
  	}
  	return valo_PrintAndExitEmbedded;
  }

  public Embedded valo_GetAttachEmbedded(boolean createIfNeeded){
  	if(valo_AttachImageEmbedded == null && createIfNeeded){
  		valo_AttachImageEmbedded = new Embedded();
  		valo_AttachImageEmbedded.setDescription("Upload Image");
  		valo_AttachImageEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_AttachImageEmbedded.setSource(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_ATTACH));
  		valo_AttachImageEmbedded.addClickListener(new MouseEvents.ClickListener() {
				
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					attachClickListener();
				}
			});
  	}
  	return valo_AttachImageEmbedded;
  }
  
  public Embedded valo_GetFullScreenEmbedded(boolean createIfNeeded){
  	boolean inPopup = true;
  	if(focVaadinMainWindow == null || focVaadinMainWindow instanceof FocWebVaadinWindow){
  		inPopup = false;
  	}
  	if(valo_FullScreenEmbedded == null && createIfNeeded && !inPopup && !FocWebApplication.getInstanceForThread().isMobile() && ConfigInfo.showFullScreenButton()){
  		valo_FullScreenEmbedded = new Embedded();
  		valo_FullScreenEmbedded.setDescription("Full screen");
  		valo_FullScreenEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
		  refreshFullScreenIcon();
		  valo_FullScreenEmbedded.addClickListener(new MouseEvents.ClickListener() {
				
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					fullScreenButtonClickListener();
				}
			});
  	}
  	return valo_FullScreenEmbedded;
  }
  
  public Button valo_GetApplyButton(boolean createIfNeeded){
    if(valo_ApplyButton == null && createIfNeeded){
    	valo_ApplyButton = new Button("Done");
    	if(isRTL()){
    		valo_ApplyButton.setCaption("إنتهاء");	
    		FocXMLGuiComponentStatic.applyStyleForArabicLabel(valo_ApplyButton);
    	}
    	valo_ApplyButton.setClickShortcut(KeyCode.ENTER, ModifierKey.CTRL);
    	valo_ApplyButton.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
    	
    	if(validationSettings != null && validationSettings.getApplyLink() != null && !validationSettings.getApplyLink().isEmpty()){
    		valo_ApplyButton.setCaption(validationSettings.getApplyLink());
  		}
    	valo_ApplyButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					applyButtonClickListener();
				}
			});
    }
    return valo_ApplyButton;
  }
  
  public Embedded valo_GetDeleteEmbedded(boolean createIfNeeded){
  	if(valo_DeleteEmbedded == null && createIfNeeded && !isObjectLocked()){
  		valo_DeleteEmbedded = new Embedded();
  		valo_DeleteEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
			if(FocWebApplication.getInstanceForThread().isMobile()){
				valo_DeleteEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_TRASH_WHITE));
			}else{
				valo_DeleteEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_TRASH_BLACK));
			}
			
			valo_DeleteEmbedded.addClickListener(new MouseEvents.ClickListener() {
				
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					deleteButtonClickListener();					
				}
			});
  	}
  	return valo_DeleteEmbedded;
	}

  public void setDeleteButtonVisible(boolean visible){
  	if(valo_DeleteEmbedded != null){
  		valo_DeleteEmbedded.setVisible(visible);
  	}
  	if(deleteButton != null){
  		deleteButton.setVisible(visible);
  	}
  }
  
  public FVCheckBox valo_GetNotCompletedYet(boolean createIfNeeded){
  	if(valo_NotCompletedYet == null && createIfNeeded){// && !isObjectLocked()){
  		FocObject focObj = getFocObject();
  		if(focObj != null){
  			FProperty prop = focObj.getFocProperty(FField.FLD_NOT_COMPLETED_YET);
  			if(prop != null){
  				FocXMLAttributes attr = new FocXMLAttributes();
  				String dataPath = prop.getFocField() != null ? prop.getFocField().getName() : null;
  				if(dataPath != null){
  					attr.addAttribute(FXML.ATT_DATA_PATH, prop.getFocField().getName());
  				}
  	  		valo_NotCompletedYet = new FVCheckBox(prop, attr);
  				if(dataPath != null){
  					FocXMLGuiComponentStatic.setRootFocDataWithDataPath(valo_NotCompletedYet, focObj, dataPath);
  				}
  	  		valo_NotCompletedYet.copyMemoryToGui();
  	  		valo_NotCompletedYet.setImmediate(true);//Was not taken into account sometimes! FENIX Station showing it
  	  		if(ConfigInfo.isGuiRTL()){
  	  			valo_NotCompletedYet.setCaption("غير مكتمل");
  	  		}else{
  	  			valo_NotCompletedYet.setCaption("Not Completed Yet");
  	  		}
  	  		if(getCentralPanel() != null && getCentralPanel() instanceof FocXMLLayout && prop.getFocField() != null){
  	  			FocXMLLayout focXMLLayout = (FocXMLLayout) getCentralPanel();
  	  			focXMLLayout.putComponent(prop.getFocField().getName(), valo_NotCompletedYet);
  	  		}
  			}
  		}
//  		valo_NotCompletedYet = new FVCheckBox("Not Completed Yet");
//  		valo_NotCompletedYet.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
//			valo_NotCompletedYet.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_TRASH_BLACK));
			
//  		valo_NotCompletedYet.addClickListener(new MouseEvents.ClickListener() {
//				
//				@Override
//				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
//					deleteButtonClickListener();					
//				}
//			});
  	}
  	return valo_NotCompletedYet;
	}
  
  private Embedded valo_GetInternalEmailEmbedded(boolean createIfNeeded){
  	if(valo_SendInternalEmailEmbedded == null && createIfNeeded){
  		valo_SendInternalEmailEmbedded = new Embedded();
  		valo_SendInternalEmailEmbedded.setDescription("Send Internal Email");
  		valo_SendInternalEmailEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_SendInternalEmailEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_EMAIL));
  		valo_SendInternalEmailEmbedded.addClickListener(new MouseEvents.ClickListener() {
				
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					sendInternalEmailClickListener();					
				}
			});
  	}
  	return valo_SendInternalEmailEmbedded;
  }

  public Embedded valo_GetEmailEmbedded(boolean createIfNeeded){
    if(valo_SendEmailEmbedded == null && createIfNeeded){
    	valo_SendEmailEmbedded = new Embedded();
    	valo_SendEmailEmbedded.setDescription("Send Email");
    	valo_SendEmailEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
    	valo_SendEmailEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_EMAIL));
    	valo_SendEmailEmbedded.addStyleName("noPrint");
    	valo_SendEmailEmbedded.addClickListener(new MouseEvents.ClickListener() {
				
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					emailClickListener();					
				}
			});
    }
    return valo_SendEmailEmbedded;
  }
  
  public Button valo_GetDiscardButton(boolean createIfNeeded){
  	if(valo_DiscardButton == null && createIfNeeded){
  		valo_DiscardButton = new Button("Cancel");
    	if(isRTL()){
    		valo_DiscardButton.setCaption("إلغاء");	
    		FocXMLGuiComponentStatic.applyStyleForArabicLabel(valo_DiscardButton);
    	}

  		valo_DiscardButton.addStyleName("noPrint");
  		valo_DiscardButton.setDescription("Discard Changes");
  		valo_DiscardButton.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		
  		if(validationSettings.getDiscardLink() != null && !validationSettings.getDiscardLink().isEmpty()){
  			valo_DiscardButton.setCaption(validationSettings.getDiscardLink());
  		}else{
	    	valo_DiscardButton.addStyleName("noFocusHighlight");
  		}
  		valo_DiscardButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					discardButtonClickListener();					
				}
			});
  	}    

  	return valo_DiscardButton;
  }
  
  public Button valo_GetSaveButton(boolean createIfNeeded){
    if(valo_SaveButton == null && createIfNeeded){
  		valo_SaveButton = new Button("Save");
  		if(isRTL()){
  			valo_SaveButton.setCaption("حفظ");	
    		FocXMLGuiComponentStatic.applyStyleForArabicLabel(valo_SaveButton);
    	}
  		valo_SaveButton.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_SaveButton.setClickShortcut(KeyCode.S, ModifierKey.CTRL);
  		valo_SaveButton.setDescription("Save changes and stay in this form");
  		valo_SaveButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					saveButtonClickListener();					
				}
			});
    }
    return valo_SaveButton;
  }

	public boolean isGoingBackAfterDoneClicked() {
		return goingBackAfterDoneClicked;
	}

	private void setGoingBackAfterDoneClicked(boolean goingBackDoneClicked) {
		this.goingBackAfterDoneClicked = goingBackDoneClicked;
	}
  
  /*
  private HelpContextComponentFocusable getHelpContextComponentFocusable(boolean createIfNeeded){
  	if(helpContextComponentFocusable == null && createIfNeeded){
  		helpContextComponentFocusable = new HelpContextComponentFocusable();
  	}
  	return helpContextComponentFocusable;
  }
  
  private class HelpContextComponentFocusable {

  	private List<FocXMLGuiComponent> contextHelpComponentsList = null;
  	private int currentContextHelpIndex = 0;
  	
  	public HelpContextComponentFocusable() {
  		FocXMLLayout focXMLLayout = (FocXMLLayout) (getCentralPanel() != null && getCentralPanel() instanceof FocXMLLayout ? getCentralPanel() : null);
  		if(focXMLLayout != null){
  			contextHelpComponentsList = newContextHelpComponentsList();
  		}
		}

  	public void dispose(){
			if(contextHelpComponentsList != null){
				contextHelpComponentsList.clear();
				contextHelpComponentsList = null;
			}
  		currentContextHelpIndex = 0;
  	}
  	
  	public void onButtonClickListener(boolean isNextFocus){
  		if(contextHelpComponentsList != null){
  			
  			currentContextHelpIndex = isNextFocus ? currentContextHelpIndex+1 : currentContextHelpIndex-1;
  			
	  		if(currentContextHelpIndex >= contextHelpComponentsList.size()){
	  			currentContextHelpIndex = 0;
	  		}
	  		
	  		if(currentContextHelpIndex < 0){
	  			currentContextHelpIndex = contextHelpComponentsList.size();
	  		}
	  		
	  		if(contextHelpComponentsList != null && currentContextHelpIndex < contextHelpComponentsList.size()){
	  			showHelpAtIndex(currentContextHelpIndex);
	  		}
  		}
  	}
  
  	public List<FocXMLGuiComponent> newContextHelpComponentsList(){
  		ICentralPanel cp = getCentralPanel();
  		if(cp instanceof FocXMLLayout){
  			FocXMLLayout xmlLayout = (FocXMLLayout) cp;
  			contextHelpComponentsList = new ArrayList<FocXMLGuiComponent>();
  			
  			Iterator<FocXMLGuiComponent> iter = xmlLayout.getXMLComponentIterator();
  			while(iter != null && iter.hasNext()){
  				FocXMLGuiComponent focXMLGuiComponent = iter.next();
  				if(focXMLGuiComponent != null && focXMLGuiComponent.getAttributes() != null){
  					String help = focXMLGuiComponent.getAttributes().getValue(FXML.ATT_HELP);
  					if(help != null){
  						contextHelpComponentsList.add(focXMLGuiComponent);
  					}
  				}
  			}
  			
  			Collections.sort(contextHelpComponentsList, new Comparator<FocXMLGuiComponent>() {
  		    
  				public int compare(FocXMLGuiComponent c1, FocXMLGuiComponent c2) {
  	        int value = 0;
  	        
  					int index1 = Utils.parseInteger(c1.getAttributes().getValue(FXML.ATT_HELP_INDEX), -1);
  					int index2 = Utils.parseInteger(c2.getAttributes().getValue(FXML.ATT_HELP_INDEX), -1);
  					
  					value = index1 - index2;
  					
  	        return value;
  		    }
  			});
  		}
  		return contextHelpComponentsList;
  	}  	
  	
  	public void showHelpAtIndex(int currentContextHelpIndex){
  		if(contextHelpComponentsList != null){
	  		FocXMLGuiComponent guiComponent = contextHelpComponentsList.get(currentContextHelpIndex);
				if(guiComponent != null && guiComponent instanceof AbstractField){
					AbstractField<?> abstractField = (AbstractField<?>) guiComponent;
					
					String help = guiComponent.getAttributes().getValue(FXML.ATT_HELP);
					
					ContextHelp contextHelp = new ContextHelp();
	      	contextHelp.extend(FocWebApplication.getInstanceForThread());
	      	contextHelp.addHelpForComponent(abstractField, help);
	      	contextHelp.showHelpFor(abstractField);
				}
  		}
  	}
  	
  	public void showScreenDescriptionWindow(){
  		
  	}
  	
  }

	public boolean isHelpOn() {
		return helpOn;
	}

	public void setHelpOn(boolean helpOn) {
		this.helpOn = helpOn;
	}
	*/
}
