package cr.ac.ucr.cicloVital.modelo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tb_chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaInicio;
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo; // opcional, ej. "Registro emocional 19 junio"
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Mensaje> mensajes = new ArrayList<>();

    //Metodo constructor con parametros
    public Chat(Integer id, LocalDateTime fechaInicio, String titulo, Usuario usuario, List<Mensaje> mensajes) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.titulo = titulo;
        this.usuario = usuario;
        this.mensajes = mensajes;
    }

    //Metodo constructor sin parametros
    public Chat() {
        this.id = 0;
        this.fechaInicio = LocalDateTime.now();
        this.titulo = "Chat sin título";
        this.usuario = new Usuario();
        this.mensajes = new ArrayList<>();
    }


//-------------------------------------------------------------------------------------------------------
    //Metodos sets, gets y toString

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //-----------------------------------------------------------\\

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    //-----------------------------------------------------------\\

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    //-----------------------------------------------------------\\

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    //-----------------------------------------------------------\\

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    //-----------------------------------------------------------\\

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", fechaInicio=" + fechaInicio +
                ", titulo='" + titulo + '\'' +
                ", usuario=" + usuario +
                ", mensajes=" + mensajes +
                '}';
    }

}//Fin de la clase chat
