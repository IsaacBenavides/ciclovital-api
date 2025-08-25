package cr.ac.ucr.cicloVital.controller;

import cr.ac.ucr.cicloVital.modelo.Chat;
import cr.ac.ucr.cicloVital.modeloDTO.ChatDTO;
import cr.ac.ucr.cicloVital.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/usuario/{id}/resumen")
    public ResponseEntity<List<ChatDTO>> obtenerResumenChatsPorUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(chatService.listarPorUsuario(id));
    }

    // POST: Crear nuevo chat
    @PostMapping
    public ResponseEntity<?> agregarChat(@Validated @RequestBody Chat chat, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errores);
        }

        //Validar límite de 100 chats por usuario
        Integer usuarioId = chat.getUsuario().getId();
        List<ChatDTO> chatsUsuario = chatService.listarPorUsuario(usuarioId);
        if (chatsUsuario.size() >= 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Has alcanzado el límite máximo de 100 chats por usuario.");
        }

        // Evitar duplicado por ID
        if (chat.getId() != null && chatService.obtenerPorId(chat.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El chat con ID " + chat.getId() + " ya existe");
        }

        Chat chatGuardado = chatService.guardar(chat);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatGuardado);
    }

    // DELETE: Eliminar chat por ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarChat(@PathVariable Integer id) {
        Optional<Chat> chat = chatService.obtenerPorId(id);
        if (!chat.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró un chat con ID " + id);
        }
        chatService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
