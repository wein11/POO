import Entity.EstadoTurno;
import Entity.Paciente;
import Entity.Turno;
import Service.OdontologoServiceImpl;
import Service.PacienteServiceImpl;
import Service.TurnoServiceImpl;
import java.time.LocalDate;
import java.time.LocalTime;

public class ValidacionesTest {

    private static int pruebasEjecutadas = 0;
    private static int pruebasFallidas = 0;

    public static void main(String[] args) {
        probarValidacionesPaciente();
        probarValidacionesOdontologo();
        probarValidacionesTurno();
        probarHistorialYEstados();

        System.out.println("\n========================================");
        System.out.println("Pruebas ejecutadas: " + pruebasEjecutadas);
        System.out.println("Pruebas fallidas: " + pruebasFallidas);
        System.out.println("========================================");

        if (pruebasFallidas > 0) {
            System.exit(1);
        }
    }

    private static void probarValidacionesPaciente() {
        PacienteServiceImpl pacientes = new PacienteServiceImpl();

        esperarFalso("Paciente sin nombre", pacientes.registrarPaciente("", "Perez", 12345678, "ana@mail.com",
                LocalDate.now(), "Av Siempre Viva", 123, "Caba", "Buenos Aires"));
        esperarFalso("Paciente con nombre numérico", pacientes.registrarPaciente("Ana1", "Perez", 12345678, "ana@mail.com",
                LocalDate.now(), "Av Siempre Viva", 123, "Caba", "Buenos Aires"));
        esperarFalso("Paciente con apellido numérico", pacientes.registrarPaciente("Ana", "Perez2", 12345678, "ana@mail.com",
                LocalDate.now(), "Av Siempre Viva", 123, "Caba", "Buenos Aires"));
        esperarFalso("Paciente con DNI corto", pacientes.registrarPaciente("Ana", "Perez", 123, "ana@mail.com",
                LocalDate.now(), "Av Siempre Viva", 123, "Caba", "Buenos Aires"));
        esperarFalso("Paciente con email inválido", pacientes.registrarPaciente("Ana", "Perez", 12345678, "ana@",
                LocalDate.now(), "Av Siempre Viva", 123, "Caba", "Buenos Aires"));
        esperarFalso("Paciente con fecha futura", pacientes.registrarPaciente("Ana", "Perez", 12345678, "ana@mail.com",
                LocalDate.now().plusDays(1), "Av Siempre Viva", 123, "Caba", "Buenos Aires"));
        esperarFalso("Paciente con calle vacía", pacientes.registrarPaciente("Ana", "Perez", 12345678, "ana@mail.com",
                LocalDate.now(), "", 123, "Caba", "Buenos Aires"));
        esperarFalso("Paciente con número de calle inválido", pacientes.registrarPaciente("Ana", "Perez", 12345678, "ana@mail.com",
                LocalDate.now(), "Av Siempre Viva", 0, "Caba", "Buenos Aires"));
        esperarFalso("Paciente con localidad numérica", pacientes.registrarPaciente("Ana", "Perez", 12345678, "ana@mail.com",
                LocalDate.now(), "Av Siempre Viva", 123, "Caba123", "Buenos Aires"));
        esperarFalso("Paciente con provincia numérica", pacientes.registrarPaciente("Ana", "Perez", 12345678, "ana@mail.com",
                LocalDate.now(), "Av Siempre Viva", 123, "Caba", "Buenos Aires1"));

        esperarVerdadero("Paciente válido", pacientes.registrarPaciente("Ana", "Perez", 12345678, "ana@mail.com",
                LocalDate.now(), "Av Siempre Viva", 123, "Caba", "Buenos Aires"));
        esperarFalso("Paciente con DNI duplicado", pacientes.registrarPaciente("Luis", "Gomez", 12345678, "luis@mail.com",
                LocalDate.now(), "San Martin", 456, "Caba", "Buenos Aires"));
    }

