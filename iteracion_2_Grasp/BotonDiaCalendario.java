import java.awt.Color;
import java.time.LocalDate;
import javax.swing.JButton;

/*
 * FUNCION PRINCIPAL: Representar visualmente, dentro del calendario, un dia
 * concreto (JButton especializado). Sabe mostrar su propio estado: dia pasado,
 * dia ocupado o dia disponible, y notificar al SelectorFechaCalendario cuando el
 * usuario lo selecciona.
 *
 * GRASP - Experto (Information Expert): es quien tiene la fecha que representa,
 * por lo que es el objeto adecuado para marcarse a si mismo como pasado u ocupado.
 * GRASP - Alta cohesion: solo se ocupa de su propia apariencia y de comunicar el
 * clic; no decide reglas de negocio (eso lo hacen ValidadorFechaEvento y
 * BuscadorFechasOcupadas).
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