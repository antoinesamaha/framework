package com.foc.vaadin.gui.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.property.FProperty;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.upload.FVImageReceiver;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

@SuppressWarnings("serial")
public class FVQrCodeField extends FVImageField implements FocXMLGuiComponent, FVImageReceiver {

	private String                value              = null;

	
	public FVQrCodeField(FProperty property, Attributes attributes){
		super(property, attributes);
		setEditable(false);
		setValue(attributes.getValue(FXML.ATT_VALUE));
		ByteArrayOutputStream out = QRCode.from(getValue()).to(ImageType.PNG).stream();
		 
		 ByteArrayOutputStreamToStreamResource streamResource = new ByteArrayOutputStreamToStreamResource(out, getValue() + ".JPG");
		 
		 FVImageField imageField = this;
		 if(imageField != null){
			 imageField.setEditable(false);
			 imageField.getEmbedded().setSource(streamResource.getStreamResource());
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
	
	@Override
	public String getXMLType() {
		return FXML.TAG_QR_CODE;
	}

	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	public class InputStreamToStreamSource implements StreamSource{

		private InputStream inputStream = null;
		
		public InputStreamToStreamSource(InputStream inputStream) {
			this.inputStream = inputStream;
		}
		
		public InputStream getStream() {
			return inputStream;
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
