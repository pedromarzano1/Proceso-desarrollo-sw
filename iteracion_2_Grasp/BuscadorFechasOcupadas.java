import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/*
 * FUNCION PRINCIPAL: Consultar al GestorEventos y devolver la lista de fechas que
 * ya tienen un evento reservado, excluyendo opcionalmente el evento que se esta
 * editando (para que su propia fecha no figure como ocupada).
 *
 * GRASP - Fabricacion Pura (Pure Fabrication): no representa un concepto del dominio
 * real (no es un Evento ni un Invitado), es una clase "inventada" para sacar esta
 * responsabilidad de consulta fuera de la interfaz grafica y del propio Evento.
 * GRASP - Alta cohesion: hace una unica cosa, calcular fechas ocupadas.
 * GRASP - Bajo Acoplamiento: quien necesita las fechas ocupadas (el calendario) no
 * tiene que saber como se recorre ni se filtra la lista de eventos.
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