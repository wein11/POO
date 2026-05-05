package Service;

import Entity.EstadoTurno;
import Entity.HistorialClinico;
import Entity.Odontologo;
import Entity.Paciente;
import Entity.Turno;
import Repository.RepositorioTurno;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TurnoServiceImpl implements iService<Turno> {

    private static final LocalTime HORA_APERTURA = LocalTime.of(8, 0);
    private static final LocalTime HORA_CIERRE = LocalTime.of(20, 0);

    private RepositorioTurno repositorio;
    private PacienteServiceImpl servicioPaciente;
    private OdontologoServiceImpl servicioOdontologo;

    public TurnoServiceImpl(PacienteServiceImpl servicioPaciente, OdontologoServiceImpl servicioOdontologo) {
        this.repositorio = new RepositorioTurno();
        this.servicioPaciente = servicioPaciente;
        this.servicioOdontologo = servicioOdontologo;
    }

    private boolean validarDatos(Turno turno) {
        if (turno.getPaciente() == null) {
            System.out.println("[ERROR] El paciente indicado no existe en el sistema.");
            return false;
        }
        if (turno.getOdontologo() == null) {
            System.out.println("[ERROR] El odontólogo indicado no existe en el sistema.");
            return false;
        }
        if (turno.getFecha() == null) {
            System.out.println("[ERROR] La fecha del turno no puede estar vacía.");
            return false;
        }
        if (turno.getFecha().isBefore(LocalDate.now())) {
            System.out.println("[ERROR] La fecha del turno no puede ser anterior a hoy.");
            return false;
        }
        if (turno.getHora() == null) {
            System.out.println("[ERROR] La hora del turno no puede estar vacía.");
            return false;
        }
        if (turno.getHora().isBefore(HORA_APERTURA) || !turno.getHora().isBefore(HORA_CIERRE)) {
            System.out.println("[ERROR] El horario del turno debe estar entre 08:00 y 19:59.");
            return false;
        }
        if (turno.getHora().getMinute() != 0 && turno.getHora().getMinute() != 30) {
            System.out.println("[ERROR] Los turnos solo pueden darse en horarios cada 30 minutos.");
            return false;
        }
        return true;
    }

    @Override
    public boolean guardar(Turno turno) {
        if (!validarDatos(turno)) return false;
        if (odontologoOcupado(turno, turno.getId())) return false;
        repositorio.guardar(turno);
        agregarTurnoAlHistorial(turno);
        return true;
    }

    public boolean registrarTurno(long idPaciente, long idOdontologo, LocalDate fecha, LocalTime hora) {
        Turno turno = crearTurno(0, idPaciente, idOdontologo, fecha, hora, EstadoTurno.PENDIENTE);
        return guardar(turno);
    }

    @Override
    public Turno buscarPorId(long id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public boolean eliminarPorId(long id) {
        Turno turno = repositorio.buscarPorId(id);
        if (turno == null) {
            System.out.println("[ERROR] No se encontró un turno con ID: " + id);
            return false;
        }
        quitarTurnoDelHistorial(turno);
        repositorio.eliminarPorId(id);
        return true;
    }

    @Override
    public boolean actualizar(Turno turno) {
        Turno existente = repositorio.buscarPorId(turno.getId());
        if (existente == null) {
            System.out.println("[ERROR] No se encontró un turno con ID: " + turno.getId());
            return false;
        }
        if (!validarDatos(turno)) return false;
        if (odontologoOcupado(turno, turno.getId())) return false;
        quitarTurnoDelHistorial(existente);
        repositorio.actualizar(turno);
        agregarTurnoAlHistorial(turno);
        return true;
    }

    public boolean actualizarTurno(long id, long idPaciente, long idOdontologo, LocalDate fecha, LocalTime hora) {
        Turno existente = repositorio.buscarPorId(id);
        if (existente == null) {
            System.out.println("[ERROR] No se encontró un turno con ID: " + id);
            return false;
        }
        Turno turno = crearTurno(id, idPaciente, idOdontologo, fecha, hora, existente.getEstado());
        return actualizar(turno);
    }

    @Override
    public List<Turno> listarTodos() {
        return repositorio.listarTodos();
    }

    public boolean confirmarTurno(long id) {
        return cambiarEstadoTurno(id, EstadoTurno.CONFIRMADO);
    }

    public boolean cancelarTurno(long id) {
        return cambiarEstadoTurno(id, EstadoTurno.CANCELADO);
    }

    public boolean completarTurno(long id) {
        return cambiarEstadoTurno(id, EstadoTurno.COMPLETADO);
    }

    private Turno crearTurno(long id, long idPaciente, long idOdontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado) {
        Paciente paciente = servicioPaciente.buscarPorId(idPaciente);
        Odontologo odontologo = servicioOdontologo.buscarPorId(idOdontologo);
        return new Turno(id, paciente, odontologo, fecha, hora, estado);
    }

    private boolean odontologoOcupado(Turno turno, long idTurno) {
        if (turno.getEstado() == EstadoTurno.CANCELADO) {
            return false;
        }
        for (Turno existente : repositorio.listarTodos()) {
            if (existente.getId() != idTurno &&
                existente.getEstado() != EstadoTurno.CANCELADO &&
                existente.getOdontologo().getId() == turno.getOdontologo().getId() &&
                existente.getFecha().equals(turno.getFecha()) &&
                existente.getHora().equals(turno.getHora())) {
                System.out.println("[ERROR] El odontólogo ya tiene un turno asignado en esa fecha y hora.");
                return true;
            }
        }
        return false;
    }

    private boolean cambiarEstadoTurno(long id, EstadoTurno nuevoEstado) {
        Turno turno = repositorio.buscarPorId(id);
        if (turno == null) {
            System.out.println("[ERROR] No se encontró un turno con ID: " + id);
            return false;
        }
        if (!estadoPermitido(turno.getEstado(), nuevoEstado)) {
            System.out.println("[ERROR] No se puede cambiar el turno de " + turno.getEstado() + " a " + nuevoEstado + ".");
            return false;
        }
        turno.setEstado(nuevoEstado);
        repositorio.actualizar(turno);
        actualizarTurnoEnHistorial(turno);
        return true;
    }

    private boolean estadoPermitido(EstadoTurno estadoActual, EstadoTurno nuevoEstado) {
        if (estadoActual == EstadoTurno.CANCELADO || estadoActual == EstadoTurno.COMPLETADO) {
            return false;
        }
        if (nuevoEstado == EstadoTurno.CONFIRMADO) {
            return estadoActual == EstadoTurno.PENDIENTE;
        }
        if (nuevoEstado == EstadoTurno.CANCELADO) {
            return estadoActual == EstadoTurno.PENDIENTE || estadoActual == EstadoTurno.CONFIRMADO;
        }
        if (nuevoEstado == EstadoTurno.COMPLETADO) {
            return estadoActual == EstadoTurno.CONFIRMADO;
        }
        return false;
    }

    private void agregarTurnoAlHistorial(Turno turno) {
        HistorialClinico historial = servicioPaciente.obtenerHistorialClinico(turno.getPaciente().getId());
        if (historial != null) {
            historial.actualizarTurno(turno);
        }
    }

    private void quitarTurnoDelHistorial(Turno turno) {
        HistorialClinico historial = servicioPaciente.obtenerHistorialClinico(turno.getPaciente().getId());
        if (historial != null) {
            historial.eliminarTurnoPorId(turno.getId());
        }
    }

    private void actualizarTurnoEnHistorial(Turno turno) {
        HistorialClinico historial = servicioPaciente.obtenerHistorialClinico(turno.getPaciente().getId());
        if (historial != null) {
            historial.actualizarTurno(turno);
        }
    }
}
