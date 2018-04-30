package com.lenta.app.controllers;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lenta.app.dao.DaoException;
import com.lenta.app.dao.DaoFactory;
import com.lenta.app.dao.PostDao;
import com.lenta.app.dao.ObjectNotFoundException;
import com.lenta.app.entity.Post;


// @WebServlet(name="posts", urlPatterns = {"/posts", "/posts/*"})
@WebServlet(name="posts", urlPatterns = {"/posts"})
public class PostServlet extends HttpServlet {

	public static final long serialVersionUID = 1L;
	public static final String LAYOUT_FILE = "WEB-INF/layout/main.jhtml";
	public static final String TEMPLATE_DIR_PATH = "/WEB-INF/views/post/";

	public static final String URL_INDEX_PATTERN = "/lenta/posts";
	public static final String URL_CREATE_PATTERN = "/lenta/posts/create";
	public static final String URL_STORE_PATTERN = "/lenta/posts/store";
	public static final String URL_SHOW_PATTERN = "/lenta/posts/([0-9]+)";
	public static final String URL_DESTROY_PATTERN = "/lenta/posts/([0-9]+)/delete";
	public static final String URL_EDIT_PATTERN = "/lenta/posts/([0-9]+)/edit";
	public static final String URL_UPDATE_PATTERN = "/lenta/posts/([0-9]+)/update";

	private HttpSession session;
	private PostDao postDao;

	public PostServlet() {
		super();
	}

	public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		session = request.getSession();
		postDao = (PostDao)session.getAttribute("postDao");
		if (null == postDao) {
			throw new ServletException("Failed to get PostDao. Check database connection.");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		init(request, response);

		if (request.getRequestURI().matches(URL_CREATE_PATTERN)) {
			create(request, response);
		} else if (request.getRequestURI().matches(URL_SHOW_PATTERN)) {
			show(request, response);
		} else if(request.getRequestURI().matches(URL_EDIT_PATTERN)) {
			show(request, response);			
		} else if (request.getRequestURI().matches(URL_DESTROY_PATTERN)) {
			destroy(request, response);
		} else {
			getList(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		init(request, response);

		if (request.getRequestURI().matches(URL_STORE_PATTERN)) {
			store(request, response);
		} else if (request.getRequestURI().matches(URL_UPDATE_PATTERN)) {
			update(request, response);
		} else {
			throw new ServletException("No post actions found.");
		}
	}

	private void getList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setAttribute("posts", postDao.getList());
			getServletContext().getRequestDispatcher(TEMPLATE_DIR_PATH + "index.jsp").forward(request, response);
		} catch (DaoException e) {
			throw new ServletException("Failed to get data from database.", e);
		}
	}

	private void show(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long postId = null;
			String template = "show.jsp";

			if (request.getRequestURI().matches(URL_SHOW_PATTERN)) {
				postId = Long.parseLong(request.getRequestURI().replaceAll(URL_SHOW_PATTERN, "$1"));
			} else if (request.getRequestURI().matches(URL_EDIT_PATTERN)) {
				postId = Long.parseLong(request.getRequestURI().replaceAll(URL_EDIT_PATTERN, "$1"));
				template = "edit.jsp";
			} else {
				throw new ServletException("Failed to find action for show.");
			}

			request.setAttribute("post", postDao.get(postId));
			getServletContext().getRequestDispatcher(TEMPLATE_DIR_PATH + template).forward(request, response);
		} catch (DaoException e) {
			throw new ServletException("Failed to get data from database.", e);
		} catch (ObjectNotFoundException e) {
			// TODO: make redirect to 404.html
			throw new ServletException("Page not found.");
		}
	}

	private void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getRequestDispatcher(TEMPLATE_DIR_PATH + "create.jsp").forward(request, response);
	}

	private void store(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Post post = new Post();
			post.setTitle(request.getParameter("title"));
			post.setContent(request.getParameter("content"));

			postDao.store(post);

			response.sendRedirect(URL_INDEX_PATTERN);
		} catch (DaoException e) {
			throw new ServletException("Failed to store post.", e);
		}
	}

	// TODO: check for post exists before update
	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long postId = Long.parseLong(request.getRequestURI().replaceAll(URL_UPDATE_PATTERN, "$1"));

			if (null == postId || 0 == postId) {
				// TODO: redirect to 404.html
				throw new ServletException("Post title can not be empty.");
			}

			if (null == request.getParameter("title") || "" == request.getParameter("title")) {
				// TODO: make validation with flash message to edit page
				throw new ServletException("Post title can not be empty.");
			}

			Post post = new Post();
			post.setId(postId);
			post.setTitle(request.getParameter("title"));
			post.setContent(request.getParameter("content"));

			postDao.update(post);

			response.sendRedirect(URL_INDEX_PATTERN);
		} catch (DaoException e){
			throw new ServletException("Failed to update data in database.", e);
		}
	}

	// TODO: check for post exists before destroy
	private void destroy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long postId = Long.parseLong(request.getRequestURI().replaceAll(URL_DESTROY_PATTERN, "$1"));

			if (null == postId || 1 > postId) {
				throw new ServletException("Invalid post id: " + request.getRequestURI().replaceAll(URL_DESTROY_PATTERN, "$1"));
			}

			DaoFactory dao = new DaoFactory();
			postDao = dao.getPostDao();

			postDao.destroy(postId);

			response.sendRedirect(URL_INDEX_PATTERN);
		} catch (DaoException e) {
			throw new ServletException("Failed to delete post.", e);
		}
	}
}
