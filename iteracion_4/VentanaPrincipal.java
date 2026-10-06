import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/*
 * La ventana principal: tabla de eventos y botones de Agregar, Editar, Ver
 * detalles, Eliminar, más el selector de la regla de disponibilidad.
 * Rol: VISTA (MVC). Muestra datos, junta lo que escribe el usuario, controla
 * el formato de las horas y le pasa todo a GestorEventos (el Controlador).
 * No decide reglas de negocio.
 */
public class VentanaPrincipal extends JFrame {

    private final GestorEventos gestor;
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Nombre", "Fecha", "Horario", "Salón"}, 0);
    private final JTable tablaEventos = new JTable(modeloTabla);

    public VentanaPrincipal(GestorEventos gestor) {
        this.gestor = gestor;
        setTitle("Gestor de Eventos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 450);
        setLocationRelativeTo(null);
        tablaEventos.setDefaultEditor(Object.class, null);
        add(new JScrollPane(tablaEventos), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.add(crearBoton("Agregar", eventoClic -> agregarEvento()));
        panelBotones.add(crearBoton("Editar", eventoClic -> editarEvento()));
        panelBotones.add(crearBoton("Ver detalles y Servicios", eventoClic -> abrirDetalles()));
        panelBotones.add(crearBoton("Eliminar", eventoClic -> eliminarEvento()));
        panelBotones.add(new JLabel("   Regla de reserva:"));
        panelBotones.add(crearSelectorEstrategia());
        add(panelBotones, BorderLayout.SOUTH);

        actualizarTablaEventos();
    }

    // Strategy: el usuario elige la regla y la ventana se la pasa al Contexto (GestorEventos).
    private JComboBox<EstrategiaDisponibilidad> crearSelectorEstrategia() {
        JComboBox<EstrategiaDisponibilidad> combo = new JComboBox<>(new EstrategiaDisponibilidad[]{
                new DisponibilidadPorSalonYHorario(),
                new DisponibilidadPorDiaCompleto()
        });
        combo.addActionListener(e -> gestor.setEstrategia((EstrategiaDisponibilidad) combo.getSelectedItem()));
        return combo;
    }

    private JButton crearBoton(String textoBoton, ActionListener accionAsignada) {
        JButton botonGenerado = new JButton(textoBoton);
        botonGenerado.addActionListener(accionAsignada);
        return botonGenerado;
    }

    private boolean solicitarDatos(String tituloVentana, String[] etiquetas, JComponent[] camposDeEntrada) {
        JPanel panelFormulario = new JPanel(new GridLayout(0, 1));
        for (int indice = 0; indice < etiquetas.length; indice++) {
            panelFormulario.add(new JLabel(etiquetas[indice]));
            panelFormulario.add(camposDeEntrada[indice]);
        }
        return JOptionPane.showConfirmDialog(this, panelFormulario, tituloVentana, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

    private void actualizarTablaEventos() {
        modeloTabla.setRowCount(0);
        for (Evento eventoActual : gestor.getEventos()) {
            String horarioFormateado = eventoActual.getHoraInicio().format(formatoHora) + " a " + eventoActual.getHoraFin().format(formatoHora);
            modeloTabla.addRow(new Object[]{eventoActual.getNombre(), eventoActual.getFecha().format(formatoFecha), horarioFormateado, eventoActual.getLugar().getNombre()});
        }
    }

    private Evento obtenerEventoSeleccionado() {
        int filaSeleccionada = tablaEventos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un evento de la lista.");
            return null;
        }
        return gestor.getEventos().get(filaSeleccionada);
    }

    private void agregarEvento() {
        if (procesarFormulario(null)) actualizarTablaEventos();
    }

    private void editarEvento() {
        Evento eventoAEditar = obtenerEventoSeleccionado();
        if (eventoAEditar != null && procesarFormulario(eventoAEditar)) actualizarTablaEventos();
    }

    private void abrirDetalles() {
        Evento eventoDetalle = obtenerEventoSeleccionado();
        if (eventoDetalle == null) return;
        ControladorDetalleEvento controlador = new ControladorDetalleEvento(eventoDetalle, gestor);
        new DialogoDetalleEvento(this, controlador).setVisible(true);
        actualizarTablaEventos();
    }

    private void eliminarEvento() {
        Evento eventoAEliminar = obtenerEventoSeleccionado();
        if (eventoAEliminar == null) return;
        int opcionConfirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar \"" + eventoAEliminar.getNombre() + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcionConfirmacion == JOptionPane.YES_OPTION) {
            gestor.eliminarEvento(eventoAEliminar);
            actualizarTablaEventos();
        }
    }

    // Muestra el formulario hasta que los datos sean válidos o el usuario cancele.
    // Devuelve true si se guardó algo.
    private boolean procesarFormulario(Evento eventoOriginal) {
        JTextField campoNombre = new JTextField(eventoOriginal == null ? "" : eventoOriginal.getNombre());
        JComboBox<Lugar> comboLugar = new JComboBox<>(Lugar.values());
        if (eventoOriginal != null) comboLugar.setSelectedItem(eventoOriginal.getLugar());

        JTextField campoHoraInicio = new JTextField(eventoOriginal == null ? "09:00" : eventoOriginal.getHoraInicio().format(formatoHora));
        JTextField campoHoraFin = new JTextField(eventoOriginal == null ? "12:00" : eventoOriginal.getHoraFin().format(formatoHora));
        JTextField campoDescripcion = new JTextField(eventoOriginal == null ? "" : eventoOriginal.getDescripcion());

        JButton botonFecha = new JButton(eventoOriginal == null ? "Abrir Calendario" : eventoOriginal.getFecha().format(formatoFecha));
        final LocalDate[] fechaElegida = {eventoOriginal == null ? null : eventoOriginal.getFecha()};

        botonFecha.addActionListener(eventoClic -> {
            Lugar lugarElegido = (Lugar) comboLugar.getSelectedItem();
            SelectorFechaCalendario selector = new SelectorFechaCalendario(this, gestor, eventoOriginal, lugarElegido);
            selector.setVisible(true);
            if (selector.getFechaSeleccionada() != null) {
                fechaElegida[0] = selector.getFechaSeleccionada();
                botonFecha.setText(fechaElegida[0].format(formatoFecha));
            }
        });

        while (true) {
            boolean formAceptado = solicitarDatos(eventoOriginal == null ? "Nuevo evento" : "Editar evento",
                    new String[]{"Salón:", "Fecha:", "Nombre:", "Hora Inicio (HH:mm):", "Hora Fin (HH:mm):", "Descripción:"},
                    new JComponent[]{comboLugar, botonFecha, campoNombre, campoHoraInicio, campoHoraFin, campoDescripcion});

            if (!formAceptado) return false;

            // Validación de FORMATO (le corresponde a la vista, según MVC)
            LocalTime horaInicio = parsearHora(campoHoraInicio.getText().trim());
            LocalTime horaFin = parsearHora(campoHoraFin.getText().trim());
            if (horaInicio == null || horaFin == null) continue;

            String nombre = campoNombre.getText().trim();
            Lugar lugar = (Lugar) comboLugar.getSelectedItem();
            String descripcion = campoDescripcion.getText().trim();

            // Las reglas de negocio las valida el Controlador
            try {
                if (eventoOriginal == null) {
                    gestor.agregarEvento(nombre, fechaElegida[0], horaInicio, horaFin, lugar, descripcion);
                } else {
                    gestor.editarEvento(eventoOriginal, nombre, fechaElegida[0], horaInicio, horaFin, lugar, descripcion);
                }
                return true;
            } catch (IllegalArgumentException error) {
                JOptionPane.showMessageDialog(this, error.getMessage());
            }
        }
    }

    private LocalTime parsearHora(String textoHora) {
        try {
            return LocalTime.parse(textoHora, formatoHora);
        } catch (Exception excepcionFormato) {
            JOptionPane.showMessageDialog(this, "Formato de hora inválido. Utilice HH:mm.");
            return null;
        }
    }
}
