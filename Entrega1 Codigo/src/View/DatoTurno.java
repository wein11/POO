package View;

import java.time.LocalDate;
import java.time.LocalTime;

public class DatoTurno extends Dato {

    private long idPaciente;
    private long idOdontologo;
    private LocalDate fecha;
    private LocalTime hora;

    public DatoTurno() {
        this.tipo = 3;
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public long getIdOdontologo() {
        return idOdontologo;
    }

    public void setIdOdontologo(long idOdontologo) {
        this.idOdontologo = idOdontologo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}
