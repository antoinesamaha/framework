package com.foc.bsp;

public interface IProduct {
	public int     getFirmCode();
	public int     getProductCode();
	public long    getTrialFeature();
	public int     getFeatureSign(long feature);
	public boolean isFeatureWithoutLicense(long feature);
	public long    getFirstUnusedFeature();
}
