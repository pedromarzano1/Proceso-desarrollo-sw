import java.time.LocalDateTime;

/*
 * Chequea si un evento se pisa en fecha, horario y salón con otro ya
 * cargado.
 * Patrón: Experto, usa el rango de fecha/hora que cada Evento ya sabe
 * calcular para aplicar la fórmula de solapamiento.
 */
public class ValidadorSuperposicion {

    public boolean haySuperposicion(Evento eventoAEvaluar, GestorEventos gestor) {
        LocalDateTime inicioNuevo = eventoAEvaluar.getFechaHoraInicio();
        LocalDateTime finNuevo = eventoAEvaluar.getFechaHoraFin();

        for (Evento e : gestor.getEventos()) {
            if (e == eventoAEvaluar) continue; // Ignoramos el evento actual si lo editamos

            // Si comparten el mismo salón, validamos el choque de fechas y horas exactas
            if (e.getLugar() == eventoAEvaluar.getLugar()) {
                LocalDateTime inicioExistente = e.getFechaHoraInicio();
                LocalDateTime finExistente = e.getFechaHoraFin();

                // Fórmula de solapamiento: (InicioA < FinB) y (FinA > InicioB)
                if (inicioNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente)) {
                    return true;
                }
            }
        }
        return false;
    }
}