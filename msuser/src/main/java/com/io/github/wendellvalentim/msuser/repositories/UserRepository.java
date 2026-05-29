package com.io.github.wendellvalentim.msuser.repositories;

import com.io.github.wendellvalentim.msuser.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
