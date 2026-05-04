package Entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {

    private long id;
    private Paciente paciente;
    private Odontologo odontologo;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;
    private Consultorio consultorio;

    public Turno() {}

    public Turno(long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public Turno(long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado, Consultorio consultorio) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.consultorio = consultorio;
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

    public Odontologo getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(Odontologo odontologo) {
        this.odontologo = odontologo;
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

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public String getDetalle() {
        String consultorioTexto = consultorio != null
                ? "Consultorio " + consultorio.getNumero() + ", Piso " + consultorio.getPiso()
                : "Sin consultorio asignado";
        return "Turno para: " + paciente.getNombreCompleto() +
               "\nAtiende: " + odontologo.getNombreCompleto() +
               "\nFecha: " + fecha +
               "\nHora: " + hora +
               "\nEstado: " + estado +
               "\nLugar: " + consultorioTexto;
    }

    @Override
    public String toString() {
        return "Turno #" + id +
               " | Paciente: " + paciente.getNombreCompleto() +
               " | Odontólogo: " + odontologo.getNombreCompleto() +
               " | Fecha: " + fecha +
               " | Hora: " + hora +
               " | Estado: " + estado +
               (consultorio != null ? " | Consultorio: " + consultorio.getNumero() : "");
    }
}
