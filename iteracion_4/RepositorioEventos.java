import java.util.List;

/*
 * Interfaz que espera el cliente (GestorEventos) para guardar y cargar eventos.
 * GestorEventos depende de esta abstracción y no sabe si atrás hay un .txt,
 * una base de datos u otra cosa.
 * Patrones: Adapter (la "interfaz Target"), Variaciones Protegidas e
 * Inversión de Dependencias (DIP).
 */
public interface RepositorioEventos {
    void guardar(List<Evento> eventos);
    List<Evento> cargar();
}
