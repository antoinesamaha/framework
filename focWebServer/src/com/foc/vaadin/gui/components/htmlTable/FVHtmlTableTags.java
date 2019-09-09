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
package com.foc.vaadin.gui.components.htmlTable;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.vaadin.gui.xmlForm.FXML;

public class FVHtmlTableTags {

	private Attributes attributes = null;
	private StringBuilder htmlStringBuilder = null;
	
	public FVHtmlTableTags(Attributes attributes) {
		this.attributes = attributes;
	}
	
	public void dispose(){
		htmlStringBuilder = null;
		attributes = null;
	}
	
	public void openHtmlTag(){
		getHtmlStringBuilder().append("<html>");
	}
	
	public void closeHtmlTag(){
		getHtmlStringBuilder().append("</html>");
	}
	
	public void openBodyTag(){
		getHtmlStringBuilder().append("<body>");
	}
	
	public void closeBodyTag(){
		getHtmlStringBuilder().append("</body>");
	}
	
	public void openHeadTag(){
		getHtmlStringBuilder().append("<head>");
	}
	
	public void closeHeadTag(){
		getHtmlStringBuilder().append("</head>");
	}
	
	public void openTableTag(){
		Object tableWidth  = attributes != null ? attributes.getValue(FXML.ATT_WIDTH) : "-1px";
		Object forPrinting = attributes != null ? attributes.getValue(FXML.ATT_HTML_TABLE_HIDE_OVERFLOW) : "false";
		if(forPrinting != null && forPrinting.equals("true")){
			getHtmlStringBuilder().append("<div style=\"overflow:hidden\"><table border=\"1\" style=\"border-spacing: 0px;\" width=\""+tableWidth+"\">");//cellpading=\"0\"
		}else{
			getHtmlStringBuilder().append("<div style=\"overflow-x:scroll\"><table border=\"1\" style=\"border-spacing: 0px;\" width=\""+tableWidth+"\">");//cellpading=\"0\"			
		}
	}
	
	public void closeTableTag(){
		getHtmlStringBuilder().append("</table></div>");
	}

	public void openRowTag(){
		getHtmlStringBuilder().append("<tr>");
	}
	
	public void closeRowTag(){
		getHtmlStringBuilder().append("</tr>");
	}
	
	private void openCellTag(){
		getHtmlStringBuilder().append("<td>");
	}
	
	private void closeCellTag(){
		getHtmlStringBuilder().append("</td>");
	}
	
	public void openHeaderTag(String colWidth){
//		getHtmlStringBuilder().append("<th style=\"font-weight: bold; background-color:silver;\" align=\"center\" >");
		getHtmlStringBuilder().append("<th bgcolor= \"#C0C0C0\" align=\"center\" width=\""+colWidth+"\">");
	}
	
	public void closeHeaderTag(){
		getHtmlStringBuilder().append("</th>");
	}
	
	public void createHeader(Object content, String colWidth){
		openHeaderTag(colWidth);
		getHtmlStringBuilder().append(content);
		closeHeaderTag();
	}
	
	public void createCell(Object content){
		openCellTag();
		getHtmlStringBuilder().append(content);
		closeCellTag();
	}
	
	public void openCssTag(){
		getHtmlStringBuilder().append("<style type=\"text/css\">");
	}
	
	public void closeCssTag(){
		getHtmlStringBuilder().append("</style>");
	}
	
	public void openJavaScriptTag(){
		getHtmlStringBuilder().append("<script language=\"javascript\" type=\"text/javascript\">");
	}
	
	public void closeJavaScriptTag(){
		getHtmlStringBuilder().append("</script>");
	}
	
	public StringBuilder getHtmlStringBuilder(){
		if(htmlStringBuilder == null){
			htmlStringBuilder = new StringBuilder("");
		}
		return htmlStringBuilder;
	}
	
	public ByteArrayInputStream getTableByteArrayInputStream(){
		ByteArrayInputStream byteArrayInputStream = null;
		try{
			byteArrayInputStream = new ByteArrayInputStream(getHtmlStringBuilder().toString().getBytes());
		}catch(Exception ex){
			Globals.logException(ex);
		}
		return byteArrayInputStream;
	}
	
}
