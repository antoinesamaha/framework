package com.foc.pivot;

import com.foc.desc.FocObject;

public interface IFocObjectDebug<O extends FocObject> {
	public void debug(O focObject, StringBuffer buffer);
}
