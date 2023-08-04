package ru.practicum.comment.mapper;

import org.mapstruct.Mapper;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.model.Comment;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = CommentMapper.class)
public interface CommentListMapper {
    List<CommentResponseDto> toCommentResponseDtoList(List<Comment> comments);
}
