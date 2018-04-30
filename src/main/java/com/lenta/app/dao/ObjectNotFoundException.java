package com.lenta.app.dao;

public class ObjectNotFoundException extends Exception {

	public ObjectNotFoundException(String message) {
		super(message);
	}

	public ObjectNotFoundException(String message, Exception e) {
		super(message, e);
	}
}