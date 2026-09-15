/*
 * FUNCION PRINCIPAL: Enumerar los salones/lugares disponibles donde puede
 * realizarse un evento, junto con su nombre descriptivo para mostrar en la UI.
 *
 * GRASP - Experto: es quien conoce el nombre legible de cada lugar (getNombre) y
 * como mostrarse como texto (toString), evitando que esa informacion se
 * disperse en otras clases.
 */
public enum Lugar {
    SANS_SOUCI("Palacio Sans Souci (Victoria, GBA Norte)"),
    DUHAU("Palacio Duhau - Park Hyatt (Recoleta, CABA)"),
    FOUR_SEASONS("Four Seasons Hotel (Retiro, CABA)"),
    HILTON("Hilton Buenos Aires (Puerto Madero, CABA)");

    private String nombre;

    Lugar(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}