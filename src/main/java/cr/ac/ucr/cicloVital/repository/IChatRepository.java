package cr.ac.ucr.cicloVital.repository;

import cr.ac.ucr.cicloVital.modelo.Chat;
import cr.ac.ucr.cicloVital.modeloDTO.ChatDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IChatRepository extends JpaRepository<Chat, Integer> {

    List<Chat> findByUsuarioIdOrderByIdDesc(Integer usuarioId);
}
