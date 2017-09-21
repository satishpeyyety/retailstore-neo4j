package com.myretailcompany.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorDetail {

	private String detail;

	private String developerMessage;
	private Map<String, List<ValidationError>> errors = new HashMap<>();
	private int status;
	private long timeStamp;
	private String title;
	public ErrorDetail() {
		super();

	}

	public String getDetail() {
		return detail;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public Map<String, List<ValidationError>> getErrors() {
		return errors;
	}

	public int getStatus() {
		return status;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public String getTitle() {
		return title;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public void setErrors(Map<String, List<ValidationError>> errors) {
		this.errors = errors;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
