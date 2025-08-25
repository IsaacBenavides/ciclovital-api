package cr.ac.ucr.cicloVital.services;

import cr.ac.ucr.cicloVital.modelo.Chat;
import cr.ac.ucr.cicloVital.modeloDTO.ChatDTO;
import cr.ac.ucr.cicloVital.repository.IChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private IChatRepository chatRepository;

    public Optional<Chat> obtenerPorId(Integer id) {
        return chatRepository.findById(id);
    }

    public Chat guardar(Chat chat) {
        return chatRepository.save(chat);
    }

    public void eliminarPorId(Integer id) {
        chatRepository.deleteById(id);
    }

    public List<ChatDTO> listarPorUsuario(Integer usuarioId) {
        List<Chat> chats = chatRepository.findByUsuarioIdOrderByIdDesc(usuarioId);
        return chats.stream()
                .map(chat -> new ChatDTO(chat.getId(), chat.getTitulo()))
                .toList();
    }

}
