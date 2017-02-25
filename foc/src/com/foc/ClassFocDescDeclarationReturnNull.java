package com.foc;

import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;

public class ClassFocDescDeclarationReturnNull extends ClassFocDescDeclaration{

	public ClassFocDescDeclarationReturnNull(FocModule module, Class cls) {
		super(module, cls);
	}

	@Override
	public FocDesc getFocDescription() {
		super.getFocDescription();
		return null;//Because we do not want to see the same FocDesc STorage name in the combo box, 
		//yet we want to call the getInstance to do the necessary modifications on the original FocDesc 
	}
	

}
