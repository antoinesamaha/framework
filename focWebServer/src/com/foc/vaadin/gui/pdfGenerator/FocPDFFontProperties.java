package com.foc.vaadin.gui.pdfGenerator;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class FocPDFFontProperties {

	private PDPageContentStream contentStream = null;
	private PDType1Font         font          = null; 
	private int                 fontSize      = 0;
	private String              content       = null;
	
	
	public FocPDFFontProperties() {
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
