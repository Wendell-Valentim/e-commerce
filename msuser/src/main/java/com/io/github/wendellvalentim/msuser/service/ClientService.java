package com.io.github.wendellvalentim.msuser.service;

import com.io.github.wendellvalentim.msuser.model.Client;
import com.io.github.wendellvalentim.msuser.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public Client salvar(Client client) {
        var senhaCriptografada = passwordEncoder.encode(client.getClientSecret());
        client.setClientSecret(senhaCriptografada);
        return clientRepository.save(client);
    }

    public Client obterPorClientId(String clientId) {
        return clientRepository.findByClientId(clientId);
    }
}
