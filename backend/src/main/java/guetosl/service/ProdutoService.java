
package guetosl.service;

import guetosl.model.entity.Produto;
import guetosl.excecao.ProdutoNaoEncontradoException;
import guetosl.model.repository.ProdutoRepository;
import guetosl.model.repository.ImagemRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ImagemRepository imagemRepository;

    // O Spring fornece os repositórios necessários para o serviço.
    public ProdutoService(
            ProdutoRepository produtoRepository,
            ImagemRepository imagemRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.imagemRepository = imagemRepository;
    }

    // Retorna todos os produtos com suas imagens carregadas.
    @Transactional(readOnly = true)
    public List<Produto> listar() {
        List<Produto> produtos = produtoRepository.findAll();

        produtos.forEach(produto ->
                produto.getImagens().size()
        );

        return produtos;
    }

    // Busca um produto pelo ID e carrega suas imagens.
    @Transactional(readOnly = true)
    public Produto buscarPorId(Integer id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() ->
                        new ProdutoNaoEncontradoException(
                                "Produto não encontrado"
                        )
                );

        produto.getImagens().size();

        return produto;
    }


    // Cadastra um novo produto no banco.
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    // Atualiza os dados de um produto já cadastrado.
    public Produto atualizar(Integer id, Produto produto) {

        // Primeiro, procura o produto que será atualizado.
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() ->
                        new ProdutoNaoEncontradoException(
                                "Produto não encontrado"
                        )
                );

        // Substitui os dados antigos pelos novos.
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setPreco(produto.getPreco());
        produtoExistente.setTamanho(produto.getTamanho());
        produtoExistente.setCor(produto.getCor());
        produtoExistente.setEstoque(produto.getEstoque());
        produtoExistente.setCategoria(produto.getCategoria());

        // Salva as alterações no banco.
        return produtoRepository.save(produtoExistente);
    }

    // Exclui todas as imagens relacionadas antes de excluir o produto.
    @Transactional
    public void deletar(Integer id) {

        // Verifica se o produto existe.
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() ->
                        new ProdutoNaoEncontradoException(
                                "Produto não encontrado"
                        )
                );

        // Procura todas as imagens associadas ao produto.
        imagemRepository.findAllByProdutoId(id).forEach(imagem -> {

            // Exclui cada registro de imagem do banco.
            imagemRepository.delete(imagem);
        });

        // Sincroniza as exclusões antes de excluir o produto.
        imagemRepository.flush();

        // Depois, exclui o produto.
        produtoRepository.delete(produto);
    }
}
