package com.foc.business.workflow.signing;

import com.foc.business.workflow.WFTitle;
import com.foc.business.workflow.map.WFSignature;

public class WFSignatureNeededResult {
	private int         titleIndex = -1;
	private boolean     onBehalfOf = false;
	private WFSignature signature  = null;
	
	public WFSignatureNeededResult(int titleIndex, boolean onBehalfOf){
		this.titleIndex = titleIndex;
		this.onBehalfOf = onBehalfOf; 
	}
	
	public int getTitleIndex(){
		return titleIndex;
	}
	
	public boolean isOnBehalfOf(){
		return onBehalfOf;
	}

	public void setTitleIndex(int titleIndex) {
		this.titleIndex = titleIndex;
	}

	public void setOnBehalfOf(boolean onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}
	
	public WFSignature getSignature(){
		return signature;
	}
	
	public void setSignature(WFSignature signature){
		this.signature = signature;
	}
	
	public WFTitle getTitle(){
		return (getSignature() != null && getTitleIndex() >= 0) ? getSignature().getTitle(getTitleIndex()) : null;
	}
}
