package ru.practicum.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(uses = UserMapper.class, componentModel = SPRING)
public interface UserListMapper {
    List<UserDto> toUsersDtoList(List<User> users);
}
