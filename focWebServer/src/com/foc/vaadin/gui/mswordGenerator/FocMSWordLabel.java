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
package com.foc.vaadin.gui.mswordGenerator;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream.GetField;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.foc.Globals;
import com.foc.property.FImageProperty;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocMSWordLabel extends FocMSWordComponent {

	private String           value        = null;
	
	public FocMSWordLabel(FocMSWordLayout pdfParent, FocXMLAttributes xmlAttribute) {
		super(pdfParent, xmlAttribute);
		value = xmlAttribute != null ? xmlAttribute.getValue(FXML.ATT_VALUE) : "";
	}
	
	public void dispose(){
		super.dispose();
		value = null;
	}
		
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void debug(int indentation){
		Globals.logString(getDebugIndentation(indentation)+"Label = "+value+" - ("+getDebugString()+")");
	}

	@Override
	public void write(IMSWordContainer container) {
		try{
			XWPFParagraph paragraph = container.insertParagraph();
			XWPFRun       run       = paragraph.createRun();
			applyStyleToFocWordLabel(run, paragraph);
			
//			FImageProperty imageProperty = (FImageProperty) getParent().getFocData().iFocData_getDataByPath(getXmlAttribute().getValue("name"));
//			
//			BufferedImage bufferedImage = imageProperty.getImageValue();
//			ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
//			ImageIO.write(bufferedImage, "png", imagebuffer);
//
//			InputStream inputStream = new ByteArrayInputStream(imagebuffer.toByteArray());
//		addPicture(InputStream pictureData, int pictureType, String filename, int width, int height)
//			run.addPicture(inputStream, 0, "temp", 20, 20);

			run.setText(value, 0);
		}catch(Exception e){
			Globals.logException(e);
		}
	}
	
	private void applyStyleToFocWordLabel(XWPFRun run, XWPFParagraph paragraph){
		FocXMLAttributes xmlAttributes = getXmlAttribute();
		if(xmlAttributes != null){
			String style = xmlAttributes.getValue(FXML.ATT_STYLE);
			if(style != null && !style.isEmpty()){
				StringTokenizer styleParsing = new StringTokenizer(style, ",");
				
				while(styleParsing.hasMoreTokens()){
					String styleToken = styleParsing.nextToken();
					
					if(styleToken.equals(FXML.MS_STYLE_VAL_BOLD)){
						run.setBold(true);
					}else if(styleToken.startsWith(FXML.MS_STYLE_VAL_FONT_SIZE) && !styleToken.startsWith(FXML.MS_STYLE_VAL_FONT_FAMILY)){
						String fontSize = styleToken.substring(1, styleToken.length());
						int size = 0;
						try{
							size = Integer.parseInt(fontSize);
						}catch(Exception ex){
							Globals.logException(ex);
						}
						run.setFontSize(size);
					}else if(styleToken.equals(FXML.MS_STYLE_VAL_ITALIC)){
						run.setItalic(true);
					}else if(styleToken.equals(FXML.MS_STYLE_VAL_UNDERLINE)){
						run.setUnderline(UnderlinePatterns.DASH);
					}else if(styleToken.startsWith(FXML.MS_STYLE_VAL_FONT_FAMILY)){
						String fontfamily = styleToken.substring(styleToken.indexOf("-")+1, styleToken.length());
						run.setFontFamily(fontfamily);
					}else if(styleToken.equals(FXML.MS_STYLE_VAL_ALIGN_LEFT)){
						paragraph.setAlignment(ParagraphAlignment.LEFT);
					}else if(styleToken.equals(FXML.MS_STYLE_VAL_ALIGN_RIGHT)){
						paragraph.setAlignment(ParagraphAlignment.RIGHT);
					}else if(styleToken.equals(FXML.MS_STYLE_VAL_ALIGN_CENTER)){
						paragraph.setAlignment(ParagraphAlignment.CENTER);
					}
				}
			}
		}
	}
}
