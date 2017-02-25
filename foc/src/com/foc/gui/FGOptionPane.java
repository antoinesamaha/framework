package com.foc.gui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.foc.Globals;
import com.foc.OptionDialog;

public class FGOptionPane {
	
	private static Runnable executeAfterPopup = null;
	
	public static boolean popupOptionPane_YesNo(String title, String message){
		return popupOptionPane_YesNo(title, message, null);
	}
	
	public static boolean popupOptionPane_YesNo(String title, String message, Object[] options){ 
		boolean error = false;
		int dialogRet = popupOptionPane_Options(title, message, options);
		error = dialogRet != JOptionPane.YES_OPTION;
		return error;
	}

	public static int popupOptionPane_Options(String title, String message, Object[] options){ 
		if(executeAfterPopup != null){
			SwingUtilities.invokeLater(executeAfterPopup);
			setExecuteAfterPopup(null);
		}
		
		Globals.logString("Before popupDialog");
		int dialogRet = JOptionPane.showOptionDialog(Globals.getDisplayManager().getMainFrame(), 
				message,
				title, 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, options, null);

		Globals.logString("After popupDialog");
		
		return dialogRet;
	}
	
	public static String popupOptionPane_Options(OptionDialog optionDialog){ 
		if(executeAfterPopup != null){
			SwingUtilities.invokeLater(executeAfterPopup);
			setExecuteAfterPopup(null);
		}
		
		Globals.logString("Before popupDialog");
		String[] optionNames    = optionDialog.getOptionNamesArray();
		String[] optionCaptions = optionDialog.getOptionCaptionArray();
		int dialogRet = JOptionPane.showOptionDialog(Globals.getDisplayManager().getMainFrame(), 
				optionDialog.getMessage(),
				optionDialog.getTitle(), 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, optionCaptions, null);

		Globals.logString("After popupDialog");
		
		return optionNames[dialogRet];
	}

	public static Runnable getExecuteAfterPopup() {
		return executeAfterPopup;
	}

	public static void setExecuteAfterPopup(Runnable executeAfterPopup) {
		FGOptionPane.executeAfterPopup = executeAfterPopup;
	}
}
