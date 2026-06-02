package com.io.github.wendellvalentim.msuser.controllers.dto;

import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(UUID id,
                              String name,
                              String email,
                              String picture,
                              boolean emailVerified,
                              Set<String> roles
                              ) {
}
