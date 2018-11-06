package com.foc.web.modules.business;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.foc.Globals;
import com.foc.jasper.JRFocListDataSource;
import com.foc.jasper.JRFocObjectParameters;
import com.vaadin.server.StreamResource.StreamSource;

@SuppressWarnings("serial")
public abstract class FStreamSource_Report<T extends Object> implements StreamSource{
	
	protected abstract void init(int reportIndex);
	protected abstract void shut(int reportIndex);
	protected abstract boolean next(int reportIndex);
	protected abstract String getReportFileName(int reportIndex);
	protected abstract JRFocObjectParameters getParams(int reportIndex);
	protected abstract JRFocListDataSource getDataSource(int reportIndex);

	private InputStream inputStream = null;
	private T data = null;
	private int reportIndex = 0;
	
	public FStreamSource_Report(T data) {
		this.data = data;
	}
	
	public void dispose(){
		if(inputStream != null){
			try{
				inputStream.close();
			}catch (IOException e){
				Globals.logException(e);
			}
			inputStream = null;
		}
		data = null;
	}
	
	public T getUserData(){
		return data;
	}

	public InputStream newInputStream(){
	  try{
	  	String reportFileName = getReportFileName(reportIndex);
	    File file = new File(reportFileName);
//	    Globals.logString("File Exists before URL : "+(file != null ? "YES" : "NO"));        
//	    if(file.exists()){
//	    	inputStream = new FileInputStream(reportFileName);
//	    }
//	    Globals.logString("Stream Exists before URL : "+(inputStream != null ? "YES" : "NO"));
//	    if(inputStream == null){
	      URL url = Thread.currentThread().getContextClassLoader().getResource(reportFileName);
	      Globals.logString("URL OK : "+(url != null ? "YES" : "NO"));
	      if(url != null){
	      	inputStream = url.openStream();  
	      }
	      Globals.logString("Stream Exists After URL : "+(inputStream != null ? "YES" : "NO"));          
//	    }
	  }catch(Exception e){
	    Globals.logException(e);
	  }
	  return inputStream; 
	}

	protected byte[] newByteArray(){
		byte[] bytes = null;
		try {
			JasperPrint jr = null;
			reportIndex = 0;
			while(next(reportIndex)){
				init(reportIndex);
				InputStream inputStream = newInputStream();
				JRFocObjectParameters params = getParams(reportIndex);
				JRFocListDataSource dataSource = getDataSource(reportIndex);
				if(inputStream != null){
					JasperPrint jrCurrent = JasperFillManager.fillReport(inputStream, params, dataSource);
					if(reportIndex == 0){
						jr = jrCurrent;
					}else{
						for(int i=0; i<jrCurrent.getPages().size(); i++){
							jr.addPage(jrCurrent.getPages().get(i));
						}
					}
					inputStream.close();
				}
				shut(reportIndex);
				reportIndex++;
			}
			
			if(jr != null) {
				bytes = JasperExportManager.exportReportToPdf(jr);
			}
//			bytes = JasperRunManager.runReportToPdf(newInputStream(), getParams(), getDataSource());
		} catch (JRException e) {
			Globals.logException(e);
		} catch (IOException e) {
			Globals.logException(e);
		}
		return bytes;
	}
	
	public InputStream getStream() {
		ByteArrayInputStream bytes = new ByteArrayInputStream(newByteArray());
		dispose();
		return bytes;
	}
}