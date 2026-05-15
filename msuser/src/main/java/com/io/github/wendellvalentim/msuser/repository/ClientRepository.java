package com.io.github.wendellvalentim.msuser.repository;

import com.io.github.wendellvalentim.msuser.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findByClientId(String clientId);
}
