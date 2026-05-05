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
    public boolean guardar(Paciente paciente) {
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            System.out.println("[ERROR] El nombre del paciente no puede estar vacío.");
            return false;
        }
        if (paciente.getDni() == null || paciente.getDni() <= 0) {
            System.out.println("[ERROR] El DNI ingresado no es válido.");
            return false;
        }
        for (Paciente existente : repositorio.listarTodos()) {
            if (existente.getDni() != null && existente.getDni().equals(paciente.getDni())) {
                System.out.println("[ERROR] Ya existe un paciente registrado con ese DNI.");
                return false;
            }
        }
        repositorio.guardar(paciente);
        return true;
    }

    @Override
    public Paciente buscarPorId(long id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public boolean eliminarPorId(long id) {
        if (repositorio.buscarPorId(id) == null) {
            System.out.println("[ERROR] No se encontró un paciente con ID: " + id);
            return false;
        }
        repositorio.eliminarPorId(id);
        return true;
    }

    @Override
    public boolean actualizar(Paciente paciente) {
        if (repositorio.buscarPorId(paciente.getId()) == null) {
            System.out.println("[ERROR] No se encontró un paciente con ID: " + paciente.getId());
            return false;
        }
        repositorio.actualizar(paciente);
        return true;
    }

    @Override
    public List<Paciente> listarTodos() {
        return repositorio.listarTodos();
    }
}
