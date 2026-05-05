package Service;

import Entity.Odontologo;
import Entity.OdontologoEndodoncista;
import Entity.OdontologoOrtodoncista;
import Repository.RepositorioOdontologo;
import java.util.List;

public class OdontologoServiceImpl implements iService<Odontologo> {

    private static final String SOLO_LETRAS = "[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+";
    private static final String MATRICULA_VALIDA = "[A-Za-z0-9-]{3,20}";

    private RepositorioOdontologo repositorio;

    public OdontologoServiceImpl() {
        this.repositorio = new RepositorioOdontologo();
    }

    private boolean validarDatos(Odontologo odontologo) {
        if (odontologo.getNombre() == null || odontologo.getNombre().trim().isEmpty()) {
            System.out.println("[ERROR] El nombre no puede estar vacío.");
            return false;
        }
        if (!odontologo.getNombre().trim().matches(SOLO_LETRAS)) {
            System.out.println("[ERROR] El nombre solo puede contener letras.");
            return false;
        }
        if (odontologo.getApellido() == null || odontologo.getApellido().trim().isEmpty()) {
            System.out.println("[ERROR] El apellido no puede estar vacío.");
            return false;
        }
        if (!odontologo.getApellido().trim().matches(SOLO_LETRAS)) {
            System.out.println("[ERROR] El apellido solo puede contener letras.");
            return false;
        }
        if (odontologo.getMatricula() == null || odontologo.getMatricula().trim().isEmpty()) {
            System.out.println("[ERROR] La matrícula no puede estar vacía.");
            return false;
        }
        if (!odontologo.getMatricula().trim().matches(MATRICULA_VALIDA)) {
            System.out.println("[ERROR] La matrícula debe tener entre 3 y 20 caracteres alfanuméricos o guiones.");
            return false;
        }
        return true;
    }

    @Override
    public boolean guardar(Odontologo odontologo) {
        if (!validarDatos(odontologo)) return false;
        if (existeMatriculaEnOtroOdontologo(odontologo.getMatricula(), odontologo.getId())) return false;
        repositorio.guardar(odontologo);
        return true;
    }

    public boolean registrarOdontologo(String nombre, String apellido, String matricula, int tipoEspecialista) {
        Odontologo odontologo = crearOdontologo(0, nombre, apellido, matricula, tipoEspecialista);
        if (odontologo == null) return false;
        return guardar(odontologo);
    }

    @Override
    public Odontologo buscarPorId(long id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public boolean eliminarPorId(long id) {
        if (repositorio.buscarPorId(id) == null) {
            System.out.println("[ERROR] No se encontró un odontólogo con ID: " + id);
            return false;
        }
        repositorio.eliminarPorId(id);
        return true;
    }

    @Override
    public boolean actualizar(Odontologo odontologo) {
        if (repositorio.buscarPorId(odontologo.getId()) == null) {
            System.out.println("[ERROR] No se encontró un odontólogo con ID: " + odontologo.getId());
            return false;
        }
        if (!validarDatos(odontologo)) return false;
        if (existeMatriculaEnOtroOdontologo(odontologo.getMatricula(), odontologo.getId())) return false;
        repositorio.actualizar(odontologo);
        return true;
    }

    public boolean actualizarOdontologo(long id, String nombre, String apellido, String matricula, int tipoEspecialista) {
        Odontologo odontologo = crearOdontologo(id, nombre, apellido, matricula, tipoEspecialista);
        if (odontologo == null) return false;
        return actualizar(odontologo);
    }

    @Override
    public List<Odontologo> listarTodos() {
        return repositorio.listarTodos();
    }

    private Odontologo crearOdontologo(long id, String nombre, String apellido, String matricula, int tipoEspecialista) {
        if (tipoEspecialista == 1) {
            return new OdontologoOrtodoncista(id, nombre, apellido, matricula);
        }
        if (tipoEspecialista == 2) {
            return new OdontologoEndodoncista(id, nombre, apellido, matricula);
        }
        System.out.println("[ERROR] Opción de especialista inválida. Ingrese 1 o 2.");
        return null;
    }

    private boolean existeMatriculaEnOtroOdontologo(String matricula, long idOdontologo) {
        for (Odontologo existente : repositorio.listarTodos()) {
            if (existente.getId() != idOdontologo && existente.getMatricula().equals(matricula)) {
                System.out.println("[ERROR] Ya existe un odontólogo registrado con esa matrícula.");
                return true;
            }
        }
        return false;
    }
}
