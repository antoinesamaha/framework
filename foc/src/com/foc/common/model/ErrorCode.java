package com.foc.common.model;

public enum ErrorCode {

	MISSING_DATA("missing_data"),INVALID_DATA("invalid_data"),CONFIGURATION_ERROR("server_configuration_error"),INVALID_USERNAME_PHONE("invalid_username_phone");
	
	ErrorCode(String code) {
		this.code=code;
	}

	private String code;

	public String getCode() {
		return code;
	}


	
}
