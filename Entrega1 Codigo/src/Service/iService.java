package Service;

import java.util.List;

public interface iService<T> {
    boolean guardar(T t);
    T buscarPorId(long id);
    boolean eliminarPorId(long id);
    boolean actualizar(T t);
    List<T> listarTodos();
}
