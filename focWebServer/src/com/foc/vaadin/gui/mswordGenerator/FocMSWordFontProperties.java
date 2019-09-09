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

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class FocMSWordFontProperties {

	private PDPageContentStream contentStream = null;
	private PDType1Font         font          = null; 
	private int                 fontSize      = 0;
	private String              content       = null;
	
	
	public FocMSWordFontProperties() {
	}
	
	public void dispose(){
		contentStream = null;
		font = null;
	}
	
	private int[] possibleWrapPoints(String value) {
		String text = value.trim();
    String[] split = text.split("(?<=\\W)");
    int[] ret = new int[split.length];
    ret[0]    = split[0].length();
    for(int i=1; i<split.length; i++){
    	ret[i] = ret[i-1] + split[i].length();
    }
    return ret;
	}
	
	public int getFontHeight(PDPageContentStream contentStream, PDType1Font font, int fontSize, String content) throws IOException{
		int start = 0;
		int end = 0;
		int stringHeight = 10;
		if(content != null && !content.isEmpty()){
			for(int i : possibleWrapPoints(content)) {
				float width = font.getStringWidth(content.substring(start,i)) / 1000 * fontSize;
		    if(start < end/* && width > paragraphWidth*/) {
		    	contentStream.moveTextPositionByAmount(10 , stringHeight);
		    	contentStream.drawString(toString().trim().substring(start,end));
	        stringHeight += font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
	        start = end;
		    }
		    end = i;
	    }
		}
		return stringHeight;
	}
	
	public int getFontWidth(PDPageContentStream contentStream, PDType1Font font, int fontSize, String content) throws IOException{
		int start = 0;
		int end = 0;
		int stringWidth = 10;
		if(content != null && !content.isEmpty()){
			for(int i : possibleWrapPoints(content)) {
				float width = font.getStringWidth(content.substring(start,i)) / 1000 * fontSize;
		    if(start < end/* && width > paragraphWidth*/) {
	        stringWidth += font.getStringWidth(content.substring(start,i)) / 1000 * fontSize;
	        start = end;
		    }
		    end = i;
	    }
		}
		return stringWidth;
	}

	public PDPageContentStream getContentStream() {
		return contentStream;
	}


	public void setContentStream(PDPageContentStream contentStream) {
		this.contentStream = contentStream;
	}


	public PDType1Font getFont() {
		return font;
	}


	public void setFont(PDType1Font font) {
		this.font = font;
	}


	public int getFontSize() {
		return fontSize;
	}


	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}
}
