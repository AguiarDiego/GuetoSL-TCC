package guetosl.controller;


import guetosl.model.entity.Imagem;
import guetosl.model.repository.ImagemRepository;
import org.springframework.transaction.annotation.Transactional;
import guetosl.model.entity.Produto;
import guetosl.service.ProdutoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

// Indica que essa classe é um Controller da API REST.
@RestController

// Define o caminho base das requisições deste Controller.
@RequestMapping("/produtos")

public class ProdutoController {

    // Service responsável pelas regras e operações de Produto.
    private final ProdutoService produtoService;

    // O Spring fornece automaticamente o ProdutoService.
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Responde às requisições GET feitas para /produtos.

    @GetMapping
    public List<Produto> listar() {
        return produtoService.listar();
    }

// Busca um produto específico pelo ID.
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Integer id) {
        return produtoService.buscarPorId(id);
    }

    // Responde às requisições POST feitas para /produtos.
    @PostMapping
    public Produto salvar(@RequestBody @Valid Produto produto){
        return produtoService.salvar(produto);
    }

    // Responde às requisições PUT feitas para /produtos/{id}.
    @PutMapping("/{id}")
    public Produto atualizar(
            @PathVariable Integer id,
            @RequestBody Produto produto) {

            return produtoService.atualizar(id,produto);
    }
    // Responde às requisições DELETE feitas para /produtos/{id}.
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id){
        produtoService.deletar(id);
    }
}
