import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.swing.*;

/*
 * El diálogo del calendario: arma la grilla del mes y devuelve la fecha
 * elegida.
 * Patrón: Controlador, coordina la navegación y delega las validaciones en
 * ValidadorFechaEvento y BuscadorFechasOcupadas.
 */
public class SelectorFechaCalendario extends JDialog {
    private LocalDate fechaSeleccionada;
    private YearMonth mesActual;
    private BuscadorFechasOcupadas buscador;
    private ValidadorFechaEvento validador;
    private JPanel panelDias;
    private JLabel etiquetaMes;
    private Evento eventoActual;

    public SelectorFechaCalendario(Frame owner, GestorEventos gestor, Evento eventoActual) {
        super(owner, "Seleccionar Fecha", true);
        this.eventoActual = eventoActual;
        this.buscador = new BuscadorFechasOcupadas(gestor);
        this.validador = new ValidadorFechaEvento();
        
        // Si estamos editando un evento futuro, abrimos el calendario en su mes
        if (eventoActual != null && eventoActual.getFecha() != null) {
            this.mesActual = YearMonth.from(eventoActual.getFecha());
        } else {
            this.mesActual = YearMonth.now();
        }
        
        setLayout(new BorderLayout());
        setSize(400, 300);
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
        List<LocalDate> fechasOcupadas = buscador.obtenerFechasOcupadas(eventoActual);

        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fechaIteracion = mesActual.atDay(dia);
            BotonDiaCalendario boton = new BotonDiaCalendario(fechaIteracion, this);

            if (!validador.esFechaValida(fechaIteracion)) {
                boton.marcarComoPasado();
            } else if (fechasOcupadas.contains(fechaIteracion)) {
                boton.marcarComoOcupado();
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