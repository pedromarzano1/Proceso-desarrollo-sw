import javax.swing.SwingUtilities;

/*
 * Arranca la aplicación: arma las piezas y levanta la ventana principal en
 * el hilo de Swing.
 * Acá se decide QUÉ implementación concreta se usa (archivo .txt y regla de
 * disponibilidad inicial). El resto del sistema solo conoce las interfaces
 * (Inversión de Dependencias: las dependencias se "inyectan" desde afuera).
 */
public class Main {
    public static void main(String[] args) {
        RepositorioEventos repositorio = new AdapterArchivoTxt(new ArchivoTexto("eventos.txt"));
        EstrategiaDisponibilidad reglaInicial = new DisponibilidadPorSalonYHorario();
        GestorEventos gestor = new GestorEventos(repositorio, reglaInicial);

        SwingUtilities.invokeLater(() -> new VentanaPrincipal(gestor).setVisible(true));
    }
}
