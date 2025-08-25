package cr.ac.ucr.cicloVital.repository;

import cr.ac.ucr.cicloVital.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar usuario por correo (ideal para login)
    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findById(Integer id);

}
