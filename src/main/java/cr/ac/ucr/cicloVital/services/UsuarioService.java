package cr.ac.ucr.cicloVital.services;

import cr.ac.ucr.cicloVital.config.PasswordEncoderConfig;
import cr.ac.ucr.cicloVital.modelo.Usuario;
import cr.ac.ucr.cicloVital.modeloDTO.UsuarioDTO;
import cr.ac.ucr.cicloVital.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<UsuarioDTO> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(u -> new UsuarioDTO(u.getId(), u.getNombre(), u.getEdad(), u.getCorreo()));
    }


    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    //Busca por edad
    public Optional<UsuarioDTO> login(String correo, String passwordPlano) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(passwordPlano, usuario.getPassword())) {
                UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getEdad(), usuario.getCorreo());
                return Optional.of(usuarioDTO); // Login correcto
            }
        }
        return Optional.empty(); // Login fallido
    }

    public Usuario guardar(Usuario usuario) {
        String contraseñaHasheada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaHasheada);
        return usuarioRepository.save(usuario);
    }

    public void eliminarPorId(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
