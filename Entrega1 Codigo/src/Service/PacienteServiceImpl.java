package Service;

import Entity.Paciente;
import Repository.RepositorioPaciente;
import java.util.List;

public class PacienteServiceImpl implements iService<Paciente> {

    private RepositorioPaciente repositorio;

    public PacienteServiceImpl() {
        this.repositorio = new RepositorioPaciente();
    }

    private boolean validarDatos(Paciente paciente) {
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            System.out.println("[ERROR] El nombre no puede estar vacío.");
            return false;
        }
        if (!paciente.getNombre().trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+")) {
            System.out.println("[ERROR] El nombre solo puede contener letras.");
            return false;
        }
        if (paciente.getApellido() == null || paciente.getApellido().trim().isEmpty()) {
            System.out.println("[ERROR] El apellido no puede estar vacío.");
            return false;
        }
        if (!paciente.getApellido().trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+")) {
            System.out.println("[ERROR] El apellido solo puede contener letras.");
            return false;
        }
        if (paciente.getDni() == null || paciente.getDni() < 1000000 || paciente.getDni() > 99999999) {
            System.out.println("[ERROR] El DNI debe tener entre 7 y 8 dígitos.");
            return false;
        }
        if (paciente.getEmail() == null || paciente.getEmail().trim().isEmpty() || !paciente.getEmail().contains("@")) {
            System.out.println("[ERROR] El email debe contener '@'.");
            return false;
        }
        if (paciente.getFechaIngreso() == null) {
            System.out.println("[ERROR] La fecha de ingreso no puede estar vacía.");
            return false;
        }
        if (paciente.getDomicilio() == null) {
            System.out.println("[ERROR] El domicilio no puede estar vacío.");
            return false;
        }
        if (paciente.getDomicilio().getCalle() == null || paciente.getDomicilio().getCalle().trim().isEmpty()) {
            System.out.println("[ERROR] La calle no puede estar vacía.");
            return false;
        }
        if (paciente.getDomicilio().getNumero() <= 0) {
            System.out.println("[ERROR] El número de calle debe ser mayor a 0.");
            return false;
        }
        if (paciente.getDomicilio().getLocalidad() == null || paciente.getDomicilio().getLocalidad().trim().isEmpty()) {
            System.out.println("[ERROR] La localidad no puede estar vacía.");
            return false;
        }
        if (paciente.getDomicilio().getProvincia() == null || paciente.getDomicilio().getProvincia().trim().isEmpty()) {
            System.out.println("[ERROR] La provincia no puede estar vacía.");
            return false;
        }
        return true;
    }

    @Override
    public boolean guardar(Paciente paciente) {
        if (!validarDatos(paciente)) return false;
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
        if (!validarDatos(paciente)) return false;
        repositorio.actualizar(paciente);
        return true;
    }

    @Override
    public List<Paciente> listarTodos() {
        return repositorio.listarTodos();
    }
}
