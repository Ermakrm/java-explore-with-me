package ru.practicum.comment.service;

import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentService {
    Comment saveComment(Comment comment, Long userId, Long eventId);

    Comment updateComment(Long userId, Long comId, Comment comment);

    void deleteComment(Long comId);

    Comment findCommentById(Long id);

    List<Comment> findCommentsByEventId(Long eventId, int from, int size);

    List<Comment> findCommentsByUserId(Long userId, int from, int size);

}
