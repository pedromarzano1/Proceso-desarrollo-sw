import java.time.LocalDateTime;

public class ValidadorSuperposicion {
    
    public boolean haySuperposicion(Evento eventoAEvaluar, GestorEventos gestor) {
        LocalDateTime inicioNuevo = obtenerInicio(eventoAEvaluar);
        LocalDateTime finNuevo = obtenerFin(eventoAEvaluar);

        for (Evento e : gestor.getEventos()) {
            if (e == eventoAEvaluar) continue; // Ignoramos el evento actual si lo editamos
            
            // Si comparten el mismo salón, validamos el choque de fechas y horas exactas
            if (e.getLugar() == eventoAEvaluar.getLugar()) {
                LocalDateTime inicioExistente = obtenerInicio(e);
                LocalDateTime finExistente = obtenerFin(e);

                // Fórmula de solapamiento: (InicioA < FinB) y (FinA > InicioB)
                if (inicioNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente)) {
                    return true;
                }
            }
        }
        return false;
    }

    private LocalDateTime obtenerInicio(Evento e) {
        return e.getFecha().atTime(e.getHoraInicio());
    }

    private LocalDateTime obtenerFin(Evento e) {
        LocalDateTime fin = e.getFecha().atTime(e.getHoraFin());
        // Si la hora de fin es menor o igual a la de inicio (ej: 21:00 a 06:00), 
        // le sumamos 1 día automáticamente a la fecha de fin.
        if (!e.getHoraFin().isAfter(e.getHoraInicio())) {
            fin = fin.plusDays(1);
        }
        return fin;
    }
}