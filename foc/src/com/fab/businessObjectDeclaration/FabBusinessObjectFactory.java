package com.fab.businessObjectDeclaration;

import java.util.ArrayList;

public class FabBusinessObjectFactory {
	
	private ArrayList<IFabBusinessObjectDeclaration> declarations = null; 
	
	public FabBusinessObjectFactory(){
		declarations = new ArrayList<IFabBusinessObjectDeclaration>();
	}

	public void add(IFabBusinessObjectDeclaration dec){
		declarations.add(dec);
	}

	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	
	static FabBusinessObjectFactory fabBusinessObjectFactory = null; 

	public static FabBusinessObjectFactory getInstance(){
		if(fabBusinessObjectFactory == null){
			fabBusinessObjectFactory = new FabBusinessObjectFactory();
		}
		return fabBusinessObjectFactory; 
	}
}
