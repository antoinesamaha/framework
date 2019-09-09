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
package com.foc.web.modules.business;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.foc.Globals;
import com.foc.business.printing.PrnReportLauncher;
import com.foc.jasper.JRFocListDataSource;
import com.foc.jasper.JRFocObjectParameters;
import com.foc.util.Utils;
import com.vaadin.server.StreamResource.StreamSource;

@SuppressWarnings("serial")
public abstract class FStreamSource_Report<T extends Object> implements StreamSource {

	protected abstract void init(int reportIndex);

	protected abstract void shut(int reportIndex);

	protected abstract boolean next(int reportIndex);

	protected abstract String getReportFileName(int reportIndex);

	protected abstract JRFocObjectParameters getParams(int reportIndex);

	protected abstract JRFocListDataSource getDataSource(int reportIndex);

	private InputStream inputStream = null;
	private T data = null;
	private int reportIndex = 0;
	private String errorMessage = null;

	private int format = PrnLayout_BrowserWindowOpenerStreamResource.PDF;

	private String forcedTemplateName = "";

	public FStreamSource_Report(T data, int format) {
		this.data = data;
		this.format = format;
	}

	public void dispose() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				Globals.logException(e);
			}
			inputStream = null;
		}
		data = null;
	}

	public T getUserData() {
		return data;
	}

	public InputStream newInputStream() {
		try {
			String reportFileName = "";
			if(!Utils.isStringEmpty(getForcedTemplateName())){
				reportFileName = getForcedTemplateName();
			}else{
				reportFileName = getReportFileName(reportIndex);
			}
			URL url = Thread.currentThread().getContextClassLoader().getResource(reportFileName);
			Globals.logString("URL OK : " + (url != null ? "YES" : "NO"));
			if (url != null) {
				inputStream = url.openStream();
			}
			Globals.logString("Stream Exists After URL : " + (inputStream != null ? "YES" : "NO"));
		} catch (Exception e) {
			Globals.logException(e);
		}
		return inputStream;
	}

	protected byte[] newByteArray() {
		byte[] bytes = null;
		try {
			JasperPrint jr = null;
			reportIndex = 0;
			while (next(reportIndex)) {
				init(reportIndex);
				InputStream inputStream = newInputStream();
				JRFocObjectParameters params = getParams(reportIndex);
				JRFocListDataSource dataSource = getDataSource(reportIndex);
				if (inputStream != null) {
					JasperPrint jrCurrent = JasperFillManager.fillReport(inputStream, params, dataSource);
					if (reportIndex == 0) {
						jr = jrCurrent;
					} else {
						for (int i = 0; i < jrCurrent.getPages().size(); i++) {
							jr.addPage(jrCurrent.getPages().get(i));
						}
					}
					inputStream.close();
				}
				shut(reportIndex);
				reportIndex++;
			}

			if (jr != null) {
				if (format == PrnLayout_BrowserWindowOpenerStreamResource.DOC) {
					bytes = PrnReportLauncher.jasperPrint2ByteArray(jr, false);
				} else if (format == PrnLayout_BrowserWindowOpenerStreamResource.RTF) {
					bytes = PrnReportLauncher.jasperPrint2ByteArray(jr, true);
				} else if (format == PrnLayout_BrowserWindowOpenerStreamResource.PDF) {
					bytes = JasperExportManager.exportReportToPdf(jr);
				}
			}
		} catch (JRException e) {
			Globals.logException(e);
		} catch (IOException e) {
			Globals.logException(e);
		}
		return bytes;
	}

	public InputStream getStream() {
		byte[] byteArray = newByteArray();
		if (byteArray != null) {
			ByteArrayInputStream bytes = new ByteArrayInputStream(byteArray);
			dispose();
			return bytes;
		} else {
			dispose();
			return null;
		}
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getForcedTemplateName() {
		return forcedTemplateName;
	}

	public void setForcedTemplateName(String forcedTemplateName) {
		this.forcedTemplateName = forcedTemplateName;
	}
}
