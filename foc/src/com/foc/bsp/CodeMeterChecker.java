package com.foc.bsp;

import java.util.Random;

import com.foc.IExitListener;

public class CodeMeterChecker implements Runnable, IExitListener{
	
	private IProduct     product             = null;
	private Thread       thread              = null;
	private Random       ran                 = null; 
	private License      license             = null;
	
	private static final int MAX_CHAR          = 90 - 65;     
	private static final int MAX_NUM           = 10     ;
	
	public CodeMeterChecker(IProduct product){
		this.product = product;
		ran          = new Random(System.currentTimeMillis());
		thread       = new Thread(this);
		thread.start();
	}
	
	public void dispose(){
		//Globals.logString("Disposing CodeMeterChecker!!!");
		if(thread != null){
			thread.interrupt();
			thread = null;
		}
		product = null;
		if(license != null){
			license.dispose();
			license = null;
		}
	}

	public IProduct getProduct(){
		return product;
	}
	
	public char newChar(){
		return (char) (ran.nextInt(MAX_CHAR)+65);
	}

	public int newNum(){
		return ran.nextInt(MAX_NUM);
	}
	
	public void run(){
	}
	
	private static CodeMeterChecker codeMeterChecker = null;
	public static synchronized CodeMeterChecker getInstance(){
		return codeMeterChecker;
	}

	public static synchronized CodeMeterChecker getInstance(IProduct product){
		try{
			if(codeMeterChecker == null && product != null){
				codeMeterChecker = new CodeMeterChecker(product);
			}
		}catch(Exception e){
			codeMeterChecker = null;
		}
		return codeMeterChecker;
	}
	
	public static boolean allowAccess(){
		boolean ok = true;
		return ok;
	}
	
	public static boolean allowAccess(long feature){
		boolean ok = true;
		return ok;
	}
	
	/**
	 * Compares two arrays of bytes
	 * 
	 * @param a
	 *            first byte array
	 * @param b
	 *            second byte array
	 * @return true if arrays equal in every byte, otherwise false
	 */
	public static boolean arrayEqual(byte[] a, byte[] b) {
		int len = a.length;
		if (len > b.length)
			len = b.length;
		int i;
		for (i = 0; i < len; ++i) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean isServer(){
		return true;
	}
	
	public void replyToExit() {
		if(codeMeterChecker != null){
			codeMeterChecker.dispose();
			codeMeterChecker = null;
		}
	}
		
	public void updateCertifiedTime(){
	}
}
