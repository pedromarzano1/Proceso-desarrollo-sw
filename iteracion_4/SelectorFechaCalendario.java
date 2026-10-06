import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.swing.*;

/*
 * El diálogo del calendario: arma la grilla del mes y devuelve la fecha
 * elegida.
 * Rol: VISTA (MVC). Le pregunta al GestorEventos (Controlador) qué fechas
 * están libres para el salón elegido; el gestor a su vez lo resuelve con la
 * estrategia de disponibilidad activa.
 */
public class SelectorFechaCalendario extends JDialog {
    private LocalDate fechaSeleccionada;
    private YearMonth mesActual;
    private final GestorEventos gestor;
    private final ValidadorFechaEvento validador = new ValidadorFechaEvento();
    private final Evento eventoActual;
    private final Lugar lugar;
    private JPanel panelDias;
    private JLabel etiquetaMes;

    public SelectorFechaCalendario(Frame owner, GestorEventos gestor, Evento eventoActual, Lugar lugar) {
        super(owner, "Seleccionar Fecha - " + lugar.getNombre(), true);
        this.gestor = gestor;
        this.eventoActual = eventoActual;
        this.lugar = lugar;

        // Si estamos editando un evento, abrimos el calendario en su mes
        if (eventoActual != null && eventoActual.getFecha() != null) {
            this.mesActual = YearMonth.from(eventoActual.getFecha());
        } else {
            this.mesActual = YearMonth.now();
        }

        setLayout(new BorderLayout());
        setSize(450, 320);
        setLocationRelativeTo(owner);
        inicializarComponentes();
        actualizarCalendario();
    }

    private void inicializarComponentes() {
        JPanel panelNavegacion = new JPanel(new BorderLayout());
        JButton btnAnterior = new JButton("<");
        btnAnterior.addActionListener(e -> { mesActual = mesActual.minusMonths(1); actualizarCalendario(); });

        JButton btnSiguiente = new JButton(">");
        btnSiguiente.addActionListener(e -> { mesActual = mesActual.plusMonths(1); actualizarCalendario(); });

        etiquetaMes = new JLabel("", SwingConstants.CENTER);
        panelNavegacion.add(btnAnterior, BorderLayout.WEST);
        panelNavegacion.add(etiquetaMes, BorderLayout.CENTER);
        panelNavegacion.add(btnSiguiente, BorderLayout.EAST);
        add(panelNavegacion, BorderLayout.NORTH);

        panelDias = new JPanel(new GridLayout(0, 7)); // Grilla de 7 columnas para los días
        add(panelDias, BorderLayout.CENTER);
    }

    private void actualizarCalendario() {
        panelDias.removeAll();
        etiquetaMes.setText(mesActual.getMonth() + " " + mesActual.getYear());

        String[] diasSemana = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (String dia : diasSemana) {
            panelDias.add(new JLabel(dia, SwingConstants.CENTER));
        }

        LocalDate primerDiaMes = mesActual.atDay(1);
        int diaSemanaInicio = primerDiaMes.getDayOfWeek().getValue();

        // Espacios vacíos antes del primer día del mes
        for (int i = 1; i < diaSemanaInicio; i++) {
            panelDias.add(new JLabel(""));
        }

        int diasEnMes = mesActual.lengthOfMonth();
        List<LocalDate> fechasConEventos = gestor.obtenerFechasConEventos(lugar, eventoActual);

        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fechaIteracion = mesActual.atDay(dia);
            BotonDiaCalendario boton = new BotonDiaCalendario(fechaIteracion, this);

            if (!validador.esFechaValida(fechaIteracion)) {
                boton.marcarComoPasado();
            } else if (!gestor.fechaDisponible(fechaIteracion, lugar, eventoActual)) {
                boton.marcarComoOcupado();
            } else if (fechasConEventos.contains(fechaIteracion)) {
                boton.marcarConReservas();
            }

            panelDias.add(boton);
        }
        panelDias.revalidate();
        panelDias.repaint();
    }

    public void setFechaSeleccionada(LocalDate fecha) {
        this.fechaSeleccionada = fecha;
        dispose(); // Cierra el calendario al elegir un día
    }

    public LocalDate getFechaSeleccionada() {
        return fechaSeleccionada;
    }
}
