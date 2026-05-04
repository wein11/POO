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
    public void guardar(Odontologo odontologo) {
        if (odontologo.getNombre() == null || odontologo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del odontólogo no puede estar vacío.");
        }
        if (odontologo.getMatricula() == null || odontologo.getMatricula().trim().isEmpty()) {
            throw new IllegalArgumentException("La matrícula no puede estar vacía.");
        }
        if (repositorio.existeMatricula(odontologo.getMatricula())) {
            throw new IllegalArgumentException("Ya existe un odontólogo registrado con esa matrícula.");
        }
        repositorio.guardar(odontologo);
    }

    @Override
    public Odontologo buscarPorId(long id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public void eliminarPorId(long id) {
        repositorio.eliminarPorId(id);
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        repositorio.actualizar(odontologo);
    }

    @Override
    public List<Odontologo> listarTodos() {
        return repositorio.listarTodos();
    }
}
