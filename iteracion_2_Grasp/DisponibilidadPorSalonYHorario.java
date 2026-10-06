import java.time.LocalDate;
import java.util.List;

/*
 * Estrategia concreta: un salón se puede usar varias veces el mismo día,
 * siempre que los horarios no se pisen.
 * Patrón: Strategy (estrategia concreta). Delega en Evento.seSuperponeCon (Experto).
 */
public class DisponibilidadPorSalonYHorario implements EstrategiaDisponibilidad {

    @Override
    public boolean estaDisponible(Evento candidato, List<Evento> otrosEventos) {
        for (Evento otro : otrosEventos) {
            if (candidato.seSuperponeCon(otro)) return false;
        }
        return true;
    }

    @Override
    public boolean fechaDisponible(LocalDate fecha, Lugar lugar, List<Evento> otrosEventos) {
        // Con esta regla un día nunca se bloquea entero: el horario se controla al guardar.
        return true;
    }

    @Override
    public String toString() { return "Salón por horario"; }
}
