package com.foc;

import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;

public interface IFocDescDeclaration {
	public static final int PRIORITY_FIRST = 1;
	public static final int PRIORITY_SECOND = 2;
	public static final int PRIORITY_THIRD = 3;
	
	public String    getName();
	public FocModule getFocModule();
	public FocDesc   getFocDescription();
	public int       getPriority();
}
