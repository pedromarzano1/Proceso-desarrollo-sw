import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*
 * El evento: nombre, fecha, horario, lugar, descripción, invitados y
 * servicios contratados.
 * Patrón: Experto, es dueño de todos esos datos, incluido el cálculo de su
 * propio rango de fecha/hora (getFechaHoraInicio/getFechaHoraFin).
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

    public LocalDateTime getFechaHoraInicio() { return fecha.atTime(horaInicio); }

    public LocalDateTime getFechaHoraFin() {
        LocalDateTime fin = fecha.atTime(horaFin);
        // Si la hora de fin es menor o igual a la de inicio (ej: 21:00 a 06:00),
        // le sumamos 1 día automáticamente a la fecha de fin.
        if (!horaFin.isAfter(horaInicio)) {
            fin = fin.plusDays(1);
        }
        return fin;
    }

    public List<Invitado> getInvitados() { return invitados; }
    public List<ServicioContratado> getServicios() { return servicios; }
    public void agregarInvitado(Invitado i) { invitados.add(i); }
    public void agregarServicio(ServicioContratado s) { servicios.add(s); }
    public void eliminarInvitado(Invitado i) { invitados.remove(i); }
    public void eliminarServicio(ServicioContratado s) { servicios.remove(s); }

    public String toString() { return nombre + " - " + fecha + " (" + lugar + ")"; }
}