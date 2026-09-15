/*
 * Categorías de servicio que se pueden contratar (catering, DJ, etc.), con
 * su nombre para mostrar en pantalla.
 * Patrón: Experto, cada categoría conoce su propio nombre legible.
 */
public enum CategoriaServicio {
    CATERING("Catering"),
    DJ("DJ e Iluminacion"),
    FOTOGRAFIA("Fotografia y Video"),
    DECORACION("Decoracion"),
    SHOW("Show en vivo");

    private String nombre;

    CategoriaServicio(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    
    @Override
    public String toString() { return nombre; }
}