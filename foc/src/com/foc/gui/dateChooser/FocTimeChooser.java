package com.foc.gui.dateChooser;

import java.util.Date;
import java.util.Calendar;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import com.foc.Globals;

public class FocTimeChooser extends JSpinner {

	private JDayChooser dayChooser = null;
	
	public FocTimeChooser(){
	  super(new SpinnerDateModel(Globals.getApp().getSystemDate(), null, null, Calendar.HOUR_OF_DAY));
	  JSpinner.DateEditor de = new JSpinner.DateEditor(this, "HH:mm");
	  setEditor(de);
	}
	
	public void dispose(){
		dayChooser = null;
	}

	public void setDayChooser(JDayChooser dayChooser) {
		this.dayChooser = dayChooser;
	}
	
	public void setDate(Date date){
		getModel().setValue(date);
	}
}
