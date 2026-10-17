
package guetosl.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import guetosl.model.entity.Imagem;
import guetosl.model.entity.Produto;
import guetosl.model.repository.ImagemRepository;
import guetosl.model.repository.ProdutoRepository;

@Service
public class ImagemService {

    // Define a pasta onde os arquivos serão armazenados.
    private final Path pastaUploads =
            Paths.get("uploads", "produtos")
                    .toAbsolutePath()
                    .normalize();

    // Repositórios para acessar o banco de dados.
    private final ImagemRepository imagemRepository;
    private final ProdutoRepository produtoRepository;

    // O Spring injeta os repositórios automaticamente.
    public ImagemService(
            ImagemRepository imagemRepository,
            ProdutoRepository produtoRepository
    ) throws IOException {

        this.imagemRepository = imagemRepository;
        this.produtoRepository = produtoRepository;

        // Cria a pasta caso ela não exista.
        Files.createDirectories(pastaUploads);
    }

    // Salva o arquivo de imagem no computador.
    public String salvarImagem(MultipartFile arquivo) throws IOException {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecione uma imagem."
            );
        }

        // Obtém a extensão do arquivo.
        String nomeOriginal = arquivo.getOriginalFilename();
        String extensao = "";

        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            extensao = nomeOriginal
                    .substring(nomeOriginal.lastIndexOf("."))
                    .toLowerCase();
        }

        // Gera um nome único para o arquivo.
        String nomeArquivo = UUID.randomUUID() + extensao;

        // Define o caminho onde o arquivo será salvo.
        Path destino = pastaUploads
                .resolve(nomeArquivo)
                .normalize();

        // Impede que o arquivo saia da pasta permitida.
        if (!destino.startsWith(pastaUploads)) {
            throw new IllegalArgumentException(
                    "Nome de arquivo inválido."
            );
        }

        // Copia o arquivo enviado para a pasta.
        Files.copy(
                arquivo.getInputStream(),
                destino,
                StandardCopyOption.REPLACE_EXISTING
        );

        return nomeArquivo;
    }

    // Associa uma imagem a um produto cadastrado.
    @Transactional
    public Imagem associarImagem(
            Integer produtoId,
            MultipartFile arquivo
    ) throws IOException {

        // Procura o produto pelo ID.
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não encontrado."
                        )
                );

        // Cria um novo registro para cada imagem enviada.
        Imagem imagem = new Imagem();

        // Salva o arquivo na pasta de imagens.
        String nomeArquivo = salvarImagem(arquivo);

        // Registra o nome e o produto relacionado.
        imagem.setCaminho(nomeArquivo);
        imagem.setProduto(produto);

        // Salva o registro no banco de dados.
        return imagemRepository.save(imagem);
    }

    // Localiza a imagem salva na pasta de uploads.
    public Path buscarCaminho(String nomeArquivo) throws IOException {

        // Verifica se o nome foi informado.
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome da imagem não foi informado."
            );
        }

        // Aceita somente nomes simples de arquivos de imagem.
        // O nome não pode conter pastas ou caminhos externos.
        if (!nomeArquivo.matches(
                "[a-zA-Z0-9_-]+\\.(png|jpg|jpeg|webp)"
        )) {
            throw new IllegalArgumentException(
                    "Nome de imagem inválido: " + nomeArquivo
            );
        }

        // Monta o caminho completo do arquivo.
        Path caminho = pastaUploads.resolve(nomeArquivo).normalize();

        // Impede que o caminho saia da pasta de uploads.
        if (!caminho.startsWith(pastaUploads)) {
            throw new IllegalArgumentException(
                    "Caminho de imagem inválido."
            );
        }

        return caminho;
    }
}