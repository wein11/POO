package Service;

import Entity.Domicilio;
import Entity.HistorialClinico;
import Entity.Paciente;
import Repository.RepositorioPaciente;
import java.time.LocalDate;
import java.util.List;

public class PacienteServiceImpl implements iService<Paciente> {

    private static final String SOLO_LETRAS = "[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+";
    private static final String EMAIL_VALIDO = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private RepositorioPaciente repositorio;

    public PacienteServiceImpl() {
        this.repositorio = new RepositorioPaciente();
    }

    private boolean validarDatos(Paciente paciente) {
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            System.out.println("[ERROR] El nombre no puede estar vacío.");
            return false;
        }
        if (!paciente.getNombre().trim().matches(SOLO_LETRAS)) {
            System.out.println("[ERROR] El nombre solo puede contener letras.");
            return false;
        }
        if (paciente.getApellido() == null || paciente.getApellido().trim().isEmpty()) {
            System.out.println("[ERROR] El apellido no puede estar vacío.");
            return false;
        }
        if (!paciente.getApellido().trim().matches(SOLO_LETRAS)) {
            System.out.println("[ERROR] El apellido solo puede contener letras.");
            return false;
        }
        if (paciente.getDni() == null || paciente.getDni() < 1000000 || paciente.getDni() > 99999999) {
            System.out.println("[ERROR] El DNI debe tener entre 7 y 8 dígitos.");
            return false;
        }
        if (paciente.getEmail() == null || paciente.getEmail().trim().isEmpty() || !paciente.getEmail().trim().matches(EMAIL_VALIDO)) {
            System.out.println("[ERROR] El email debe tener un formato válido.");
            return false;
        }
        if (paciente.getFechaIngreso() == null) {
            System.out.println("[ERROR] La fecha de ingreso no puede estar vacía.");
            return false;
        }
        if (paciente.getFechaIngreso().isAfter(LocalDate.now())) {
            System.out.println("[ERROR] La fecha de ingreso no puede ser futura.");
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
        if (!paciente.getDomicilio().getLocalidad().trim().matches(SOLO_LETRAS)) {
            System.out.println("[ERROR] La localidad solo puede contener letras.");
            return false;
        }
        if (paciente.getDomicilio().getProvincia() == null || paciente.getDomicilio().getProvincia().trim().isEmpty()) {
            System.out.println("[ERROR] La provincia no puede estar vacía.");
            return false;
        }
        if (!paciente.getDomicilio().getProvincia().trim().matches(SOLO_LETRAS)) {
            System.out.println("[ERROR] La provincia solo puede contener letras.");
            return false;
        }
        return true;
    }

    @Override
    public boolean guardar(Paciente paciente) {
        if (!validarDatos(paciente)) return false;
        if (existeDniEnOtroPaciente(paciente.getDni(), paciente.getId())) return false;
        repositorio.guardar(paciente);
        asegurarHistorialClinico(paciente);
        return true;
    }

    public boolean registrarPaciente(String nombre, String apellido, int dni, String email, LocalDate fechaIngreso,
                                     String calle, int numeroCalle, String localidad, String provincia) {
        Domicilio domicilio = crearDomicilio(calle, numeroCalle, localidad, provincia);
        Paciente paciente = new Paciente(0, nombre, apellido, dni, email, fechaIngreso, domicilio);
        return guardar(paciente);
    }

    @Override
    public Paciente buscarPorId(long id) {
        Paciente paciente = repositorio.buscarPorId(id);
        if (paciente != null) {
            asegurarHistorialClinico(paciente);
        }
        return paciente;
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
        Paciente existente = repositorio.buscarPorId(paciente.getId());
        if (existente == null) {
            System.out.println("[ERROR] No se encontró un paciente con ID: " + paciente.getId());
            return false;
        }
        if (!validarDatos(paciente)) return false;
        if (existeDniEnOtroPaciente(paciente.getDni(), paciente.getId())) return false;
        paciente.setHistorialClinico(existente.getHistorialClinico());
        asegurarHistorialClinico(paciente);
        repositorio.actualizar(paciente);
        return true;
    }

    public boolean actualizarPaciente(long id, String nombre, String apellido, int dni, String email, LocalDate fechaIngreso,
                                      String calle, int numeroCalle, String localidad, String provincia) {
        Domicilio domicilio = crearDomicilio(calle, numeroCalle, localidad, provincia);
        Paciente paciente = new Paciente(id, nombre, apellido, dni, email, fechaIngreso, domicilio);
        return actualizar(paciente);
    }

    @Override
    public List<Paciente> listarTodos() {
        for (Paciente paciente : repositorio.listarTodos()) {
            asegurarHistorialClinico(paciente);
        }
        return repositorio.listarTodos();
    }

    public HistorialClinico obtenerHistorialClinico(long idPaciente) {
        Paciente paciente = repositorio.buscarPorId(idPaciente);
        if (paciente == null) {
            System.out.println("[ERROR] No se encontró un paciente con ID: " + idPaciente);
            return null;
        }
        asegurarHistorialClinico(paciente);
        return paciente.getHistorialClinico();
    }

    private Domicilio crearDomicilio(String calle, int numeroCalle, String localidad, String provincia) {
        return new Domicilio(calle, numeroCalle, localidad, provincia, "");
    }

    private boolean existeDniEnOtroPaciente(Integer dni, long idPaciente) {
        for (Paciente existente : repositorio.listarTodos()) {
            if (existente.getId() != idPaciente && existente.getDni() != null && existente.getDni().equals(dni)) {
                System.out.println("[ERROR] Ya existe un paciente registrado con ese DNI.");
                return true;
            }
        }
        return false;
    }

    private void asegurarHistorialClinico(Paciente paciente) {
        if (paciente.getHistorialClinico() == null) {
            paciente.setHistorialClinico(new HistorialClinico(paciente.getId(), paciente, "Sin antecedentes registrados"));
        } else {
            paciente.getHistorialClinico().setPaciente(paciente);
        }
    }
}
