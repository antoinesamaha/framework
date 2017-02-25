package com.foc.vaadin.gui.layouts;

import org.xml.sax.helpers.AttributesImpl;

import com.foc.Globals;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.DDGridLayout;
import fi.jasoft.dragdroplayouts.DDGridLayout.GridLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

@SuppressWarnings("serial")
public class FVGridDropHandler extends FVDropHandler {
  private final Alignment dropAlignment;
  
  public FVGridDropHandler(FVLayout layout, Alignment dropAlignment) {
    super(layout);
    this.dropAlignment = dropAlignment;
  }

  @Override
  protected void handleComponentReordering(DragAndDropEvent event) {
    GridLayoutTargetDetails details = (GridLayoutTargetDetails) event.getTargetDetails();
    DDGridLayout layout = (DDGridLayout) details.getTarget();
    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    
    Component comp = transferable.getComponent();
    layout.removeComponent(comp);
    
    int row = details.getOverRow();
    int column = details.getOverColumn();
    
    AttributesImpl newAttributes = new AttributesImpl(((FocXMLGuiComponent) comp).getAttributes());
    newAttributes.setValue(newAttributes.getIndex(FXML.ATT_COL), column+"");
    newAttributes.setValue(newAttributes.getIndex(FXML.ATT_ROW), row+"");
    ((FocXMLGuiComponent) comp).setAttributes(newAttributes);
    
    addComponent(event, comp, column, row);
  }

  @Override
  protected void handleDropFromLayout(DragAndDropEvent event) {
    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    GridLayoutTargetDetails details = (GridLayoutTargetDetails) event.getTargetDetails();
    DDGridLayout layout = (DDGridLayout) details.getTarget();
    Component source = event.getTransferable().getSourceComponent();
    Component comp = transferable.getComponent();
    
    ComponentContainer sourceLayout = null;
    
    if (comp == layout) {
    // Dropping myself on myself, if parent is absolute layout then
    // move
      if (comp.getParent() instanceof DDAbsoluteLayout) {
          MouseEventDetails mouseDown = transferable.getMouseDownEvent();
          MouseEventDetails mouseUp = details.getMouseEvent();
          int movex = mouseUp.getClientX() - mouseDown.getClientX();
          int movey = mouseUp.getClientY() - mouseDown.getClientY();
      
          DDAbsoluteLayout parent = (DDAbsoluteLayout) comp.getParent();
          ComponentPosition position = parent.getPosition(comp);
          
          sourceLayout = parent;
      
          float x = position.getLeftValue() + movex;
          float y = position.getTopValue() + movey;
          position.setLeft(x, Sizeable.Unit.PIXELS);
          position.setTop(y, Sizeable.Unit.PIXELS);
      
          return;
      }
    
    } else {
    
    // Check that we are not dragging an outer layout into an inner
    // layout
      Component parent = layout.getParent();
      while (parent != null) {
          if (parent == comp) {
              return;
          }
          parent = parent.getParent();
      }
      
      // Remove component from its source
      if (source instanceof ComponentContainer) {
          sourceLayout = (ComponentContainer) source;
          sourceLayout.removeComponent(comp);
      }
    }
    
    int row = details.getOverRow();
    int column = details.getOverColumn();
    
    AttributesImpl newAttributes = removeNotNeededAttributes(((FocXMLGuiComponent) comp).getAttributes(), (FVLayout)sourceLayout);
    
    if(newAttributes.getValue(FXML.ATT_COL) != null) {
      newAttributes.setValue(newAttributes.getIndex(FXML.ATT_COL), column+"");
    } else {
      newAttributes.addAttribute("", FXML.ATT_COL, FXML.ATT_COL, "CDATA", column+"");
    }
    
    if(newAttributes.getValue(FXML.ATT_ROW) != null) {
      newAttributes.setValue(newAttributes.getIndex(FXML.ATT_ROW), row+"");
    } else {
      newAttributes.addAttribute("", FXML.ATT_ROW, FXML.ATT_ROW, "CDATA", row+"");
    }
    
    
    ((FocXMLGuiComponent) comp).setAttributes(newAttributes);
    
    addComponent(event, comp, column, row);
  }
  
  protected void addComponent(DragAndDropEvent event, Component component, int column, int row) {
    GridLayoutTargetDetails details = (GridLayoutTargetDetails) event.getTargetDetails();
    DDGridLayout layout = (DDGridLayout) details.getTarget();
  
    // If no components exist in the grid, then just add the
    // component
    if (!layout.getComponentIterator().hasNext()) {
        layout.addComponent(component, column, row);
        return;
    }
  
    // If component was dropped on top of another component, abort
    if (layout.getComponent(column, row) != null) {
        return;
    }
  
    // Add the component
    layout.addComponent(component, column, row);
  
    // Add component alignment if given
    if (dropAlignment != null) {
        layout.setComponentAlignment(component, dropAlignment);
    }
  }

  public void drop(DragAndDropEvent event) {
    GridLayoutTargetDetails details = (GridLayoutTargetDetails) event.getTargetDetails();
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
      
      FVLayout targetLayout = (FVLayout) details.getTarget();
      
      FocXMLAttributes attributes = new FocXMLAttributes();
      attributes.addAttribute("", FXML.ATT_ROW, FXML.ATT_ROW, "CDATA", details.getOverRow()+"");
      attributes.addAttribute("", FXML.ATT_COL, FXML.ATT_COL, "CDATA", details.getOverColumn()+"");
      
      handleSourceContainer(t, targetLayout, name, attributes);
      
      Globals.logString(targetLayout.toString());
    }
  }
  
}
