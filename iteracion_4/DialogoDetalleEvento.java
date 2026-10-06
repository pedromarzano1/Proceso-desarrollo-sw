import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/*
 * Ventana de detalle de un evento: ver sus datos y administrar invitados y
 * servicios contratados.
 * Rol: VISTA (MVC). Muestra y junta datos; todo lo que sea validar, crear o
 * guardar se lo pide a ControladorDetalleEvento.
 */
public class DialogoDetalleEvento extends JDialog {
    private final ControladorDetalleEvento controlador;
    private final Evento evento;

    private final DefaultListModel<Invitado> mInvi = new DefaultListModel<>();
    private final DefaultListModel<ServicioContratado> mServ = new DefaultListModel<>();
    private final JList<Invitado> listaInvitados = new JList<>(mInvi);
    private final JList<ServicioContratado> listaServicios = new JList<>(mServ);

    public DialogoDetalleEvento(JFrame owner, ControladorDetalleEvento controlador) {
        super(owner, "Gestión de Detalles: " + controlador.getEvento().getNombre(), true);
        this.controlador = controlador;
        this.evento = controlador.getEvento();

        setSize(750, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        cargarListas();
        armarInterfaz();
    }

    private void cargarListas() {
        mInvi.clear();
        mServ.clear();
        for (Invitado i : evento.getInvitados()) mInvi.addElement(i);
        for (ServicioContratado s : evento.getServicios()) mServ.addElement(s);
    }

    private void armarInterfaz() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm");

        JTextArea info = new JTextArea("Nombre: " + evento.getNombre() +
                "\nFecha: " + evento.getFecha().format(fmt) +
                "\nHorario: " + evento.getHoraInicio().format(fmtHora) + " a " + evento.getHoraFin().format(fmtHora) +
                "\nSalón: " + evento.getLugar().getNombre() +
                "\nDescripción: " + evento.getDescripcion());
        info.setEditable(false);
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(info), BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 0));
        centro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centro.add(armarPanelLista("Invitados", listaInvitados,
                e -> formularioInvitado(null), e -> editarInvitado(), e -> eliminarInvitado()));

        centro.add(armarPanelLista("Servicios Contratados", listaServicios,
                e -> formularioServicio(null), e -> editarServicio(), e -> eliminarServicio()));

        add(centro, BorderLayout.CENTER);

        JPanel abajo = new JPanel();
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        abajo.add(btnCerrar);
        add(abajo, BorderLayout.SOUTH);
    }

    private JPanel armarPanelLista(String titulo, JList<?> lista, ActionListener add, ActionListener edit, ActionListener del) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(titulo, SwingConstants.CENTER), BorderLayout.NORTH);
        p.add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(1, 3, 5, 0));
        botones.add(crearBoton("+", add));
        botones.add(crearBoton("Editar", edit));
        botones.add(crearBoton("-", del));
        p.add(botones, BorderLayout.SOUTH);
        return p;
    }

    private JButton crearBoton(String texto, ActionListener accion) {
        JButton b = new JButton(texto);
        b.addActionListener(accion);
        return b;
    }

    // ---------------- Invitados ----------------

    private void editarInvitado() {
        Invitado i = listaInvitados.getSelectedValue();
        if (i == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un invitado de la lista para editar.");
            return;
        }
        formularioInvitado(i);
    }

    private void eliminarInvitado() {
        Invitado i = listaInvitados.getSelectedValue();
        if (i != null) {
            controlador.eliminarInvitado(i);
            cargarListas();
        }
    }

    // inv == null -> alta; inv != null -> edición
    private void formularioInvitado(Invitado inv) {
        JTextField nombre = new JTextField(inv != null ? inv.getNombre() : "");
        JTextField email = new JTextField(inv != null ? inv.getEmail() : "");

        while (true) {
            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("Nombre:")); p.add(nombre);
            p.add(new JLabel("Email:")); p.add(email);

            int op = JOptionPane.showConfirmDialog(this, p, inv == null ? "Registrar Invitado" : "Editar", JOptionPane.OK_CANCEL_OPTION);
            if (op != JOptionPane.OK_OPTION) return;

            try {
                if (inv == null) controlador.agregarInvitado(nombre.getText().trim(), email.getText().trim());
                else controlador.editarInvitado(inv, nombre.getText().trim(), email.getText().trim());
                cargarListas();
                return;
            } catch (IllegalArgumentException error) {
                JOptionPane.showMessageDialog(this, error.getMessage());
            }
        }
    }

    // ---------------- Servicios ----------------

    private void editarServicio() {
        ServicioContratado s = listaServicios.getSelectedValue();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio de la lista para editar.");
            return;
        }
        formularioServicio(s);
    }

    private void eliminarServicio() {
        ServicioContratado s = listaServicios.getSelectedValue();
        if (s != null) {
            controlador.eliminarServicio(s);
            cargarListas();
        }
    }

    // s == null -> alta; s != null -> edición
    private void formularioServicio(ServicioContratado s) {
        JTextField nombre = new JTextField(s != null ? s.getNombre() : "");
        JComboBox<CategoriaServicio> categoria = new JComboBox<>(CategoriaServicio.values());
        JTextField precio = new JTextField(s != null ? String.valueOf(s.getPrecio()) : "0.0");

        if (s != null) categoria.setSelectedItem(s.getCategoria());

        while (true) {
            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("Proveedor/Detalle:")); p.add(nombre);
            p.add(new JLabel("Categoría:")); p.add(categoria);
            p.add(new JLabel("Precio ($):")); p.add(precio);

            int op = JOptionPane.showConfirmDialog(this, p, s == null ? "Agregar Servicio" : "Editar Servicio", JOptionPane.OK_CANCEL_OPTION);
            if (op != JOptionPane.OK_OPTION) return;

            // Validación de FORMATO (vista): que el precio sea un número
            double precioParsed;
            try {
                precioParsed = Double.parseDouble(precio.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese un precio válido (solo números).");
                continue;
            }

            CategoriaServicio cat = (CategoriaServicio) categoria.getSelectedItem();
            try {
                if (s == null) controlador.agregarServicio(nombre.getText().trim(), cat, precioParsed);
                else controlador.editarServicio(s, nombre.getText().trim(), cat, precioParsed);
                cargarListas();
                return;
            } catch (IllegalArgumentException error) {
                JOptionPane.showMessageDialog(this, error.getMessage());
            }
        }
    }
}
