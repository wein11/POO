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
    public void guardar(Turno turno) {
        if (servicioPaciente.buscarPorId(turno.getPaciente().getId()) == null) {
            throw new IllegalArgumentException("El paciente indicado no existe en el sistema.");
        }
        if (servicioOdontologo.buscarPorId(turno.getOdontologo().getId()) == null) {
            throw new IllegalArgumentException("El odontólogo indicado no existe en el sistema.");
        }
        for (Turno existente : repositorio.listarTodos()) {
            if (existente.getOdontologo().getId() == turno.getOdontologo().getId() &&
                existente.getFecha().equals(turno.getFecha()) &&
                existente.getHora().equals(turno.getHora())) {
                throw new IllegalArgumentException("El odontólogo ya tiene un turno asignado en esa fecha y hora.");
            }
        }
        repositorio.guardar(turno);
    }

    @Override
    public Turno buscarPorId(long id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public void eliminarPorId(long id) {
        repositorio.eliminarPorId(id);
    }

    @Override
    public void actualizar(Turno turno) {
        repositorio.actualizar(turno);
    }

    @Override
    public List<Turno> listarTodos() {
        return repositorio.listarTodos();
    }
}
