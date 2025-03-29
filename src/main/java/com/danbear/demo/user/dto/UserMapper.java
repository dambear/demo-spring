package com.danbear.demo.user.dto;

import com.danbear.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
  UserDto toDto(User user);
  User toEntity(UserDto dto);
}