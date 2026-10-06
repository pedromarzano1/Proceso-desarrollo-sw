public class Asistente {
    private String nombre, email;

    public Asistente(String nombre, String email) { this.nombre = nombre; this.email = email; }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String toString() { return nombre + " (" + email + ")"; }
}