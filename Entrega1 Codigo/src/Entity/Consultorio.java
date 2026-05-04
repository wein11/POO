package Entity;

public class Consultorio {

    private long id;
    private int numero;
    private int piso;

    public Consultorio() {}

    public Consultorio(long id, int numero, int piso) {
        this.id = id;
        this.numero = numero;
        this.piso = piso;
    }

    public Consultorio(long id, int numero) {
        this.id = id;
        this.numero = numero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    @Override
    public String toString() {
        return "Consultorio #" + id + " | Número: " + numero + " | Piso: " + piso;
    }
}
