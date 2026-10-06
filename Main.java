import javax.swing.SwingUtilities;

/*
 * Arranca la aplicación levantando la ventana principal en el hilo de
 * Swing.
 * Patrón: Bajo acoplamiento, no depende de nada del dominio.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}