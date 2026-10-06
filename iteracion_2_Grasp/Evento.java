import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * El evento: nombre, fecha, horario, lugar, descripción, invitados y
 * servicios contratados.
 * Patrones:
 *  - Experto: es dueño de todos esos datos, calcula su propio rango de
 *    fecha/hora y sabe decir si se superpone con otro evento (seSuperponeCon).
 *  - Creador: contiene a sus Invitados y ServiciosContratados, por eso es
 *    quien los crea (agregarInvitado / agregarServicio reciben los datos).
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

    // Experto: el evento tiene su salón y su rango horario, así que es quien
    // sabe si choca con otro. Fórmula de solapamiento: (InicioA < FinB) y (FinA > InicioB)
    public boolean seSuperponeCon(Evento otro) {
        if (this.lugar != otro.getLugar()) return false;
        return this.getFechaHoraInicio().isBefore(otro.getFechaHoraFin())
            && this.getFechaHoraFin().isAfter(otro.getFechaHoraInicio());
    }

    // Se devuelven listas de solo lectura: para agregar o sacar hay que pasar
    // por los métodos del Evento (encapsulamiento).
    public List<Invitado> getInvitados() { return Collections.unmodifiableList(invitados); }
    public List<ServicioContratado> getServicios() { return Collections.unmodifiableList(servicios); }

    // Creador: el Evento contiene a sus invitados, entonces él los instancia.
    public Invitado agregarInvitado(String nombre, String email) {
        Invitado nuevo = new Invitado(nombre, email);
        invitados.add(nuevo);
        return nuevo;
    }

    // Creador: el Evento contiene a sus servicios, entonces él los instancia.
    public ServicioContratado agregarServicio(String nombre, CategoriaServicio categoria, double precio) {
        ServicioContratado nuevo = new ServicioContratado(nombre, categoria, precio);
        servicios.add(nuevo);
        return nuevo;
    }

    public void eliminarInvitado(Invitado i) { invitados.remove(i); }
    public void eliminarServicio(ServicioContratado s) { servicios.remove(s); }

    public String toString() { return nombre + " - " + fecha + " (" + lugar + ")"; }
}
