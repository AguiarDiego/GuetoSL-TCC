
package guetosl.controller;

import guetosl.model.entity.Cliente;
import guetosl.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody CadastroRequest dados) {
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(dados.nome());
            cliente.setEmail(dados.email());
            cliente.setTelefone(dados.telefone());

            Cliente clienteSalvo =
                    clienteService.cadastrar(cliente, dados.senha());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new CadastroResponse(
                            clienteSalvo.getId(),
                            clienteSalvo.getNome(),
                            clienteSalvo.getEmail(),
                            clienteSalvo.getTelefone()
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dados) {
        try {
            Cliente cliente = clienteService.login(
                    dados.email(),
                    dados.senha()
            );

            return ResponseEntity.ok(
                    new CadastroResponse(
                            cliente.getId(),
                            cliente.getNome(),
                            cliente.getEmail(),
                            cliente.getTelefone()
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("E-mail ou senha inválidos.");
        }
    }

    public record CadastroRequest(
            String nome,
            String email,
            String telefone,
            String senha
    ) {}

    public record LoginRequest(
            String email,
            String senha
    ) {}

    public record CadastroResponse(
            Integer id,
            String nome,
            String email,
            String telefone
    ) {}
}
