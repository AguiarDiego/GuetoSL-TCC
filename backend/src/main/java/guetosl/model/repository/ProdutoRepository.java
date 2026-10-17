package guetosl.model.repository;

import guetosl.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository responsável pelo acesso aos dados de Produto.
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}