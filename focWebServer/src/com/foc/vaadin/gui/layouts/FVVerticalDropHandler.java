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
public class FVVerticalDropHandler extends FVDropHandler {
  
  private Alignment dropAlignment;
  
  public FVVerticalDropHandler(FVLayout layout) {
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
    layout.removeComponent(comp);
    idx--;

    // Increase index if component is dropped after or above a previous
    // component
    VerticalDropLocation loc = details.getDropLocation();
    if (loc == VerticalDropLocation.MIDDLE || loc == VerticalDropLocation.BOTTOM) {
        idx++;
    }
    
    // Add component
    if (idx >= 0) {
        layout.addComponent(comp, idx);
    } else {
        layout.addComponent(comp);
    }
    
    if (comp instanceof FVLayout) {
      ((FVLayout) comp).setDragDrop(true);
    }

    // Add component alignment if given
    if (dropAlignment != null) {
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
