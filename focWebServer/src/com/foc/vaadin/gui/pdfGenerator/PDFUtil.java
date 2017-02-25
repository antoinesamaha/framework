package com.foc.vaadin.gui.pdfGenerator;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFUtil {

	public static int getStringHeight(PDType1Font font, int fontSize, String content) {
		int stringHeight = (int) (font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize);
		return stringHeight;
	}
	
	public static float getStringWidth(PDType1Font font, int fontSize, String content){
    float stringWidth = 0;
		try {
			stringWidth = font.getStringWidth(content) / 1000 * fontSize;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringWidth;
	}
	
}
