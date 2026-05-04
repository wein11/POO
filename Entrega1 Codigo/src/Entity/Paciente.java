package Entity;

import java.time.LocalDate;

public class Paciente {

    private long id;
    private String nombre;
    private String apellido;
    private Integer dni;
    private String email;
    private LocalDate fechaIngreso;
    private Domicilio domicilio;
    private String obraSocial;
    private HistorialClinico historialClinico;

    public Paciente() {}

    public Paciente(long id, String nombre, String apellido, Integer dni, String email, LocalDate fechaIngreso, Domicilio domicilio) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
    }

    public Paciente(long id, String nombre, String apellido, Integer dni, String email, LocalDate fechaIngreso, Domicilio domicilio, String obraSocial, HistorialClinico historialClinico) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
        this.obraSocial = obraSocial;
        this.historialClinico = historialClinico;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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
               " | Email: " + email +
               " | Fecha de ingreso: " + fechaIngreso +
               " | Domicilio: " + domicilio +
               " | Obra Social: " + obraSocial;
    }
}
