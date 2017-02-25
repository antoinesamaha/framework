package com.foc.util.stringMatch;

import com.foc.desc.FocObject;

public class StringMatchMakingLine {
	private FocObject        focObj    = null;
	private int              nbrWords  = 0;
	private IMatchTextObject matchText = null;
	private boolean          selected  = false;
	
	public StringMatchMakingLine(FocObject focObj, IMatchTextObject matchTextObj, int nbrWords){
		this.focObj      = focObj;
		this.matchText   = matchTextObj;
		this.nbrWords    = nbrWords;
	}
	
	public void dispose(){
		this.focObj    = null;
		this.matchText = null;
		this.nbrWords  = 0;
	}

	public FocObject getObj() {
		return focObj;
	}

	public int getNbrWords() {
		return nbrWords;
	}

	public String getText() {
		IMatchTextObject iMatchText = getMatchTextObject();
		return iMatchText.getMatchText();
	}
	
	public IMatchTextObject getMatchTextObject() {
		return matchText;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
