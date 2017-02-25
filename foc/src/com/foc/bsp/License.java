package com.foc.bsp;

import java.util.Random;

import com.foc.Globals;
import com.foc.performance.PerfManager;
import com.wibu.cm.CodeMeter;
import com.wibu.cm.CodeMeter.CMBOXENTRY;
import com.wibu.cm.CodeMeter.CMCRYPT;

public class License {
	private CMCRYPT          cmcEncrypt = null;
	private CodeMeterChecker cmChecker  = null; 
	
	public License(CodeMeterChecker cmChecker){
		this.cmChecker = cmChecker;
	}
	
	public void dispose(){
		if(cmcEncrypt != null){
			cmcEncrypt = null;
		}
		cmChecker = null;
	}
	
	protected int getEncriptedSecurityShift(){
		Random ran = new Random(System.currentTimeMillis());
		return ran.nextInt();
	}
	
	private synchronized long getHandle(){
		PerfManager.startProtection();
		long l = FocCodeMeterHandle.getInstance(cmChecker).getHandle();
		PerfManager.endProtection();
		return l;
	}

	//--------------------------------------------------
	// CERTIFIED TIME
	//--------------------------------------------------	
	
	public void updateCertifiedTime(){
		String url = "http://cmtime.codemeter.com";
		int nRet = CodeMeter.cmSetCertifiedTimeUpdate(getHandle(), url.getBytes());
		if(nRet == 0){
			Globals.logString("Error " + CodeMeter.cmGetLastErrorText() + " in Certified Time.");
		}
	}
	
	//--------------------------------------------------
	// FEATURE
	//--------------------------------------------------	
	
	public boolean hasFeature(long featureCode){
		boolean hasFeature = false;
		boolean theFeatureBitIsOn = getFeature(featureCode) * cmChecker.getProduct().getFeatureSign(featureCode) == 1;
		if(theFeatureBitIsOn){
			boolean withLicense = !cmChecker.getProduct().isFeatureWithoutLicense(featureCode);
			if(!withLicense || FocCodeMeterHandle.getInstance().isWithLicenseBooking()){
				hasFeature = getFeature(featureCode) * cmChecker.getProduct().getFeatureSign(featureCode) == 1;
			}
		}
		return  hasFeature;
	}

	public synchronized boolean isHandleOK(){
		return getHandle() > 0;
	}
	
	public synchronized int getFeature(long featureCode){
		PerfManager.startProtection();
		getHandle();
		int hasFeature = -1;
		CMBOXENTRY cmboxentry = new CMBOXENTRY();
		//Globals.logString("Before has Features");
		if(CodeMeter.cmGetInfo(getHandle(), CodeMeter.CM_GEI_ENTRYINFO, cmboxentry)){
			if((cmboxentry.setPios & CodeMeter.CM_GF_FEATUREMAP) == CodeMeter.CM_GF_FEATUREMAP){
				if((featureCode & cmboxentry.featureMap) == featureCode){
					hasFeature = 1;
				}
			}
		}
		PerfManager.endProtection();
		//Globals.logString("After has Features");		
		return hasFeature;
	}
	
	//--------------------------------------------------
	// ENCRYPT
	//--------------------------------------------------	
	
	public synchronized byte[] encrypt(byte[] source){
		PerfManager.startProtection();
		getHandle();
		byte[] encriptedSequence = source.clone();
		// set encryption parameters
		cmcEncrypt = new CMCRYPT();
		// for encryption in CmSticks (hardware encryption): use AES, calculate
		// CRC
		cmcEncrypt.cmBaseCrypt.ctrl = CodeMeter.CM_CRYPT_AES | CodeMeter.CM_CRYPT_CALCCRC;
		// random encryption code
		Random rnd = new Random(System.currentTimeMillis());
		cmcEncrypt.cmBaseCrypt.encryptionCode = rnd.nextLong();
		// some options for encryption
		if(hasFeature(cmChecker.getProduct().getTrialFeature())){
			cmcEncrypt.cmBaseCrypt.encryptionCodeOptions = CodeMeter.CM_CRYPT_UCCHECK;
		}
		// for software encryption: use AES with ECB and automatic key mechanism
		long myCtrl = CodeMeter.CM_CRYPT_AUTOKEY | CodeMeter.CM_CRYPT_AES_ENC_ECB;
		// encrypt sequence
		int nRet = CodeMeter.cmCrypt(getHandle(), myCtrl, cmcEncrypt, encriptedSequence);
		if(nRet == 0){
			encriptedSequence = null;
			Globals.logString("Error " + CodeMeter.cmGetLastErrorText() + " in CmCrypt().");
		}
		PerfManager.endProtection();

		return encriptedSequence; 
	}
	
	public synchronized byte[] decrypt(byte[] encriptedSequence){
		PerfManager.startProtection();
		getHandle();
		long myCtrl = CodeMeter.CM_CRYPT_AUTOKEY | CodeMeter.CM_CRYPT_AES_DEC_ECB;
		// remove calculation of CRC
		cmcEncrypt.cmBaseCrypt.ctrl &= ~CodeMeter.CM_CRYPT_CALCCRC;
		// add check of CRC
		cmcEncrypt.cmBaseCrypt.ctrl |= CodeMeter.CM_CRYPT_CHKCRC;
		// decrypt sequence
		byte[] decriptedSequence = encriptedSequence.clone();
		
		int nRet = CodeMeter.cmCrypt(getHandle(), myCtrl, cmcEncrypt, decriptedSequence);
		if(0 == nRet){
			Globals.logString("Error " + CodeMeter.cmGetLastErrorCode() + " - " + CodeMeter.cmGetLastErrorText());
			decriptedSequence = null;
		}
		PerfManager.endProtection();
		return decriptedSequence;
	}
}
