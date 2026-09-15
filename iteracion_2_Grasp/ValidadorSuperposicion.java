import java.time.LocalDateTime;

/*
 * FUNCION PRINCIPAL: Determinar si un evento (nuevo o editado) se superpone en
 * fecha, horario y salon con algun otro evento ya registrado en el GestorEventos.
 *
 * GRASP - Experto en Informacion: reune los datos de fecha, horario y salon que
 * expone cada Evento y la lista de eventos que expone GestorEventos para calcular
 * el solapamiento; VentanaPrincipal solo le pregunta "hay superposicion?" sin
 * conocer la formula ni el manejo de horarios que cruzan la medianoche.
 */
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