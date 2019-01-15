package com.revolut.testtask.exception;

public class DAOException extends Exception {
	public DAOException() {
		super();
	}
	public DAOException(Exception e) {
		super(e);
	}
	
	public DAOException(String errorDesc,Exception e) {
		super(errorDesc,e);
	}
}
