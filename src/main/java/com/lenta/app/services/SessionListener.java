package com.lenta.app.services;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.lenta.app.dao.DaoException;
import com.lenta.app.dao.DaoFactory;
import com.lenta.app.dao.PostDao;
import com.lenta.app.dao.ObjectNotFoundException;

public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		try {
			HttpSession session = event.getSession();
			DaoFactory dao = new DaoFactory();
			session.setAttribute("postDao", dao.getPostDao());
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		Object o = session.getAttribute("postDao");

		if (null == o || !(o instanceof PostDao)) {
			return ;
		}

		PostDao postDao = (PostDao)o;
		try {
			postDao.close();
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}
}