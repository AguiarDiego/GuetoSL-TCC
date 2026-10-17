
package guetosl.model.repository;

import guetosl.model.entity.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Integer> {

    // Procura todas as imagens associadas ao ID de um produto.
    List<Imagem> findAllByProdutoId(Integer produtoId);
}
