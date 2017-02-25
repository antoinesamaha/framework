package com.foc.feature;

public class FFeatureSet {
	private Object featureArray[] = null;
	
	public FFeatureSet(int size){
		featureArray = new Object[size];
	}
	
	public void dispose(){
		featureArray = null;
	}
	
	public int getInteger(int feature){
		return (Integer)featureArray[feature];
	}
	
	public void setInteger(int feature, int i){
		featureArray[feature] = i;
	}
	
	public String getString(int feature){
		return (String)featureArray[feature];
	}
	
	public void setString(int feature, String str){
		featureArray[feature] = str;
	}
	
	public boolean getBoolean(int feature){
		return (Boolean)featureArray[feature];
	}
	
	public void setBoolean(int feature, Boolean b){
		featureArray[feature] = b;
	}
	
	public double getDouble(int feature){
		return (Double)featureArray[feature];
	}
	
	public void setDouble(int feature, double d){
		featureArray[feature] = d;
	}	
}
