package cr.ac.ucr.cicloVital.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_registro_diario")
public class RegistroDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;

    private Integer horasSueno;
    private Integer energia;
    private Integer estadoAnimo;
    private Integer motivacion;
    private Integer ejercicio;
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public RegistroDiario() {
        this.date = LocalDate.now();
    }

    // Getters y setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getHorasSueno() {
        return horasSueno;
    }

    public void setHorasSueno(Integer horasSueno) {
        this.horasSueno = horasSueno;
    }

    public Integer getEnergia() {
        return energia;
    }

    public void setEnergia(Integer energia) {
        this.energia = energia;
    }

    public Integer getEstadoAnimo() {
        return estadoAnimo;
    }

    public void setEstadoAnimo(Integer estadoAnimo) {
        this.estadoAnimo = estadoAnimo;
    }

    public Integer getMotivacion() {
        return motivacion;
    }

    public void setMotivacion(Integer motivacion) {
        this.motivacion = motivacion;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
