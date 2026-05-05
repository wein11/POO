package Controller;

import Entity.HistorialClinico;
import Entity.Odontologo;
import Entity.Paciente;
import Entity.Turno;
import Service.OdontologoServiceImpl;
import Service.PacienteServiceImpl;
import Service.TurnoServiceImpl;
import View.DatoOdontologo;
import View.DatoPaciente;
import View.DatoTurno;
import View.VistaClinica;
import java.util.List;

public class ClinicaController implements Runnable {

    private VistaClinica vista;
    private PacienteServiceImpl servicioPaciente;
    private OdontologoServiceImpl servicioOdontologo;
    private TurnoServiceImpl servicioTurno;
    private boolean ejecutando;

    public ClinicaController(VistaClinica vista) {
        this.vista = vista;
        this.servicioPaciente = new PacienteServiceImpl();
        this.servicioOdontologo = new OdontologoServiceImpl();
        this.servicioTurno = new TurnoServiceImpl(servicioPaciente, servicioOdontologo);
        this.ejecutando = true;
    }

    @Override
    public void run() {
        while (ejecutando) {
            vista.mostrarMenuInicial();
            int opcion = vista.pedirOpcion();
            switch (opcion) {
                case 1 -> menuPacientes();
                case 2 -> menuOdontologos();
                case 3 -> menuTurnos();
                case 0 -> salir();
                default -> vista.mostrarError("Opcion invalida. Intente nuevamente.");
            }
        }
        vista.cerrar();
    }

    // ===================== PACIENTES =====================

    private void menuPacientes() {
        boolean enSubmenu = true;
        while (enSubmenu) {
            vista.mostrarMenuPacientes();
            int op = vista.pedirOpcion();
            switch (op) {
                case 1 -> registrarPaciente();
                case 2 -> buscarPaciente();
                case 3 -> eliminarPaciente();
                case 4 -> actualizarPaciente();
                case 5 -> listarPacientes();
                case 6 -> mostrarHistorialClinico();
                case 0 -> enSubmenu = false;
                default -> vista.mostrarError("Opcion invalida.");
            }
        }
    }

    private void registrarPaciente() {
        DatoPaciente dato = vista.pedirDatosPaciente();
        if (dato == null) {
            vista.pausar();
            return;
        }
        if (servicioPaciente.registrarPaciente(dato.getNombre(), dato.getApellido(), dato.getDni(), dato.getEmail(), dato.getFechaIngreso(),
                dato.getCalle(), dato.getNumeroCalle(), dato.getLocalidad(), dato.getProvincia())) {
            vista.mostrarMensaje("Paciente registrado correctamente.");
        }
        vista.pausar();
    }

    private void buscarPaciente() {
        long id = vista.pedirId();
        Paciente paciente = servicioPaciente.buscarPorId(id);
        if (paciente != null) {
            vista.mostrarMensaje(paciente.toString());
        } else {
            vista.mostrarError("No se encontro un paciente con ID: " + id);
        }
        vista.pausar();
    }

    private void eliminarPaciente() {
        long id = vista.pedirId();
        if (vista.pedirConfirmacion("Confirma la eliminacion del paciente con ID " + id + "?")) {
            if (servicioPaciente.eliminarPorId(id)) {
                vista.mostrarMensaje("Paciente eliminado correctamente.");
            }
        } else {
            vista.mostrarMensaje("Operacion cancelada.");
        }
        vista.pausar();
    }

    private void actualizarPaciente() {
        long id = vista.pedirId();
        vista.mostrarMensaje("Ingrese los nuevos datos para el paciente con ID: " + id);
        DatoPaciente dato = vista.pedirDatosPaciente();
        if (dato == null) {
            vista.pausar();
            return;
        }
        if (servicioPaciente.actualizarPaciente(id, dato.getNombre(), dato.getApellido(), dato.getDni(), dato.getEmail(), dato.getFechaIngreso(),
                dato.getCalle(), dato.getNumeroCalle(), dato.getLocalidad(), dato.getProvincia())) {
            vista.mostrarMensaje("Paciente actualizado correctamente.");
        }
        vista.pausar();
    }

    private void listarPacientes() {
        List<Paciente> pacientes = servicioPaciente.listarTodos();
        if (pacientes.isEmpty()) {
            vista.mostrarMensaje("No hay pacientes registrados.");
        } else {
            System.out.println("\n--- LISTA DE PACIENTES ---");
            for (Paciente p : pacientes) {
                System.out.println(p);
            }
        }
        vista.pausar();
    }

    private void mostrarHistorialClinico() {
        long id = vista.pedirId();
        HistorialClinico historial = servicioPaciente.obtenerHistorialClinico(id);
        if (historial != null) {
            System.out.println("\n--- HISTORIAL CLINICO ---");
            System.out.println(historial);
        }
        vista.pausar();
    }

    // ===================== ODONTOLOGOS =====================

    private void menuOdontologos() {
        boolean enSubmenu = true;
        while (enSubmenu) {
            vista.mostrarMenuOdontologos();
            int op = vista.pedirOpcion();
            switch (op) {
                case 1 -> registrarOdontologo();
                case 2 -> buscarOdontologo();
                case 3 -> eliminarOdontologo();
                case 4 -> actualizarOdontologo();
                case 5 -> listarOdontologos();
                case 0 -> enSubmenu = false;
                default -> vista.mostrarError("Opcion invalida.");
            }
        }
    }

    private void registrarOdontologo() {
        DatoOdontologo dato = vista.pedirDatosOdontologo();
        if (dato == null) {
            vista.pausar();
            return;
        }
        if (servicioOdontologo.registrarOdontologo(dato.getNombre(), dato.getApellido(), dato.getMatricula(), dato.getTipoEspecialista())) {
            vista.mostrarMensaje("Odontologo registrado correctamente.");
        }
        vista.pausar();
    }

