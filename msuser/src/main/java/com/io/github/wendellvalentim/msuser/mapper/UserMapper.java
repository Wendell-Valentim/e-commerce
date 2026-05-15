package com.io.github.wendellvalentim.msuser.mapper;

import com.io.github.wendellvalentim.msuser.model.User;
import com.io.github.wendellvalentim.msuser.security.dto.IdentificacaoUsuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    IdentificacaoUsuario toDTO(User user);

}
