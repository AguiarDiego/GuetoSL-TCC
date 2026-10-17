
package guetosl.controller;

import guetosl.model.entity.MensagemContato;
import guetosl.model.repository.MensagemContatoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contato")
@CrossOrigin(origins = "*")
public class MensagemContatoController {

    private final MensagemContatoRepository repository;

    public MensagemContatoController(
            MensagemContatoRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/mensagens")
    public ResponseEntity<MensagemContato> salvarMensagem(
            @RequestBody MensagemContato mensagem) {

        mensagem.setNome(mensagem.getNome().trim());
        mensagem.setEmail(mensagem.getEmail().trim());
        mensagem.setTelefone(mensagem.getTelefone().trim());
        mensagem.setMensagem(mensagem.getMensagem().trim());

        if (mensagem.getNome().isBlank()
                || mensagem.getEmail().isBlank()
                || mensagem.getTelefone().isBlank()
                || mensagem.getMensagem().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        MensagemContato salva = repository.save(mensagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping("/mensagens")
    public ResponseEntity<java.util.List<MensagemContato>> listarMensagens() {
        return ResponseEntity.ok(
                repository.findAll(
                        org.springframework.data.domain.Sort.by(
                                org.springframework.data.domain.Sort.Direction.DESC,
                                "dataEnvio"
                        )
                )
        );
    }

}
