package Repository;

import Entity.Paciente;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPaciente {

    private List<Paciente> pacientes;
    private long contadorId;

    public RepositorioPaciente() {
        this.pacientes = new ArrayList<>();
        this.contadorId = 1;
    }

    public void guardar(Paciente paciente) {
        paciente.setId(contadorId++);
        pacientes.add(paciente);
    }

    public Paciente buscarPorId(long id) {
        for (Paciente p : pacientes) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public void eliminarPorId(long id) {
        pacientes.removeIf(p -> p.getId() == id);
    }

    public void actualizar(Paciente paciente) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() == paciente.getId()) {
                pacientes.set(i, paciente);
                return;
            }
        }
    }

    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes);
    }

}