    private void buscarOdontologo() {
        long id = vista.pedirId();
        Odontologo odontologo = servicioOdontologo.buscarPorId(id);
        if (odontologo != null) {
            vista.mostrarMensaje(odontologo.toString());
        } else {
            vista.mostrarError("No se encontro un odontologo con ID: " + id);
        }
        vista.pausar();
    }

    private void eliminarOdontologo() {
        long id = vista.pedirId();
        if (vista.pedirConfirmacion("Confirma la eliminacion del odontologo con ID " + id + "?")) {
            if (servicioOdontologo.eliminarPorId(id)) {
                vista.mostrarMensaje("Odontologo eliminado correctamente.");
            }
        } else {
            vista.mostrarMensaje("Operacion cancelada.");
        }
        vista.pausar();
    }

    private void actualizarOdontologo() {
        long id = vista.pedirId();
        vista.mostrarMensaje("Ingrese los nuevos datos para el odontologo con ID: " + id);
        DatoOdontologo dato = vista.pedirDatosOdontologo();
        if (dato == null) {
            vista.pausar();
            return;
        }
        if (servicioOdontologo.actualizarOdontologo(id, dato.getNombre(), dato.getApellido(), dato.getMatricula(), dato.getTipoEspecialista())) {
            vista.mostrarMensaje("Odontologo actualizado correctamente.");
        }
        vista.pausar();
    }

    private void listarOdontologos() {
        List<Odontologo> odontologos = servicioOdontologo.listarTodos();
        if (odontologos.isEmpty()) {
            vista.mostrarMensaje("No hay odontologos registrados.");
        } else {
            System.out.println("\n--- LISTA DE ODONTOLOGOS ---");
            for (Odontologo o : odontologos) {
                System.out.println(o);
            }
        }
        vista.pausar();
    }

    // ===================== TURNOS =====================

    private void menuTurnos() {
        boolean enSubmenu = true;
        while (enSubmenu) {
            vista.mostrarMenuTurnos();
            int op = vista.pedirOpcion();
            switch (op) {
                case 1 -> registrarTurno();
                case 2 -> buscarTurno();
                case 3 -> eliminarTurno();
                case 4 -> actualizarTurno();
                case 5 -> listarTurnos();
                case 6 -> confirmarTurno();
                case 7 -> cancelarTurno();
                case 8 -> completarTurno();
                case 0 -> enSubmenu = false;
                default -> vista.mostrarError("Opcion invalida.");
            }
        }
    }

    private void registrarTurno() {
        DatoTurno dato = vista.pedirDatosTurno();
        if (dato == null) {
            vista.pausar();
            return;
        }
        if (servicioTurno.registrarTurno(dato.getIdPaciente(), dato.getIdOdontologo(), dato.getFecha(), dato.getHora())) {
            vista.mostrarMensaje("Turno registrado correctamente.");
        }
        vista.pausar();
    }

    private void buscarTurno() {
        long id = vista.pedirId();
        Turno turno = servicioTurno.buscarPorId(id);
        if (turno != null) {
            vista.mostrarMensaje(turno.toString());
        } else {
            vista.mostrarError("No se encontro un turno con ID: " + id);
        }
        vista.pausar();
    }

    private void eliminarTurno() {
        long id = vista.pedirId();
        if (vista.pedirConfirmacion("Confirma la eliminacion del turno con ID " + id + "?")) {
            if (servicioTurno.eliminarPorId(id)) {
                vista.mostrarMensaje("Turno eliminado correctamente.");
            }
        } else {
            vista.mostrarMensaje("Operacion cancelada.");
        }
        vista.pausar();
    }

    private void actualizarTurno() {
        long id = vista.pedirId();
        vista.mostrarMensaje("Ingrese los nuevos datos para el turno con ID: " + id);
        DatoTurno dato = vista.pedirDatosTurno();
        if (dato == null) {
            vista.pausar();
            return;
        }
        if (servicioTurno.actualizarTurno(id, dato.getIdPaciente(), dato.getIdOdontologo(), dato.getFecha(), dato.getHora())) {
            vista.mostrarMensaje("Turno actualizado correctamente.");
        }
        vista.pausar();
    }

    private void listarTurnos() {
        List<Turno> turnos = servicioTurno.listarTodos();
        if (turnos.isEmpty()) {
            vista.mostrarMensaje("No hay turnos registrados.");
        } else {
            System.out.println("\n--- LISTA DE TURNOS ---");
            for (Turno t : turnos) {
                System.out.println(t);
            }
        }
        vista.pausar();
    }

    private void confirmarTurno() {
        long id = vista.pedirId();
        if (servicioTurno.confirmarTurno(id)) {
            vista.mostrarMensaje("Turno confirmado correctamente.");
        }
        vista.pausar();
    }

    private void cancelarTurno() {
        long id = vista.pedirId();
        if (servicioTurno.cancelarTurno(id)) {
            vista.mostrarMensaje("Turno cancelado correctamente.");
        }
        vista.pausar();
    }

    private void completarTurno() {
        long id = vista.pedirId();
        if (servicioTurno.completarTurno(id)) {
            vista.mostrarMensaje("Turno completado correctamente.");
        }
        vista.pausar();
    }

    // ===================== SALIR =====================

    private void salir() {
        if (vista.pedirConfirmacion("Confirma que desea salir del sistema?")) {
            ejecutando = false;
            vista.mostrarMensaje("Hasta luego!");
        }
    }
}
