package Entity;

public abstract class Odontologo {

    private long id;
    private String nombre;
    private String apellido;
    private String matricula;
    private Especialidad especialidad;

    public Odontologo() {}

    public Odontologo(long id, String nombre, String apellido, String matricula) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
    }

    public Odontologo(long id, String nombre, String apellido, String matricula, Especialidad especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
        this.especialidad = especialidad;
    }

    public abstract int calcularDuracionConsulta();

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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getPresentacion() {
        return "Dr. " + getNombreCompleto() + " - Matrícula: " + matricula;
    }

    @Override
    public String toString() {
        return "Odontólogo #" + id + ": " + getNombreCompleto() +
               " | Matrícula: " + matricula +
               " | Especialidad: " + especialidad +
               " | Duración consulta: " + calcularDuracionConsulta() + " min";
    }
}
