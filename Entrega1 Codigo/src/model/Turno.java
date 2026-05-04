package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {

    private int id;
    private Paciente paciente;
    private Odontologo odontologo;
    private LocalDate fecha;
    private LocalTime hora;
    private Consultorio consultorio;

    public Turno(int id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, Consultorio consultorio) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.consultorio = consultorio;
    }

    public Turno(int id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, String historialMedico, Consultorio consultorio) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.consultorio = consultorio;
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

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public String getDetalle() {

        String nombrePaciente = paciente.getNombreCompleto();

        String nombreOdontologo = odontologo.getNombreCompleto();
      
        String consultorioTexto = "Consultorio " + consultorio.getNumero() + ", Piso " + consultorio.getPiso();

        return "Turno para: " + nombrePaciente +
               "\nAtiende: " + nombreOdontologo +
               "\nFecha: " + fecha +
               "\nHora: " + hora +
               "\nLugar: " + consultorioTexto;
    }

    @Override
    public String toString() {
            return "Turno #" + id +
                     " | Paciente: " + paciente.getNombreCompleto() +
                     " | Odontólogo: " + odontologo.getNombreCompleto() +
                     " | Fecha: " + fecha +
                     " | Hora: " + hora +
                     " | Consultorio: " + consultorio.getNumero();
    }
}