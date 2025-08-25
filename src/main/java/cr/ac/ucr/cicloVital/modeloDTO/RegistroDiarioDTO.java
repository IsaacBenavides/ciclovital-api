package cr.ac.ucr.cicloVital.modeloDTO;

import java.time.LocalDate;

public class RegistroDiarioDTO {

    private Integer id;
    private LocalDate date;
    private Integer horasSueno;
    private Integer energia;
    private Integer estadoAnimo;
    private Integer motivacion;
    private Integer ejercicio;
    private String comentario;

    public RegistroDiarioDTO(Integer id, LocalDate date, Integer horasSueno, Integer energia, Integer estadoAnimo, Integer motivacion, Integer ejercicio, String comentario) {
        this.id = id;
        this.date = date;
        this.horasSueno = horasSueno;
        this.energia = energia;
        this.estadoAnimo = estadoAnimo;
        this.motivacion = motivacion;
        this.ejercicio = ejercicio;
        this.comentario = comentario;
    }

    public RegistroDiarioDTO(){};

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
}
