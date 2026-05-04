package Entity;

public class OdontologoEndodoncista extends Odontologo {

    public OdontologoEndodoncista() {
        super();
    }

    public OdontologoEndodoncista(long id, String nombre, String apellido, String matricula) {
        super(id, nombre, apellido, matricula, Especialidad.ENDODONCIA);
    }

    @Override
    public int calcularDuracionConsulta() {
        return 60;
    }

    @Override
    public String toString() {
        return "Endodoncista - " + super.toString();
    }
}
