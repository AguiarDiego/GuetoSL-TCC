
package guetosl.service;

import guetosl.model.entity.Cliente;
import guetosl.model.repository.ClienteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(Cliente cliente, String senha) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado."
            );
        }

        cliente.setSenhaHash(passwordEncoder.encode(senha));

        return clienteRepository.save(cliente);
    }

    public Cliente login(String email, String senha) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "E-mail ou senha inválidos."
                ));

        if (cliente.getSenhaHash() == null ||
                !passwordEncoder.matches(senha, cliente.getSenhaHash())) {
            throw new IllegalArgumentException(
                    "E-mail ou senha inválidos."
            );
        }

        return cliente;
    }
}
