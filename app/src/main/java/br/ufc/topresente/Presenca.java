package br.ufc.topresente;

/**
 * Created by Thiago on 20/05/2017.
 */
public class Presenca {

    private Integer idUsuario;
    private String codAula, hora;

    public Presenca() { }

    public Presenca(Integer idUsuario, String codAula, String hora) {
        this.idUsuario = idUsuario;
        this.codAula = codAula;
        this.hora = hora;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodAula() {
        return codAula;
    }

    public void setCodAula(String codAula) {
        this.codAula = codAula;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
