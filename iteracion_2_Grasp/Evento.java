import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*
 * FUNCION PRINCIPAL: Representar el concepto central del dominio, un evento,
 * con sus datos (nombre, fecha, horario, lugar, descripcion) y sus colecciones
 * asociadas de invitados y servicios contratados.
 *
 * GRASP - Experto: es el dueño de sus propios datos y de las listas de
 * invitados/servicios, por lo que es el responsable de agregarlos, eliminarlos
 * y exponerlos (agregarInvitado, eliminarServicio, etc.).
 * GRASP - Alta cohesion: agrupa unicamente el estado y comportamiento propio de
 * un evento, sin mezclar validaciones ni persistencia (eso esta delegado a
 * ValidadorFechaEvento, ValidadorSuperposicion y RepositorioEventos).
 */
public class Evento {
    private String nombre, descripcion;
    private LocalDate fecha;
    private LocalTime horaInicio, horaFin;
    private Lugar lugar;
    private final List<Invitado> invitados = new ArrayList<>();
    private final List<ServicioContratado> servicios = new ArrayList<>();

    public Evento(String nombre, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Lugar lugar, String descripcion) {
        this.nombre = nombre; this.fecha = fecha; 
        this.horaInicio = horaInicio; this.horaFin = horaFin;
        this.lugar = lugar; this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public Lugar getLugar() { return lugar; }
    public String getDescripcion() { return descripcion; }
    
    public void setNombre(String n) { nombre = n; }
    public void setFecha(LocalDate f) { fecha = f; }
    public void setHoraInicio(LocalTime hI) { horaInicio = hI; }
    public void setHoraFin(LocalTime hF) { horaFin = hF; }
    public void setLugar(Lugar l) { lugar = l; }
    public void setDescripcion(String d) { descripcion = d; }

    public List<Invitado> getInvitados() { return invitados; }
    public List<ServicioContratado> getServicios() { return servicios; }
    public void agregarInvitado(Invitado i) { invitados.add(i); }
    public void agregarServicio(ServicioContratado s) { servicios.add(s); }
    public void eliminarInvitado(Invitado i) { invitados.remove(i); }
    public void eliminarServicio(ServicioContratado s) { servicios.remove(s); }

    public String toString() { return nombre + " - " + fecha + " (" + lugar + ")"; }
}