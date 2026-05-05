package Repository;

import Entity.Turno;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTurno {

    private List<Turno> turnos;
    private long contadorId;

    public RepositorioTurno() {
        this.turnos = new ArrayList<>();
        this.contadorId = 1;
    }

    public void guardar(Turno turno) {
        turno.setId(contadorId++);
        turnos.add(turno);
    }

    public Turno buscarPorId(long id) {
        for (Turno t : turnos) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    public void eliminarPorId(long id) {
        turnos.removeIf(t -> t.getId() == id);
    }

    public void actualizar(Turno turno) {
        for (int i = 0; i < turnos.size(); i++) {
            if (turnos.get(i).getId() == turno.getId()) {
                turnos.set(i, turno);
                return;
            }
        }
    }

    public List<Turno> listarTodos() {
        return new ArrayList<>(turnos);
    }

}
