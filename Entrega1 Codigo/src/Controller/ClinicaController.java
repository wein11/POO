package Controller;

import Entity.Domicilio;
import Entity.EstadoTurno;
import Entity.Odontologo;
import Entity.OdontologoEndodoncista;
import Entity.OdontologoOrtodoncista;
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
        Domicilio domicilio = new Domicilio(dato.getCalle(), dato.getNumeroCalle(), dato.getLocalidad(), dato.getProvincia(), "");
        Paciente paciente = new Paciente(0, dato.getNombre(), dato.getApellido(), dato.getDni(), dato.getEmail(), dato.getFechaIngreso(), domicilio);
        if (servicioPaciente.guardar(paciente)) {
            vista.mostrarMensaje("Paciente registrado con ID: " + paciente.getId());
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
        Domicilio domicilio = new Domicilio(dato.getCalle(), dato.getNumeroCalle(), dato.getLocalidad(), dato.getProvincia(), "");
        Paciente actualizado = new Paciente(id, dato.getNombre(), dato.getApellido(), dato.getDni(), dato.getEmail(), dato.getFechaIngreso(), domicilio);
        if (servicioPaciente.actualizar(actualizado)) {
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
        Odontologo odontologo;
        if (dato.getTipoEspecialista() == 1) {
            odontologo = new OdontologoOrtodoncista(0, dato.getNombre(), dato.getApellido(), dato.getMatricula());
        } else {
            odontologo = new OdontologoEndodoncista(0, dato.getNombre(), dato.getApellido(), dato.getMatricula());
        }
        if (servicioOdontologo.guardar(odontologo)) {
            vista.mostrarMensaje("Odontologo registrado con ID: " + odontologo.getId());
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
        Odontologo actualizado;
        if (dato.getTipoEspecialista() == 1) {
            actualizado = new OdontologoOrtodoncista(id, dato.getNombre(), dato.getApellido(), dato.getMatricula());
        } else {
            actualizado = new OdontologoEndodoncista(id, dato.getNombre(), dato.getApellido(), dato.getMatricula());
        }
        if (servicioOdontologo.actualizar(actualizado)) {
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
        Paciente paciente = servicioPaciente.buscarPorId(dato.getIdPaciente());
        Odontologo odontologo = servicioOdontologo.buscarPorId(dato.getIdOdontologo());
        Turno turno = new Turno(0, paciente, odontologo, dato.getFecha(), dato.getHora(), EstadoTurno.PENDIENTE);
        if (servicioTurno.guardar(turno)) {
            vista.mostrarMensaje("Turno registrado con ID: " + turno.getId());
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
        Turno existente = servicioTurno.buscarPorId(id);
        EstadoTurno estado = (existente != null) ? existente.getEstado() : EstadoTurno.PENDIENTE;
        vista.mostrarMensaje("Ingrese los nuevos datos para el turno con ID: " + id);
        DatoTurno dato = vista.pedirDatosTurno();
        if (dato == null) {
            vista.pausar();
            return;
        }
        Paciente paciente = servicioPaciente.buscarPorId(dato.getIdPaciente());
        Odontologo odontologo = servicioOdontologo.buscarPorId(dato.getIdOdontologo());
        Turno actualizado = new Turno(id, paciente, odontologo, dato.getFecha(), dato.getHora(), estado);
        if (servicioTurno.actualizar(actualizado)) {
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

    // ===================== SALIR =====================

    private void salir() {
        if (vista.pedirConfirmacion("Confirma que desea salir del sistema?")) {
            ejecutando = false;
            vista.mostrarMensaje("Hasta luego!");
        }
    }
}
