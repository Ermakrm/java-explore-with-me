package ru.practicum.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.practicum.comment.dto.CommentRequestDto;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.model.Comment;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = SPRING)
public interface CommentMapper {
    Comment toComment(CommentRequestDto commentRequestDto);

    @Mapping(source = "author.name", target = "author")
    CommentResponseDto toCommentResponseDto(Comment comment);
}
