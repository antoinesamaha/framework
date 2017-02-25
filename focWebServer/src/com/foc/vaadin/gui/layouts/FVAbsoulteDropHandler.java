package com.foc.vaadin.gui.layouts;

import org.xml.sax.helpers.AttributesImpl;

import com.foc.Globals;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.details.AbsoluteLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

@SuppressWarnings("serial")
public class FVAbsoulteDropHandler extends FVDropHandler {
  
  public FVAbsoulteDropHandler(FVLayout layout) {
    super(layout);
  }
  
  /**
   * Called when a component changed location within the layout
   * 
   * @param event
   *            The drag and drop event
   */
  @Override
  protected void handleComponentReordering(DragAndDropEvent event) {
      AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails) event
              .getTargetDetails();
      DDAbsoluteLayout layout = (DDAbsoluteLayout) details.getTarget();
      LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
              .getTransferable();
      Component component = transferable.getComponent();

      // Get top-left pixel position
      int leftPixelPosition = details.getRelativeLeft();
      int topPixelPosition = details.getRelativeTop();

      ComponentPosition position = layout.getPosition(component);

      position.setLeft((float) leftPixelPosition, com.vaadin.server.Sizeable.Unit.PIXELS);
      position.setTop((float) topPixelPosition, com.vaadin.server.Sizeable.Unit.PIXELS);
      
      AttributesImpl newAttributes = new AttributesImpl(((FocXMLGuiComponent) component).getAttributes());
      newAttributes.setValue(newAttributes.getIndex(FXML.ATT_LEFT), leftPixelPosition+"px");
      newAttributes.setValue(newAttributes.getIndex(FXML.ATT_TOP), topPixelPosition+"px");
      ((FocXMLGuiComponent) component).setAttributes(newAttributes);
      
      if (component instanceof FVLayout) {
        ((FVLayout) component).setDragDrop(true);
      }
  }

  /**
   * Handle a drop from another layout
   * 
   * @param event
   *            The drag and drop event
   */
  @Override
  protected void handleDropFromLayout(DragAndDropEvent event) {
      AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails) event
              .getTargetDetails();
      LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
              .getTransferable();
      Component component = transferable.getComponent();
      Component source = event.getTransferable().getSourceComponent();
      DDAbsoluteLayout layout = (DDAbsoluteLayout) details.getTarget();
      int leftPixelPosition = details.getRelativeLeft();
      int topPixelPosition = details.getRelativeTop();

      // Check that we are not dragging an outer layout into an
      // inner
      // layout
      Component parent = source.getParent();
      while (parent != null) {
          parent = parent.getParent();
      }

      ComponentContainer sourceLayout = null;
      
      // remove component from source
      if (source instanceof ComponentContainer) {
          sourceLayout = (ComponentContainer) source;
          sourceLayout.removeComponent(component);
      }

      AttributesImpl newAttributes = removeNotNeededAttributes(((FocXMLGuiComponent) component).getAttributes(), (FVLayout)sourceLayout);
      
      if (newAttributes.getIndex(FXML.ATT_LEFT) != -1) {
        newAttributes.setValue(newAttributes.getIndex(FXML.ATT_LEFT), leftPixelPosition+"px");
      } else {
        newAttributes.addAttribute("", FXML.ATT_LEFT, FXML.ATT_LEFT, "CDATA", leftPixelPosition+"px");
      }
      
      if (newAttributes.getIndex(FXML.ATT_TOP) != -1) {
        newAttributes.setValue(newAttributes.getIndex(FXML.ATT_TOP), topPixelPosition+"px");
      } else {
        newAttributes.addAttribute("", FXML.ATT_TOP, FXML.ATT_TOP, "CDATA", topPixelPosition+"px");
      }
      
      ((FocXMLGuiComponent) component).setAttributes(newAttributes);
      
      // Add component to absolute layout
      layout.addComponent(component, "left:" + leftPixelPosition + "px;top:"
              + topPixelPosition + "px");
  }
  
  public void drop(DragAndDropEvent event) {
    AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails) event.getTargetDetails();
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
      attributes.addAttribute("", FXML.ATT_LEFT, FXML.ATT_LEFT, "CDATA", details.getRelativeLeft()+"px");
      attributes.addAttribute("", FXML.ATT_TOP, FXML.ATT_TOP, "CDATA", details.getRelativeTop()+"px");
      
      handleSourceContainer(t, targetLayout, name, attributes);
      
      Globals.logString(targetLayout.toString());
    }
  }

}
