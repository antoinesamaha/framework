package com.foc.vaadin.gui.pdfGenerator;

import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.foc.Globals;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocPDFLayout extends FocPDFComponent {

	private int                        layoutType = VERTICAL_LAYOUT;
			
	private ArrayList<FocPDFComponent> childrenArray  = null;
	private FocPDFComponent            currentChild   = null;
 
	private final static int HORIZONTAL_LAYOUT  = 0;
	private final static int VERTICAL_LAYOUT    = 1;
	
	private float marginTop    = 10;
	private float marginBottom =  0;
	private float marginRight  =  0;
	private float marginLeft   =  0;

	private float x_NewLineStartingPoint = 0;
	private int   pageNumber             = 0;
	private float x                      = 0;
	private float y                      = 0;
	
	public FocPDFLayout(FocPDFLayout pdfLayout, FocXMLAttributes attribute){
		super(pdfLayout, attribute);
	}
	
	public void dispose(){
//		if(fontProperties != null){
//			fontProperties.dispose();
//			fontProperties = null;
//		}
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
	
	public FocPDFComponent getCurrentChild(){
		return currentChild;
	}
	
	public void setCurrentChild(FocPDFComponent currentChild){
		this.currentChild = currentChild;
	}
	
	private ArrayList<FocPDFComponent> getComponentArrayList(){
		if(childrenArray == null){
			childrenArray = new ArrayList<FocPDFComponent>();
		}
		return childrenArray;
	}
	
	public int getComponentNumber(){
		return getComponentArrayList().size();
	}
	
	public FocPDFComponent getComponentAt(int at){
		return getComponentArrayList().get(at);
	}
		
	public IFocData getFocData(){
		return getParent() != null ? getParent().getFocData() : null;
	}
	
	private void addChild(FocPDFComponent newChild){
		setCurrentChild(newChild);
		getComponentArrayList().add(newChild);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(getCurrentChild() != null){
			getCurrentChild().startElement(uri, localName, qName, attributes);
		}else if(qName.equals(FXML.TAG_VERTICAL_LAYOUT)){
			addChild(new FocPDFVerticalLayout(this, (FocXMLAttributes) attributes));
		}else if(qName.equals(FXML.TAG_HORIZONTAL_LAYOUT)){
			addChild(new FocPDFHorizontalLayout(this, (FocXMLAttributes) attributes));
		}else if(qName.equals(FXML.TAG_LABEL)){
			addChild(new FocPDFLabel(this, (FocXMLAttributes) attributes));
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
	
	public boolean scan(IPDFScanner scanner) {
		boolean stop = false;
		if(scanner != null){
			for(int i = 0; i < getComponentArrayList().size() && !stop; i++){
				FocPDFComponent pdfComponent = getComponentArrayList().get(i);
				stop = scanner.before(pdfComponent);
				if(!stop){
					if(pdfComponent instanceof FocPDFLayout){
						((FocPDFLayout) pdfComponent).x = x;
						((FocPDFLayout) pdfComponent).y = y;
						((FocPDFLayout) pdfComponent).x_NewLineStartingPoint = x_NewLineStartingPoint;
						stop = ((FocPDFLayout) pdfComponent).scan(scanner);
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
	public void layout(float left, float top, float maxWidth, float maxHeight){
	}	
	
	@Override
	public void write(){
		for(int i=0; i<getComponentArrayList().size(); i++){
			FocPDFComponent pdfComp = getComponentArrayList().get(i);
			try{
				PDPageContentStream contentStream = getPDPageContentStream();
//				contentStream.moveTo(pdfComp.getAbsoluteLeft(), pdfComp.getAbsoluteTop());
//				contentStream.moveTextPositionByAmount(pdfComp.getAbsoluteLeft(), pdfComp.getAbsoluteTop());
			}catch(Exception e){
				Globals.logException(e);
			}
			pdfComp.write();
			adjustNewContentPosition(pdfComp.getHeight(), pdfComp.getWidth());
		}
	}
	
	private void adjustNewContentPosition(float contentHeight, float contentWidth){
		if(getLayoutType() == HORIZONTAL_LAYOUT){
			x = x + contentWidth;
		}else if(getLayoutType() == VERTICAL_LAYOUT){
			y -= contentHeight;
			x_NewLineStartingPoint = x;
		}
	}
	
	private void wrappTextIfNeeded(float contentWidth, float contentHeight){
		if((x + contentWidth) > getPageWidth()){
			x = x_NewLineStartingPoint;
			y -= contentHeight;
		}
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
}
