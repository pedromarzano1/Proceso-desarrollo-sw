/*
 * FUNCION PRINCIPAL: Representar un servicio contratado para un evento (nombre
 * del proveedor, categoria y precio).
 *
 * GRASP - Experto: conoce y gestiona sus propios datos (nombre, categoría,
 * precio) y sabe representarse como texto, siendo el responsable natural de
 * esa parte del dominio.
 */
public class ServicioContratado {
    private String nombre;
    private CategoriaServicio categoria;
    private double precio;

    public ServicioContratado(String nombre, CategoriaServicio categoria, double precio) { 
        this.nombre = nombre; 
        this.categoria = categoria; 
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public CategoriaServicio getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCategoria(CategoriaServicio categoria) { this.categoria = categoria; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public String toString() { return categoria.getNombre() + ": " + nombre + " ($" + precio + ")"; }
}