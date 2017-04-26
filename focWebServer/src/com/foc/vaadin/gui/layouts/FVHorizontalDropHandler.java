package com.foc.vaadin.gui.layouts;

import org.xml.sax.helpers.AttributesImpl;

import com.foc.Globals;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.DDHorizontalLayout.HorizontalLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

@SuppressWarnings("serial")
public class FVHorizontalDropHandler extends FVDropHandler {

  private Alignment dropAlignment;
  
  public FVHorizontalDropHandler(FVLayout layout) {
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
    HorizontalLayoutTargetDetails details = (HorizontalLayoutTargetDetails) event.getTargetDetails();
    AbstractOrderedLayout layout = (AbstractOrderedLayout) details.getTarget();
    Component comp = transferable.getComponent();
    int idx = (details).getOverIndex();

    // Detach
    if(layout != null && comp != null) layout.removeComponent(comp);
    idx--;

    // Increase index if component is dropped after or above a previous
    // component
    HorizontalDropLocation loc = details.getDropLocation();
    if (loc == HorizontalDropLocation.CENTER || loc == HorizontalDropLocation.RIGHT) {
        idx++;
    }

    if(comp != null){
	    AttributesImpl newAttributes = removeNotNeededAttributes(((FocXMLGuiComponent) comp).getAttributes(), (FVLayout)comp.getParent());
	    
	    if (newAttributes.getIndex(FXML.ATT_IDX) != -1) {
	      newAttributes.setValue(newAttributes.getIndex("idx"), idx+"");
	    } else {
	      newAttributes.addAttribute("", FXML.ATT_IDX, FXML.ATT_IDX, "CDATA", idx+"");
	    }
	    
	    ((FocXMLGuiComponent) comp).setAttributes(newAttributes);
    
	    // Add component
	    if(layout != null){
		    if (idx >= 0) {
		        layout.addComponent(comp, idx);
		    } else {
		        layout.addComponent(comp);
		    }
	    }
    
	    if (comp instanceof FVLayout) {
	      Globals.logString("Here3");
	      ((FVLayout) comp).setDragDrop(true);
	    }
	
	    // Add component alignment if given
	    if (dropAlignment != null && layout != null && comp != null) {
	      layout.setComponentAlignment(comp, dropAlignment);
	    }
  	}
  }

  @Override
  protected void handleDropFromLayout(DragAndDropEvent event) {
    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    HorizontalLayoutTargetDetails details = (HorizontalLayoutTargetDetails) event.getTargetDetails();
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
    HorizontalDropLocation loc = (details).getDropLocation();
    
    if (loc == HorizontalDropLocation.CENTER || loc == HorizontalDropLocation.RIGHT) {
      idx++;
    }
    
    AttributesImpl newAttributes = removeNotNeededAttributes(((FocXMLGuiComponent) comp).getAttributes(), (FVLayout) sourceLayout);
    
    if(newAttributes.getIndex(FXML.ATT_IDX) > -1) {
      newAttributes.setValue(newAttributes.getIndex(FXML.ATT_IDX), idx+"");
    }
    
    ((FocXMLGuiComponent) comp).setAttributes(newAttributes);
    
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
    HorizontalLayoutTargetDetails details = (HorizontalLayoutTargetDetails) event.getTargetDetails();
    DropTarget layout = details.getTarget();
    Component source = event.getTransferable().getSourceComponent();
    
    if (layout == source) {
      Globals.logString("Here in");
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
      
      FVLayout targetLayout = (FVLayout) details.getTarget();
      
      FocXMLAttributes attributes = new FocXMLAttributes();
      attributes.addAttribute("", FXML.ATT_IDX, FXML.ATT_IDX, "CDATA", details.getOverIndex()+"");
      
      handleSourceContainer(t, targetLayout, name, attributes);
        
      Globals.logString(targetLayout.toString());
    }
  }
}
