import javax.swing.SwingUtilities;

/*
 * FUNCION PRINCIPAL: Punto de entrada de la aplicacion. Su unica responsabilidad
 * es arrancar el sistema lanzando la ventana principal dentro del hilo de eventos
 * de Swing (Event Dispatch Thread).
 *
 * GRASP - Bajo Acoplamiento / Alta cohesion: no conoce nada del dominio (eventos,
 * invitados, persistencia); solo depende de VentanaPrincipal, por lo que cualquier
 * cambio interno del sistema no lo afecta.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}