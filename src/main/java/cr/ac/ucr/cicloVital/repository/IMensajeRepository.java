package cr.ac.ucr.cicloVital.repository;

import cr.ac.ucr.cicloVital.modelo.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IMensajeRepository extends JpaRepository<Mensaje, Integer> {

    // Obtener todos los mensajes de un chat ordenados por fechaHora
    List<Mensaje> findByChatIdOrderByFechaHoraAsc(Integer chatId);

}
