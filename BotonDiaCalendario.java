import java.awt.Color;
import java.time.LocalDate;
import javax.swing.JButton;

/*
 * El botón de cada día del calendario. Se pinta solo (pasado, ocupado) y
 * avisa al SelectorFechaCalendario cuando lo tocan.
 * Patrón: Experto, es el propio botón el que tiene la fecha que representa.
 */
public class BotonDiaCalendario extends JButton {
    private LocalDate fechaRepresentada;

    public BotonDiaCalendario(LocalDate fecha, SelectorFechaCalendario selector) {
        super(String.valueOf(fecha.getDayOfMonth()));
        this.fechaRepresentada = fecha;
        
        // Al hacer clic, le avisamos al calendario principal qué fecha se eligió
        this.addActionListener(e -> selector.setFechaSeleccionada(fechaRepresentada));
    }

    public void marcarComoOcupado() {
        this.setBackground(Color.RED);
        this.setForeground(Color.WHITE);
        this.setOpaque(true); // Necesario en algunos sistemas operativos para que se vea el fondo
        this.setEnabled(false);
        this.setToolTipText("Esta fecha ya se encuentra reservada");
    }
    
    public void marcarComoPasado() {
        this.setEnabled(false);
        this.setToolTipText("No se pueden reservar fechas pasadas");
    }
}