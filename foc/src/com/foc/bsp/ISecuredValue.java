package com.foc.bsp;

public interface ISecuredValue {
	public String getOriginalText();
	public int    getIndex1();
	public int    getIndex2();
	public void   setValue(int value);
	public int    getValue();
}
