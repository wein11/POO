import model.Consultorio;
import model.Domicilio;
import model.Especialidad;
import model.HistorialClinico;
import model.Odontologo;
import model.Paciente;
import model.Turno;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {

        // DOMICILIOS --------------------------------------------

        // Constructor completo (calle, numero, localidad, provincia, codigoPostal, piso, especificaciones)
        Domicilio domicilio1 = new Domicilio("Av. Corrientes", 742, "Buenos Aires", "CABA", "1043", 3, "Depto B");

        // Constructor sin piso ni especificaciones
        Domicilio domicilio2 = new Domicilio("San Martín", 450, "Rosario", "Santa Fe", "2000");

        // PACIENTES --------------------------------------------
  
        // Constructor sin obra social
        Paciente paciente1 = new Paciente(1, "Paolo", "Maffei", 12345678,
                LocalDate.of(2025, 1, 10), domicilio1, null);

        // Constructor con obra social
        Paciente paciente2 = new Paciente(2, "Mateo", "Galluzzo", 87654321,
                LocalDate.of(2025, 3, 5), domicilio2, "OSDE", null);

        // ODONTÓLOGOS --------------------------------------------

        // Constructor sin especialidad
        Odontologo odontologo1 = new Odontologo(1, "Santiago", "Weinbinder", "MAT-001");

        // Constructor con especialidad (usando el enum Especialidad)
        Odontologo odontologo2 = new Odontologo(2, "Jorge Agustin", "Pereyra", "MAT-002", Especialidad.ENDODONCIA);

        // CONSULTORIOS --------------------------------------------

        // Constructor con piso
        Consultorio consultorio1 = new Consultorio(1, 101, 1);

        // Constructor sin piso
        Consultorio consultorio2 = new Consultorio(2, 203);

        // HISTORIAL CLÍNICO 

        HistorialClinico historial1 = new HistorialClinico(1, paciente1);
        HistorialClinico historial2 = new HistorialClinico(2, paciente2);

        // TURNOS --------------------------------------------

        // Constructor sin nota clínica
        Turno turno1 = new Turno(1, paciente1, odontologo1,
                LocalDate.of(2026, 4, 20), LocalTime.of(9, 30), consultorio1);

        // Constructor con nota clínica (String)
        Turno turno2 = new Turno(2, paciente2, odontologo2,
                LocalDate.of(2026, 4, 21), LocalTime.of(11, 0), "Revisión de conducto previo", consultorio2);

        // Tercer turno del mismo paciente que turno1
        Turno turno3 = new Turno(3, paciente1, odontologo2,
                LocalDate.of(2026, 4, 22), LocalTime.of(14, 0), consultorio1);

        // Vincular turnos al historial y el historial al paciente
        historial1.agregarTurno(turno1);
        historial1.agregarTurno(turno3);
        historial2.agregarTurno(turno2);
        paciente1.setHistorialClinico(historial1);
        paciente2.setHistorialClinico(historial2);

        // SALIDA POR CONSOLA --------------------------------------------

        System.out.println("DOMICILIOS --------------------------------------------");
        System.out.println(domicilio1);
        System.out.println(domicilio2);

        System.out.println("\nPACIENTES --------------------------------------------");
        System.out.println(paciente1);
        System.out.println(paciente2);

        System.out.println("\nODONTOLOGOS --------------------------------------------");
        System.out.println(odontologo1);
        System.out.println(odontologo2);

        System.out.println("\nCONSULTORIOS --------------------------------------------");
        System.out.println(consultorio1);
        System.out.println(consultorio2);

        System.out.println("\nTURNOS --------------------------------------------");
        System.out.println(turno1);
        System.out.println(turno2);
        System.out.println(turno3);

        System.out.println("\nDETALLE DE TURNO --------------------------------------------");
        System.out.println(turno1.getDetalle());
        System.out.println("---");
        System.out.println(turno2.getDetalle());

        System.out.println("\nHISTORIAL CLINICO --------------------------------------------");
        System.out.println(historial1);
    }
}