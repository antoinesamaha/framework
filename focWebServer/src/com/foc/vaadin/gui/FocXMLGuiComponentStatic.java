package com.foc.vaadin.gui;

import org.xml.sax.Attributes;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVTable;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public class FocXMLGuiComponentStatic {
	
	public static final String STYLE_NO_PRINT = "noPrint";
	public static final String STYLE_HAND_POINTER_ON_HOVER = "handPointerOnHover";
	
	public static final String STYLE_BUTTON_ORANGE = "foc-button-orange";
	public static final String STYLE_BUTTON_GREEN  = "foc-button-green";
	public static final String STYLE_BUTTON_BLUE   = "foc-button-blue";
	public static final String STYLE_BUTTON_GRAY   = "foc-button-gray";
	
	private static final int MAX_BUTTON_STYLE_INDEX = 6;
	
	public static String getButtonStyleForIndex(int index){
		String style = "foc-button-dark-";
		index = (index % MAX_BUTTON_STYLE_INDEX) + 1;
		if(index < 10){
			style += "0"+index;
		}else{
			style += index;
		}
		return style;
	}
			
	public static void applyAttributes_WidthHeight(Component component, Attributes attributes) {
    if (attributes != null) {
      try {
			  String width = attributes.getValue(FXML.ATT_WIDTH);
			  if (width != null) {
			  	component.setWidth(width);
			  	//hadi 01-Dec-15 when we set table height or width we need to set it to wrapper layout too
			  	if(component instanceof FVTable){
		  		}
  			  //hadi 01-Dec-15
			  }
			  
			  String height = attributes.getValue(FXML.ATT_HEIGHT);
			  if (height != null) {
			  		component.setHeight(height);
			  		//hadi 01-Dec-15
			  		if(component instanceof FVTable){
			  			FVTable table = (FVTable) component;
			  			if(table.getTableTreeDelegate() != null && table.getTableTreeDelegate().getWrapperLayout() != null){
			  				table.getTableTreeDelegate().getWrapperLayout().setHeight(height);
			  			}
			  		}
			  		//hadi 01-Dec-15
			  }
      }catch(Exception e){
      	Globals.logException(e);
      }
    }
	}
	
	public static void applyLayoutBackgroundImageAttributes(FocXMLGuiComponent xmlComponent, Attributes attributes) {
		if(attributes != null){
    	String imageDir = attributes.getValue(FXML.ATT_IMAGE_DIR);
    	if(imageDir != null && !imageDir.isEmpty()){
    		((Component) xmlComponent).setStyleName(imageDir);
    	}
    }
	}
	
	@SuppressWarnings("serial")
	private static class ComponentShortcutListener extends ShortcutListener{

		public ComponentShortcutListener(int keyCode) {
      super(null, keyCode, null);
		}
		
		@Override
		public void handleAction(Object sender, Object target) {
			FocXMLGuiComponent focXMLGuiComponent = target instanceof FocXMLGuiComponent ? (FocXMLGuiComponent) target : null;
			if(focXMLGuiComponent != null && focXMLGuiComponent.getDelegate() != null && focXMLGuiComponent.getDelegate().getFocXMLLayout() != null && focXMLGuiComponent.getAttributes() != null){
				Component component = focXMLGuiComponent.getDelegate().getFocXMLLayout().getComponentByName(focXMLGuiComponent.getAttributes().getValue(FXML.ATT_HELP_INDEX));
				if(component != null && component instanceof FVTextField){
					FVTextField field = (FVTextField) component;
					field.focus();
				}
			}
		}
		
	}
	
  public static void applyAttributes(FocXMLGuiComponent xmlComponent, Attributes attributes) {
    if (attributes != null && xmlComponent != null) {
      try {
        Component component = (Component) xmlComponent;
        
        String visible = attributes.getValue(FXML.ATT_VISIBLE);
        if(visible != null){
        	if(visible.equals("false")){
        		component.setVisible(false);
        	}else{
        		component.setVisible(true);
        	}
        }
        
        String captionMode = attributes.getValue(FXML.ATT_CAPTION_MODE);
        if(captionMode != null){
        	if(captionMode.equals("vertical")){
        		component.addStyleName("vertical");
        	}
        }

        applyAttributes_WidthHeight(component, attributes);

        if (component instanceof AbstractOrderedLayout) {
          String spacing = attributes.getValue(FXML.ATT_SPACING);
          if (spacing == null || spacing.equals("false")) {
            ((AbstractOrderedLayout) component).setSpacing(false);
          } else {
            ((AbstractOrderedLayout) component).setSpacing(true);
          }
        }

        if (component instanceof AbstractLayout) {
          String margin = attributes.getValue(FXML.ATT_MARGIN);
          if(Globals.isValo()){
            if (margin != null && margin.equals("true")) {
            	((AbstractLayout) component).addStyleName("focMargin");
            }
          }else{
            if (margin == null || margin.equals("false")) {
            	((AbstractLayout) component).addStyleName("focNoMargin");
            }
          }
        }
        
        String description = attributes.getValue(FXML.ATT_DESCRIPTION);
        if(description != null && !description.isEmpty() && component instanceof AbstractComponent){
        	AbstractComponent abstractComponent = (AbstractComponent) component;
        	abstractComponent.setDescription(description);
        }

        String caption = attributes.getValue(FXML.ATT_CAPTION);
        String captPos = attributes.getValue(FXML.ATT_CAPTION_POSITION);
        if (caption != null) {
          if (captPos != null) {
            if (!captPos.equals("left") && !captPos.equals("right")) component.setCaption(caption);
          } else {
            component.setCaption(caption);
          }
        }

        String captMargin = attributes.getValue(FXML.ATT_CAPTION_MARGIN);
        if (captMargin != null && captMargin.equals("0") && (caption == null || (captPos != null && !captPos.equals("top")))) {
        	setCaptionMargin_Zero(component);
        }
        
        //Style Attribute
        String style = attributes.getValue(FXML.ATT_STYLE);
        applyStyle(component, style);
        
        xmlComponent.refreshEditable();
        if (attributes != null){
        	String border = attributes.getValue(FXML.ATT_BORDER);
        	if(border != null){
	        	border = border.toLowerCase();
	        	if(border.equals("true")) {
	        		component.addStyleName("border");
	        	}else{
	        		component.addStyleName("foc-border-"+border);
	        	}
        	}
        }
        
        String tabIndexString = attributes.getValue(FXML.ATT_TABINDEX);
        if(!Utils.isStringEmpty(tabIndexString) && xmlComponent.getFormField() != null){
        	int tabIndex = Utils.parseInteger(tabIndexString, -1);
        	xmlComponent.getFormField().setTabIndex(tabIndex);
        }
        
        String iconName = attributes.getValue(FXML.ATT_ICON);
        if(!Utils.isStringEmpty(iconName)){
        	FontIcon icon = FontAwesome.valueOf(iconName.toUpperCase());
        	if(icon != null){
        		component.setIcon(icon);
        	}
        }
        if(!(component instanceof Layout) && ConfigInfo.isGuiRTL()) {
        	component.addStyleName("foc-floatNone");
        }
      } catch (Exception e) {
        Globals.logException(e);
      }
    }
  }
  
  public static void setCaptionMargin_Zero(Component component){
  	component.setCaption(null);
    component.addStyleName("focNoCaptionMargin");
    if(Globals.isValo()){
    	if(component instanceof AbstractOrderedLayout){
    		AbstractOrderedLayout lay = (AbstractOrderedLayout) component;
    		lay.setMargin(false);
    	}
    }
  }
  
  public static void applyStyle(Component component, String style){
    if (component != null && style != null) {
    	String[] styleArray = style.split(",");
    	for(int i=0; i<styleArray.length; i++){
      	component.addStyleName("foc-"+styleArray[i]);
    	}
    }  
  }
	
	public static boolean equalsByDataPath(FocXMLGuiComponent c1, FocXMLGuiComponent c2){
	  return c1 != null && c2 != null && c1.getDelegate() != null && c2.getDelegate() != null && c1.getDelegate().getDataPath() != null && c2.getDelegate().getDataPath() != null && c1.getDelegate().getDataPath().equals(c2.getDelegate().getDataPath());
	}
	
	public static void applyAlignment(AbstractOrderedLayout layout, Component component, String alignment){
	  alignment = alignment.toLowerCase();
    if(alignment.equals("right") || alignment.equals("middle_right")) {
      layout.setComponentAlignment(component, Alignment.MIDDLE_RIGHT);
    } else if(alignment.equals("left") || alignment.equals("middle_left")) {
      layout.setComponentAlignment(component, Alignment.MIDDLE_LEFT);
    } else if(alignment.equals("center") || alignment.equals("middle_center")) {
      layout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
    } else if(alignment.equals("top_right")) {
      layout.setComponentAlignment(component, Alignment.TOP_RIGHT);
    } else if(alignment.equals("top_left")) {
      layout.setComponentAlignment(component, Alignment.TOP_LEFT);
    } else if(alignment.equals("top_center")) {
      layout.setComponentAlignment(component, Alignment.TOP_CENTER);
    } else if(alignment.equals("bottom_right")) {
      layout.setComponentAlignment(component, Alignment.BOTTOM_RIGHT);
    } else if(alignment.equals("bottom_left")) {
      layout.setComponentAlignment(component, Alignment.BOTTOM_LEFT);
    } else if(alignment.equals("bottom_center")) {
      layout.setComponentAlignment(component, Alignment.BOTTOM_CENTER);
    }
	}
	
	public static void setRootFocDataWithDataPath(FocXMLGuiComponent comp, IFocData rootFocData, String dataPath){
		if(comp != null && comp.getDelegate() != null){
			comp.getDelegate().setDataPathWithRoot(rootFocData, dataPath);
		}
	}
	
	public static void applyStyleForArabicLabel(Component component){
		applyStyle(component, "f16,bold");		
	}
}
