import java.time.LocalDate;

public class ValidadorFechaEvento {
    
    public boolean esFechaValida(LocalDate fecha) {
        // Retorna true si la fecha de hoy es igual o anterior a la fecha pasada por parámetro
        return !fecha.isBefore(LocalDate.now());
    }
}