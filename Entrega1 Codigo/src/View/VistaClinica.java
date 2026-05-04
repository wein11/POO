package View;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class VistaClinica {

    private Scanner scanner;

    public VistaClinica() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenuInicial() {
        System.out.println("\n========================================");
        System.out.println("      CLINICA ODONTOLOGICA SONRISA FELIZ");
        System.out.println("========================================");
        System.out.println("  1. Gestion de Pacientes");
        System.out.println("  2. Gestion de Odontologos");
        System.out.println("  3. Gestion de Turnos");
        System.out.println("  0. Salir");
        System.out.println("========================================");
        System.out.print("Seleccione una opcion: ");
    }

    public void mostrarMenuPacientes() {
        System.out.println("\n--- MENU PACIENTES ---");
        System.out.println("  1. Registrar paciente");
        System.out.println("  2. Buscar paciente por ID");
        System.out.println("  3. Eliminar paciente");
        System.out.println("  4. Actualizar paciente");
        System.out.println("  5. Listar todos los pacientes");
        System.out.println("  0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");
    }

    public void mostrarMenuOdontologos() {
        System.out.println("\n--- MENU ODONTOLOGOS ---");
        System.out.println("  1. Registrar odontologo");
        System.out.println("  2. Buscar odontologo por ID");
        System.out.println("  3. Eliminar odontologo");
        System.out.println("  4. Actualizar odontologo");
        System.out.println("  5. Listar todos los odontologos");
        System.out.println("  0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");
    }

    public void mostrarMenuTurnos() {
        System.out.println("\n--- MENU TURNOS ---");
        System.out.println("  1. Registrar turno");
        System.out.println("  2. Buscar turno por ID");
        System.out.println("  3. Eliminar turno");
        System.out.println("  4. Actualizar turno");
        System.out.println("  5. Listar todos los turnos");
        System.out.println("  0. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");
    }

    public int pedirOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public long pedirId() {
        System.out.print("Ingrese el ID: ");
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean pedirConfirmacion(String mensaje) {
        System.out.print(mensaje + " (s/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("[OK] " + mensaje);
    }

    public void mostrarError(String error) {
        System.out.println("[ERROR] " + error);
    }

    public void pausar() {
        System.out.println("Presione ENTER para continuar...");
        scanner.nextLine();
    }

    public void cerrar() {
        scanner.close();
    }

    public DatoPaciente pedirDatosPaciente() {
        DatoPaciente dato = new DatoPaciente();
        System.out.print("Nombre: ");
        dato.setNombre(scanner.nextLine().trim());
        System.out.print("Apellido: ");
        dato.setApellido(scanner.nextLine().trim());
        System.out.print("DNI: ");
        dato.setDni(Integer.parseInt(scanner.nextLine().trim()));
        System.out.print("Email: ");
        dato.setEmail(scanner.nextLine().trim());
        System.out.print("Fecha de ingreso (AAAA-MM-DD): ");
        dato.setFechaIngreso(LocalDate.parse(scanner.nextLine().trim()));
        System.out.println("-- Domicilio --");
        System.out.print("Calle: ");
        dato.setCalle(scanner.nextLine().trim());
        System.out.print("Numero: ");
        dato.setNumeroCalle(Integer.parseInt(scanner.nextLine().trim()));
        System.out.print("Localidad: ");
        dato.setLocalidad(scanner.nextLine().trim());
        System.out.print("Provincia: ");
        dato.setProvincia(scanner.nextLine().trim());
        return dato;
    }

    public DatoOdontologo pedirDatosOdontologo() {
        DatoOdontologo dato = new DatoOdontologo();
        System.out.print("Nombre: ");
        dato.setNombre(scanner.nextLine().trim());
        System.out.print("Apellido: ");
        dato.setApellido(scanner.nextLine().trim());
        System.out.print("Matricula: ");
        dato.setMatricula(scanner.nextLine().trim());
        System.out.println("Tipo de especialista: 1=Ortodoncista  2=Endodoncista");
        System.out.print("Opcion: ");
        dato.setTipoEspecialista(Integer.parseInt(scanner.nextLine().trim()));
        return dato;
    }

    public DatoTurno pedirDatosTurno() {
        DatoTurno dato = new DatoTurno();
        System.out.print("ID del Paciente: ");
        dato.setIdPaciente(Long.parseLong(scanner.nextLine().trim()));
        System.out.print("ID del Odontologo: ");
        dato.setIdOdontologo(Long.parseLong(scanner.nextLine().trim()));
        System.out.print("Fecha (AAAA-MM-DD): ");
        dato.setFecha(LocalDate.parse(scanner.nextLine().trim()));
        System.out.print("Hora (HH:MM): ");
        dato.setHora(LocalTime.parse(scanner.nextLine().trim()));
        return dato;
    }
}
