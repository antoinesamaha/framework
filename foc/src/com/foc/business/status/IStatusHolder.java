package com.foc.business.status;

public interface IStatusHolder {
	public StatusHolder getStatusHolder();
	public boolean      allowSettingStatusTo(int newStatus);
	public void         afterSettingStatusTo(int newStatus);
}
