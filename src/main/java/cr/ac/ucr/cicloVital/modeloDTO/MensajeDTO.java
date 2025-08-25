package cr.ac.ucr.cicloVital.modeloDTO;

public class MensajeDTO {
    private Integer chatId;
    private String contenido;
    private boolean esUsuario;
    private String timestamp; // ISO-8601 recomendado

    // constructors, getters, setters

    public MensajeDTO() {
    }

    public MensajeDTO(Integer chatId, String contenido, boolean esUsuario, String timestamp) {
        this.chatId = chatId;
        this.contenido = contenido;
        this.esUsuario = esUsuario;
        this.timestamp = timestamp;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isEsUsuario() {
        return esUsuario;
    }

    public void setEsUsuario(boolean esUsuario) {
        this.esUsuario = esUsuario;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

