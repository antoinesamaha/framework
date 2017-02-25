package com.foc.bsp;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.wibu.cm.CodeMeter;
import com.wibu.cm.CodeMeter.CMACCESS;

public class FocCodeMeterHandle {
	private long             handle             = 0;
	private CodeMeterChecker cmChecker          = null;
	private boolean          withLicenseBooking = true;
	
	public FocCodeMeterHandle(CodeMeterChecker cmChecker){
		this.cmChecker = cmChecker;
	}
	
	public void dispose(){
		release();
		cmChecker = null;
	}
	
	private boolean access(){
		boolean myReturn = true;
		CMACCESS cmacc;
		
		cmacc = new CMACCESS();
		withLicenseBooking = ConfigInfo.isWithLicenseBooking();
		cmacc.ctrl = withLicenseBooking ? CodeMeter.CM_ACCESS_USERLIMIT : CodeMeter.CM_ACCESS_NOUSERLIMIT;
		/*
		cmacc.firmCode = 100497;
		cmacc.productCode = 7778;
		cmacc.featureCode = 44;
		*/
		cmacc.firmCode    = cmChecker.getProduct().getFirmCode();
		cmacc.productCode = cmChecker.getProduct().getProductCode();
		//cmacc.featureCode = 55;
		
		//cmacc.cmBoxInfo = arrayBoxInfo[arrayBoxInfo.length - 1]; // last
		// found
		// CmSticks
		handle = CodeMeter.cmAccess(CodeMeter.CM_ACCESS_LAN_LOCAL, cmacc);
		myReturn = 0 == handle; 
		if(myReturn){
			String message = "Error " + CodeMeter.cmGetLastErrorCode() + "-" + CodeMeter.cmGetLastErrorText() + " ac";
			if(Globals.getDisplayManager() != null){
				Globals.getDisplayManager().popupMessage(message);
			}
			Globals.logString(message);
			myReturn = true;
		}else{
			//Globals.logString("Success " +" Entry found "+" in CmAccess().");			
		}
		return myReturn;
	}
	
	public synchronized void release(){
		if(handle != 0){
			CodeMeter.cmRelease(handle);
			handle = 0;
		}
	}
	
	public synchronized long getHandle(){
		if(handle == 0){
			access();
		}
		return handle;
	}
	
	public static FocCodeMeterHandle focCodeMeterHandle = null;
	public static synchronized FocCodeMeterHandle getInstance(CodeMeterChecker cmChecker){
		if(focCodeMeterHandle == null){
			focCodeMeterHandle = new FocCodeMeterHandle(cmChecker); 
		}
		return focCodeMeterHandle;
	}
	
	public static FocCodeMeterHandle getInstance(){
		return focCodeMeterHandle;
	}

	/**
	 * @return the withLicenseBooking
	 */
	public boolean isWithLicenseBooking() {
		return withLicenseBooking;
	}
}
