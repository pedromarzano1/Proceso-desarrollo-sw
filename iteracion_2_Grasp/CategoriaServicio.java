/*
 * FUNCION PRINCIPAL: Enumerar las categorias posibles de un servicio contratado
 * (catering, DJ, fotografia, etc.), junto con su nombre descriptivo.
 *
 * GRASP - Experto: concentra el conocimiento de su propio nombre legible, en
 * lugar de que ServicioContratado u otra clase tengan que traducirlo.
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