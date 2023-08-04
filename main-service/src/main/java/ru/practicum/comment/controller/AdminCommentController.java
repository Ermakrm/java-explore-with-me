package ru.practicum.comment.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.service.CommentService;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminCommentController {
    CommentService commentService;
    CommentMapper commentMapper;

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long comId) {
        log.info("deleteCommentById id={}", comId);
        commentService.deleteComment(comId);
    }

    @GetMapping("/{comId}")
    public CommentResponseDto findCommentById(@PathVariable Long comId) {
        log.info("findCommentById id={}", comId);
        return commentMapper.toCommentResponseDto(
                commentService.findCommentById(comId)
        );
    }
}
