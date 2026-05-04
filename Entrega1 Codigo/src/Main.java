import Controller.ClinicaController;
import View.VistaClinica;

public class Main {

    public static void main(String[] args) {
        VistaClinica vista = new VistaClinica();
        ClinicaController controller = new ClinicaController(vista);
        controller.run();
    }
}
