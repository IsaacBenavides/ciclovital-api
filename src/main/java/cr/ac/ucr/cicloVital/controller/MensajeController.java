package cr.ac.ucr.cicloVital.controller;

import cr.ac.ucr.cicloVital.modelo.Mensaje;
import cr.ac.ucr.cicloVital.modelo.Chat;
import cr.ac.ucr.cicloVital.modeloDTO.RegistroDiarioDTO;
import cr.ac.ucr.cicloVital.modeloDTO.UsuarioDTO;
import cr.ac.ucr.cicloVital.services.AIService;
import cr.ac.ucr.cicloVital.services.MensajeService;
import cr.ac.ucr.cicloVital.services.RegistroDiarioService;
import cr.ac.ucr.cicloVital.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin(origins = "*")
public class MensajeController {

    @Autowired private MensajeService mensajeService;
    @Autowired private AIService aiService;
    @Autowired private RegistroDiarioService registroDiarioService;
    @Autowired private UsuarioService usuarioService;

    // ---------- Helpers ----------
    private static Integer toInt(Object obj, String field) {
        if (obj instanceof Number) return ((Number) obj).intValue();
        try { return Integer.parseInt(String.valueOf(obj)); }
        catch (Exception e) { throw new IllegalArgumentException("Campo '" + field + "' inválido"); }
    }
    private static Integer nvl(Integer v, Integer d) { return v != null ? v : d; }

    // Prompt SYSTEM siempre calculado al vuelo (no se persiste)
    private String construirPromptInicial(Integer usuarioId) {
        Optional<UsuarioDTO> usuarioDTO = usuarioService.obtenerPorId(usuarioId);
        String nombre = usuarioDTO.map(UsuarioDTO::getNombre).orElse("desconocido");
        Integer edad = usuarioDTO.map(UsuarioDTO::getEdad).orElse(0);
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Costa_Rica"));
        // Si tu método aún se llama porUsarioYFecha, ajusta esta línea:
        Optional<RegistroDiarioDTO> regOpt = registroDiarioService.porUsuarioYFecha(usuarioId, hoy);

        if (regOpt.isEmpty()) {
            return "Actúa como un asistente personal de bienestar emocional y salud mental.\n"
                    + "El usuario: " + nombre + "de" + edad + " años, no cuenta con un registro diario hoy.\n"
                    + "Puedes ofrecerle registrar sus hábitos o brindarle apoyo empático.";
        }

        RegistroDiarioDTO r = regOpt.get();
        String comentario = Optional.ofNullable(r.getComentario()).orElse("");

        return "Actúa como un asistente personal de bienestar emocional y salud mental.\n"
                + "El usuario: " + nombre + "de" + edad + " años, sí cuenta con un registro diario hoy.\n"
                + "Analiza los siguientes datos y proporciona un breve resumen del estado general del usuario.\n"
                + "Si identificas áreas preocupantes (como puntuaciones muy bajas o hábitos irregulares), resáltalas brevemente y ofrece una sugerencia empática.\n"
                + "- Sueño: " + nvl(r.getHorasSueno(), 0) + " horas\n"
                + "- Energía: " + nvl(r.getEnergia(), 0) + "/10\n"
                + "- Estado de ánimo: " + nvl(r.getEstadoAnimo(), 0) + "/10\n"
                + "- Motivación: " + nvl(r.getMotivacion(), 0) + "/10\n"
                + "- Ejercicio: " + nvl(r.getEjercicio(), 0) + " horas\n"
                + "- Comentario: \"" + comentario + "\"\n"
                + "Tu respuesta debe ser amable, clara y directa, en un párrafo de no más de 4 a 6 oraciones.";
    }

    // ---------- Endpoints ----------
    @GetMapping("/porChat/{chatId}")
    public ResponseEntity<?> listarPorChat(@PathVariable Integer chatId) {
        List<Mensaje> mensajes = mensajeService.listarPorChat(chatId);
        return ResponseEntity.ok(mensajes); // mejor 200 [] que 204
    }

    @PostMapping("/ia")
    public ResponseEntity<?> procesarMensajeIA(@RequestBody Map<String, Object> payload) {
        try {
            if (payload == null)
                return ResponseEntity.badRequest().body(Map.of("error", "Payload vacío"));

            // Validaciones + parse tolerante de tipos
            Integer chatId = toInt(payload.get("chatId"), "chatId");
            Integer userId = toInt(payload.get("userId"), "userId");

            String contenido = String.valueOf(payload.get("mensaje"));
            if (contenido == null || contenido.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El mensaje no puede estar vacío"));
            }
            contenido = contenido.trim();

            // 1) Referencia del chat
            Chat chat = new Chat();
            chat.setId(chatId);

            // 2) Guardar mensaje del usuario
            Mensaje mensajeUsuario = new Mensaje();
            mensajeUsuario.setChat(chat);
            mensajeUsuario.setEsUsuario(true);
            mensajeUsuario.setContenido(contenido);
            mensajeUsuario.setFechaHora(LocalDateTime.now());
            mensajeService.guardar(mensajeUsuario);

            // 3) Historial (asegúrate que tu repo lo devuelva ordenado ASC por fechaHora)
            List<Mensaje> historial = mensajeService.listarPorChat(chatId);

            // 4) Payload para la IA: SYSTEM + historial user/assistant
            List<Map<String, String>> historialFormateado = new ArrayList<>();
            String systemPrompt = construirPromptInicial(userId);
            historialFormateado.add(Map.of("role", "system", "content", systemPrompt));
            for (Mensaje m : historial) {
                historialFormateado.add(Map.of(
                        "role", m.isEsUsuario() ? "user" : "assistant",
                        "content", m.getContenido()
                ));
            }

            // 5) Llamada a la IA
            String respuestaIA = Optional.ofNullable(
                    aiService.obtenerRespuestaDesdeIA(historialFormateado)
            ).orElse("").trim();

            // 6) Guardar respuesta de la IA
            Mensaje mensajeIA = new Mensaje();
            mensajeIA.setChat(chat);
            mensajeIA.setEsUsuario(false);
            mensajeIA.setContenido(respuestaIA);
            mensajeIA.setFechaHora(LocalDateTime.now());
            mensajeService.guardar(mensajeIA);

            // 7) Respuesta al frontend
            return ResponseEntity.ok(Map.of("respuesta", respuestaIA));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error al procesar el mensaje con IA: " + e.getMessage()));
        }
    }
}
