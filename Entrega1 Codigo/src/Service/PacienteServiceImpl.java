package Service;

import Entity.Paciente;
import Repository.RepositorioPaciente;
import java.util.List;

public class PacienteServiceImpl implements iService<Paciente> {

    private RepositorioPaciente repositorio;

    public PacienteServiceImpl() {
        this.repositorio = new RepositorioPaciente();
    }

    @Override
    public void guardar(Paciente paciente) {
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del paciente no puede estar vacío.");
        }
        if (paciente.getDni() == null || paciente.getDni() <= 0) {
            throw new IllegalArgumentException("El DNI ingresado no es válido.");
        }
        if (repositorio.existeDni(paciente.getDni())) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con ese DNI.");
        }
        repositorio.guardar(paciente);
    }

    @Override
    public Paciente buscarPorId(long id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public void eliminarPorId(long id) {
        repositorio.eliminarPorId(id);
    }

    @Override
    public void actualizar(Paciente paciente) {
        repositorio.actualizar(paciente);
    }

    @Override
    public List<Paciente> listarTodos() {
        return repositorio.listarTodos();
    }
}
