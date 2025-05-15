package models;

public class Trabajador {
    private int id;
    private String nombre;
    private String codigo_personal;
    private String horaEntrada;

    public Trabajador(int id, String nombre, String codigo_personal, String horaEntrada) {
        this.id = id;
        this.nombre = nombre;
        this.codigo_personal = codigo_personal;
        this.horaEntrada = horaEntrada;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCodigo_personal() { return codigo_personal; }
    public String getHoraEntrada() {
    	return horaEntrada;
    }
}
