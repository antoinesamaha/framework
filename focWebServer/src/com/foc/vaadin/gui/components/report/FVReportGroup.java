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
