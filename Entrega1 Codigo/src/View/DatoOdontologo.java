package View;

public class DatoOdontologo extends Dato {

    private String nombre;
    private String apellido;
    private String matricula;
    private int tipoEspecialista;

    public DatoOdontologo() {
        this.tipo = 2;
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

    public int getTipoEspecialista() {
        return tipoEspecialista;
    }

    public void setTipoEspecialista(int tipoEspecialista) {
        this.tipoEspecialista = tipoEspecialista;
    }
}
