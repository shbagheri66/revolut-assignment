package com.revolut.testtask.exception;

public class InsufficientBalanceException extends Exception {
	public InsufficientBalanceException() {
		super();
	}

	public InsufficientBalanceException(String error) {
		super(error);
	}
	public InsufficientBalanceException(Exception e) {
		super(e);
	}
	
	public InsufficientBalanceException(String errorDesc,Exception e) {
		super(errorDesc,e);
	}
}
