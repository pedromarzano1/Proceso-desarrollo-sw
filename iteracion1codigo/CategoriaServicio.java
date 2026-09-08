public enum CategoriaServicio {
    CATERING("Catering"),
    DJ("DJ e Iluminación"),
    FOTOGRAFIA("Fotografía y Video"),
    DECORACION("Decoración"),
    SHOW("Show en vivo");

    private String nombre;

    CategoriaServicio(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    
    @Override
    public String toString() { return nombre; }
}