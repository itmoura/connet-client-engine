package com.connet.api.resource.v1;

import com.connet.api.exception.ClientException;
import com.connet.api.model.dto.ClientDTO;
import com.connet.api.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController("ClientResource")
@RequestMapping("/api/client/v1/clients")
public class ClientResource {
    @Autowired
    private ClientService clientService;

    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createClient(@RequestBody ClientDTO clientDTO) throws ClientException {
        return ResponseEntity.ok(clientService.save(clientDTO));
    }

    @PostMapping(value = "/login", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ClientDTO> login(@RequestParam String email, @RequestParam String password) throws ClientException {
        return ResponseEntity.ok(clientService.login(email, password));
    }

    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UUID> updateClient(@PathVariable("id") UUID id, @RequestBody ClientDTO clientDTO) throws ClientException {
        return ResponseEntity.ok(clientService.update(id, clientDTO));
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ClientDTO> getClient(@PathVariable("id") UUID id) throws ClientException {
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteClient(@PathVariable("id") UUID id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
