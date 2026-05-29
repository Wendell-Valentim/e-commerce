package com.io.github.wendellvalentim.msuser.mapper;

import com.io.github.wendellvalentim.msuser.controllers.dto.UserRequestDTO;
import com.io.github.wendellvalentim.msuser.entities.UserEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sub", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserRequestDTO dto);

}
