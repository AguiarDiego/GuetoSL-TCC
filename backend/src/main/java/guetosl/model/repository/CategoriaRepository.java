
package guetosl.model.repository;

import guetosl.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository responsável pelo acesso aos dados de Categoria.
// O Spring Data JPA fornece automaticamente os métodos básicos.
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
