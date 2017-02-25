package com.foc;

public interface IEMailSender {
	public boolean sendEMail_SelectContactAndPopupEMail(String contactFilterExpression, String attachment);
}
