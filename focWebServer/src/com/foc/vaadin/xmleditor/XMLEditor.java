package com.foc.vaadin.xmleditor;

import com.foc.vaadin.ICentralPanel;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class XMLEditor extends Window {
  private XMLView       xmlView      = null;
  
  private VerticalLayout layout;
  private HorizontalLayout buttonLayout;
  private TextArea editor;
  private Button save;
  private Button cancel;
  
  public XMLEditor(ICentralPanel panel, String title, String xml) {
  	this(panel != null ? panel.getXMLView() : null, title, xml);
  }
  
  public XMLEditor(XMLView xmlView, String title, String xml) {
    setXMLView(xmlView);
    layout = new VerticalLayout();
    buttonLayout = new HorizontalLayout();
//    buttonLayout.setMargin(true, false, false, false);
    buttonLayout.setSpacing(true);
    
    setCaption(title);
    
    editor = new TextArea(title);
//    editor.setRows(40);
//    editor.setColumns(80);
    editor.setWidth("100%");
    editor.setHeight("100%");
    editor.setValue(xml);
    editor.addStyleName("focXMLEditor");
    
    save = new Button("save");
    cancel = new Button("cancel");
    
    addListenersToButtons();
 
    layout.setWidth("100%");
    layout.setHeight("100%");
    layout.setSpacing(false);
    layout.addComponent(editor);
    layout.setComponentAlignment(editor, Alignment.TOP_CENTER);
    layout.setExpandRatio(editor, 1);
    
    buttonLayout.addComponent(save);
    buttonLayout.setComponentAlignment(save, Alignment.MIDDLE_CENTER);
    buttonLayout.addComponent(cancel);
    buttonLayout.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);
    
    layout.addComponent(buttonLayout);
    layout.setComponentAlignment(buttonLayout, Alignment.TOP_CENTER);
    
    setWidth("100%");
    setHeight("100%");
    setModal(true);
    
    setContent(layout);
  }

  private void addListenersToButtons() {
    save.addClickListener(new Button.ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
         String newXml = editor.getValue().toString();
         
         XMLView view = getXMLView();
         view.saveXML(newXml);
         XMLEditor.this.close();
      }
    });
    
    cancel.addClickListener(new Button.ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        XMLEditor.this.close();
      }
    });
  }

	public XMLView getXMLView() {
		return xmlView;
	}

	public void setXMLView(XMLView xmlView) {
		this.xmlView = xmlView;
	}
}
