package cr.ac.ucr.cicloVital.controller;

import cr.ac.ucr.cicloVital.modelo.Usuario;
import cr.ac.ucr.cicloVital.modeloDTO.UsuarioDTO;
import cr.ac.ucr.cicloVital.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET: Listar todos
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay usuarios registrados.");
        }
        return ResponseEntity.ok(usuarios);
    }

    // GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Optional<UsuarioDTO> usuario = usuarioService.obtenerPorId(id);
        if (!usuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario con ID " + id + " no existe.");
        }
        return ResponseEntity.ok(usuario);
    }

    // POST: Agregar nuevo usuario
    @PostMapping
    public ResponseEntity<?> agregarUsuario(@Validated @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errores);
        }

        if (usuarioService.buscarPorCorreo(usuario.getCorreo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un usuario con el correo " + usuario.getCorreo());
        }

        Usuario guardado = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // PUT: Actualizar
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@Validated @PathVariable Integer id,
                                        @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errores);
        }

        Optional<UsuarioDTO> existente = usuarioService.obtenerPorId(id);
        if (!existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario con ID " + id + " no existe.");
        }

        usuario.setId(id);
        Usuario actualizado = usuarioService.guardar(usuario);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE: Eliminar
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        Optional<UsuarioDTO> usuario = usuarioService.obtenerPorId(id);
        if (!usuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario con ID " + id + " no existe.");
        }

        usuarioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // POST: Login (correo + contraseña)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datos) {
        String correo = datos.get("correo");
        String password = datos.get("password");

        Optional<UsuarioDTO> usuario = usuarioService.login(correo, password);
        if (!usuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }
        return ResponseEntity.ok(usuario.get());
    }

}
