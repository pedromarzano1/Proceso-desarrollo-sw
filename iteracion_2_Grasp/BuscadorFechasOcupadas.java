import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Le pide al GestorEventos las fechas ya reservadas, sacando de la lista al
 * evento que se está editando (si corresponde).
 * Patrón: Fabricación Pura, no es un concepto real del dominio, se inventó
 * para no meter esta lógica en la interfaz ni en Evento.
 */
public class BuscadorFechasOcupadas {
    private GestorEventos gestor;

    public BuscadorFechasOcupadas(GestorEventos gestor) {
        this.gestor = gestor;
    }

    public List<LocalDate> obtenerFechasOcupadas(Evento eventoAExcluir) {
        return gestor.getEventos().stream()
                .filter(e -> e != eventoAExcluir) // Excluir el evento que estamos editando
                .map(Evento::getFecha)
                .collect(Collectors.toList());
    }
}