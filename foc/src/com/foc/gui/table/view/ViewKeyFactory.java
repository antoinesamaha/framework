package com.foc.gui.table.view;

import java.util.ArrayList;

public class ViewKeyFactory {

	private ArrayList<ViewKey> viewKeyArray = null;
	
	private ViewKeyFactory(){
		viewKeyArray = new ArrayList<ViewKey>();
	}
	
	public void add(String key, String context){
		viewKeyArray.add(new ViewKey(key, context));
	}

	public int size(){
		return viewKeyArray.size();
	}

	public String getViewKey(int at){
		return viewKeyArray.get(at).key;
	}

	public String getViewContext(int at){
		return viewKeyArray.get(at).context;
	}

	private class ViewKey {
		public String key     = "";
		public String context = "";
		
		public ViewKey(String key, String context){
			this.key     = key;
			this.context = context;
		}
	}
	
	private static ViewKeyFactory factory = null;
	
	public static ViewKeyFactory getInstance(){
		if(factory == null){
			factory = new ViewKeyFactory();
		}
		return factory;
	}
}
