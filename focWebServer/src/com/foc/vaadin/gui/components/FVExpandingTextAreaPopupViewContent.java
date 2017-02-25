package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView;

@SuppressWarnings("serial")
public class FVExpandingTextAreaPopupViewContent implements PopupView.Content{

	private StringBuilder htmlStringBuilder = null;
	private FVExpandingTextArea expandingTextArea = null;
	private PopupView  popupView  = null;	
	private FProperty  property   = null;
	private Attributes attributes = null;
	
	public FVExpandingTextAreaPopupViewContent(FProperty property, Attributes attributes) {
		this.property   = property;
		this.attributes = attributes;
	}
	
	public void dispose(){
		if(expandingTextArea != null){
			expandingTextArea.dispose();
			expandingTextArea = null;
		}
		htmlStringBuilder = null;
		property   = null;
		attributes = null;
		popupView  = null;
	}
	
	public IFocData getFocData(){
		return property;
	}

	public void setFocData(IFocData focData){
		try{
			if(focData instanceof FProperty){
				property = (FProperty) focData;
			}
		}catch(ClassCastException e){
			Globals.logExceptionWithoutPopup(e);
		}
	}
	
	@Override
	public String getMinimizedValueAsHTML() {
		String value = "";
		if(getFocData() != null){
			value = (String) getFocData().iFocData_getValue();
		}
//		String html = "<div class=\"v-table-cell-wrapper\" style=\"text-align: left; padding-left: 90px; \"><span class=\"v-treetable-treespacer\" style=\"width: 90px;\"></span>"+value+"</div>";
		return getHtmlStringBuilder(value).toString();
	}
	
	private StringBuilder getHtmlStringBuilder(String value){
		if(htmlStringBuilder == null){
			htmlStringBuilder = new StringBuilder();
		}
		htmlStringBuilder.replace(0, htmlStringBuilder.length(), "");
		
		htmlStringBuilder.append("<div ");
			htmlStringBuilder.append(" class=\"v-table-cell-wrapper\"");
			htmlStringBuilder.append(">");
			htmlStringBuilder.append(value);
		htmlStringBuilder.append("</div>");

		return htmlStringBuilder;
	}
	
	@Override
	public Component getPopupComponent() {
		return getPopupComponent(true);
	}
	
	public Component getPopupComponent(boolean createIfNeeded) {
		if(expandingTextArea == null && createIfNeeded){
			expandingTextArea = new FVExpandingTextArea(property, attributes);
			if(property != null){
				expandingTextArea.setValue(String.valueOf(property.getValue()));
			}		
			expandingTextArea.addValueChangeListener(new ValueChangeListener() {
	      @Override
	      public void valueChange(ValueChangeEvent event) {
	      	if(event != null && event.getProperty() != null && property != null){
						if(getPopupView() != null){
							getPopupView().setPopupVisible(false);
						}
      			property.setObject(event.getProperty().getValue());
	      	}
	      }
			});
		}
		return expandingTextArea;
	}
	
	public PopupView getPopupView() {
		return popupView;
	}

	public void setPopupView(PopupView popupView) {
		this.popupView = popupView;
	}

}
