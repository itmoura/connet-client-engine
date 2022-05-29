package com.connet.api.service;

import com.connet.api.exception.ClientException;
import com.connet.api.model.dto.ClientDTO;
import com.connet.api.model.entity.Client;
import com.connet.api.repository.ClientRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ResourceHttpMessageWriter;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.connet.api.model.entity.Client.convert;

@Service
@Log4j2
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(@Autowired ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDTO save(ClientDTO clientDTO) throws ClientException {
        Optional<Client> byEmail = clientRepository.findByEmail(clientDTO.getEmail());
        if(byEmail.isPresent()) {
            throw new ClientException("Email already registered");
        }
        Client client = convert(clientDTO);
        client.setPassword(passwordEncoder(clientDTO.getPassword()));
        return ClientDTO.convert(clientRepository.save(client));
    }

    public ClientDTO getClient(UUID id) throws ClientException {
        return clientRepository.findById(id).map(ClientDTO::convert).orElseThrow(() -> new ClientException("Client Not Found"));
    }

    public void delete(UUID id) {
        clientRepository.deleteById(id);
    }

    public UUID update(UUID id, ClientDTO clientDTO) throws ClientException{
        ClientDTO c = ClientDTO.convert(clientRepository.findById(id).orElseThrow(() -> new ClientException("Client Not Found")));
        clientDTO.setClientId(c.getClientId());
        clientDTO.setEmail(c.getEmail());
        Client client = convert(clientDTO);
        return clientRepository.save(client).getClientId();
    }

    public ClientDTO login(String email, String password) throws ClientException {
        ClientDTO c = ClientDTO.convert(clientRepository.findByEmail(email).orElseThrow(() -> new ClientException("Invalid Email")));
        if(passwordDecoder(c.getPassword()).equals(password)) {
            return c;
        }
        throw new ClientException("Invalid Password");
    }

    public static String passwordEncoder(String password) {
        return new Base64().encodeToString(password.getBytes());
    }

    public static String passwordDecoder(String passwordCripto) {
        return new String(new Base64().decode(passwordCripto));
    }
}
