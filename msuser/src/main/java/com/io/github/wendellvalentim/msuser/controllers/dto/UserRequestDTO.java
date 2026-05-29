package com.io.github.wendellvalentim.msuser.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @Email
        @NotNull(message = "Este campo deve ser preenchido!")
        String email,
        String name,
        String picture,
        @NotNull(message = "Este campo deve ser preenchido!")
        String password) {
}
