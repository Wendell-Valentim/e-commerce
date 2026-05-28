package com.io.github.wendellvalentim.msauth.repositories;

import com.io.github.wendellvalentim.msauth.entities.OAuthClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OAuthClientRepository  extends JpaRepository<OAuthClient, UUID> {

    Optional<OAuthClient> findByClientId(String clientId);
}
