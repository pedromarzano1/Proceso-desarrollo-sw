import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String nombre, ubicacion, descripcion;
    private LocalDate fecha;
    private final List<Asistente> asistentes = new ArrayList<>();
    private final List<Recurso> recursos = new ArrayList<>();

    public Evento(String nombre, LocalDate fecha, String ubicacion, String descripcion) {
        this.nombre = nombre; this.fecha = fecha; this.ubicacion = ubicacion; this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public LocalDate getFecha() { return fecha; }
    public String getUbicacion() { return ubicacion; }
    public String getDescripcion() { return descripcion; }
    public void setNombre(String n) { nombre = n; }
    public void setFecha(LocalDate f) { fecha = f; }
    public void setUbicacion(String u) { ubicacion = u; }
    public void setDescripcion(String d) { descripcion = d; }

    public List<Asistente> getAsistentes() { return asistentes; }
    public List<Recurso> getRecursos() { return recursos; }
    public void agregarAsistente(Asistente a) { asistentes.add(a); }
    public void agregarRecurso(Recurso r) { recursos.add(r); }

    public boolean esFuturo() { return !fecha.isBefore(LocalDate.now()); }
    public String toString() { return nombre + " - " + fecha; }
}