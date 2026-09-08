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