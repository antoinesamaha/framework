package com.foc.msword;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;

import com.foc.Globals;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.vaadin.server.ClassResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileResource;

import b01.officeLink.ExtendedWordDocument;

@SuppressWarnings("serial")
public class WordTemplateFillerResource extends FileResource {

	private FocDataDictionary    dataDictionary   = null;
	private String               tempateFileName  = null;
	private String               downloadFileName = null;
	private IFocData             focData  = null;
  private ByteArrayInputStream bais     = null;
  
  public WordTemplateFillerResource(String templateFileName, String downloadFileName, IFocData focData){
  	this(templateFileName, downloadFileName, FocDataDictionary.getInstance(), focData);
  }
  
  public WordTemplateFillerResource(String templateFileName, String downloadFileName, FocDataDictionary dataDictionary, IFocData focData){
  	super(new File("fileName"));
  	this.tempateFileName  = templateFileName;
  	this.downloadFileName = downloadFileName;
  	this.dataDictionary   = dataDictionary;
  	this.focData          = focData;
  }
  
  public void dispose(){
  	dataDictionary = null;
  	tempateFileName = null;
  	focData = null;
  	if(bais != null){
  		try{
				bais.close();
			}catch (IOException e){
				Globals.logException(e);
			}
  		bais =  null;	
  	}
  }
  
  private void fillParagraph(ExtendedWordDocument xWord, XWPFParagraph para){
  	if(para != null){
  		boolean replaceDone = true;
  		while(replaceDone){
  			replaceDone = false;
  			
		  	String paragraphText = para.getParagraphText();
		  	if(			paragraphText != null 
		  			&& (paragraphText.contains("$F{") || paragraphText.contains("$P{"))
		  			){
		  		int dollarIndex = paragraphText.indexOf("$F{");
		  		int pIndex = paragraphText.indexOf("$P{");
		  		if(pIndex >=0 && (pIndex < dollarIndex || dollarIndex <0)){
		  			dollarIndex = pIndex;
		  		}
		  		
		  		if(dollarIndex >= 0){
		  			int endAcccolade = paragraphText.indexOf("}", dollarIndex);
		  			if(endAcccolade >=0){
		  				String toReplace   = paragraphText.substring(dollarIndex, endAcccolade+1);
		  				String replaceWith = dataDictionary.resolveExpression(getFocData(), toReplace, true);
		  				xWord.replaceInParagraph(para, toReplace, replaceWith);
		  				replaceDone = true;
		  			}
		  		}
		  	}
  		}
  	}
  }
  
  private void replaceCellTextStartWithIndex(ExtendedWordDocument xWord, XWPFTableRow rowToDuplicate, XWPFTableRow newRow, String propertyName, int index){
    List<XWPFTableCell> tableCells = rowToDuplicate.getTableCells();
    for (int c=0; c<tableCells.size(); c++) {
    	XWPFTableCell tarCell = newRow.getCell(c);
    	
    	for(XWPFParagraph para : tarCell.getParagraphs()){
    		boolean didReplace = true;
    		while(didReplace){
    			didReplace = xWord.replaceInParagraph(para, propertyName+"[*]", propertyName+"["+index+"]");
    		}
    		fillParagraph(xWord, para);
    	}
    }
  }
  
  private void fillTable(ExtendedWordDocument xWord, XWPFTable xwpfTable){
    List<XWPFTableRow> tableRows = xwpfTable.getRows();
    
    XWPFTableRow rowToDuplicate = null;
    String propertyName = null;
    FocList slaveList = null;
    		
    //First we check if this row is to be duplicated [*]
    for (int r=0; r<tableRows.size() && slaveList == null; r++) {
    	XWPFTableRow xwpfTableRow = tableRows.get(r);

      List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
      for (XWPFTableCell xwpfTableCell : tableCells) {
      	List<XWPFParagraph> paragraphs = xwpfTableCell.getParagraphs();
        if(paragraphs != null){
    	    for (int p=0; p<paragraphs.size() && slaveList == null; p++) {
    	    	XWPFParagraph para = paragraphs.get(p);
    	    	String paragraphText = para.getParagraphText();
    	    	int listStartIndex = paragraphText.indexOf("_LIST[*].");
    	    	int dollarIndex    = paragraphText.indexOf("$F{");
    	    	if(dollarIndex >=0 && listStartIndex > dollarIndex){
    	    		propertyName = paragraphText.substring(dollarIndex+3, listStartIndex+5);
    	    		Object res = getFocData().iFocData_getDataByPath(propertyName);
    	    		if(res instanceof FocList){
    	    			slaveList = (FocList) res;
    	    			rowToDuplicate = xwpfTableRow;
    	    		}
    	    	}
    	    }
        }
      }
    }
    
    if(slaveList != null){
    	for(int i=1; i<slaveList.size(); i++){
	      // Copying a existing table row‍ 
	      CTRow ctRow = CTRow.Factory.newInstance(); 
	      ctRow.set(rowToDuplicate.getCtRow()); 
	      XWPFTableRow newRow = new XWPFTableRow(ctRow, xwpfTable);
	      
	      replaceCellTextStartWithIndex(xWord, rowToDuplicate, newRow, propertyName, i);
	      
	      xwpfTable.addRow(newRow); 
    	}
    	
    	replaceCellTextStartWithIndex(xWord, rowToDuplicate, rowToDuplicate, propertyName, 0);
      
    }else{
	    tableRows = xwpfTable.getRows();
	    for (int r=0; r<tableRows.size(); r++) {
	    	XWPFTableRow xwpfTableRow = tableRows.get(r);
	      List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
	      for (XWPFTableCell xwpfTableCell : tableCells) {
	      	List<XWPFParagraph> paragraphs = xwpfTableCell.getParagraphs();
	        if(paragraphs != null){
	    	    for (XWPFParagraph para : paragraphs) {
	    	      fillParagraph(xWord, para);
	    	    }
	        }
	      }
	    }
    }
  }
  
  public void fill(ExtendedWordDocument xWord){
    List<XWPFParagraph> paragraphs = xWord.getXwpfDocument().getParagraphs();

    if(paragraphs != null){
	    for (XWPFParagraph para : paragraphs) {
	      fillParagraph(xWord, para);
	    }
    }
    
    List<XWPFTable> tables = xWord.getXwpfDocument().getTables();
    for (XWPFTable xwpfTable : tables) {
    	fillTable(xWord, xwpfTable);
    }
  }

  @Override
  public DownloadStream getStream() {
    DownloadStream downloadStream = null;
    try{    
	    ClassResource resource = null;
	    InputStream inputStream = null;
	    resource = new ClassResource(tempateFileName);
	    inputStream = resource.getStream().getStream();
	
	    ExtendedWordDocument xWord = new ExtendedWordDocument(inputStream);
	    if(xWord != null){
	    	fill(xWord);
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	xWord.write(baos);
	    	bais = new ByteArrayInputStream(baos.toByteArray());
	    	
	    }
	    xWord.dispose();
    }catch(Exception e){
    	Globals.logException(e);
    }
    
    if(bais != null){
    	String fileName2 = downloadFileName;
    	if(!fileName2.endsWith(".doc") && !fileName2.endsWith(".docx")){
    		fileName2 += ".docx";
    	}
    	
      downloadStream = new DownloadStream(bais, "application/x-unknown", fileName2);
      downloadStream.setParameter("Content-Disposition", "attachment; filename=" + fileName2);
      downloadStream.setCacheTime(0);  
    }
    
    return downloadStream;
  }

	public IFocData getFocData() {
		return focData;
	}
}