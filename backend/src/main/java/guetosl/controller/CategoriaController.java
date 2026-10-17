
package guetosl.controller;

import guetosl.model.entity.Categoria;
import guetosl.model.repository.CategoriaRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Informa ao Spring que esta classe recebe requisições HTTP
// e retorna dados, normalmente em formato JSON.
@RestController

// Define o endereço principal deste controlador.
// O endpoint ficará disponível em /categorias.
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    // O Spring injeta automaticamente o repositório
    // para permitir o acesso aos dados do banco.
    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Responde às requisições GET feitas em /categorias.
    @GetMapping
    public List<Categoria> listarCategorias() {

        // Busca todas as categorias cadastradas no banco.
        return categoriaRepository.findAll();
    }
}
