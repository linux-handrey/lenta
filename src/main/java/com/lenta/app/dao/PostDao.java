package com.lenta.app.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import com.lenta.app.entity.Post;

import javax.sql.RowSet;

public class PostDao {

	private Connection conn;
	private PreparedStatement pstmtGet;
	private PreparedStatement pstmtGetList;
	private PreparedStatement pstmtStore;
	private PreparedStatement pstmtUpdate;
	private PreparedStatement pstmtDestroy;

	public PostDao(Connection conn) {
		this.conn = conn;
	}


	public Post get(Long id) throws DaoException, ObjectNotFoundException {
		ResultSet rs = null;

		try {
			if (null == pstmtGet) {
				String sql = "SELECT `id`, `title`, `content`, `created_at` FROM `posts` WHERE `id` = ?";
				pstmtGet = conn.prepareStatement(sql);
			}

			pstmtGet.setLong(1, id);

			rs = pstmtGet.executeQuery();
			if (rs.next()) {
				return new Post(
					rs.getLong("id"),
					rs.getString("title"),
					rs.getString("content"),
					rs.getDate("created_at")
					);
			} else {
				throw new ObjectNotFoundException("No post found.");
			}

		} catch (SQLException e) {
			throw new DaoException("Failed to execute query.", e);
		} finally {
			if (null != rs) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// public List<Post> getList() throws DaoException, ObjectNotFoundException {
	public List<Post> getList() throws DaoException {
		ResultSet rs = null;
		
		try {
			if (null == pstmtGetList) {
				String sql = "SELECT `id`, `title`, `content`, `created_at` FROM `posts` ORDER BY `created_at` DESC";
				pstmtGetList = conn.prepareStatement(sql);
			}

			rs = pstmtGetList.executeQuery();

			ArrayList<Post> list = new ArrayList<Post>();

			while (rs.next()) {
				list.add(new Post(
						rs.getLong("id"),
						rs.getString("title"),
						rs.getString("content"),
						rs.getDate("created_at")
					)
				);
			}

			// if (0 == list.size()) {
			// 	throw new ObjectNotFoundException("No data found.");
			// }

			return list;

		} catch (SQLException e) {
			throw new DaoException("Failed to execute query.", e);
		} finally {
			if (null != rs) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	public Post store(Post post) throws DaoException {
		ResultSet rs = null;

		try {
			if (null == pstmtStore) {
				String sql = "INSERT INTO `posts`(`title`, `content`) VALUE(?, ?)";
				pstmtStore = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			}

			pstmtStore.setString(1, post.getTitle());
			pstmtStore.setString(2, post.getContent());

			if (0 == pstmtStore.executeUpdate()) {
				throw new DaoException("Failed to store post.");
			}

			rs = pstmtStore.getGeneratedKeys();

			if (rs.next()) {
				post.setId(rs.getLong(1));
				return post;
			} else {
				throw new DaoException("Failed to get inserted id.");
			}
		} catch (SQLException e) {
			throw new DaoException("Failed to execute query.", e);
		} finally {
			 if (null != rs) {
			 	try {
			 		rs.close();
				} catch (Exception e) {
			 		e.printStackTrace();
				}
			 }
		}
	}


	public void update(Post post) throws DaoException {
		try {
			if (null == pstmtUpdate) {
				String sql = "UPDATE `posts` SET `title` = ?, `content` = ? WHERE `id` = ?";
				pstmtUpdate = conn.prepareStatement(sql);
			}

			pstmtUpdate.setString(1, post.getTitle());
			pstmtUpdate.setString(2, post.getContent());
			pstmtUpdate.setLong(3, post.getId());

			if (0 == pstmtUpdate.executeUpdate()) {
				throw new DaoException("No data updated.");
			}
		} catch (SQLException e) {
			throw new DaoException("Failed to execute sql query.", e);
		}
	}


	public void destroy(Long id) throws DaoException {
		try {
			if (null == pstmtDestroy) {
				String sql = "DELETE FROM `posts` WHERE `id` = ?";
				pstmtDestroy = conn.prepareStatement(sql);
			}

			pstmtDestroy.setLong(1, id);

			if (0 == pstmtDestroy.executeUpdate()) {
				throw new DaoException("Failed to delete post.");
			}
 		} catch (SQLException e) {
 			throw new DaoException("Failed to executeQuery", e);
 		}
	}


	public void close() throws DaoException {
		Exception ex = null;

		if (null != pstmtGet) {
			try {
				pstmtGet.close();
			} catch (Exception e) {
				ex = e;
			}
		}

		if (null != pstmtGetList) {
			try {
				pstmtGetList.close();
			} catch (Exception e) {
				ex = e;
			}
		}

		if (null != pstmtStore) {
			try {
				pstmtStore.close();
			} catch (Exception e) {
				ex = e;
			}
		}

		if (null != pstmtUpdate) {
			try {
				pstmtUpdate.close();
			} catch (Exception e) {
				ex = e;
			}
		}

		if (null != pstmtDestroy) {
			try {
				pstmtDestroy.close();
			} catch (Exception e) {
				ex = e;
			}
		}

		if (null != conn) {
			try {
				conn.close();
			} catch (Exception e) {
				ex = e;
			}
		}

		if (null != ex) {
			throw new DaoException("Failed to close PostDao.", ex);
		}
	}
}