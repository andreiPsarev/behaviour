package com.fluffy.universe.services;

import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.models.Post;
import com.fluffy.universe.utils.DataSource;
import io.javalin.http.HttpCode;
import org.sql2o.Connection;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public final class PostService {
    private PostService() {}

    private static final String INSERT_POST_SQL = "INSERT INTO Post "
            + "(UserID, Title, Description, PublicationDateTime) VALUES"
            + "(:userId, :title, :description, :publicationDateTime)";
    private static final String UPDATE_POST_SQL = "UPDATE Post SET "
            + "UserID = :userId, "
            + "Title = :title, "
            + "Description = :description, "
            + "PublicationDateTime = :publicationDateTime";
    private static final String POSTS_BY_USER_ID_SQL = "SELECT * FROM Post WHERE UserID = :userId";
    private static final String POSTS_SQL = "SELECT * FROM Post";
    private static final String USER_POSTS_SQL = "SELECT "
            + "Post.ID AS `Post.ID`, "
            + "Post.Title AS `Post.Title`, "
            + "CASE WHEN LENGTH(Post.Description) > 255 THEN SUBSTR(Post.Description, 1, 255) || '...' ELSE Post.Description END AS `Post.Description`, "
            + "Post.PublicationDateTime AS `Post.PublicationDateTime`, "
            + "User.FirstName AS `User.FirstName`, "
            + "User.LastName AS `User.LastName` "
            + "FROM Post JOIN User ON Post.UserID = User.ID "
            + "ORDER BY Post.PublicationDateTime DESC "
            + "LIMIT :limit OFFSET :offset ";
    private static final String POST_COUNT_SQL = "SELECT COUNT(*) FROM Post";
    private static final String USER_POST_SQL = "SELECT "
            + "Post.ID AS `Post.ID`, "
            + "Post.Title AS `Post.Title`, "
            + "Post.Description AS `Post.Description`, "
            + "Post.PublicationDateTime AS `Post.PublicationDateTime`, "
            + "User.FirstName AS `User.FirstName`, "
            + "User.LastName AS `User.LastName` "
            + "FROM Post JOIN User ON Post.UserID = User.ID "
            + "WHERE Post.ID = :id";
    private static final String POSTS_WITH_COMMENT_COUNT_SQL = "SELECT "
            + "Post.ID, Post.Title, COUNT(Comment.ID) AS CommentCount, Post.PublicationDateTime "
            + "FROM Post "
            + "LEFT JOIN Comment ON Post.ID = Comment.PostID "
            + "GROUP BY Post.ID, Post.Title, Post.PublicationDateTime "
            + "ORDER BY Post.PublicationDateTime DESC";

    public static List<Post> getPosts() {
        try (Connection connection = DataSource.getConnection()) {
            return connection.createQuery(POSTS_SQL).executeAndFetch(Post.class);
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }

    public static int getPostCount() {
        try (Connection connection = DataSource.getConnection()) {
            return connection.createQuery(POST_COUNT_SQL).executeScalar(Integer.class);
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }

    public static List<Map<String, Object>> getUserPosts(int pageNumber, int pageSize) {
        try (Connection connection = DataSource.getConnection()) {
            return connection.createQuery(USER_POSTS_SQL)
                    .addParameter("limit", pageSize)
                    .addParameter("offset", pageSize * (pageNumber - 1))
                    .executeAndFetchTable().asList();
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }

    public static Map<String, Object> getUserPost(int id) {
        try (Connection connection = DataSource.getConnection()) {
            List<Map<String, Object>> table = connection.createQuery(USER_POST_SQL)
                    .addParameter("id", id)
                    .executeAndFetchTable().asList();
            if (table.size() == 0) {
                return null;
            }
            return table.get(0);
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }

    public static List<Map<String, Object>> getPostsWithCommentCount() {
        try (Connection connection = DataSource.getConnection()) {
            return connection.createQuery(POSTS_WITH_COMMENT_COUNT_SQL).executeAndFetchTable().asList();
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }

    public static void savePost(Post post) {
        try (Connection connection = DataSource.getConnection()) {
            if (post.getId() == null) {
                Integer id = connection.createQuery(INSERT_POST_SQL).bind(post).executeUpdate().getKey(Integer.class);
                post.setId(id);
            } else {
                connection.createQuery(UPDATE_POST_SQL).bind(post).executeUpdate();
            }
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }
}
