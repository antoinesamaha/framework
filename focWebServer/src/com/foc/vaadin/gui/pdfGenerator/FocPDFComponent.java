package com.foc.vaadin.gui.pdfGenerator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.xml.sax.helpers.DefaultHandler;

import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public abstract class FocPDFComponent extends DefaultHandler{

	public abstract void write();
	public abstract void layout(float left, float top, float maxWidth, float maxHeight);
	
	private FocPDFLayout     parentLayout = null;
	private FocXMLAttributes xmlAttribute = null;
	
	private float left        =  0;
	private float top         =  0;
	private float width       = -1;
	private float height      = -1;
	private float widthLimit  = -1;
	private float heightLimit = -1;
	
//	private String unit  = null;

	public FocPDFComponent(FocPDFLayout pdfParent, FocXMLAttributes xmlAttribute){
		this.parentLayout = pdfParent;
		setXmlAttribute(xmlAttribute);
	}
	
	public void dispose(){
		parentLayout = null;
	}
	
	public FocPDFLayout getParent(){
		return parentLayout;
	}

	/*
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	*/
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public float getTop() {
		return top;
	}

	public void setTop(float top) {
		this.top = top;
	}
	
	public float getAbsoluteLeft(){
		return getLeft() + getParent().getAbsoluteLeft();
	}
	
	public float getAbsoluteTop(){
		return getParent().getAbsoluteTop() - getTop();
	}

	public PDPageContentStream getPDPageContentStream() {
		PDPageContentStream stream = getParent() != null ? getParent().getPDPageContentStream() : null;
		return stream;
	}
	
	public PDDocument getPDDocument() {
		return getParent() != null ? getParent().getPDDocument() : null;
	}
	
	public PDPage getPDPage(){
		return getParent() != null ? getParent().getPDPage() : null;
	}

	public float getPageWidth(){
		return getPDPage().getArtBox().getWidth();
	}
	
	public float getPageHeight(){
		return getPDPage().getArtBox().getHeight();
	}

	public FocXMLAttributes getXmlAttribute() {
		return xmlAttribute;
	}

	public void setXmlAttribute(FocXMLAttributes xmlAttribute) {
		this.xmlAttribute = xmlAttribute;
	}
	
	public String getDebugString(){
		return " l="+getLeft()+"|t="+getTop()+"|w="+getWidth()+"|h="+getHeight();
	}
	
	public String getDebugIndentation(int indentation){
		String res = "";
		while(indentation > 0){
			res += " ";
			indentation--;
		}
		return res;
	}
	
	public void debug(int indentation){
		
	}
	
	public boolean hasOpenedChild(){
		return false;
	}
	
	private float getAttributeDimension(String attribute){
		float val = 0;
		String attrib = getXmlAttribute().getValue(attribute);
		if(attrib != null){
			if(attrib.endsWith("px")){
				attrib = attrib.substring(0, attrib.length() - 2);
				val = Float.valueOf(attrib);
			}else if(attrib.endsWith("%")){
				attrib = attrib.substring(0, attrib.length() - 1);
				val = -Float.valueOf(attrib);
			}
		}
		return val;
	}
	
	public float getAttributeWidth(){
		return getAttributeDimension(FXML.ATT_WIDTH);
	}
	
	public float getAttributeHeight(){
		return getAttributeDimension(FXML.ATT_HEIGHT);
	}
	public float getWidthLimit() {
		return widthLimit;
	}
	public void setWidthLimit(float widthLimit) {
		this.widthLimit = widthLimit;
	}
	public float getHeightLimit() {
		return heightLimit;
	}
	public void setHeightLimit(float heightLimit) {
		this.heightLimit = heightLimit;
	}
}

