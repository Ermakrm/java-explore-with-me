package ru.practicum.comment.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentRequestDto;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.mapper.CommentListMapper;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PrivateCommentController {
    CommentService commentService;
    CommentMapper commentMapper;
    CommentListMapper commentListMapper;

    @PatchMapping("/{comId}")
    public CommentResponseDto updateComment(
            @PathVariable Long userId,
            @PathVariable Long comId,
            @RequestBody @Valid CommentRequestDto commentRequestDto
    ) {
        log.info("updateComment userId={};comId={}comment={}", userId, comId, commentRequestDto);
        Comment comment = commentService.updateComment(
                userId, comId, commentMapper.toComment(commentRequestDto)
        );
        return commentMapper.toCommentResponseDto(comment);
    }

    @GetMapping
    public List<CommentResponseDto> getCommentsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("getCommentsByUserId userId={};from={};size{}", userId, from, size);
        List<Comment> comments = commentService.findCommentsByUserId(
                userId, from, size
        );
        return commentListMapper.toCommentResponseDtoList(comments);
    }
}
