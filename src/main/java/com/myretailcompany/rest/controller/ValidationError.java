package com.myretailcompany.rest.controller;

public class ValidationError {
	private String code;

	private String message;

	public ValidationError() {
		super();
	}
	public ValidationError(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
