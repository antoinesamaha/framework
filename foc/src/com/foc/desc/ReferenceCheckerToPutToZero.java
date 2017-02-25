package com.foc.desc;

public class ReferenceCheckerToPutToZero {
	private ReferenceChecker 	referenceChecker 			= null;
	private FocObject 				objectToRedirecctFrom = null;
	
	public ReferenceCheckerToPutToZero(ReferenceChecker referenceChecker, FocObject objectToRedirecctFrom){
		this.referenceChecker      = referenceChecker;
		this.objectToRedirecctFrom = objectToRedirecctFrom;
	}
	
	public void dispose(){
		this.referenceChecker      = null;
		this.objectToRedirecctFrom = null;
	}
	
	/**
	 * @return the referenceChecker
	 */
	public ReferenceChecker getReferenceChecker() {
		return referenceChecker;
	}

	/**
	 * @param referenceChecker the referenceChecker to set
	 */
	public void setReferenceChecker(ReferenceChecker referenceChecker) {
		this.referenceChecker = referenceChecker;
	}

	/**
	 * @return the objectToRedirecctFrom
	 */
	public FocObject getObjectToRedirecctFrom() {
		return objectToRedirecctFrom;
	}

	/**
	 * @param objectToRedirecctFrom the objectToRedirecctFrom to set
	 */
	public void setObjectToRedirecctFrom(FocObject objectToRedirecctFrom) {
		this.objectToRedirecctFrom = objectToRedirecctFrom;
	}	
}
