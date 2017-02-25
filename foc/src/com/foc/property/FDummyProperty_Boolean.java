package com.foc.property;


public class FDummyProperty_Boolean extends FBoolean{

	public FDummyProperty_Boolean() {
		super(null, 0, false);
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
