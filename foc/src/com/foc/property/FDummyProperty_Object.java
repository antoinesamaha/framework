package com.foc.property;


public class FDummyProperty_Object extends FObject{

	public FDummyProperty_Object() {
		super(null, 0, null);
	}
	
	@Override
  public boolean isDummy(){
  	return true;
  }
	
	@Override
  public void addListener(FPropertyListener propListener) {
  }
	
	@Override
  public void notifyListeners() {
		super.notifyListeners();
		if(getFocObject() != null){
			getFocObject().dummyPropertyModified(this);
		}
  }
}
