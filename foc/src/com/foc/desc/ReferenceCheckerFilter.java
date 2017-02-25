package com.foc.desc;

public interface ReferenceCheckerFilter {
	public boolean applyForThisObject(FocObject focObj);
	public void dispose();
}
