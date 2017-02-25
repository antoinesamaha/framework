package com.foc.vaadin.gui.mswordGenerator;

import java.util.ArrayList;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTable.XWPFBorderType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocMSWordLayout extends FocMSWordComponent {

	private int                        layoutType = VERTICAL_LAYOUT;
			
	private ArrayList<FocMSWordComponent> childrenArray  = null;
	private FocMSWordComponent            currentChild   = null;
 
	public final static int HORIZONTAL_LAYOUT = 0;
	public final static int VERTICAL_LAYOUT   = 1;
	
	private float marginTop    = 10;
	private float marginBottom =  0;
	private float marginRight  =  0;
	private float marginLeft   =  0;

	private float x_NewLineStartingPoint = 0;
	private int   pageNumber             = 0;
	private float x                      = 0;
	private float y                      = 0;
	
	public FocMSWordLayout(FocMSWordLayout pdfLayout, FocXMLAttributes attribute){
		super(pdfLayout, attribute);
	}
	
	public void dispose(){
		if(childrenArray != null){
			for(int i=0; i<childrenArray.size(); i++){
				childrenArray.get(i).dispose();
			}
			childrenArray.clear();
			childrenArray = null;			
		}
		currentChild  = null;
		super.dispose();
	}
	
	public FocMSWordComponent getCurrentChild(){
		return currentChild;
	}
	
	public void setCurrentChild(FocMSWordComponent currentChild){
		this.currentChild = currentChild;
	}
	
	private ArrayList<FocMSWordComponent> getComponentArrayList(){
		if(childrenArray == null){
			childrenArray = new ArrayList<FocMSWordComponent>();
		}
		return childrenArray;
	}
	
	public int getComponentNumber(){
		return getComponentArrayList().size();
	}
	
	public FocMSWordComponent getComponentAt(int at){
		return getComponentArrayList().get(at);
	}
		
	public IFocData getFocData(){
		return getParent() != null ? getParent().getFocData() : null;
	}
	
	private void addChild(FocMSWordComponent newChild){
		setCurrentChild(newChild);
		getComponentArrayList().add(newChild);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(getCurrentChild() != null){
			getCurrentChild().startElement(uri, localName, qName, attributes);
		}else if(qName.equals(FXML.TAG_VERTICAL_LAYOUT)){
			addChild(new FocMSWordVerticalLayout(this, (FocXMLAttributes) attributes));
		}else if(qName.equals(FXML.TAG_HORIZONTAL_LAYOUT)){
			addChild(new FocMSWordHorizontalLayout(this, (FocXMLAttributes) attributes));
		}else if(qName.equals(FXML.TAG_LABEL)){
			addChild(new FocMSWordLabel(this, (FocXMLAttributes) attributes));
		}else if(qName.equals(FXML.TAG_TABLE)){
			addChild(new FocMSWordTable(this, (FocXMLAttributes) attributes));
		}else if(qName.equals(FXML.TAG_TABLE_COLUMN)){
			addChild(new FocMSWordTableColumn(this, (FocXMLAttributes) attributes));
		}else if(qName.equals(FXML.TAG_INCLUDE_XML)){
//			addChild(new FocMSWordIncludeXML(this, (FocXMLAttributes) attributes));
		}else if(qName.equals(FXML.TAG_FIELD)){
			addChild(new FocMSWordLabel(this, (FocXMLAttributes) attributes));
		}else{
			IFocData focData = getFocData();
			
			String fldName   = attributes.getValue(FXML.ATT_NAME);
			String dataPath  = attributes.getValue(FXML.ATT_DATA_PATH);
			
			if(dataPath == null){
				dataPath = fldName;
			}
			
			if(focData != null && dataPath != null){
				IFocData data = focData.iFocData_getDataByPath(dataPath);
				if(data != null){

				}
			}
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(getCurrentChild() != null){
			if(getCurrentChild().hasOpenedChild()){
				getCurrentChild().endElement(uri, localName, qName);
			}else{
				setCurrentChild(null);
			}
		}
	}
	
	public boolean scan(IMSWordScanner scanner) {
		boolean stop = false;
		if(scanner != null){
			for(int i = 0; i < getComponentArrayList().size() && !stop; i++){
				FocMSWordComponent pdfComponent = getComponentArrayList().get(i);
				stop = scanner.before(pdfComponent);
				if(!stop){
					if(pdfComponent instanceof FocMSWordLayout){
						((FocMSWordLayout) pdfComponent).x = x;
						((FocMSWordLayout) pdfComponent).y = y;
						((FocMSWordLayout) pdfComponent).x_NewLineStartingPoint = x_NewLineStartingPoint;
						stop = ((FocMSWordLayout) pdfComponent).scan(scanner);
					}
					if(!stop){
						stop = scanner.after(pdfComponent);
					}
				}
			}
		}
		return stop;
	}
	
	@Override
	public void write(IMSWordContainer container){
	}
	
	public float getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}

	public float getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(float marginBottom) {
		this.marginBottom = marginBottom;
	}

	public float getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}

	public float getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}

	public int getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}
	
	public void debug(int indentation){
		indentation += 2;
		for(int i=0; i<getComponentNumber(); i++){
			getComponentAt(i).debug(indentation);
		}
	}
	
	public boolean hasOpenedChild(){
		return getCurrentChild() != null;
	}
	
	public void applyBorderAttribute(XWPFTable newTable){
		String borderAtt = getXmlAttribute().getValue(FXML.ATT_BORDER);

		if(borderAtt != null && (borderAtt.toLowerCase().equals("true") || borderAtt.equals("1"))){
			
		}else{
			newTable.getCTTbl().getTblPr().unsetTblBorders();
			newTable.setInsideHBorder(XWPFBorderType.NONE, 0, 0, null);
			newTable.setInsideVBorder(XWPFBorderType.NONE, 0, 0, null);
			newTable.setRowBandSize(200);
		}
		
		newTable.setCellMargins(0, 0, 0, 0);
	}
}
