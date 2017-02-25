package com.foc.property;

public class FDummyProperty_Double extends FDouble{

	public FDummyProperty_Double() {
		super(null, 0, 0);
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
