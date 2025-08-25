package cr.ac.ucr.cicloVital.controller;

import cr.ac.ucr.cicloVital.modelo.RegistroDiario;
import cr.ac.ucr.cicloVital.modeloDTO.RegistroDiarioDTO;
import cr.ac.ucr.cicloVital.services.RegistroDiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/registros")
public class RegistroDiarioController {

    @Autowired
    private RegistroDiarioService registroService;

    // POST: Crear nuevo registro diario
    @PostMapping
    public ResponseEntity<?> agregarRegistro(@Validated @RequestBody RegistroDiario registro, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errores);
        }

        if (registro.getUsuario() == null || registro.getUsuario().getId() == null) {
            return ResponseEntity.badRequest().body("Debe incluir el ID del usuario.");
        }

        Integer usuarioId = registro.getUsuario().getId();
        LocalDate fecha = (registro.getDate() != null) ? registro.getDate() : LocalDate.now();
        registro.setDate(fecha);

        if (registroService.yaExisteRegistroEnFecha(usuarioId, fecha)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un registro diario para este usuario en la fecha " + fecha);
        }

        RegistroDiario guardado = registroService.guardar(registro);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // PUT: Actualizar registro existente
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@Validated @PathVariable Integer id,
                                        @RequestBody RegistroDiario registro, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errores);
        }

        Optional<RegistroDiario> existente = registroService.obtenerPorId(id);
        if (!existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró un registro con ID " + id);
        }

        registro.setId(id);
        RegistroDiario actualizado = registroService.guardar(registro);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE: Eliminar registro por ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        Optional<RegistroDiario> registro = registroService.obtenerPorId(id);
        if (!registro.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El registro con ID " + id + " no existe.");
        }
        registroService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // GET: Obtener registro de hoy
    @GetMapping("/{id}/hoy")
    public ResponseEntity<?> obtenerRegistroDeHoy(@PathVariable("id") Integer usuarioId) {
        LocalDate hoy = LocalDate.now();
        Optional<RegistroDiario> registro = registroService.obtenerRegistroPorUsuarioYFecha(usuarioId, hoy);

        if (registro.isPresent()) {
            return ResponseEntity.ok(registro.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró un registro para hoy.");
        }
    }

    // GET: Obtener todos los registros del usuario
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> obtenerRegistrosPorUsuario(@PathVariable Integer id) {
        List<RegistroDiarioDTO> registros = registroService.obtenerRegistrosPorUsuario(id);
        if (registros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No hay registros para este usuario.");
        }
        return ResponseEntity.ok(registros);
    }
}
