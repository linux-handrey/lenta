package com.lenta.app.dao;

public class DaoException extends Exception
{
	public DaoException(String message) {
		super(message);
	}

	public DaoException(String message, Exception e) {
		super(message, e);
	}
}