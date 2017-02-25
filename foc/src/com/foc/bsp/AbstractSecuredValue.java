package com.foc.bsp;

import java.util.Random;

public abstract class AbstractSecuredValue implements ISecuredValue{

	private int     value            = 0;
	private long    lastTimeGetValue = 0;
	private Random  ran              = null;
	private License license          = null;  
	
	public AbstractSecuredValue(){
		ran = new Random(System.currentTimeMillis());
	}
	
	public void dispose(){
		ran = null;
		if(license != null){
			license.dispose();
			license = null;
		}
	}

	public License getLicense(){
		if(license == null){
			license = new License(CodeMeterChecker.getInstance()); 
		}
		return license;
	}
	
	public int getValue() {
		long curr = System.currentTimeMillis();
		if(value == 0 || lastTimeGetValue == 0 || curr-lastTimeGetValue > 30000){
			value = ran.nextInt();
			byte[] original  = getOriginalText().getBytes();
			byte[] encripted = getLicense().encrypt(original);
			byte[] decripted = getLicense().decrypt(encripted);
			if(CodeMeterChecker.arrayEqual(decripted, original)){
				String decryptedText = new String(decripted);
				String valueInString = decryptedText.substring(getIndex1(), getIndex2());
				value = Integer.valueOf(valueInString);
			}
		}
		lastTimeGetValue = curr;
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
