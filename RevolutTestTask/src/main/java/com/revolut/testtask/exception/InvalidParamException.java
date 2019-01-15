package com.revolut.testtask.exception;

public class InvalidParamException extends Exception {
	public InvalidParamException() {
		super();
	}

	public InvalidParamException(String error) {
		super(error);
	}
	public InvalidParamException(Exception e) {
		super(e);
	}
	
	public InvalidParamException(String errorDesc,Exception e) {
		super(errorDesc,e);
	}
}
