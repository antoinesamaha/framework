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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileResource;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FVDownloader extends VerticalLayout{

  //FVDownloader class not used
  
  private FVButton downloadButton = null;
  
  public FVDownloader() {
    init();
  }
  
  private void init(){
    setWidth("100%");
    setHeight("100%");
    
    /*
    downloadButton = new FVButton("Download file");
    addComponent(downloadButton);
    
    downloadButton.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        String path = getApplication().getContext().getBaseDirectory().getAbsolutePath();
        
//        StreamResource streamResource = new StreamResource(null, null, FocThreadLocal.getApplication());
//        event.getButton().getWindow().open(streamResource);
        event.getButton().getWindow().open(new FileDownloadResource(new File("c:/temp/acc_chart_tree.xml"), FocThreadLocal.getApplication()));
      }
    });
    */
  }
  
  private StreamSource getStreamSource(){
    
    StreamSource streamSource = new StreamSource(){
      public InputStream getStream() {
        try {
          ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
          
          
          return new ByteArrayInputStream(byteArray.toByteArray());
        } catch (Exception e) {
          return null;
        }
      }
      
    };
    return streamSource;
  }
  
  public class FileDownloadResource extends FileResource{

    public FileDownloadResource(File sourceFile) {
      super(sourceFile);
    }
    
    public DownloadStream getStream() {
      try {
        DownloadStream ds = new DownloadStream(new FileInputStream(getSourceFile()), getMIMEType(), getFilename());
        ds.setParameter("Content-Disposition", "attachment; filename="+getFilename());
        ds.setCacheTime(0);
        return ds;
      } catch (Exception e) {
        Globals.showNotification("Download interruption.", ""+e, IFocEnvironment.TYPE_ERROR_MESSAGE);
        return null;
      }
    }
  }
}