    private static void probarValidacionesOdontologo() {
        OdontologoServiceImpl odontologos = new OdontologoServiceImpl();

        esperarFalso("Odontólogo sin nombre", odontologos.registrarOdontologo("", "Lopez", "MAT123", 1));
        esperarFalso("Odontólogo con nombre numérico", odontologos.registrarOdontologo("Juan1", "Lopez", "MAT123", 1));
        esperarFalso("Odontólogo con apellido numérico", odontologos.registrarOdontologo("Juan", "Lopez2", "MAT123", 1));
        esperarFalso("Odontólogo sin matrícula", odontologos.registrarOdontologo("Juan", "Lopez", "", 1));
        esperarFalso("Odontólogo con matrícula inválida", odontologos.registrarOdontologo("Juan", "Lopez", "M@T!", 1));
        esperarFalso("Odontólogo con tipo inválido", odontologos.registrarOdontologo("Juan", "Lopez", "MAT123", 3));

        esperarVerdadero("Odontólogo válido", odontologos.registrarOdontologo("Juan", "Lopez", "MAT123", 1));
        esperarFalso("Odontólogo con matrícula duplicada", odontologos.registrarOdontologo("Maria", "Suarez", "MAT123", 2));
    }

    private static void probarValidacionesTurno() {
        PacienteServiceImpl pacientes = new PacienteServiceImpl();
        OdontologoServiceImpl odontologos = new OdontologoServiceImpl();
        TurnoServiceImpl turnos = new TurnoServiceImpl(pacientes, odontologos);

        pacientes.registrarPaciente("Ana", "Perez", 12345678, "ana@mail.com", LocalDate.now(), "Av Siempre Viva", 123, "Caba", "Buenos Aires");
        odontologos.registrarOdontologo("Juan", "Lopez", "MAT123", 1);

        esperarFalso("Turno con paciente inexistente", turnos.registrarTurno(99, 1, LocalDate.now().plusDays(1), LocalTime.of(10, 0)));
        esperarFalso("Turno con odontólogo inexistente", turnos.registrarTurno(1, 99, LocalDate.now().plusDays(1), LocalTime.of(10, 0)));
        esperarFalso("Turno con fecha pasada", turnos.registrarTurno(1, 1, LocalDate.now().minusDays(1), LocalTime.of(10, 0)));
        esperarFalso("Turno antes de apertura", turnos.registrarTurno(1, 1, LocalDate.now().plusDays(1), LocalTime.of(7, 30)));
        esperarFalso("Turno después del cierre", turnos.registrarTurno(1, 1, LocalDate.now().plusDays(1), LocalTime.of(20, 0)));
        esperarFalso("Turno en minuto no permitido", turnos.registrarTurno(1, 1, LocalDate.now().plusDays(1), LocalTime.of(10, 15)));

        esperarVerdadero("Turno válido", turnos.registrarTurno(1, 1, LocalDate.now().plusDays(1), LocalTime.of(10, 0)));
        esperarFalso("Turno duplicado para mismo odontólogo", turnos.registrarTurno(1, 1, LocalDate.now().plusDays(1), LocalTime.of(10, 0)));
    }

    private static void probarHistorialYEstados() {
        PacienteServiceImpl pacientes = new PacienteServiceImpl();
        OdontologoServiceImpl odontologos = new OdontologoServiceImpl();
        TurnoServiceImpl turnos = new TurnoServiceImpl(pacientes, odontologos);

        pacientes.registrarPaciente("Ana", "Perez", 12345678, "ana@mail.com", LocalDate.now(), "Av Siempre Viva", 123, "Caba", "Buenos Aires");
        odontologos.registrarOdontologo("Juan", "Lopez", "MAT123", 1);
        turnos.registrarTurno(1, 1, LocalDate.now().plusDays(1), LocalTime.of(10, 0));

        Paciente paciente = pacientes.buscarPorId(1);
        esperarVerdadero("Paciente tiene historial clínico", paciente.getHistorialClinico() != null);
        esperarVerdadero("Historial registra el turno", paciente.getHistorialClinico().getTurnos().size() == 1);

        esperarVerdadero("Confirmar turno pendiente", turnos.confirmarTurno(1));
        Turno turno = turnos.buscarPorId(1);
        esperarVerdadero("Turno queda confirmado", turno.getEstado() == EstadoTurno.CONFIRMADO);
        esperarVerdadero("Completar turno confirmado", turnos.completarTurno(1));
        esperarFalso("No cancelar turno completado", turnos.cancelarTurno(1));
    }

    private static void esperarVerdadero(String nombrePrueba, boolean resultado) {
        pruebasEjecutadas++;
        if (resultado) {
            System.out.println("[OK] " + nombrePrueba);
        } else {
            pruebasFallidas++;
            System.out.println("[FALLO] " + nombrePrueba);
        }
    }

    private static void esperarFalso(String nombrePrueba, boolean resultado) {
        esperarVerdadero(nombrePrueba, !resultado);
    }
}
