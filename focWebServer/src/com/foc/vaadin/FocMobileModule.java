package com.foc.vaadin;

public abstract class FocMobileModule implements IFocMobileModule{

	private int    order = 0;
	private String name  = null;
	private String title = null;
	
	public FocMobileModule(String name, String title){
	  setName(name);
	  setTitle(title);
	}
	
	@Override
	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getPriorityInDeclaration() {
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void declareLeafMenuItems() {
	}

}
