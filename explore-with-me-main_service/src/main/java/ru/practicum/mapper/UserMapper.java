package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.NewUserRequest;
import ru.practicum.model.user.dto.UserDto;
import ru.practicum.model.user.dto.UserShortDto;

import java.util.Collection;

@Mapper
public interface UserMapper {

    UserDto toUserDto(User user);

    Collection<UserDto> toUserDto(Collection<User> users);

    UserShortDto toUserShortDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(NewUserRequest userRequest);

}
