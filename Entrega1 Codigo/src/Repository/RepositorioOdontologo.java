package Repository;

import Entity.Odontologo;
import java.util.ArrayList;
import java.util.List;

public class RepositorioOdontologo {

    private List<Odontologo> odontologos;
    private long contadorId;

    public RepositorioOdontologo() {
        this.odontologos = new ArrayList<>();
        this.contadorId = 1;
    }

    public void guardar(Odontologo odontologo) {
        odontologo.setId(contadorId++);
        odontologos.add(odontologo);
    }

    public Odontologo buscarPorId(long id) {
        for (Odontologo o : odontologos) {
            if (o.getId() == id) return o;
        }
        return null;
    }

    public void eliminarPorId(long id) {
        odontologos.removeIf(o -> o.getId() == id);
    }

    public void actualizar(Odontologo odontologo) {
        for (int i = 0; i < odontologos.size(); i++) {
            if (odontologos.get(i).getId() == odontologo.getId()) {
                odontologos.set(i, odontologo);
                return;
            }
        }
    }

    public List<Odontologo> listarTodos() {
        return new ArrayList<>(odontologos);
    }

    public boolean existeMatricula(String matricula) {
        for (Odontologo o : odontologos) {
            if (o.getMatricula().equals(matricula)) return true;
        }
        return false;
    }
}
