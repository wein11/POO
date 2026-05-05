package Service;

import Entity.Odontologo;
import Repository.RepositorioOdontologo;
import java.util.List;

public class OdontologoServiceImpl implements iService<Odontologo> {

    private RepositorioOdontologo repositorio;

    public OdontologoServiceImpl() {
        this.repositorio = new RepositorioOdontologo();
    }

    private boolean validarDatos(Odontologo odontologo) {
        if (odontologo.getNombre() == null || odontologo.getNombre().trim().isEmpty()) {
            System.out.println("[ERROR] El nombre no puede estar vacío.");
            return false;
        }
        if (!odontologo.getNombre().trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+")) {
            System.out.println("[ERROR] El nombre solo puede contener letras.");
            return false;
        }
        if (odontologo.getApellido() == null || odontologo.getApellido().trim().isEmpty()) {
            System.out.println("[ERROR] El apellido no puede estar vacío.");
            return false;
        }
        if (!odontologo.getApellido().trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+")) {
            System.out.println("[ERROR] El apellido solo puede contener letras.");
            return false;
        }
        if (odontologo.getMatricula() == null || odontologo.getMatricula().trim().isEmpty()) {
            System.out.println("[ERROR] La matrícula no puede estar vacía.");
            return false;
        }
        return true;
    }

    @Override
    public boolean guardar(Odontologo odontologo) {
        if (!validarDatos(odontologo)) return false;
        for (Odontologo existente : repositorio.listarTodos()) {
            if (existente.getMatricula().equals(odontologo.getMatricula())) {
                System.out.println("[ERROR] Ya existe un odontólogo registrado con esa matrícula.");
                return false;
            }
        }
        repositorio.guardar(odontologo);
        return true;
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
        repositorio.actualizar(odontologo);
        return true;
    }

    @Override
    public List<Odontologo> listarTodos() {
        return repositorio.listarTodos();
    }
}
