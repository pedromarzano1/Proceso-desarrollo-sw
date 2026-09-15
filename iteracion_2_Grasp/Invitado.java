/*
 * FUNCION PRINCIPAL: Representar a un invitado de un evento, con su nombre y
 * email, exponiendo los datos necesarios para mostrarlo y editarlo.
 *
 * GRASP - Experto: es dueño de sus propios atributos (nombre, email) y de como
 * se representa como texto (toString), por lo que es el responsable natural de
 * esa informacion dentro del modelo de dominio.
 */
public class Invitado {
    private String nombre, email;

    public Invitado(String nombre, String email) { this.nombre = nombre; this.email = email; }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    
    public String toString() { return nombre + " (" + email + ")"; }
}