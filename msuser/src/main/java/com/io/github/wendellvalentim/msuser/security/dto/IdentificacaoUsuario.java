package com.io.github.wendellvalentim.msuser.security.dto;

import java.util.List;

public record IdentificacaoUsuario(String login,
                                   String email,
                                   List<String> roles) {
}
