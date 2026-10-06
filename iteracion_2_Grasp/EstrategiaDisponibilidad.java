import java.time.LocalDate;
import java.util.List;

/*
 * Interfaz Strategy: define CÓMO se decide si un salón está disponible.
 * Hay varias reglas posibles (por horario, por día completo...) y se pueden
 * intercambiar en tiempo de ejecución sin tocar GestorEventos (el Contexto).
 * Patrones: Strategy, Polimorfismo y Variaciones Protegidas (GRASP).
 */
public interface EstrategiaDisponibilidad {

    // Se usa al guardar: ¿el evento candidato puede reservarse sin chocar con los demás?
    boolean estaDisponible(Evento candidato, List<Evento> otrosEventos);

    // Se usa en el calendario: ¿esa fecha queda libre para ese salón?
    boolean fechaDisponible(LocalDate fecha, Lugar lugar, List<Evento> otrosEventos);
}
