package com.foc.property;


public class FDummyProperty_String extends FString{

	public FDummyProperty_String() {
		super(null, 0, "");
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
