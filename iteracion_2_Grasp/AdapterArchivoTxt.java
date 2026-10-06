import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Adapter: implementa la interfaz RepositorioEventos (lo que el cliente espera)
 * y por dentro usa ArchivoTexto (la clase adaptada, que solo entiende líneas).
 * Su trabajo es TRADUCIR: Evento -> línea de texto al guardar, y
 * línea de texto -> Evento al cargar.
 * Patrones: Adapter, Indirección y Fabricación Pura (no es un concepto del dominio).
 */
public class AdapterArchivoTxt implements RepositorioEventos {
    private final ArchivoTexto archivo;   // el Adaptado (composición)

    public AdapterArchivoTxt(ArchivoTexto archivo) { this.archivo = archivo; }

    private String limpiarTexto(String textoBruto) {
        return textoBruto == null ? "" : textoBruto.replace("|", " ").replace("\n", " ").replace("\r", " ").trim();
    }

    @Override
    public void guardar(List<Evento> listaEventos) {
        List<String> lineas = new ArrayList<>();
        for (Evento eventoActual : listaEventos) {
            lineas.add("E|" + limpiarTexto(eventoActual.getNombre()) + "|" + eventoActual.getFecha() + "|" + eventoActual.getHoraInicio() + "|" + eventoActual.getHoraFin() + "|" + eventoActual.getLugar().name() + "|" + limpiarTexto(eventoActual.getDescripcion()));
            for (Invitado invitadoActual : eventoActual.getInvitados()) {
                lineas.add("I|" + limpiarTexto(invitadoActual.getNombre()) + "|" + limpiarTexto(invitadoActual.getEmail()));
            }
            for (ServicioContratado servicioActual : eventoActual.getServicios()) {
                lineas.add("S|" + limpiarTexto(servicioActual.getNombre()) + "|" + servicioActual.getCategoria().name() + "|" + servicioActual.getPrecio());
            }
        }
        try {
            archivo.escribirLineas(lineas);
        } catch (IOException excepcion) {
            System.out.println("Error al guardar: " + excepcion.getMessage());
        }
    }

    @Override
    public List<Evento> cargar() {
        List<Evento> eventosCargados = new ArrayList<>();
        if (!archivo.existe()) return eventosCargados;

        try {
            Evento eventoEnProceso = null;
            for (String lineaArchivo : archivo.leerLineas()) {
                if (lineaArchivo.trim().isEmpty()) continue;
                String[] partesLinea = lineaArchivo.split("\\|", -1);

                if (partesLinea[0].equals("E")) {
                    eventoEnProceso = new Evento(partesLinea[1], LocalDate.parse(partesLinea[2]), LocalTime.parse(partesLinea[3]), LocalTime.parse(partesLinea[4]), Lugar.valueOf(partesLinea[5]), partesLinea[6]);
                    eventosCargados.add(eventoEnProceso);
                }
                else if (partesLinea[0].equals("I") && eventoEnProceso != null) {
                    eventoEnProceso.agregarInvitado(partesLinea[1], partesLinea[2]);
                }
                else if (partesLinea[0].equals("S") && eventoEnProceso != null) {
                    eventoEnProceso.agregarServicio(partesLinea[1], CategoriaServicio.valueOf(partesLinea[2]), Double.parseDouble(partesLinea[3]));
                }
            }
        } catch (IOException excepcion) {
            System.out.println("Error al cargar: " + excepcion.getMessage());
        }
        return eventosCargados;
    }
}
