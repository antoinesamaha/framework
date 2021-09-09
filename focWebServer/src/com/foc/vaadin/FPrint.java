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
package com.foc.vaadin;

import java.io.InputStream;

//import com.lowagie.text.Document;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.pdf.PdfWriter;
import com.vaadin.server.StreamResource.StreamSource;

public class FPrint implements StreamSource {

	@Override
	public InputStream getStream() {
		// TODO Auto-generated method stub
		return null;
	}
/*
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
*/  
}
