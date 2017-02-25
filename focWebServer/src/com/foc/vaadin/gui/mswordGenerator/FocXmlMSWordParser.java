package com.foc.vaadin.gui.mswordGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.foc.Globals;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

public class FocXmlMSWordParser extends FocMSWordVerticalLayout/*extends FocPDFHorizontalLayout*/{

	private XWPFDocument document = null;
	
	private ICentralPanel         centralPanel  = null;
	private IFocData              focData       = null;
	private ByteArrayOutputStream byteArrayOutputStream = null;
	private boolean               rootNodeEncountered = false;
	
	public FocXmlMSWordParser(ICentralPanel centralPanel, IFocData focData) {
		super(null, null);
		this.focData      = focData;
		this.centralPanel = centralPanel;
	}
	
	public void dispose(){
		dispose_ParserAndDocument();
		
		focData = null;
		centralPanel = null;
	}
	
	public void dispose_ParserAndDocument(){
		document = null;
		
		super.dispose();
		
		rootNodeEncountered = false;
		
		if(byteArrayOutputStream != null){
			try{
				byteArrayOutputStream.close();
			}catch (IOException e){
				Globals.logException(e);
			}
			byteArrayOutputStream = null;			
		}
	}

	public FocXMLLayout getFocXMLLayout(){
		ICentralPanel centralPanel = getCentralPane();
		return centralPanel != null && centralPanel instanceof FocXMLLayout ? (FocXMLLayout) centralPanel : null;
	}
	
	public ICentralPanel getCentralPane(){
		return centralPanel;
	}
	
	@Override
	public IFocData getFocData(){
		return focData;
	}

	@Override
	public XWPFDocument getWordDocument(){
		return createDocument();
	}
	
	private XWPFDocument createDocument(){
		if(document == null){
			try {
				FocXMLLayout xmlLayout = getFocXMLLayout();
				if(xmlLayout != null && xmlLayout.isArabic()){
					document = new XWPFDocument(this.getClass().getClassLoader().getResourceAsStream("templates/rtl.dotx"));
				}else{
					document = new XWPFDocument();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}
	
	private XMLView getXMLView(){
		return getCentralPane() != null ? getCentralPane().getXMLView() : null;
	}
	
	private InputStream getXmlFileName(){
		return getXMLView() != null ? getXMLView().getXMLStream_ForView() : null;
	}
	
	private void parseXmlFile(){
		try{
			SAXParserFactory factory   = SAXParserFactory.newInstance();
			SAXParser        saxParser = factory.newSAXParser();
			
			if(getXmlFileName() != null){
				saxParser.parse(getXmlFileName(), this);
			}
		}catch(Exception ex){
			Globals.logException(ex);
		}
	}
	
	public void generateWordDocument(){
//		dispose_ParserAndDocument();		
			createDocument();
		
//		FVValidationLayout.fillWordDocument(getWordDocument());
//		parseXmlFile();
		parseXmlFile();
//		debug(0);
		MSWordWrapper wrapper = new MSWordWrapper(getWordDocument());
		write(wrapper);
		
		closeWordDocument();
	}
	
	public void closeWordDocument(){
		try {
			getWordDocument().write(getWordOutputStream());
			getWordOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ByteArrayOutputStream getWordOutputStream(){
		if(byteArrayOutputStream == null){
			byteArrayOutputStream = new ByteArrayOutputStream(); 
		}
		return byteArrayOutputStream;
	}
	
	@SuppressWarnings("serial")
	public class CreateWordDoc implements StreamSource{

		private ByteArrayOutputStream byteArrayOutputStream = null;
		
		public CreateWordDoc(ByteArrayOutputStream byteArrayOutputStream) {
			this.byteArrayOutputStream = byteArrayOutputStream;
		}
		
		@Override
		public InputStream getStream() {
			try{
				generateWordDocument();
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		    return byteArrayInputStream;
			}catch(Exception ex){
				Globals.logExceptionWithoutPopup(ex);
			}
			return null;
		}
		
	}

	public StreamResource getStreamResource() {
//		StreamSource source = new WordDocumentStreamSource("C://Users//user//Desktop//POI Word Doc Sample Table 1.docx");
		StreamSource source = new CreateWordDoc(getWordOutputStream());
		StreamResource resource = new StreamResource(source, "wordfile");
		
		resource.setMIMEType("application/msword");
		return resource;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(MSWordXmlFactory.getInstance().get(qName) != null && rootNodeEncountered){
			FocXMLAttributes xmlAttrib = new FocXMLAttributes((FocXMLLayout) getCentralPane(), attributes);
			super.startElement(uri, localName, qName, xmlAttrib);
		}
		rootNodeEncountered = true;
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(MSWordXmlFactory.getInstance().get(qName) != null){
			super.endElement(uri, localName, qName);
		}
	}
	
	public float getAbsoluteLeft(){
		return getLeft();
	}
	
	public float getAbsoluteTop(){
		return getPageHeight();
	}
	
	@Override
	public void debug(int indentation){
		Globals.logString("XML PDF Document "+getDebugString());
		super.debug(0);
	}
	
	@Override
	public void write(IMSWordContainer container) {
		XWPFDocument wordDocument = getWordDocument();
		if(wordDocument != null){
//			XWPFTable newTable = container.insertTable();
			
			for(int rowIndex=0; rowIndex<getComponentNumber(); rowIndex++){
//				XWPFTableRow  tableRow  = newTable.createRow();
//				XWPFTableCell tableCell = tableRow.addNewTableCell();
				
				MSWordWrapper wrapper = new MSWordWrapper(wordDocument);
				
				FocMSWordComponent component = getComponentAt(rowIndex);
				if(component != null){
					component.write(wrapper);
				}
			}
		}		
	}
}
