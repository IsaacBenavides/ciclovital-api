package cr.ac.ucr.cicloVital.repository;

import cr.ac.ucr.cicloVital.modelo.RegistroDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IRegistroDiarioRepository extends JpaRepository<RegistroDiario, Integer> {

    List<RegistroDiario> findByUsuarioId(Integer usuarioId);

    Optional<RegistroDiario> findByUsuarioIdAndDate(Integer usuarioId, LocalDate date);
}
