package com.foc.vaadin.gui.layouts.validationLayout;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.helpBook.HelpUI;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;

@SuppressWarnings("serial")
public class FVHelpButton extends NativeButton {

	private FocWebVaadinWindow focWebVaadinWindow = null;
	private XMLView            xmlView            = null;
	
	public FVHelpButton(XMLView xmlView){
		this.xmlView = xmlView;
		init();
	}
	
	public FVHelpButton(FocWebVaadinWindow focWebVaadinWindow){
		this.focWebVaadinWindow = focWebVaadinWindow;
		init();
	}

	public void dispose(){
		xmlView            = null;
		focWebVaadinWindow = null;
	}
	
	private boolean isForContextHelp(){
		return ConfigInfo.isContextHelpActive();
	}

	private void init(){
		setDescription("Help popup");
		setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_TIPS));
		
//		if(!isForContextHelp()){
//			applyBrowserWindowOpenerToPrintButton(this);
//		}
		
		addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				FocXMLLayout layout = getFocXmlLayout();
				if(layout != null){
					FocWebApplication focWebApplication = findAncestor(FocWebApplication.class);
					if(focWebApplication != null){
						if(focWebApplication.isHelpOn()){
							focWebApplication.removeHelpFromFooterLayout();
						}else{

							if(!Utils.isStringEmpty(layout.getScreenHelpText())){
								OptionDialog dateOptionDialog = new OptionDialog("Help", layout.getScreenHelpText()) {
									
									@Override
									public boolean executeOption(String optionName) {
										if(optionName.equals("OKAY")){
											setFooterLayoutToHelp();
										}
										return false;
									}
								};
								dateOptionDialog.addOption("OKAY", "Next Tip >");
								Globals.popupDialog(dateOptionDialog);
							}else{
								setFooterLayoutToHelp();								
							}
						}
					}
					
				}
				//
				//DO NOT DELET THIS 
				//DO NOT DELET THIS
				//DO NOT DELET THIS
				//DO NOT DELET THIS
				//DO NOT DELET THIS
				/*
				if(focWebVaadinWindow != null && focWebVaadinWindow.getCentralPanel() != null){
					FocXMLLayout focXMLLayout = (FocXMLLayout) focWebVaadinWindow.getCentralPanel();
					
					XMLViewKey xmlViewKey = focXMLLayout.getXMLView() != null ? focXMLLayout.getXMLView().getXmlViewKey() : null;
					if(xmlViewKey != null){
						FocHelpPage page = FocHelpBook.getInstance().getPage(xmlViewKey);
						if(page == null){
							page = FocHelpBook.getInstance().getFirstPage();
						}
						if(page != null){
							FocWebApplication.getFocWebSession_Static().putParameter("HELP_CONTENT", page.getHelpMessage());
						}else{
							page = FocHelpBook.getInstance().getFirstPage();
							FocWebApplication.getFocWebSession_Static().putParameter("HELP_CONTENT", page.getHelpMessage());
						}
					}
				}
				*/
				//DO NOT DELET THIS 
				//DO NOT DELET THIS
				//DO NOT DELET THIS
				//DO NOT DELET THIS
				//DO NOT DELET THIS
			}
		});
	}

	public void setFooterLayoutToHelp(){
		FocXMLLayout layout = getFocXmlLayout();
		
		FocWebApplication focWebApplication = findAncestor(FocWebApplication.class);
		if(focWebApplication != null){
			FVHelpLayout helpLayout = new FVHelpLayout(focWebVaadinWindow, layout);
			helpLayout.fillHelpLayout();
			focWebApplication.setFooterLayoutToHelp(helpLayout);
		}
	}
	
	public void setFocWebVaadinWindow(FocWebVaadinWindow focWebVaadinWindow){
		this.focWebVaadinWindow = focWebVaadinWindow;
	}

  private void applyBrowserWindowOpenerToPrintButton(Button printButton){
  	BrowserWindowOpener opener = new BrowserWindowOpener(HelpUI.class);
//    opener.setFeatures("height=700,width=600,resizable");
    opener.extend(printButton);
  }

	public XMLView getXmlView() {
		return xmlView;
	}
	
	private FocXMLLayout getFocXmlLayout(){
		FocXMLLayout focXMLLayout = null;
		if(focWebVaadinWindow != null && focWebVaadinWindow.getCentralPanel() != null && focWebVaadinWindow.getCentralPanel() instanceof FocXMLLayout){
			focXMLLayout = (FocXMLLayout) focWebVaadinWindow.getCentralPanel();
		}
		return focXMLLayout;
	}
	
	private FVValidationLayout getValidationLayout(){
		FVValidationLayout validationLayout = null;
		if(getFocXmlLayout() != null){
			validationLayout = getFocXmlLayout().getValidationLayout();
		}
		return validationLayout;
	}
}
