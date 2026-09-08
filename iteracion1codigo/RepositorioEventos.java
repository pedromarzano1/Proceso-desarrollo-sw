import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEventos {
    private static final String ARCHIVO = "eventos.txt";

    private String limpiarTexto(String textoBruto) {
        return textoBruto == null ? "" : textoBruto.replace("|", " ").replace("\n", " ").replace("\r", " ").trim();
    }

    public void guardar(List<Evento> listaEventos) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Evento eventoActual : listaEventos) {
                escritor.write("E|" + limpiarTexto(eventoActual.getNombre()) + "|" + eventoActual.getFecha() + "|" + eventoActual.getHoraInicio() + "|" + eventoActual.getHoraFin() + "|" + eventoActual.getLugar().name() + "|" + limpiarTexto(eventoActual.getDescripcion())); 
                escritor.newLine();
                for (Invitado invitadoActual : eventoActual.getInvitados()) { 
                    escritor.write("I|" + limpiarTexto(invitadoActual.getNombre()) + "|" + limpiarTexto(invitadoActual.getEmail())); 
                    escritor.newLine(); 
                }
                for (ServicioContratado servicioActual : eventoActual.getServicios()) { 
                    escritor.write("S|" + limpiarTexto(servicioActual.getNombre()) + "|" + servicioActual.getCategoria().name() + "|" + servicioActual.getPrecio()); 
                    escritor.newLine(); 
                }
            }
        } catch (IOException excepcion) { 
            System.out.println("Error al guardar: " + excepcion.getMessage()); 
        }
    }

    public List<Evento> cargar() {
        List<Evento> eventosCargados = new ArrayList<>();
        if (!new File(ARCHIVO).exists()) return eventosCargados;
        
        try (BufferedReader lector = new BufferedReader(new FileReader(ARCHIVO))) {
            String lineaArchivo; 
            Evento eventoEnProceso = null;
            while ((lineaArchivo = lector.readLine()) != null) {
                if (lineaArchivo.trim().isEmpty()) continue;
                String[] partesLinea = lineaArchivo.split("\\|", -1);
                
                if (partesLinea[0].equals("E")) { 
                    eventoEnProceso = new Evento(partesLinea[1], LocalDate.parse(partesLinea[2]), LocalTime.parse(partesLinea[3]), LocalTime.parse(partesLinea[4]), Lugar.valueOf(partesLinea[5]), partesLinea[6]); 
                    eventosCargados.add(eventoEnProceso); 
                }
                else if (partesLinea[0].equals("I") && eventoEnProceso != null) {
                    eventoEnProceso.agregarInvitado(new Invitado(partesLinea[1], partesLinea[2]));
                }
                else if (partesLinea[0].equals("S") && eventoEnProceso != null) {
                    eventoEnProceso.agregarServicio(new ServicioContratado(partesLinea[1], CategoriaServicio.valueOf(partesLinea[2]), Double.parseDouble(partesLinea[3])));
                }
            }
        } catch (IOException excepcion) { 
            System.out.println("Error al cargar: " + excepcion.getMessage()); 
        }
        return eventosCargados;
    }
}