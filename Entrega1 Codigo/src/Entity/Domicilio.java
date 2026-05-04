package Entity;

public class Domicilio {

    private String calle;
    private int numero;
    private String localidad;
    private String provincia;
    private String codigoPostal;
    private Integer piso;
    private String especificaciones;
    
    public Domicilio(String calle, int numero, String localidad, String provincia, String codigoPostal, int piso, String especificaciones) {
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.piso = piso;
        this.especificaciones = especificaciones;
    }

        public Domicilio(String calle, int numero, String localidad, String provincia, String codigoPostal, int piso) {
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.piso = piso;
    }

        public Domicilio(String calle, int numero, String localidad, String provincia, String codigoPostal, String especificaciones) {
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.especificaciones = especificaciones;
    }
    

    public Domicilio(String calle, int numero, String localidad, String provincia, String codigoPostal) {
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
    }
    

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    @Override
    public String toString() {
        return "Calle " + calle + " " + numero + ", " + localidad + ", " + provincia + " " + codigoPostal + ", " +
               (piso != null ? "Piso: " + piso : "Sin piso,") +
               (especificaciones != null ? ", " + especificaciones : " Sin especificaciones");
    }
}