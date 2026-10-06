public class Recurso {
    public static final String[] TIPOS = { "Salón", "Audiovisual", "Catering" };
    private String nombre, tipo;

    public Recurso(String nombre, String tipo) { this.nombre = nombre; this.tipo = tipo; }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String toString() { return tipo + ": " + nombre; }
}