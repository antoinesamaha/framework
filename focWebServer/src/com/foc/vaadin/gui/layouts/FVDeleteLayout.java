package com.foc.vaadin.gui.layouts;

import com.foc.vaadin.gui.FVIconFactory;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;

@SuppressWarnings("serial")
public class FVDeleteLayout extends DDAbsoluteLayout {
  public FVDeleteLayout() {
    setWidth("256px");
    setHeight("256px");
    setDropHandler(new FVDeleteDropHandler());
    addComponent(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_TRASH_SMALL, false),"left:20px;");
  }
}
