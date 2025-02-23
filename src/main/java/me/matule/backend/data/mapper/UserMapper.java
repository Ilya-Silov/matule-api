package me.matule.backend.data.mapper;

import me.matule.backend.data.dto.UserDto;
import me.matule.backend.data.dto.request.UserCreateRequest;
import me.matule.backend.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);
    User toEntity(UserCreateRequest userDto);

    UserDto toUserDto(User user);

    User updateWithNull(UserDto userDto, @MappingTarget User user);
}