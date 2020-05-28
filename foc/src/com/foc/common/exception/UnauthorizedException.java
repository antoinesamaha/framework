package com.foc.common.exception;

import com.foc.common.model.ErrorCode;

public class UnauthorizedException extends Exception {

	private String message;

	private ErrorCode errorCode;

	public UnauthorizedException(String message, ErrorCode errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}

	public UnauthorizedException() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

}
