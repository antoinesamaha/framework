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
