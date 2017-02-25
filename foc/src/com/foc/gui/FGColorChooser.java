package com.foc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;

import com.foc.Globals;
import com.foc.desc.field.FColorField;
import com.foc.property.FColorProperty;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;

@SuppressWarnings("serial")
public class FGColorChooser extends FGButton implements FPropertyListener{
	private FColorProperty colorProperty = null;
	private Color          color         = null;
	
	public FGColorChooser(){
		this(null);
	}
	
	public FGColorChooser(FColorProperty colorProperty){
		setProperty(colorProperty);
		addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(Globals.getDisplayManager().getMainFrame(), "Choose A Color", getColor());
				setColor(newColor);
			}
		});
		
		setPreferredSize(new Dimension(40, 25));
	}
	
	public void dispose(){
		if(colorProperty != null){
			colorProperty.removeListener(this);
		}
		colorProperty = null;
	}
	
	public void setProperty(FColorProperty property){
		if(this.colorProperty != null){
			this.colorProperty.removeListener(this);
		}
		this.colorProperty = property;
		if(this.colorProperty != null){
			this.colorProperty.addListener(this);
			setColor(this.colorProperty.getColor());
		}
	}
	
	public Color getColor(){
		Color clr = null;
		if(colorProperty != null){
			clr = colorProperty.getColor();
		}else{
			clr = color;
		}
		return clr;
	}
	
	public void setColor(Color color){
		if(colorProperty != null){
			colorProperty.setColor(color);
		}else{
			this.color = color;
		}
		setBackground(color);
	}

	@Override
	public void setBackground(Color color){
		if(color == null && colorProperty != null){
			FColorField clrFld = (FColorField) colorProperty.getFocField();
			color = clrFld.getNullColorDisplay();
		}
		super.setBackground(color);
	}
	
	@Override
	public void propertyModified(FProperty property) {
		setBackground(getColor());
	}
}
