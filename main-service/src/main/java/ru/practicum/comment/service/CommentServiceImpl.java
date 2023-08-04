package ru.practicum.comment.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.IllegalActionException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    UserService userService;
    EventService eventService;

    @Override
    public Comment saveComment(Comment comment, Long userId, Long eventId) {
        User author = userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new IllegalActionException("You can only comment on published events");
        }
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setPublishDate(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long userId, Long comId, Comment newComment) {
        Comment comment = findCommentById(comId);
        if (!Objects.equals(userId, comment.getAuthor().getId())) {
            throw new IllegalActionException("The comment can only be updated by the owner");
        }
        comment.setText(newComment.getText());
        comment.setUpdateDate(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long comId) {
        existCommentById(comId);
        commentRepository.deleteById(comId);
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Comment with id=%d not found", id))
        );
    }

    @Override
    public List<Comment> findCommentsByEventId(Long eventId, int from, int size) {
        return commentRepository.findAllByEventIdOrderByPublishDate(eventId, PageRequest.of(from / size, size));
    }

    @Override
    public List<Comment> findCommentsByUserId(Long userId, int from, int size) {
        return commentRepository.findAllByAuthorIdOrderByPublishDate(userId, PageRequest.of(from / size, size));
    }

    private void existCommentById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ObjectNotFoundException(String.format("Comment with id=%d not found", id));
        }
    }
}
