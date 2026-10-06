/*
 * Un servicio contratado: proveedor, categoría y precio.
 * Patrón: Experto, de sus propios datos.
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