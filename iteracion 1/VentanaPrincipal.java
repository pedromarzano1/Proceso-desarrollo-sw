import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaPrincipal extends JFrame {

    private GestorEventos gestor = new GestorEventos();
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Nombre", "Fecha", "Ubicación", "Asistentes"}, 0);
    private JTable tabla = new JTable(modelo);

    public VentanaPrincipal() {
        setTitle("Gestor de Eventos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        tabla.setDefaultEditor(Object.class, null);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        JPanel botones = new JPanel();
        botones.add(boton("Agregar", e -> agregar()));
        botones.add(boton("Editar", e -> editar()));
        botones.add(boton("Ver detalle / Asistentes", e -> verDetalle()));
        botones.add(boton("Eliminar", e -> eliminar()));
        add(botones, BorderLayout.SOUTH);
        mostrarEventos();
    }

    private JButton boton(String texto, ActionListener accion) {
        JButton b = new JButton(texto);
        b.addActionListener(accion);
        return b;
    }

    // Arma un panel con pares etiqueta/campo y muestra Aceptar/Cancelar
    private boolean pedirDatos(String titulo, String[] etiquetas, JComponent[] campos) {
        JPanel p = new JPanel(new GridLayout(0, 1));
        for (int i = 0; i < etiquetas.length; i++) {
            p.add(new JLabel(etiquetas[i]));
            p.add(campos[i]);
        }
        return JOptionPane.showConfirmDialog(this, p, titulo, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

    private boolean vacio(JTextField campo) {
        if (campo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return true;
        }
        return false;
    }

    private void mostrarEventos() {
        modelo.setRowCount(0);
        for (Evento e : gestor.getEventos())
            modelo.addRow(new Object[]{e.getNombre(), e.getFecha().format(fmt), e.getUbicacion(), e.getAsistentes().size()});
    }

    private Evento eventoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná un evento de la lista.");
            return null;
        }
        return gestor.getEventos().get(fila);
    }

    private void agregar() {
        Evento nuevo = formulario(null);
        if (nuevo != null) {
            gestor.agregarEvento(nuevo);
            mostrarEventos();
        }
    }

    private void editar() {
        Evento e = eventoSeleccionado();
        if (e != null && formulario(e) != null) {
            gestor.actualizar();
            mostrarEventos();
        }
    }

    // Si e es null crea un evento; si no, modifica el que se pasa. Devuelve null si se cancela.
    private Evento formulario(Evento e) {
        JTextField nombre = new JTextField(e == null ? "" : e.getNombre());
        JTextField ubicacion = new JTextField(e == null ? "" : e.getUbicacion());
        JTextField descripcion = new JTextField(e == null ? "" : e.getDescripcion());
        
        // Creamos un botón en lugar de un campo de texto para la fecha
        JButton btnFecha = new JButton(e == null ? "Abrir Calendario" : e.getFecha().format(fmt));
        final LocalDate[] fechaElegida = {e == null ? null : e.getFecha()};

        btnFecha.addActionListener(ev -> {
            SelectorFechaCalendario selector = new SelectorFechaCalendario(this, gestor, e);
            selector.setVisible(true);
            LocalDate seleccion = selector.getFechaSeleccionada();
            if (seleccion != null) {
                fechaElegida[0] = seleccion;
                btnFecha.setText(seleccion.format(fmt));
            }
        });

        boolean ok = pedirDatos(e == null ? "Nuevo evento" : "Editar evento",
                new String[]{"Nombre:", "Fecha:", "Ubicación:", "Descripción:"},
                new JComponent[]{nombre, btnFecha, ubicacion, descripcion});
                
        if (!ok || vacio(nombre)) return null;
        
        if (fechaElegida[0] == null) {
            JOptionPane.showMessageDialog(this, "Es obligatorio seleccionar una fecha desde el calendario.");
            return null;
        }

        if (e == null) {
            return new Evento(nombre.getText().trim(), fechaElegida[0], ubicacion.getText().trim(), descripcion.getText().trim());
        }
        
        e.setNombre(nombre.getText().trim());
        e.setFecha(fechaElegida[0]);
        e.setUbicacion(ubicacion.getText().trim());
        e.setDescripcion(descripcion.getText().trim());
        return e;
    }

    private void eliminar() {
        Evento e = eventoSeleccionado();
        if (e == null) return;
        int op = JOptionPane.showConfirmDialog(this, "¿Eliminar \"" + e.getNombre() + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            gestor.eliminarEvento(e);
            mostrarEventos();
        }
    }

    private void verDetalle() {
        Evento e = eventoSeleccionado();
        if (e == null) return;
        JDialog d = new JDialog(this, "Detalle: " + e.getNombre(), true);
        d.setSize(500, 470);
        d.setLocationRelativeTo(this);
        JTextArea info = new JTextArea("Nombre: " + e.getNombre() + "\nFecha: " + e.getFecha().format(fmt)
                + "\nUbicación: " + e.getUbicacion() + "\nDescripción: " + e.getDescripcion());
        info.setEditable(false);
        d.add(new JScrollPane(info), BorderLayout.NORTH);
        DefaultListModel<String> mAsist = new DefaultListModel<>();
        DefaultListModel<String> mRec = new DefaultListModel<>();
        for (Asistente a : e.getAsistentes()) mAsist.addElement(a.toString());
        for (Recurso r : e.getRecursos()) mRec.addElement(r.toString());
        JPanel centro = new JPanel(new GridLayout(1, 2));
        centro.add(panelLista("Asistentes", mAsist));
        centro.add(panelLista("Recursos", mRec));
        d.add(centro, BorderLayout.CENTER);
        JPanel abajo = new JPanel();
        abajo.add(boton("Inscribir asistente", ev -> {
            Asistente a = pedirAsistente();
            if (a != null) {
                e.agregarAsistente(a);
                mAsist.addElement(a.toString());
                gestor.actualizar();
                mostrarEventos();
            }
        }));
        abajo.add(boton("Agregar recurso", ev -> {
            Recurso r = pedirRecurso();
            if (r != null) {
                e.agregarRecurso(r);
                mRec.addElement(r.toString());
                gestor.actualizar();
            }
        }));
        abajo.add(boton("Cerrar", ev -> d.dispose()));
        d.add(abajo, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private JPanel panelLista(String titulo, DefaultListModel<String> modelo) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(titulo), BorderLayout.NORTH);
        p.add(new JScrollPane(new JList<>(modelo)), BorderLayout.CENTER);
        return p;
    }

    private Asistente pedirAsistente() {
        JTextField nombre = new JTextField();
        JTextField email = new JTextField();
        if (!pedirDatos("Inscribir asistente", new String[]{"Nombre:", "Email:"}, new JComponent[]{nombre, email})) return null;
        if (vacio(nombre)) return null;
        return new Asistente(nombre.getText().trim(), email.getText().trim());
    }

    private Recurso pedirRecurso() {
        JTextField nombre = new JTextField();
        JComboBox<String> tipo = new JComboBox<>(Recurso.TIPOS);
        if (!pedirDatos("Agregar recurso", new String[]{"Nombre del recurso:", "Tipo:"}, new JComponent[]{nombre, tipo})) return null;
        if (vacio(nombre)) return null;
        return new Recurso(nombre.getText().trim(), (String) tipo.getSelectedItem());
    }
}