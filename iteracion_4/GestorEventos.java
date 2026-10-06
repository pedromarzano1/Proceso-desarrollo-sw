import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Controlador de fachada del sistema de eventos. La interfaz le pasa los
 * datos que cargó el usuario y él valida, crea, modifica, elimina y guarda.
 * Patrones:
 *  - Controlador (GRASP): recibe los eventos del sistema que vienen de la UI.
 *    No es una ventana: la UI solo muestra y junta datos.
 *  - Creador: contiene la lista de Eventos, por eso es quien los crea.
 *  - Experto: tiene la lista de eventos, sabe qué fechas tienen reservas.
 *  - Strategy: es el "Contexto". Delega en EstrategiaDisponibilidad la
 *    decisión de si un salón está libre, sin saber qué regla concreta es.
 *  - DIP: depende de las interfaces RepositorioEventos y
 *    EstrategiaDisponibilidad, que recibe por constructor (no hace "new").
 */
public class GestorEventos {
    private final List<Evento> listaEventos;
    private final RepositorioEventos repositorio;
    private EstrategiaDisponibilidad estrategia;
    private final ValidadorFechaEvento validadorFecha = new ValidadorFechaEvento();

    public GestorEventos(RepositorioEventos repositorio, EstrategiaDisponibilidad estrategia) {
        this.repositorio = repositorio;
        this.estrategia = estrategia;
        this.listaEventos = repositorio.cargar();
    }

    // Strategy: la regla se puede cambiar en tiempo de ejecución.
    public void setEstrategia(EstrategiaDisponibilidad estrategia) { this.estrategia = estrategia; }
    public EstrategiaDisponibilidad getEstrategia() { return estrategia; }

    // Lista de solo lectura: nadie de afuera la modifica sin pasar por el controlador.
    public List<Evento> getEventos() { return Collections.unmodifiableList(listaEventos); }

    // --- Casos de uso ---

    public Evento agregarEvento(String nombre, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Lugar lugar, String descripcion) {
        Evento nuevo = new Evento(nombre, fecha, horaInicio, horaFin, lugar, descripcion);   // Creador
        validar(nuevo, null);
        listaEventos.add(nuevo);
        repositorio.guardar(listaEventos);
        return nuevo;
    }

    public void editarEvento(Evento original, String nombre, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Lugar lugar, String descripcion) {
        // Se valida sobre una copia temporal: si algo falla, el original queda intacto.
        Evento candidato = new Evento(nombre, fecha, horaInicio, horaFin, lugar, descripcion);
        validar(candidato, original);

        original.setNombre(nombre);
        original.setFecha(fecha);
        original.setHoraInicio(horaInicio);
        original.setHoraFin(horaFin);
        original.setLugar(lugar);
        original.setDescripcion(descripcion);
        repositorio.guardar(listaEventos);
    }

    public void eliminarEvento(Evento evento) {
        listaEventos.remove(evento);
        repositorio.guardar(listaEventos);
    }

    // Lo llaman los otros controladores cuando cambian datos internos de un evento.
    public void guardarCambios() {
        repositorio.guardar(listaEventos);
    }

    // --- Consultas para el calendario ---

    public boolean fechaDisponible(LocalDate fecha, Lugar lugar, Evento eventoAExcluir) {
        return estrategia.fechaDisponible(fecha, lugar, otrosEventos(eventoAExcluir));
    }

    public List<LocalDate> obtenerFechasConEventos(Lugar lugar, Evento eventoAExcluir) {
        List<LocalDate> fechas = new ArrayList<>();
        for (Evento e : otrosEventos(eventoAExcluir)) {
            if (e.getLugar() == lugar) fechas.add(e.getFecha());
        }
        return fechas;
    }

    // --- Validaciones (si algo está mal, se lanza una excepción con el mensaje para el usuario) ---

    private void validar(Evento candidato, Evento original) {
        if (candidato.getNombre() == null || candidato.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (candidato.getFecha() == null)
            throw new IllegalArgumentException("Debe seleccionar una fecha.");

        // Si es un evento nuevo, o si al editar se cambió la fecha, no puede ser pasada.
        boolean fechaCambiada = original == null || !candidato.getFecha().equals(original.getFecha());
        if (fechaCambiada && !validadorFecha.esFechaValida(candidato.getFecha()))
            throw new IllegalArgumentException("No se pueden reservar fechas pasadas.");

        // Strategy: el Contexto delega, no pregunta qué regla es.
        if (!estrategia.estaDisponible(candidato, otrosEventos(original)))
            throw new IllegalArgumentException("El salón no está disponible (regla: " + estrategia + ").");
    }

    private List<Evento> otrosEventos(Evento eventoAExcluir) {
        List<Evento> otros = new ArrayList<>(listaEventos);
        otros.remove(eventoAExcluir);
        return otros;
    }
}
