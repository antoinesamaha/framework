package com.foc.vaadin.gui.components.menuBar;

import java.io.InputStream;

public interface IUploadReader {
	public String getExplanation();
	public CSVFileFormat getFileFormat();
  public void handleUploadStream(InputStream inputStream, String fileName);
}
