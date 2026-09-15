import java.time.LocalDate;

/*
 * FUNCION PRINCIPAL: Determinar si una fecha es valida para un evento, es decir,
 * si es hoy o una fecha futura (no permite fechas pasadas).
 *
 * GRASP - Fabricacion Pura: encapsula una regla de validacion que no pertenece
 * naturalmente a ninguna entidad del dominio.
 * GRASP - Alta Cohesion: su unica responsabilidad es esa validacion de fecha.
 * GRASP - Experto (parcial): concentra el conocimiento necesario para decidir si
 * una fecha es "valida", evitando que esa logica se repita en la interfaz.
 */
public class ValidadorFechaEvento {
    
    public boolean esFechaValida(LocalDate fecha) {
        // Retorna true si la fecha de hoy es igual o anterior a la fecha pasada por parámetro
        return !fecha.isBefore(LocalDate.now());
    }
}