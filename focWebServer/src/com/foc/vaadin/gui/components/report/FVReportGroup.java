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
package com.foc.vaadin.gui.components.report;

import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FVReportGroup {

	private FVReport         report        = null;
	private FocXMLAttributes headerXMLAttributes = null;
	private FocXMLAttributes footerXMLAttributes = null;
	private String headerXML = "";
	private String footerXML = "";
	
	public FVReportGroup(FVReport report) {
		this.report = report;
	}
	
	public void dispose() {
		headerXMLAttributes = null;
		report = null;
		headerXML = null;
	}

	public void setHeaderXMLAttributes(FocXMLAttributes attribs) {
		headerXMLAttributes = attribs;
	}
	
	public FocXMLAttributes getHeaderXMLAttributes() {
		return headerXMLAttributes;
	}
	
	public void setFooterXMLAttributes(FocXMLAttributes attribs) {
		footerXMLAttributes = attribs;
	}
	
	public FocXMLAttributes getFooterXMLAttributes() {
		return footerXMLAttributes;
	}
	
	public FVReport getReport() {
		return report;
	}
	
	public void setHeaderXML(String cData) {
		this.headerXML = cData;
	}

	public String getHeaderXML() {
		return headerXML;
	}

	public String getFooterXML() {
		return footerXML;
	}

	public void setFooterXML(String footerXML) {
		this.footerXML = footerXML;
	}
	
}
