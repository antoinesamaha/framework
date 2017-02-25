package com.foc.vaadin.gui.tableExports;

import com.foc.Globals;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.BlobResource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class EXCELExport
{
  private File textFile = null;
  private XSSFWorkbook workbook = null;
  private XSSFSheet sheet = null;
  private int rowCount = 0;
  private int columnCount = 0;
  private XSSFRow row = null;
  private XSSFCell cell = null;
  
  protected abstract String getFileName();
  
  protected abstract void fillFile();
  
  public void init()
  {
    createFile();
    fillFile();
    downloadFile();
  }
  
  public void dispose()
  {
    this.textFile = null;
  }
  
  private void createFile()
  {
    try
    {
      this.textFile = new File(getFileName() + ".xlsx");
      FileWriter localFileWriter = new FileWriter(this.textFile);
      this.workbook = new XSSFWorkbook();
      this.sheet = this.workbook.createSheet(getFileName());
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void setRightToLeft(boolean r2l){ 
	  if(this.sheet != null){
	  	this.sheet.setRightToLeft(r2l);
	  }
  }
  
  private void downloadFile()
  {
    try
    {
      File localFile = getFile();
      if (localFile != null)
      {
        FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
        this.workbook.write(localFileOutputStream);
        byte[] arrayOfByte = new byte[(int)localFile.length()];
        FileInputStream localFileInputStream = new FileInputStream(localFile);
        localFileInputStream.read(arrayOfByte);
        localFileInputStream.close();
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
        String str = ".xlsx";
        BlobResource localBlobResource = new BlobResource(new File(""), localByteArrayInputStream, getFileName() + str);
        localBlobResource.openDownloadWindow();
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
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
      addCellValue("\"" + str + paramObject + "\"");
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
      XSSFCell localXSSFCell = this.row.createCell(this.columnCount++);
      if ((paramObject instanceof String)) {
        if (Utils.isNumeric((String)paramObject))
        {
          double d = Double.parseDouble((String)paramObject);
          localXSSFCell.setCellValue(d);
        }
        else
        {
          localXSSFCell.setCellValue((String)paramObject);
        }
      }
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
      if (this.sheet != null)
      {
        this.row = this.sheet.createRow(this.rowCount++);
        this.columnCount = 0;
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}
