import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorEventos {
    private static final String ARCHIVO = "eventos.txt";
    private final List<Evento> eventos = new ArrayList<>();

    public GestorEventos() { cargar(); }

    public List<Evento> getEventos() { return eventos; }
    public void agregarEvento(Evento e) { eventos.add(e); guardar(); }
    public void eliminarEvento(Evento e) { eventos.remove(e); guardar(); }
    public void actualizar() { guardar(); }

    private String limpiar(String t) {
        return t == null ? "" : t.replace("|", " ").replace("\n", " ").replace("\r", " ").trim();
    }

    public void guardar() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Evento e : eventos) {
                bw.write("E|" + limpiar(e.getNombre()) + "|" + e.getFecha() + "|" + limpiar(e.getUbicacion()) + "|" + limpiar(e.getDescripcion())); bw.newLine();
                for (Asistente a : e.getAsistentes()) { bw.write("A|" + limpiar(a.getNombre()) + "|" + limpiar(a.getEmail())); bw.newLine(); }
                for (Recurso r : e.getRecursos()) { bw.write("R|" + limpiar(r.getNombre()) + "|" + limpiar(r.getTipo())); bw.newLine(); }
            }
        } catch (IOException ex) { System.out.println("Error al guardar: " + ex.getMessage()); }
    }

    public final void cargar() {
        eventos.clear();
        if (!new File(ARCHIVO).exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea; Evento actual = null;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split("\\|", -1);
                if (p[0].equals("E")) { actual = new Evento(p[1], LocalDate.parse(p[2]), p[3], p[4]); eventos.add(actual); }
                else if (p[0].equals("A") && actual != null) actual.agregarAsistente(new Asistente(p[1], p[2]));
                else if (p[0].equals("R") && actual != null) actual.agregarRecurso(new Recurso(p[1], p[2]));
            }
        } catch (IOException ex) { System.out.println("Error al cargar: " + ex.getMessage()); }
    }
}