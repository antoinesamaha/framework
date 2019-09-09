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
package com.foc.vaadin.gui.tableExports;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.vaadin.gui.components.BlobResource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public abstract class CSVExport
{
  private File textFile = null;
  private FileWriter fileWriter = null;
  
  protected abstract String getFileName();
  
  protected abstract void fillFile();
  
  public void init()
  {
    createFile();
    fillFile();
    closeFile();
    downloadFile();
  }
  
  public void dispose()
  {
    this.fileWriter = null;
    this.textFile = null;
  }
  
  private void createFile()
  {
    try
    {
      this.textFile = new File(getFileName() + ".txt");
      this.fileWriter = new FileWriter(this.textFile);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  private void closeFile()
  {
    FileWriter localFileWriter = getFileWriter();
    if (localFileWriter != null) {
      try
      {
        localFileWriter.flush();
        localFileWriter.close();
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }
  
  private void downloadFile()
  {
    try
    {
      File localFile = getFile();
      if (localFile != null)
      {
        byte[] arrayOfByte = new byte[(int)localFile.length()];
        FileInputStream localFileInputStream = new FileInputStream(localFile);
        localFileInputStream.read(arrayOfByte);
        localFileInputStream.close();
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
        String str = ConfigInfo.isArabic() ? ".txt" : ".csv";
        BlobResource localBlobResource = new BlobResource(new File(""), localByteArrayInputStream, getFileName() + str);
        localBlobResource.openDownloadWindow();
        if (ConfigInfo.isArabic())
        {
          OptionDialog local1 = new OptionDialog("Instructions", "The Downloaded file is a csv format file containing eventually Arabic letters.<br>To view it correctly please follow these steps: <br>1- Save it in a directory of yur choice and keep the extention to *.txt<br>2- Open Excel<br>3- Open the File from within excel<br>4-When prompted to specify the format make sure Excel is indicating the encoding \"UTF-8\"")
          {
            public boolean executeOption(String paramAnonymousString)
            {
              return false;
            }
          };
          local1.addOption("OK", "Got it");
          local1.setWidth("700px");
          local1.setHeight("300px");
          local1.popup();
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public FileWriter getFileWriter()
  {
    return this.fileWriter;
  }
  
  public File getFile()
  {
    return this.textFile;
  }
  
  public void addCellValue(Object paramObject, int paramInt)
  {
    String str = "";
    try
    {
      while (paramInt > 0)
      {
        str = str + " ";
        paramInt--;
      }
      getFileWriter().append("\"" + str + paramObject + "\",");
    }
    catch (Exception localException)
    {
      Globals.logException(localException);
    }
  }
  
  public void addCellValue(Object paramObject)
  {
    try
    {
      getFileWriter().append("\"" + paramObject + "\",");
    }
    catch (Exception localException)
    {
      Globals.logException(localException);
    }
  }
  
  public void addNewLine()
  {
    try
    {
      getFileWriter().append("\n");
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}
