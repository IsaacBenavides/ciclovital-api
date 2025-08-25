package cr.ac.ucr.cicloVital.services;

import cr.ac.ucr.cicloVital.modelo.Mensaje;
import cr.ac.ucr.cicloVital.repository.IMensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    @Autowired
    private IMensajeRepository mensajeRepository;

    public List<Mensaje> listarPorChat(Integer chatId) {
        return mensajeRepository.findByChatIdOrderByFechaHoraAsc(chatId);
    }

    public Mensaje guardar(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

}
