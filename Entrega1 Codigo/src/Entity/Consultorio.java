package Entity;

public class Consultorio {

    private int id;
    private int numero;
    private int piso;

    public Consultorio(int id, int numero, int piso) {
        this.id = id;
        this.numero = numero;
        this.piso = piso;
    }

    public Consultorio(int id, int numero) {
        this.id = id;
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
