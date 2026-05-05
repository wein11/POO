package Service;

import Entity.Turno;
import Repository.RepositorioTurno;
import java.util.List;

public class TurnoServiceImpl implements iService<Turno> {

    private RepositorioTurno repositorio;
    private PacienteServiceImpl servicioPaciente;
    private OdontologoServiceImpl servicioOdontologo;

    public TurnoServiceImpl(PacienteServiceImpl servicioPaciente, OdontologoServiceImpl servicioOdontologo) {
        this.repositorio = new RepositorioTurno();
        this.servicioPaciente = servicioPaciente;
        this.servicioOdontologo = servicioOdontologo;
    }

    @Override
    public boolean guardar(Turno turno) {
        if (turno.getPaciente() == null) {
            System.out.println("[ERROR] El paciente indicado no existe en el sistema.");
            return false;
        }
        if (turno.getOdontologo() == null) {
            System.out.println("[ERROR] El odontólogo indicado no existe en el sistema.");
            return false;
        }
        for (Turno existente : repositorio.listarTodos()) {
            if (existente.getOdontologo().getId() == turno.getOdontologo().getId() &&
                existente.getFecha().equals(turno.getFecha()) &&
                existente.getHora().equals(turno.getHora())) {
                System.out.println("[ERROR] El odontólogo ya tiene un turno asignado en esa fecha y hora.");
                return false;
            }
        }
        repositorio.guardar(turno);
        return true;
    }

    @Override
    public Turno buscarPorId(long id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public boolean eliminarPorId(long id) {
        if (repositorio.buscarPorId(id) == null) {
            System.out.println("[ERROR] No se encontró un turno con ID: " + id);
            return false;
        }
        repositorio.eliminarPorId(id);
        return true;
    }

    @Override
    public boolean actualizar(Turno turno) {
        if (repositorio.buscarPorId(turno.getId()) == null) {
            System.out.println("[ERROR] No se encontró un turno con ID: " + turno.getId());
            return false;
        }
        if (turno.getPaciente() == null) {
            System.out.println("[ERROR] El paciente indicado no existe en el sistema.");
            return false;
        }
        if (turno.getOdontologo() == null) {
            System.out.println("[ERROR] El odontólogo indicado no existe en el sistema.");
            return false;
        }
        repositorio.actualizar(turno);
        return true;
    }

    @Override
    public List<Turno> listarTodos() {
        return repositorio.listarTodos();
    }
}
