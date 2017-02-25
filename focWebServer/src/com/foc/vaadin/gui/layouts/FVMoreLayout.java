package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

import com.foc.ConfigInfo;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class FVMoreLayout extends FVVerticalLayout {
  private boolean extended         = false;
  private Button  button           = null;
  private Attributes attributes = null;
  	
  public FVMoreLayout(Attributes attributes) {
  	super(attributes);
  	this.attributes = attributes;
  	button = new Button("More...");
  	button.setStyleName(BaseTheme.BUTTON_LINK);
  	button.addStyleName("moreBackground");
  	getMoreLayoutButton().addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				setExtended(!isExtended());
			}
		});
  	addComponent(button);
  }
  
  private Button getMoreLayoutButton(){
  	if(button == null){
	  	button = new Button("More...");
	  	button.setStyleName(BaseTheme.BUTTON_LINK);
	  	button.addStyleName("moreBackground");
  	}
  	return button;
  }

  @Override
  public void addComponent(Component c) {
  	super.addComponent(c);
  	setExtended(isExtended());
  }
  
	public Button getButton() {
		return button;
	}

	public boolean isExtended() {
		return extended;
	}
	
	public void setExtended(boolean extended) {
    this.extended = extended;
    boolean arabic = ConfigInfo.isArabic();
    String caption = arabic ? "المزيد" : "More";
    if(attributes != null && attributes.getValue(FXML.ATT_MORE_CAPTION) != null){
      caption = attributes.getValue(FXML.ATT_MORE_CAPTION);
    }
    
    if(arabic){
    	getMoreLayoutButton().setCaption(extended ? "اخف "+caption+" >>>" : " <<< اظهر "+caption);
    }else{
    	getMoreLayoutButton().setCaption(extended ? " <<< Hide "+caption : "Show "+caption+" >>>");
    }
    for(int i=0; i<getComponentCount(); i++){
      Component comp = getComponent(i);
      if(comp != getButton()){
        comp.setVisible(isExtended());
      }
    }
  }

  @Override
  public void setAttributes(Attributes attributes) {
  	super.setAttributes(attributes);
    if(attributes != null && attributes.getValue(FXML.ATT_EXPAND) != null){
    	String expand = attributes.getValue(FXML.ATT_EXPAND);
    	if(expand.equalsIgnoreCase("true")){
    		setExtended(true);
    	}
    }
  }
	
//	public void setExtended(boolean extended) {
//		this.extended = extended;
//		button.setCaption(extended ? " <<< Collapse" : "More >>>");
//		for(int i=0; i<getComponentCount(); i++){
//			Component comp = getComponent(i);
//			if(comp != getButton()){
//				comp.setVisible(isExtended());
//			}
//		}
//	}
	
  @Override
  public String getXMLType() {
    return FXML.TAG_MORE_LAYOUT;
  }
}
