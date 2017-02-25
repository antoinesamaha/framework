package com.foc.vaadin.gui.layouts.validationLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.foc.Globals;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class FVHelpLayout extends HorizontalLayout {
	private ICentralPanel centralPanel = null;
  
//  private FVHorizontalLayout buttonsLayout = null;
	private Button nextContextHelpButton     = null;
  private Button previousContextHelpButton = null;
  private Button exitContextHelpButton     = null;
  
  private INavigationWindow             focVaadinMainWindow           = null;
  
  private HelpContextComponentFocusable helpContextComponentFocusable = null;
  
  public FVHelpLayout(INavigationWindow focVaadinMainWindow, ICentralPanel centralPanel) {
  	super();
  	setMargin(false);
  	setSpacing(true);
  	setCaption(null);

  	this.focVaadinMainWindow = focVaadinMainWindow;
  	this.centralPanel = centralPanel;
  	
    setWidth("100%");
    setHeight("-1px");
    setStyleName("foc-validation");
    
    if(Globals.isValo()){
    	addStyleName("foc-footerLayout");
    }
    addStyleName("noPrint");
    addStyleName("foc-validationHelpOn");
    
  	initButtonsLayout();
  }
  
  public void dispose(){
  	dispose_HelpContextComponentFocusable();
	  
  	focVaadinMainWindow = null;
  	
  	nextContextHelpButton = null;
  	exitContextHelpButton = null;
  	previousContextHelpButton = null;
  }
  
  public void dispose_HelpContextComponentFocusable(){
  	if(helpContextComponentFocusable != null){
  		helpContextComponentFocusable.dispose();
  		helpContextComponentFocusable = null;
  	}
  }
  
  public ICentralPanel getCentralPanel() {
    return centralPanel;
  }
  
  private void initButtonsLayout(){
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
  
  public void fillHelpLayout(){
  	dispose_HelpContextComponentFocusable();
  	
  	getHelpContextComponentFocusable(true).showHelpAtIndex(0);
  }
  
  private Button getPreviousContextHelpButton(boolean createIfNeeded){
  	if(previousContextHelpButton == null && createIfNeeded){
  		previousContextHelpButton = new FVButton("< Previous Help", new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					getHelpContextComponentFocusable(true).onButtonClickListener(false);					
				}
			});
  		previousContextHelpButton.setDescription("Previous Field Tip");
  		previousContextHelpButton.addStyleName("foc-button-dark-07");
  	}
  	return previousContextHelpButton;
  }
  
  private Button getNextContextHelpButton(boolean createIfNeeded){
  	if(nextContextHelpButton == null && createIfNeeded){
  		nextContextHelpButton = new FVButton("Next Help Tip >", new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					getHelpContextComponentFocusable(true).onButtonClickListener(true);
				}
			});
  		nextContextHelpButton.setDescription("Next Field Tip");
  		nextContextHelpButton.addStyleName("foc-button-dark-07");
  	}
  	return nextContextHelpButton;
  }
  
  private Button getExitContextHelpButton(boolean createIfNeeded){
  	if(exitContextHelpButton == null && createIfNeeded){
  		exitContextHelpButton = new FVButton("Exit Help", new ClickListener() {
				
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
  		exitContextHelpButton.addStyleName("foc-button-dark-07");
  		exitContextHelpButton.setDescription("Exit Help");
  	}
  	return exitContextHelpButton;
  }
	
	public void addComponentToContextHelp(FocXMLGuiComponent focXMLGuiComponent){
		HelpContextComponentFocusable helpContextFocusable = getHelpContextComponentFocusable(false);
		if(helpContextFocusable != null){
			helpContextFocusable.contextHelpComponentsList.add(focXMLGuiComponent);
		}
	}
  
  private HelpContextComponentFocusable getHelpContextComponentFocusable(boolean createIfNeeded){
  	if(helpContextComponentFocusable == null && createIfNeeded){
  		helpContextComponentFocusable = new HelpContextComponentFocusable();
  		helpContextComponentFocusable.newContextHelpComponentsList();
  	}
  	return helpContextComponentFocusable;
  }
  
  private class HelpContextComponentFocusable {

  	private List<FocXMLGuiComponent> contextHelpComponentsList = null;
  	private int currentContextHelpIndex = 0;
  	
  	public HelpContextComponentFocusable() {
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
  			
  			xmlLayout.scanComponentsAndAddToHelpContext(FVHelpLayout.this);
  			
  			/*
  			Iterator<FocXMLGuiComponent> iter = xmlLayout.getXMLComponentIterator();
  			while(iter != null && iter.hasNext()){
  				FocXMLGuiComponent focXMLGuiComponent = iter.next();
  				if(focXMLGuiComponent != null && focXMLGuiComponent.getAttributes() != null){
  					String help = focXMLGuiComponent.getAttributes().getValue(FXML.ATT_HELP);
  					if(help != null){
  						
  					}
  				}
  			}
  			*/
  			
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
  		if(contextHelpComponentsList != null && currentContextHelpIndex<contextHelpComponentsList.size() && currentContextHelpIndex >= 0){
	  		FocXMLGuiComponent guiComponent = contextHelpComponentsList.get(currentContextHelpIndex);
				if(guiComponent != null && guiComponent instanceof Component){
					Component abstractField = (Component) guiComponent;
					
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
}
