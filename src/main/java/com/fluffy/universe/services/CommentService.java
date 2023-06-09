package com.fluffy.universe.services;

import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.models.Comment;
import com.fluffy.universe.utils.DataSource;
import io.javalin.http.HttpCode;
import org.sql2o.Connection;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public final class CommentService {
    private CommentService() {}

    private static final String INSERT_COMMENT_SQL = "INSERT INTO Comment "
            + "(ParentID, PostID, UserID, Description, PublicationDateTime) VALUES"
            + "(:parentId, :postId, :userId, :description, :publicationDateTime)";
    private static final String UPDATE_COMMENT_SQL = "UPDATE Comment SET "
            + "ParentID = :parentId, "
            + "PostID = :postId, "
            + "Description = :description, "
            + "PublicationDateTime = :publicationDateTime";
    private static final String USER_COMMENTS_BY_POST_ID_SQL = "SELECT "
            + "Comment.ID AS `Comment.ID`, "
            + "Comment.ParentID AS `Comment.ParentID`, "
            + "Comment.Description AS `Comment.Description`, "
            + "Comment.PublicationDateTime AS `Comment.PublicationDateTime`, "
            + "User.FirstName AS `User.FirstName`, "
            + "User.LastName AS `User.LastName`, "
            + "ParentCommentUser.FirstName AS `ParentCommentUser.FirstName`, "
            + "ParentCommentUser.LastName AS `ParentCommentUser.LastName` "
            + "FROM Comment "
            + "LEFT JOIN User ON Comment.UserID = User.ID "
            + "LEFT JOIN Comment AS ParentComment ON Comment.ParentID = ParentComment.ID "
            + "LEFT JOIN User AS ParentCommentUser ON ParentComment.UserID = ParentCommentUser.ID "
            + "WHERE Comment.PostID = :postId "
            + "ORDER BY Comment.PublicationDateTime";

    public static List<Map<String, Object>> getUserCommentsByPostId(int postId) {
        try (Connection connection = DataSource.getConnection()) {
            return connection.createQuery(USER_COMMENTS_BY_POST_ID_SQL)
                    .addParameter("postId", postId).executeAndFetchTable().asList();
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }

    public static void saveComment(Comment comment) {
        try (Connection connection = DataSource.getConnection()) {
            if (comment.getId() == null) {
                int id = connection.createQuery(INSERT_COMMENT_SQL).bind(comment).executeUpdate().getKey(Integer.class);
                comment.setId(id);
            } else {
                connection.createQuery(UPDATE_COMMENT_SQL).bind(comment).executeUpdate();
            }
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }
}
