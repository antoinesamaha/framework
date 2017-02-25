package com.foc.vaadin.gui.components;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.IRightPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FValidationSettings;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.Field;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class FVPanel extends Panel implements ICentralPanel, FocXMLGuiComponent {

  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  private boolean goBackRequested = false;
  
  public FVPanel(){
    delegate = new FocXMLGuiComponentDelegate(this);
  }
  
  public FVPanel(Attributes attributes){
  	delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
  }
  
  @Override
  public void refresh() {
  }

  @Override
  public XMLView getXMLView() {
    return null;
  }

	@Override
	public IRightPanel getRightPanel(boolean createIfNeeded) {
		return null;
	}

  @Override
  public ArrayList<FVLayout> getLayouts() {
    return null;
  }

  @Override
  public FocWebVaadinWindow getMainWindow() {
    return (FocWebVaadinWindow) FocWebApplication.getInstanceForThread().getContent();
  }

	@Override
	public FValidationSettings getValidationSettings(boolean createIfNeeded) {
		return null;
	}

  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
    
  }

  @Override
  public void showValidationLayout(boolean showBackButton) {
    
  }

  @Override
  public String getXMLType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Attributes getAttributes() {
    return null;
  }

  @Override
  public void setAttributes(Attributes attributes) {
  	FocXMLGuiComponentStatic.applyAttributes(this, attributes);    
  }

  @Override
  public void print() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public IFocData getFocData() {
    // TODO Auto-generated method stub
    return null;
  }

	@Override
	public Field getFormField() {
		return null;
	}

	@Override
	public boolean copyGuiToMemory() {
		return false;
	}

	@Override
	public void copyMemoryToGui() {
	}
	
	@Override
  public void setDelegate(FocXMLGuiComponentDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public FocXMLGuiComponentDelegate getDelegate() {
    return delegate;
  }
  
  @Override
  public void dispose(){
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }    
  }

  @Override
  public void setFocData(IFocData focData) {
  }

	@Override
	public void parseXMLAndBuildGui() {
		// TODO Auto-generated method stub
		
	}

  @Override
  public String getPreferredPageWidth() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addMoreMenuItems(FVValidationLayout validationLayout) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getValueString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setValueString(String value) {
  }

  @Override
  public int getViewRights() {
    return 0;
  }

  @Override
  public void setViewRights(int viewRights) {
  }

	@Override
	public void re_parseXMLAndBuildGui() {
	}

	@Override
	public void setXMLView(XMLView xmlView) {
	}

	@Override
	public void goBack(FocCentralPanel focCentralPanel) {
	}
	
	@Override
	public void refreshEditable() {
	}

	@Override
	public String getLinkSerialisation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLinkSerialisation(String serialisation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addedToNavigator() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removedFromNavigator() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void optionButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFocDataOwner() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocDataOwner(boolean focDataOwner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FVValidationLayout getValidationLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isGoBackRequested() {
		return goBackRequested;
	}

	@Override
	public void setGoBackRequested(boolean goBackRequested) {
		this.goBackRequested = goBackRequested;
	}

	@Override
	public void beforeViewChangeListenerFired() {
		// TODO Auto-generated method stub
		
	}
}
