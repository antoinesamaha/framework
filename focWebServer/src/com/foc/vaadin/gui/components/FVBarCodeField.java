package com.foc.vaadin.gui.components;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.property.FProperty;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.upload.FVImageReceiver;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

@SuppressWarnings("serial")
public class FVBarCodeField extends FVImageField implements FocXMLGuiComponent, FVImageReceiver {

	private String value = null;
	
	public FVBarCodeField(FProperty property, Attributes attributes){
		super(property, attributes);
		setEditable(false);
		setValue(attributes.getValue(FXML.ATT_VALUE));
		try{
			BarcodeUtil barcodeUtil = BarcodeUtil.getInstance();
			Configuration configuration = buildCfg("Code39");
	    BarcodeGenerator barcodeGenerator = barcodeUtil.createBarcodeGenerator(configuration);
	    
			BitmapCanvasProvider bitmapCanvasProvider = new BitmapCanvasProvider(200, BufferedImage.TYPE_BYTE_BINARY, false, 0);
			barcodeGenerator.generateBarcode(bitmapCanvasProvider, getValue());
			bitmapCanvasProvider.finish();
			
			ByteArrayOutputStream byteArrayOutputStream = changetToByteArrayOutputStream(bitmapCanvasProvider);
			ByteArrayOutputStreamToStreamResource arrayOutputStreamToStreamResource = new ByteArrayOutputStreamToStreamResource(byteArrayOutputStream, ".JPG");
			
			if(getEmbedded() != null){
				getEmbedded().setSource(arrayOutputStreamToStreamResource.getStreamResource());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private ByteArrayOutputStream changetToByteArrayOutputStream(BitmapCanvasProvider canvas){
		ByteArrayOutputStream imagebuffer = null;
		try{
  		BufferedImage barcodeImage = canvas.getBufferedImage();
      imagebuffer = new ByteArrayOutputStream();
      ImageIO.write(barcodeImage, "png", imagebuffer);
		}catch(Exception ex){
			ex.printStackTrace();
		}
    return imagebuffer;
	}
	
	private Configuration buildCfg(String type) {
		DefaultConfiguration cfg = new DefaultConfiguration("barcode");

    //Bar code type
    DefaultConfiguration child = new DefaultConfiguration(type);
    cfg.addChild(child);
    
    //Human readable text position
    DefaultConfiguration attr = new DefaultConfiguration("human-readable");
    DefaultConfiguration subAttr = new DefaultConfiguration("placement");
    subAttr.setValue("bottom");
    attr.addChild(subAttr);
        
    child.addChild(attr);
    return cfg;
  }
	
	@Override
	public String getXMLType() {
		return FXML.TAG_BAR_CODE;
	}

	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	private class InputStreamToStreamSource implements StreamSource{

		private InputStream inputStream = null;
		
		public InputStreamToStreamSource(InputStream inputStream) {
			this.inputStream = inputStream;
		}
		
		public InputStream getStream() {
			return inputStream;
		}
		
	}
	
	private class ByteArrayOutputStreamToStreamResource{
		
		private ByteArrayOutputStream byteArrayOutputStream = null;
		private String                fileName              = null;
		
		public ByteArrayOutputStreamToStreamResource(ByteArrayOutputStream byteArrayOutputStream, String fileName) {
			this.byteArrayOutputStream = byteArrayOutputStream;
			this.fileName              = fileName;
		}
		
		public StreamResource getStreamResource(){
			StreamResource streamResource = null;
			if(byteArrayOutputStream != null){
				ByteArrayOutputStreamToInputStream outputStreamToInputStream = new ByteArrayOutputStreamToInputStream(byteArrayOutputStream);
				InputStream inputStream = outputStreamToInputStream.getStream();
				InputStreamToStreamSource inputStreamToStreamSource = new InputStreamToStreamSource(inputStream);
				streamResource = new StreamResource(inputStreamToStreamSource, fileName);
			}
			return streamResource;
		}
	}
	
	public class ByteArrayOutputStreamToInputStream implements StreamSource{

		private ByteArrayOutputStream byteArrayOutputStream = null;
		
		public ByteArrayOutputStreamToInputStream(ByteArrayOutputStream byteArrayOutputStream) {
			this.byteArrayOutputStream = byteArrayOutputStream;
		}

		public InputStream getStream() {
			if(byteArrayOutputStream != null){
				try{
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			    return byteArrayInputStream;
				}catch(Exception ex){
					Globals.logException(ex);
				}
			}
			return null;
		}
	}
}
