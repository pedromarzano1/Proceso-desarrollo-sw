import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/*
 * FUNCION PRINCIPAL: Ventana principal de la aplicacion. Muestra la tabla de
 * eventos y los botones de accion (Agregar, Editar, Ver detalles, Eliminar),
 * arma los formularios de alta/edicion de evento y coordina las validaciones
 * (fecha, superposicion de horario/salon) antes de delegar los cambios al
 * GestorEventos.
 *
 * GRASP - Controlador: es el controlador de la interfaz principal; recibe los
 * eventos de los botones y orquesta las llamadas a GestorEventos,
 * ValidadorSuperposicion y SelectorFechaCalendario, sin implementar el mismo
 * esas reglas de negocio.
 * GRASP - Bajo Acoplamiento: delega la validacion de superposicion y de fecha a
 * clases especializadas en lugar de resolverlas dentro de la ventana.
 */
public class VentanaPrincipal extends JFrame {

    private GestorEventos gestor = new GestorEventos();
    private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
    private ValidadorSuperposicion validadorSuperposicion = new ValidadorSuperposicion();
    
    private DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Nombre", "Fecha", "Horario", "Salón"}, 0);
    private JTable tablaEventos = new JTable(modeloTabla);

    public VentanaPrincipal() {
        setTitle("Gestor de Eventos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750, 450);
        setLocationRelativeTo(null);
        tablaEventos.setDefaultEditor(Object.class, null);
        add(new JScrollPane(tablaEventos), BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel();
        panelBotones.add(crearBoton("Agregar", eventoClic -> agregarEvento()));
        panelBotones.add(crearBoton("Editar", eventoClic -> editarEvento()));
        panelBotones.add(crearBoton("Ver detalles y Servicios", eventoClic -> abrirDetalles()));
        panelBotones.add(crearBoton("Eliminar", eventoClic -> eliminarEvento()));
        add(panelBotones, BorderLayout.SOUTH);
        
        actualizarTablaEventos();
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
        Evento eventoNuevo = procesarFormulario(null);
        if (eventoNuevo != null) {
            gestor.agregarEvento(eventoNuevo);
            actualizarTablaEventos();
        }
    }

    private void editarEvento() {
        Evento eventoAEditar = obtenerEventoSeleccionado();
        if (eventoAEditar != null && procesarFormulario(eventoAEditar) != null) {
            gestor.actualizar();
            actualizarTablaEventos();
        }
    }

    private void abrirDetalles() {
        Evento eventoDetalle = obtenerEventoSeleccionado();
        if (eventoDetalle == null) return;
        DialogoDetalleEvento dialogo = new DialogoDetalleEvento(this, eventoDetalle, gestor, () -> actualizarTablaEventos());
        dialogo.setVisible(true);
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

    // --- MÉTODOS DESCOMPUESTOS PARA SOLUCIONAR "LONG METHOD" ---

    private Evento procesarFormulario(Evento eventoOriginal) {
        JTextField campoNombre = new JTextField(eventoOriginal == null ? "" : eventoOriginal.getNombre());
        JComboBox<Lugar> comboLugar = new JComboBox<>(Lugar.values());
        if (eventoOriginal != null) comboLugar.setSelectedItem(eventoOriginal.getLugar());
        
        JTextField campoHoraInicio = new JTextField(eventoOriginal == null ? "09:00" : eventoOriginal.getHoraInicio().format(formatoHora));
        JTextField campoHoraFin = new JTextField(eventoOriginal == null ? "12:00" : eventoOriginal.getHoraFin().format(formatoHora));
        JTextField campoDescripcion = new JTextField(eventoOriginal == null ? "" : eventoOriginal.getDescripcion());
        
        JButton botonFecha = new JButton(eventoOriginal == null ? "Abrir Calendario" : eventoOriginal.getFecha().format(formatoFecha));
        final LocalDate[] fechaElegida = {eventoOriginal == null ? null : eventoOriginal.getFecha()};

        botonFecha.addActionListener(eventoClic -> {
            SelectorFechaCalendario selector = new SelectorFechaCalendario(this, gestor, eventoOriginal);
            selector.setVisible(true);
            if (selector.getFechaSeleccionada() != null) {
                fechaElegida[0] = selector.getFechaSeleccionada();
                botonFecha.setText(fechaElegida[0].format(formatoFecha));
            }
        });

        while (true) {
            boolean formAceptado = solicitarDatos(eventoOriginal == null ? "Nuevo evento" : "Editar evento",
                    new String[]{"Nombre:", "Fecha:", "Hora Inicio (HH:mm):", "Hora Fin (HH:mm):", "Salón:", "Descripción:"},
                    new JComponent[]{campoNombre, botonFecha, campoHoraInicio, campoHoraFin, comboLugar, campoDescripcion});
                    
            if (!formAceptado) return null; 
            
            if (campoNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                continue;
            }
            if (fechaElegida[0] == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha.");
                continue;
            }

            LocalTime horaInicioParseada = parsearHoraSegmento(campoHoraInicio.getText().trim());
            LocalTime horaFinParseada = parsearHoraSegmento(campoHoraFin.getText().trim());
            
            if (horaInicioParseada == null || horaFinParseada == null) continue;

            Evento eventoTemporal = construirEventoTemporal(campoNombre, fechaElegida[0], horaInicioParseada, horaFinParseada, comboLugar, campoDescripcion, eventoOriginal);

            if (validadorSuperposicion.haySuperposicion(eventoTemporal, gestor)) {
                JOptionPane.showMessageDialog(this, "El salón seleccionado ya está reservado en ese horario.");
                continue; 
            }

            return aplicarCambiosAEvento(eventoOriginal, eventoTemporal);
        }
    }

    private LocalTime parsearHoraSegmento(String textoHora) {
        try {
            return LocalTime.parse(textoHora, formatoHora);
        } catch (Exception excepcionFormato) {
            JOptionPane.showMessageDialog(this, "Formato de hora inválido. Utilice HH:mm.");
            return null;
        }
    }

    private Evento construirEventoTemporal(JTextField nombre, LocalDate fecha, LocalTime inicio, LocalTime fin, JComboBox<Lugar> lugar, JTextField desc, Evento original) {
        if (original != null) {
            original.setFecha(fecha);
            original.setHoraInicio(inicio);
            original.setHoraFin(fin);
            original.setLugar((Lugar) lugar.getSelectedItem());
            return original;
        }
        return new Evento(nombre.getText().trim(), fecha, inicio, fin, (Lugar) lugar.getSelectedItem(), desc.getText().trim());
    }

    private Evento aplicarCambiosAEvento(Evento original, Evento temporal) {
        if (original == null) return temporal;
        original.setNombre(temporal.getNombre());
        original.setDescripcion(temporal.getDescripcion());
        return original;
    }
}