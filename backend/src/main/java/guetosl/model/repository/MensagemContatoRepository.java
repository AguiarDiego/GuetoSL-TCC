
package guetosl.model.repository;

import guetosl.model.entity.MensagemContato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensagemContatoRepository
        extends JpaRepository<MensagemContato, Integer> {
}
