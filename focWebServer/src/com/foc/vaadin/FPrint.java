package com.foc.vaadin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Stack;

import com.foc.vaadin.gui.components.FVTextField;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Component;

public class FPrint implements StreamSource {

  public final ByteArrayOutputStream os = new ByteArrayOutputStream();
  
  public FPrint(Stack<Component> stack){
    Document document = null;
    try {
      document = new Document(PageSize.A4, 50, 50, 50, 50);
      
      PdfWriter.getInstance(document, os);
      document.open();
      ////////////////////////////////////////
      String value = null;
      String caption = null;
      while(!stack.isEmpty()){
        Component comp = stack.pop();
        if(comp instanceof FVTextField){
          caption = ((FVTextField)comp).getCaption();
          value = (String) ((FVTextField)comp).getValue();
        }
        
        document.add(new Paragraph("\n"+caption+": "+value));

      }
      ////////////////////////////////////////
      Paragraph pp = new Paragraph();
     // Chapter ch = new Chapter();
     // ch.addSection(pp);
      
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
      if (document != null) {
        document.close();
       }
    }
  }
  
  @Override
  public InputStream getStream() {
    // Here we return the pdf contents as a byte-array
    return new ByteArrayInputStream(os.toByteArray());
  }
}