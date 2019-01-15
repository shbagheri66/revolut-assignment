package com.revolut.testtask.model.dto;

public class ResultDto<T> {
	private T result;
	private String error;
	private boolean success;
	
	public ResultDto() {
	}
	
	public ResultDto(T result) {
		this.result = result;
		this.success = true;
	}
	
	public ResultDto(String error) {
		this.error = error;
		this.success = false;
	}

	public T getResult() {
		return result;
	}
	public void setResul(T result) {
		this.result = result;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
