package com.io.github.wendellvalentim.msuser.repositories;

import com.io.github.wendellvalentim.msuser.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByAuthority(String authority);
}
