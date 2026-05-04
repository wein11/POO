package Entity;

import java.util.ArrayList;
import java.util.List;

public class HistorialClinico {

    private int id;
    private Paciente paciente;
    private List<Turno> turnos;
    private String resumenPaciente;

    public HistorialClinico(int id, Paciente paciente,String resumenPaciente) {
        this.id = id;
        this.paciente = paciente;
        this.turnos = new ArrayList<>();
    }

    public HistorialClinico(int id, Paciente paciente, List<Turno> turnos, String resumenPaciente) {
        this.id = id;
        this.paciente = paciente;
        this.turnos = turnos;
    }
    public HistorialClinico(int id, Paciente paciente) {
        this.id = id;
        this.paciente = paciente;
        this.turnos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getResumenPaciente(){
        return resumenPaciente;
    }

    public void setResumenPaciente(String resumenPaciente) {
        this.resumenPaciente = resumenPaciente;
    }


    @Override
    public String toString() {
        String resultado = "HistorialClinico #" + id +
                           " | Paciente: " + paciente.getNombreCompleto() +
                           " | Resumen del historial: " + getResumenPaciente() +
                           " | Turnos registrados: " + turnos.size();
        for (Turno turno : turnos) {
            resultado = resultado + "- " + turno;
        }
        return resultado;
    }
}
