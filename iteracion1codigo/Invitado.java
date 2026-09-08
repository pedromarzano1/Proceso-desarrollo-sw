public class Invitado {
    private String nombre, email;

    public Invitado(String nombre, String email) { this.nombre = nombre; this.email = email; }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    
    public String toString() { return nombre + " (" + email + ")"; }
}