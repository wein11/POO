package Entity;

import java.util.ArrayList;
import java.util.List;

public class HistorialClinico {

    private long id;
    private Paciente paciente;
    private List<Turno> turnos;
    private String resumenPaciente;

    public HistorialClinico() {}

    public HistorialClinico(long id, Paciente paciente, String resumenPaciente) {
        this.id = id;
        this.paciente = paciente;
        this.resumenPaciente = resumenPaciente;
        this.turnos = new ArrayList<>();
    }

    public HistorialClinico(long id, Paciente paciente, List<Turno> turnos, String resumenPaciente) {
        this.id = id;
        this.paciente = paciente;
        this.turnos = turnos;
        this.resumenPaciente = resumenPaciente;
    }

    public HistorialClinico(long id, Paciente paciente) {
        this.id = id;
        this.paciente = paciente;
        this.turnos = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }

    public void agregarTurno(Turno turno) {
        turnos.add(turno);
    }

    public String getResumenPaciente() {
        return resumenPaciente;
    }

    public void setResumenPaciente(String resumenPaciente) {
        this.resumenPaciente = resumenPaciente;
    }

    @Override
    public String toString() {
        String resultado = "HistorialClinico #" + id +
                           " | Paciente: " + paciente.getNombreCompleto() +
                           " | Resumen del historial: " + resumenPaciente +
                           " | Turnos registrados: " + turnos.size();
        for (Turno turno : turnos) {
            resultado = resultado + " - " + turno;
        }
        return resultado;
    }
}
