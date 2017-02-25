package com.foc.plugin;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

public interface IFocDescPlugIn {
	public void             dispose();
	public IFocObjectPlugIn newFocObjectPlugIn(FocObject focObject);
	public void             afterConstruction(FocDesc focDesc);
}
