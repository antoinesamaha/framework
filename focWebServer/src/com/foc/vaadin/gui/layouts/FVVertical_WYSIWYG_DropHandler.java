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

import com.foc.Globals;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.DDVerticalLayout.VerticalLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

@SuppressWarnings("serial")
public class FVVertical_WYSIWYG_DropHandler extends FVDropHandler {
  
  private Alignment dropAlignment;
  
  public FVVertical_WYSIWYG_DropHandler(FVLayout layout) {
    super(layout);
  }
  
  public Alignment getDropAlignment() {
    return dropAlignment;
  }

  public void setDropAlignment(Alignment dropAlignment) {
    this.dropAlignment = dropAlignment;
  }
  
  @Override
  protected void handleComponentReordering(DragAndDropEvent event) {
 // Component re-ordering
    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    VerticalLayoutTargetDetails details = (VerticalLayoutTargetDetails) event.getTargetDetails();
    AbstractOrderedLayout layout = (AbstractOrderedLayout) details.getTarget();
    Component comp = transferable.getComponent();
    int idx = (details).getOverIndex();

    // Detach
    if(comp != null && layout != null) layout.removeComponent(comp);
    idx--;

    // Increase index if component is dropped after or above a previous
    // component
    VerticalDropLocation loc = details.getDropLocation();
    if (loc == VerticalDropLocation.MIDDLE || loc == VerticalDropLocation.BOTTOM) {
        idx++;
    }
    
    // Add component
    if(layout != null){
	    if (idx >= 0) {
	        layout.addComponent(comp, idx);
	    } else {
	        layout.addComponent(comp);
	    }
    }
    
    if (comp instanceof FVLayout) {
      ((FVLayout) comp).setDragDrop(true);
    }

    // Add component alignment if given
    if (dropAlignment != null && layout != null) {
    	layout.setComponentAlignment(comp, dropAlignment);
    }
  }

  @Override
  protected void handleDropFromLayout(DragAndDropEvent event) {
    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    VerticalLayoutTargetDetails details = (VerticalLayoutTargetDetails) event.getTargetDetails();
    AbstractOrderedLayout layout = (AbstractOrderedLayout) details.getTarget();
    Component source = event.getTransferable().getSourceComponent();
    int idx = (details).getOverIndex();
    Component comp = transferable.getComponent();
    
    // Check that we are not dragging an outer layout into an inner
    // layout
    Component parent = layout.getParent();
    while (parent != null) {
      if (parent == comp) {
        return;
      }
      parent = parent.getParent();
    }
    
    // If source is an instance of a component container then remove
    // it
    // from there,
    // the component cannot have two parents.
    
    ComponentContainer sourceLayout = null;
    
    if (source instanceof ComponentContainer) {
      sourceLayout = (ComponentContainer) source;
      sourceLayout.removeComponent(comp);
    }
    
    // Increase index if component is dropped after or above a
    // previous
    // component
    VerticalDropLocation loc = (details).getDropLocation();
    if (loc == VerticalDropLocation.MIDDLE || loc == VerticalDropLocation.BOTTOM) {
      idx++;
    }
    
    // Add component
    if (idx >= 0) {
      layout.addComponent(comp, idx);
    } else {
      layout.addComponent(comp);
    }
    
    // Add component alignment if given
    if (dropAlignment != null) {
      layout.setComponentAlignment(comp, dropAlignment);
    }
  }
  
  public void drop(DragAndDropEvent event) {
    VerticalLayoutTargetDetails details = (VerticalLayoutTargetDetails) event.getTargetDetails();
    DropTarget layout = details.getTarget();
    Component source = event.getTransferable().getSourceComponent();
    
    if (layout == source) {
      handleComponentReordering(event);
    } else if (event.getTransferable() instanceof LayoutBoundTransferable) {
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
        Component comp = transferable.getComponent();
        
        if (comp == layout) {
          if (comp.getParent() instanceof DDAbsoluteLayout) {
            handleDropFromAbsoluteParentLayout(event);
          }
        } else {
          handleDropFromLayout(event);
        }
    } else if (event.getTransferable() instanceof DataBoundTransferable) {
      
      DataBoundTransferable t = (DataBoundTransferable) event.getTransferable();
      
      String name = t.getItemId()+"";
      
      Globals.logString("Item ID = "+t.getItemId());
      Globals.logString("Here in DataBoundTrans");
      
      FVLayout targetLayout = (FVLayout) details.getTarget();
      
      FocXMLAttributes attributes = new FocXMLAttributes();
      attributes.addAttribute("", FXML.ATT_IDX, FXML.ATT_IDX, "CDATA", details.getOverIndex()+"");
      
      handleSourceContainer(t, targetLayout, name, attributes);
      
      Globals.logString(targetLayout.toString());
    }
  }
  
}
