package cr.ac.ucr.cicloVital.services;

import cr.ac.ucr.cicloVital.modelo.RegistroDiario;
import cr.ac.ucr.cicloVital.modeloDTO.RegistroDiarioDTO;
import cr.ac.ucr.cicloVital.modeloDTO.UsuarioDTO;
import cr.ac.ucr.cicloVital.repository.IRegistroDiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RegistroDiarioService {

    @Autowired
    private IRegistroDiarioRepository registroRepo;

    public Optional<RegistroDiarioDTO> porUsuarioYFecha(Integer usuarioId, LocalDate fecha){

        return registroRepo.findByUsuarioIdAndDate(usuarioId, fecha)
                .map(r -> new RegistroDiarioDTO(
                        r.getId(),
                        r.getDate(),
                        r.getHorasSueno(),
                        r.getEnergia(),
                        r.getEstadoAnimo(),
                        r.getMotivacion(),
                        r.getEjercicio(),
                        r.getComentario()));
    }

    public RegistroDiario guardar(RegistroDiario registro) {
        return registroRepo.save(registro);
    }

    public Optional<RegistroDiario> obtenerPorId(Integer id) {
        return registroRepo.findById(id);
    }

    public void eliminarPorId(Integer id) {
        registroRepo.deleteById(id);
    }

    public boolean yaExisteRegistroEnFecha(Integer usuarioId, LocalDate fecha) {
        return registroRepo.findByUsuarioIdAndDate(usuarioId, fecha).isPresent();
    }

    public Optional<RegistroDiario> obtenerRegistroPorUsuarioYFecha(Integer usuarioId, LocalDate fecha) {
        return registroRepo.findByUsuarioIdAndDate(usuarioId, fecha);
    }

    public List<RegistroDiarioDTO> obtenerRegistrosPorUsuario(Integer usuarioId) {
        List<RegistroDiario> listaRegistros = registroRepo.findByUsuarioId(usuarioId);
        return listaRegistros.stream()
                .map(registro -> new RegistroDiarioDTO(registro.getId(), registro.getDate(), registro.getHorasSueno(), registro.getEnergia(), registro.getEstadoAnimo(), registro.getMotivacion(), registro.getEjercicio(), registro.getComentario() ))
                .toList();

    }
}
