package com.revolut.testtask.exception;

public class DuplicateAccountException extends Exception {
	public DuplicateAccountException() {
		super();
	}

	public DuplicateAccountException(String error) {
		super(error);
	}
	public DuplicateAccountException(Exception e) {
		super(e);
	}
	
	public DuplicateAccountException(String errorDesc,Exception e) {
		super(errorDesc,e);
	}
}
