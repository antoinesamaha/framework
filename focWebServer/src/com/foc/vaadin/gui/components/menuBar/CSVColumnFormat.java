package com.foc.vaadin.gui.components.menuBar;

public class CSVColumnFormat {
	
  public String title = "";
  public String explanation = "";
  public String sample = "";
  
  public CSVColumnFormat(String title, String explanation, String sample){
  	this.title = title;
  	this.explanation = explanation;
  	this.sample = sample;
  }
  
  public void dispose(){
  	
  }

	public String getTitle() {
		return title;
	}

	public String getExplanation() {
		return explanation;
	}

	public String getSample() {
		return sample;
	}
}
