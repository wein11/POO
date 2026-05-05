package Entity;

import java.util.ArrayList;
import java.util.List;

public class HistorialClinico {

    private long id;
    private Paciente paciente;
    private List<Turno> turnos;
    private String resumenPaciente;

    public HistorialClinico() {
        this.turnos = new ArrayList<>();
    }

    public HistorialClinico(long id, Paciente paciente, String resumenPaciente) {
        this.id = id;
        this.paciente = paciente;
        this.resumenPaciente = resumenPaciente;
        this.turnos = new ArrayList<>();
    }

    public HistorialClinico(long id, Paciente paciente, List<Turno> turnos, String resumenPaciente) {
        this.id = id;
        this.paciente = paciente;
        this.turnos = turnos != null ? turnos : new ArrayList<>();
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
        this.turnos = turnos != null ? turnos : new ArrayList<>();
    }

    public void agregarTurno(Turno turno) {
        if (turnos == null) {
            turnos = new ArrayList<>();
        }
        turnos.add(turno);
    }

    public void eliminarTurnoPorId(long idTurno) {
        if (turnos != null) {
            turnos.removeIf(turno -> turno.getId() == idTurno);
        }
    }

    public void actualizarTurno(Turno turnoActualizado) {
        eliminarTurnoPorId(turnoActualizado.getId());
        agregarTurno(turnoActualizado);
    }

    public String getResumenPaciente() {
        return resumenPaciente;
    }

    public void setResumenPaciente(String resumenPaciente) {
        this.resumenPaciente = resumenPaciente;
    }

    @Override
    public String toString() {
        if (turnos == null) {
            turnos = new ArrayList<>();
        }
        String resultado = "HistorialClinico #" + id +
                           "\nPaciente: " + paciente.getNombreCompleto() +
                           "\nResumen del historial: " + resumenPaciente +
                           "\nTurnos registrados: " + turnos.size();
        for (Turno turno : turnos) {
            resultado = resultado + "\n- " + turno;
        }
        return resultado;
    }
}
