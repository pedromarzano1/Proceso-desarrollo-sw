import java.awt.Color;
import java.time.LocalDate;
import javax.swing.JButton;

/*
 * El botón de cada día del calendario. Se pinta solo (pasado, ocupado, con
 * reservas) y avisa al SelectorFechaCalendario cuando lo tocan.
 * Patrón: Experto, es el propio botón el que tiene la fecha que representa
 * y sabe cómo mostrarse en cada estado.
 */
public class BotonDiaCalendario extends JButton {
    private final LocalDate fechaRepresentada;

    public BotonDiaCalendario(LocalDate fecha, SelectorFechaCalendario selector) {
        super(String.valueOf(fecha.getDayOfMonth()));
        this.fechaRepresentada = fecha;

        // Al hacer clic, le avisamos al calendario principal qué fecha se eligió
        this.addActionListener(e -> selector.setFechaSeleccionada(fechaRepresentada));
    }

    // Día bloqueado según la regla de disponibilidad activa
    public void marcarComoOcupado() {
        this.setBackground(Color.RED);
        this.setForeground(Color.WHITE);
        this.setOpaque(true); // Necesario en algunos sistemas operativos para que se vea el fondo
        this.setEnabled(false);
        this.setToolTipText("El salón ya está reservado ese día");
    }

    // El salón tiene eventos ese día pero se puede elegir: el horario se valida al guardar
    public void marcarConReservas() {
        this.setBackground(Color.ORANGE);
        this.setOpaque(true);
        this.setToolTipText("El salón ya tiene eventos este día; al guardar se controla que el horario no se pise");
    }

    public void marcarComoPasado() {
        this.setEnabled(false);
        this.setToolTipText("No se pueden reservar fechas pasadas");
    }
}
