package com.foc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import com.foc.property.FEMailProperty;
import com.foc.util.EMail;

@SuppressWarnings("serial")
public class FGEMailComponent extends FPanel {
	private JComponent     jTextComp = null; 
	private FEMailProperty property  = null;
	
	public FGEMailComponent(FEMailProperty property, JComponent jTextComp){
		super();
		this.property = property;
		add(jTextComp, 0, 0);
		FGButton sendMail = new FGButton("Send");
		add(sendMail, 1, 0);

		sendMail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getProperty() != null && getProperty().getString() != null && !getProperty().getString().isEmpty()){
					EMail.sendMail(getProperty().getString(), null);
				}
			}
		});
	}
	
	public void dispose(){
		super.dispose();
		property = null;
		jTextComp = null;
	}
	
	public FEMailProperty getProperty(){
		return property;
	}
}
