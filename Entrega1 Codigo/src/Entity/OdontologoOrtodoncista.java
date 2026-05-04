package Entity;

public class OdontologoOrtodoncista extends Odontologo {

    public OdontologoOrtodoncista() {
        super();
    }

    public OdontologoOrtodoncista(long id, String nombre, String apellido, String matricula) {
        super(id, nombre, apellido, matricula, Especialidad.ORTODONCIA);
    }

    @Override
    public int calcularDuracionConsulta() {
        return 45;
    }

    @Override
    public String toString() {
        return "Ortodoncista - " + super.toString();
    }
}
