/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.vaadin.gui.layouts;

import java.util.Random;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import com.foc.desc.dataModelTree.DataModelNode;
import com.foc.desc.dataModelTree.DataModelNodeTree;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.windows.EditFieldsWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.data.util.HierarchicalContainerOrderedWrapper;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Window;

import fi.jasoft.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler;

@SuppressWarnings("serial")
public abstract class FVDropHandler extends AbstractDefaultLayoutDropHandler {

  private   FVLayout      fvLayout     = null;
  private   ICentralPanel centralPanel = null;  
  protected Random        rand         = null;
  //protected String[] randomLayoutColors = {"random-bg1", "random-bg2", "random-bg3",
  //                                         "random-bg4", "random-bg5", "random-bg6"};
  
  public FVDropHandler(FVLayout layout) {
    this.fvLayout = layout;
    rand = new Random();
  }
  
  public ICentralPanel getCentralPanel(){
    if(centralPanel == null){
      Component comp = (Component) fvLayout;      
      while(comp.getParent() != null){
        if(comp instanceof ICentralPanel){
          centralPanel = (ICentralPanel) comp;
        }
        comp = comp.getParent();
      }
    }
    return centralPanel;
  }
  
  public IFocData getFocData() {
    return getCentralPanel() != null ? getCentralPanel().getFocData() : null;
  }
  
  protected FProperty getFocPropertyForName(String name){
    return (FProperty) getFocData().iFocData_getDataByPath(name);
  }
  
  //TODELETE
  /*
  protected void addComponent(FVLayout targetLayout, Component comp, Attributes attrib) {
  	if(comp instanceof FVLayout){
      comp.setStyleName(FVLayout.randomLayoutColors[rand.nextInt(6)]);
      ((FVLayout) comp).setDragDrop(true);
  	}
  	
    if (targetLayout instanceof FVAbsoluteLayout) {
      targetLayout.addComponent(comp, attrib);
    } else {
      targetLayout.addComponent(comp);
    }
  }
  */
  
  protected void createAndAddPaletteComponent(String name, FVLayout targetLayout, Attributes attrib) {
    FocXMLLayout form = (FocXMLLayout) getCentralPanel();
//    Window w = form.getMainWindow();
//    if(w != null && w.getParent() != null){
//    	w = w.getParent();
//    }
    EditFieldsWindow editWindow = EditFieldsWindow.getInstanceForThread();
    editWindow.addTab(form, null, targetLayout, name, attrib, true);
//    w.addWindow(new CreateComponentWindow(name, form, targetLayout, attrib));
    
//TODELETE  	
//    Component comp = (Component) FVGUIFactory.getInstance().get(name).create("", getFocData(), getCentralPanel(), attrib);
//
//    getCentralPanel().
//    addComponentToGuiAndMap_AndStack(comp, name, impl);
//    
//    if (comp instanceof FVLabel) {
//      if(name.equals(FXML.LINE_LABEL) || name.equals(FXML.LINE_LABEL_PALETTE)) {
//        //targetLayout.getWindow().addWindow(new LineLabelCreator((FVLabel)comp));
//        AttributesImpl attributes = new AttributesImpl(((FVLabel) comp).getAttributes());
//        attributes.addAttribute("", "name", "name", "CDATA", "null");
//        attributes.addAttribute("", "value", "value", "CDATA", " ");
//        attributes.addAttribute("", "style", "style", "CDATA", "line");
//        attributes.addAttribute("", "height", "height", "CDATA", "1.5px");
//        attributes.addAttribute("", "width", "width", "CDATA", "800px");
//        ((FVLabel) comp).setAttributes(attributes);
//        
//      } else {
//        targetLayout.getWindow().addWindow(new LabelCreator((FVLabel)comp));
//      }
//    } else if (comp instanceof FVLayout) {
//      targetLayout.getWindow().addWindow(new CreateLayoutWindow((FVLayout)comp));
//    }
//    return comp;
  }
  
  protected void createAndAddGuiField(String name, FVLayout targetLayout, Attributes attrib){
    FocXMLLayout form = (FocXMLLayout) getCentralPanel();
//    Window w = form.getWindow();
//    if(w != null && w.getParent() != null){
//    	w = w.getParent();
//    }
    
    EditFieldsWindow editWindow = EditFieldsWindow.getInstanceForThread();
    editWindow.addTab(form, null, targetLayout, name, attrib, false);

//    w.addWindow(new CreateComponentWindow(name, form, targetLayout, attrib));
  }
  
  private void createAndAddColumn(String name, FVTableWrapperLayout targetLayout, FocXMLAttributes attributes) {
    targetLayout.getTableTreeDelegate().addColumn(attributes);
  }
  
  protected void handleSourceContainer(DataBoundTransferable t, FVLayout targetLayout, String name, FocXMLAttributes attributes) {
    if (t.getSourceContainer() instanceof HierarchicalContainerOrderedWrapper) {
    	HierarchicalContainerOrderedWrapper tree = (HierarchicalContainerOrderedWrapper) t.getSourceContainer();
    	Object droppedFocObject = tree.getItem(t.getItemId());
    	if(droppedFocObject instanceof DataModelNode){
	      DataModelNode dataModelField = (DataModelNode) droppedFocObject;
	      name = dataModelField.getFullPath();
	      attributes.addAttribute("", "name", "name", "CDATA", name);
	
	      if (targetLayout instanceof FVTableWrapperLayout) {
	        createAndAddColumn(name, (FVTableWrapperLayout) targetLayout, attributes);
	      } else {
	        createAndAddGuiField(name, targetLayout, attributes);
	      }
    	}
    } else {
      createAndAddPaletteComponent(name.replace(" ", ""), targetLayout, attributes);
    }
  }
  
  @Override
  protected abstract void handleComponentReordering(DragAndDropEvent event);
  
  @Override
  protected abstract void handleDropFromLayout(DragAndDropEvent event);
  
  protected AttributesImpl removeNotNeededAttributes(Attributes attributes, FVLayout layout) {
    AttributesImpl attr = new AttributesImpl(attributes);
    
    if (layout instanceof FVAbsoluteLayout) {
      if (attr.getIndex("left") != -1) {
        attr.removeAttribute(attr.getIndex("left"));
      } 
      
      if (attr.getIndex("top") != -1) {
        attr.removeAttribute(attr.getIndex("top"));
      }
    } else if (layout instanceof FVGridLayout) {
      if (attr.getIndex("row") != -1) {
        attr.removeAttribute(attr.getIndex("row"));
      }
      
      if (attr.getIndex("col") != -1) {
        attr.removeAttribute(attr.getIndex("col"));
      }
    } else if (layout instanceof FVHorizontalLayout || layout instanceof FVVerticalLayout) {
      if (attr.getIndex("idx") != -1) {
        attr.removeAttribute(attr.getIndex("idx"));
      }
    }
    
    return attr;
  }
  
	@Override
	public Class<? extends HasComponents> getTargetLayoutType() {
		return (Class<? extends HasComponents>) fvLayout.getClass();
	}
}
