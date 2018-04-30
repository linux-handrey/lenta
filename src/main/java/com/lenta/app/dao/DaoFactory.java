package com.lenta.app.dao;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DaoFactory {

	private DataSource dataSource;

	public PostDao getPostDao() throws DaoException {
		return new PostDao(getConnection());
	}

	public Connection getConnection() throws DaoException {
		try {
			Context ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/lentaDB");
			return dataSource.getConnection();
		} catch (NamingException e) {
			throw new DaoException("NamingException", e);
		} catch (SQLException e) {
			throw new DaoException("Failed to get connection to the database.", e);
		}
	}

	public Connection getConnection(Properties prop) throws DaoException {
		try {
			Class.forName(prop.getProperty("driverClassName"));
			return DriverManager.getConnection(prop.getProperty("url"), prop);
		} catch (ClassNotFoundException e) {
			throw new DaoException("Failed to load database driver", e);
		} catch (SQLException e) {
			throw new DaoException("Failed to get database connection", e);
		}
	}
}