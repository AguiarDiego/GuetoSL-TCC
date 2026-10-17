
package guetosl.controller;

import guetosl.model.entity.Imagem;
import guetosl.service.ImagemService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/imagens")
public class ImagemController {

    // Serviço responsável pelas operações com imagens.
    private final ImagemService imagemService;

    // O Spring injeta o serviço automaticamente.
    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }

    // Envia uma imagem e associa ao produto informado.
    @PostMapping(
            value = "/produto/{produtoId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, Object>> enviarImagem(
            @PathVariable Integer produtoId,
            @RequestParam("arquivo") MultipartFile arquivo
    ) throws IOException {

        // Salva a imagem e registra a associação no banco.
        Imagem imagem = imagemService.associarImagem(
                produtoId,
                arquivo
        );

        // Retorna os dados básicos da imagem.
        return ResponseEntity.ok(Map.of(
                "id", imagem.getId(),
                "caminho", imagem.getCaminho(),
                "produtoId", produtoId
        ));
    }

    // Disponibiliza uma imagem salva para o navegador e o React.
    @GetMapping("/{nomeArquivo:.+}")
    public ResponseEntity<Resource> buscarImagem(
            @PathVariable String nomeArquivo
    ) throws IOException {

        // Localiza o arquivo dentro da pasta de uploads.
        Path caminho = imagemService.buscarCaminho(nomeArquivo);

        // Transforma o arquivo em um recurso que pode ser enviado.
        Resource recurso = new UrlResource(caminho.toUri());

        // Retorna erro caso o arquivo não exista ou não possa ser lido.
        if (!recurso.exists() || !recurso.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        // Define o tipo de conteúdo da imagem.
        String nome = nomeArquivo.toLowerCase();
        MediaType tipo;

        if (nome.endsWith(".png")) {
            tipo = MediaType.IMAGE_PNG;
        } else if (nome.endsWith(".jpg") || nome.endsWith(".jpeg")) {
            tipo = MediaType.IMAGE_JPEG;
        } else if (nome.endsWith(".webp")) {
            tipo = MediaType.parseMediaType("image/webp");
        } else {
            return ResponseEntity.badRequest().build();
        }

        // Entrega o arquivo como imagem para o navegador.
        return ResponseEntity.ok()
                .contentType(tipo)
                .body(recurso);
    }
}