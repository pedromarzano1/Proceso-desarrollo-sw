import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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