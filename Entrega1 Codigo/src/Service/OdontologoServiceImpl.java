package Service;

import Entity.Odontologo;
import Repository.RepositorioOdontologo;
import java.util.List;

public class OdontologoServiceImpl implements iService<Odontologo> {

    private RepositorioOdontologo repositorio;

    public OdontologoServiceImpl() {
        this.repositorio = new RepositorioOdontologo();
    }

    @Override
    public boolean guardar(Odontologo odontologo) {
        if (odontologo.getNombre() == null || odontologo.getNombre().trim().isEmpty()) {
            System.out.println("[ERROR] El nombre del odontólogo no puede estar vacío.");
            return false;
        }
        if (odontologo.getMatricula() == null || odontologo.getMatricula().trim().isEmpty()) {
            System.out.println("[ERROR] La matrícula no puede estar vacía.");
            return false;
        }
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
        repositorio.actualizar(odontologo);
        return true;
    }

    @Override
    public List<Odontologo> listarTodos() {
        return repositorio.listarTodos();
    }
}
