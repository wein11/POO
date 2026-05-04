package model;

import java.time.LocalDate;

public class Paciente {

    private int id;
    private String nombre;
    private String apellido;
    private Integer dni;
    private LocalDate fechaAlta;
    private Domicilio domicilio;
    private String obraSocial;
    private HistorialClinico historialClinico;

    public Paciente(int id, String nombre, String apellido, Integer dni, LocalDate fechaAlta, Domicilio domicilio, HistorialClinico historialClinico) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaAlta = fechaAlta;
        this.domicilio = domicilio;
        this.historialClinico = historialClinico;
    }

    public Paciente(int id, String nombre, String apellido, Integer dni, LocalDate fechaAlta, Domicilio domicilio, String obraSocial, HistorialClinico historialClinico) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaAlta = fechaAlta;
        this.domicilio = domicilio;
        this.obraSocial = obraSocial;
        this.historialClinico = historialClinico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public HistorialClinico getHistorialClinico() {
        return historialClinico;
    }

    public void setHistorialClinico(HistorialClinico historialClinico) {
        this.historialClinico = historialClinico;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Paciente #" + id + ": " + getNombreCompleto() +
               " | DNI: " + dni +
               " | Fecha de alta: " + fechaAlta +
               " | Domicilio: " + domicilio +
               " | Obra Social: " + obraSocial;
    }
}
