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
