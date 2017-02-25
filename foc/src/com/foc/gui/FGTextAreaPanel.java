package com.foc.gui;

@SuppressWarnings("serial")
public class FGTextAreaPanel extends FPanel{
	
	private FGTextArea textArea = null;
	
	public FGTextAreaPanel(FGTextArea textArea, String title){
		this.textArea = textArea;
	  FGScrollPane scroll = new FGScrollPane(textArea);
	  add(scroll);
	  setBorder(title);
	}
	
	public void dispose(){
		super.dispose();
		textArea = null;
	}
	
	public FGTextArea getTextArea(){
		return textArea;
	}
}
