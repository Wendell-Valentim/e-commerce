package com.io.github.wendellvalentim.msuser.mapper;

import com.io.github.wendellvalentim.msuser.controllers.dto.UserRequestDTO;
import com.io.github.wendellvalentim.msuser.controllers.dto.UserResponseDTO;
import com.io.github.wendellvalentim.msuser.entities.RoleEntity;
import com.io.github.wendellvalentim.msuser.entities.UserEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default Set<String> map(Set<RoleEntity> value) {
        if (value == null) {
            return null;
        }

        return value.stream()
                .map(RoleEntity::getAuthority)
                .collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sub", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserRequestDTO dto);

    UserResponseDTO toResponseDTO(UserEntity entity);

}
