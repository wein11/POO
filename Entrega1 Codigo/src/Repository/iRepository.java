package Repository;

import java.util.List;

public interface iRepository<T> {
    void guardar(T t);
    T buscarPorId(long id);
    void eliminarPorId(long id);
    void actualizar(T t);
    List<T> listarTodos();
}
