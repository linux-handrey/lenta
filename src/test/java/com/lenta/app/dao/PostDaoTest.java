package com.lenta.app.dao;

import com.lenta.app.entity.Post;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class PostDaoTest {

    private JdbcTemplate jdbcTemplate;

    private Connection conn;
    private PostDao postDao;


    @Before
    public void setUp() {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("app.properties");

        try {
            prop.load(inputStream);
            DaoFactory dao = new DaoFactory();
            conn = dao.getConnection(prop);
            postDao = new PostDao(conn);
        } catch (IOException e) {
            System.out.println("Failed to load app properties.");
            e.printStackTrace();
        } catch (DaoException e) {
            System.out.println("Failed to get database connection");
            e.printStackTrace();
        }

        try {
            SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
            dataSource.setDriver(new com.mysql.cj.jdbc.Driver());
            dataSource.setUrl(prop.getProperty("url"));
            dataSource.setUsername(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));

            jdbcTemplate = new JdbcTemplate(dataSource);
        } catch (SQLException e) {
            System.out.println("Failed to load JdbcTemplate.");
            e.printStackTrace();
        }
    }

    @Test
    public void get() throws ObjectNotFoundException, DaoException {
        String insertSql = "INSERT INTO `posts` (`title`, `content`) VALUES(?,?)";

        Post post = new Post();
        post.setTitle("This title for get get test");
        post.setContent("This title for get get test");

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            return ps;
        }, keyHolder);

        post.setId(keyHolder.getKey().longValue());

        Post postFromDB = postDao.get(post.getId());

        Assert.assertNotNull(postFromDB.getId());
        Assert.assertEquals(post.getContent(), postFromDB.getTitle());
        Assert.assertEquals(post.getContent(), postFromDB.getTitle());
        Assert.assertNotNull(postFromDB.getCreatedAt());
    }

    @Test(expected = ObjectNotFoundException.class)
    public  void getObjectNotExists() throws ObjectNotFoundException, DaoException {
        postDao.get(-1L);
    }

    @Test
    public void getList() throws DaoException {
        List<Post> postList = postDao.getList();
        Assert.assertTrue(3 <= postList.size());

        for (Post post: postList) {
            Assert.assertNotNull(post.getId());
            Assert.assertNotNull(post.getTitle());
            Assert.assertNotNull(post.getContent());
            Assert.assertNotNull(post.getCreatedAt());
        }
    }

    @Test
    public void store() throws DaoException {
        Post post = new Post();
        post.setTitle("This is title for store test");
        post.setContent("This is content for store test");

        post = postDao.store(post);

        String sqlQuery = "SELECT id, title, content FROM posts WHERE id = ?";
        List<Post> posts = jdbcTemplate.query(sqlQuery,
                new Object[]{post.getId()},
                (rs, rowNum)-> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("content")
                )
        );

        Assert.assertEquals(1, posts.size());
        Assert.assertEquals(post.getTitle(), posts.get(0).getTitle());
        Assert.assertEquals(post.getContent(), posts.get(0).getContent());
    }

    @Test
    public void update() throws ObjectNotFoundException, DaoException, SQLException {
        Long id = 1L;

        Post post = postDao.get(id);
        post.setId(id);
        post.setTitle("Updated title");
        post.setContent("Updated content");

        conn.setAutoCommit(false);
        postDao.update(post);
        Post updatedPost = postDao.get(id);
        conn.rollback();

        Assert.assertTrue(post.equals(updatedPost));
    }

    @Test(expected = DaoException.class)
    public void updateAbsentObject() throws DaoException {
        Post post = new Post(-1L, "title", "content");
        postDao.update(post);
    }

    @Test
    public void destroy() throws SQLException, DaoException {
        Long id = 1L;

        conn.setAutoCommit(false);
        postDao.destroy(id);

        try {
            postDao.get(id);
            Assert.assertTrue(false);
        } catch (ObjectNotFoundException e) {
            Assert.assertTrue(true);
        } finally {
            conn.rollback();
        }
    }

    @Test(expected = DaoException.class)
    public void destroyAbsentObject() throws DaoException {
        postDao.destroy(-1L);
    }

    @After
    public void tearDown() throws DaoException {
        postDao.close();
    }
}