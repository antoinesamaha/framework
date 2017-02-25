package com.foc.vaadin.gui.pdfGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.foc.Globals;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.ASCII;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

public class FocXmlPDFParser extends FocPDFLayout/*extends FocPDFHorizontalLayout*/{

	private ICentralPanel         centralPanel  = null;
	private IFocData              focData       = null;
	private PDDocument            document      = null;	
	private PDPage                page          = null;
	private PDPageContentStream   contentStream = null;
	private ByteArrayOutputStream byteArrayOutputStream = null;
	
	public FocXmlPDFParser(ICentralPanel centralPanel, IFocData focData) {
		super(null, null);
		this.focData      = focData;
		this.centralPanel = centralPanel;
	}
	
	public void dispose(){
		super.dispose();
		focData = null;
		centralPanel = null;
	}

	public ICentralPanel getCentralPane(){
		return centralPanel;
	}
	
	@Override
	public IFocData getFocData(){
		return focData;
	}

	@Override
	public PDDocument getPDDocument(){
		if(document == null){
			try {
				document = new PDDocument();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}
	
	@Override
	public PDPage getPDPage(){
		if(page == null){
//			page = new PDPage(PDPage.PAGE_SIZE_A4);
			page = new PDPage();
		}
		return page;
	}
	
	@Override
	public PDPageContentStream getPDPageContentStream() {
		if(contentStream == null){
			try{
				contentStream = new PDPageContentStream(getPDDocument(), getPDPage());
			}catch(Exception e){
				Globals.logException(e);
			}
		}
		return contentStream;
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
	
	public void createDocument(){
		PDDocument document = getPDDocument();
		PDPage page = getPDPage();
    document.addPage(page);
	}
	
	public void closePdf(){
		try{
			getPDPageContentStream().close();
			getPDDocument().save(getPDFOutoutStream());
			getPDDocument().close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void generatePDFFile(){
		createDocument();
		parseXmlFile();
		layout(0, 0, getPageWidth(), -1);
		debug(0);
		write();
		closePdf();
	}
	
	public ByteArrayOutputStream getPDFOutoutStream(){
		if(byteArrayOutputStream == null){
			byteArrayOutputStream = new ByteArrayOutputStream(); 
		}
		return byteArrayOutputStream;
	}
	
	@SuppressWarnings("serial")
	public class CreatePDF implements StreamSource{

		private ByteArrayOutputStream byteArrayOutputStream = null;
		
		public CreatePDF(ByteArrayOutputStream byteArrayOutputStream) {
			this.byteArrayOutputStream = byteArrayOutputStream;
		}
		
		@Override
		public InputStream getStream() {
			try{
				generatePDFFile();
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		    return byteArrayInputStream;
			}catch(Exception ex){
				Globals.logExceptionWithoutPopup(ex);
			}
			return null;
		}
		
	}

	public StreamResource getStreamResource() {
		StreamSource source = new CreatePDF(getPDFOutoutStream());
		
		String filename = "pdf"+ASCII.generateRandomString(10);
		StreamResource resource = new StreamResource(source, filename);

		resource.setMIMEType("application/pdf");
//		resource.getStream().setParameter("Content-Disposition", "attachment; filename=" + filename);
		return resource;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(PDFXmlFactory.getInstance().get(qName) != null){
			FocXMLAttributes xmlAttrib = new FocXMLAttributes((FocXMLLayout) getCentralPane(), attributes);
			super.startElement(uri, localName, qName, xmlAttrib);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(PDFXmlFactory.getInstance().get(qName) != null){
			super.endElement(uri, localName, qName);
		}
	}
	
	@Override
	public void layout(float left, float top, float maxWidth, float maxHeight){
		/*
		String widthStr =  getXmlAttribute() != null ? getXmlAttribute().getValue(FXML.ATT_WIDTH) : null;
		if(widthStr != null){
			if(widthStr.endsWith("px")){
				widthStr = widthStr.substring(0, widthStr.length() - 2);
				setWidth(Float.valueOf(widthStr));
			}else if(widthStr.endsWith("%")){
				widthStr = widthStr.substring(0, widthStr.length() - 1);
				float perc = Float.valueOf(widthStr);
				setWidth(perc * getPageWidth() / 100);
			}
		}
		*/

		//Calling the Layout of the only child
		if(getComponentNumber() > 0){
			FocPDFComponent pdfComp = getComponentAt(0);
			pdfComp.layout(left, top, maxWidth, maxHeight);
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
}
