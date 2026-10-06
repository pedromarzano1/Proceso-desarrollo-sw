import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Clase de bajo nivel que solo sabe leer y escribir líneas de texto en un
 * archivo. No sabe nada de Eventos: su interfaz (líneas de texto) no es la
 * que necesita GestorEventos (listas de Evento).
 * Patrón: Adapter -> es la clase "Adaptada". No se toca: la usa el Adapter.
 */
public class ArchivoTexto {
    private final String ruta;

    public ArchivoTexto(String ruta) { this.ruta = ruta; }

    public boolean existe() { return new File(ruta).exists(); }

    public List<String> leerLineas() throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = lector.readLine()) != null) lineas.add(linea);
        }
        return lineas;
    }

    public void escribirLineas(List<String> lineas) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ruta))) {
            for (String linea : lineas) {
                escritor.write(linea);
                escritor.newLine();
            }
        }
    }
}
