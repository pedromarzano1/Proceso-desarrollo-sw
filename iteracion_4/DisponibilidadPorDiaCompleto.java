import java.time.LocalDate;
import java.util.List;

/*
 * Estrategia concreta: un salón se reserva por el día entero, aunque los
 * horarios no se pisen (un solo evento por salón por día).
 * Patrón: Strategy (estrategia concreta).
 */
public class DisponibilidadPorDiaCompleto implements EstrategiaDisponibilidad {

    @Override
    public boolean estaDisponible(Evento candidato, List<Evento> otrosEventos) {
        return fechaDisponible(candidato.getFecha(), candidato.getLugar(), otrosEventos);
    }

    @Override
    public boolean fechaDisponible(LocalDate fecha, Lugar lugar, List<Evento> otrosEventos) {
        for (Evento otro : otrosEventos) {
            if (otro.getLugar() == lugar && otro.getFecha().equals(fecha)) return false;
        }
        return true;
    }

    @Override
    public String toString() { return "Salón por día completo"; }
}
