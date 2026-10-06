import java.time.LocalDate;

/*
 * Dice si una fecha es válida para un evento (hoy o futura, no pasada).
 * Patrón: Fabricación Pura, regla de negocio aislada de la interfaz.
 */
public class ValidadorFechaEvento {
    
    public boolean esFechaValida(LocalDate fecha) {
        // Retorna true si la fecha de hoy es igual o anterior a la fecha pasada por parámetro
        return !fecha.isBefore(LocalDate.now());
    }
}