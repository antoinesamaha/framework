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
package com.foc.web.unitTesting.methods;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.mail.FocMailSender;
import com.foc.util.FocFileUtil;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVUploadLayout_Form;
import com.foc.web.unitTesting.FXMLUnit;
import com.foc.web.unitTesting.FocUnitTestingCommand;
import com.foc.web.unitTesting.FocUnitXMLAttributes;
import com.foc.web.unitTesting.IFUnitMethod;
import com.vaadin.ui.Window;

public class FUploadFile_UnitMethod implements IFUnitMethod {
	@Override
	public void executeMethod(FocUnitTestingCommand command, FocUnitXMLAttributes attributes) throws Exception {
		if(attributes != null){
			String filename = attributes.getValue(FXMLUnit.ATT_FILE_NAME);
			if(filename == null){
				command.getLogger().addFailure("No File name attribute! "+"["+FXMLUnit.ATT_FILE_NAME+"]");
			}else if(filename.isEmpty()){
				command.getLogger().addFailure("File name attribute ["+FXMLUnit.ATT_FILE_NAME+"] is Empty String");
			}else{
				ICentralPanel centralPanel = command.getMainWindow().getCentralPanel();
//				if(!(centralPanel instanceof FVUploadLayout_Form)){
//					command.getLogger().addFailure("Expected Upload Form to be visible! FVUploadLayout_Form");
//				}else{
					Collection<Window> coll = command.getMainWindow().getFocWebApplication().getWindows();
					Iterator<Window> iter = coll.iterator();
					while(iter.hasNext()){
						Window window = iter.next();
						FocCentralPanel focCP = (FocCentralPanel) window.getContent();
						ICentralPanel cp = focCP.getCentralPanel();
						if(cp instanceof FVUploadLayout_Form){
							FVUploadLayout_Form uploadForm = (FVUploadLayout_Form) cp;
							if(uploadForm != null){
								try{
									InputStream fis = Globals.getInputStream(filename);
//									FileInputStream fis = new FileInputStream(filename);
									ByteArrayOutputStream baos = FocFileUtil.inputStreamToByteArrayOutputStream(fis);
									uploadForm.setOutputStream_ForUnitTesting(baos);
									uploadForm.uploadFinished(filename);
								}catch (Exception e){
									Globals.logException(e);
								}
							}

						}
					}
					
//				}
			}
		}
	}
}
