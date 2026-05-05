package Service;

import java.util.List;

public interface iService<T> {
    boolean guardar(T t);
    T buscarPorId(long id);
    void eliminarPorId(long id);
    void actualizar(T t);
    List<T> listarTodos();
}
