import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/*
 * Ventana de detalle de un evento: ver sus datos y administrar invitados y
 * servicios contratados.
 * Patrón: Controlador, coordina esas acciones delegando el formato del
 * email a ValidadorEmail y el guardado a GestorEventos.
 */
public class DialogoDetalleEvento extends JDialog {
    private Evento evento;
    private GestorEventos gestor;
    private Runnable callbackActualizar;
    
    private DefaultListModel<Invitado> mInvi = new DefaultListModel<>();
    private DefaultListModel<ServicioContratado> mServ = new DefaultListModel<>();
    private JList<Invitado> listaInvitados = new JList<>(mInvi);
    private JList<ServicioContratado> listaServicios = new JList<>(mServ);
    private ValidadorEmail validadorEmail = new ValidadorEmail();

    public DialogoDetalleEvento(JFrame owner, Evento evento, GestorEventos gestor, Runnable callbackActualizar) {
        super(owner, "Gestión de Detalles: " + evento.getNombre(), true);
        this.evento = evento;
        this.gestor = gestor;
        this.callbackActualizar = callbackActualizar;
        
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
                e -> agregarInvitado(), e -> editarInvitado(), e -> eliminarInvitado()));
        
        centro.add(armarPanelLista("Servicios Contratados", listaServicios, 
                e -> agregarServicio(), e -> editarServicio(), e -> eliminarServicio()));
        
        add(centro, BorderLayout.CENTER);

        JPanel abajo = new JPanel();
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        abajo.add(btnCerrar);
        add(abajo, BorderLayout.SOUTH);
    }

    private JPanel armarPanelLista(String titulo, JList<?> lista, 
            java.awt.event.ActionListener add, 
            java.awt.event.ActionListener edit, 
            java.awt.event.ActionListener del) {
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

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton b = new JButton(texto);
        b.addActionListener(accion);
        return b;
    }

    private void agregarInvitado() {
        Invitado i = formularioInvitado(null);
        if (i != null) {
            evento.agregarInvitado(i);
            gestor.actualizar();
            cargarListas();
        }
    }

    private void editarInvitado() {
        Invitado i = listaInvitados.getSelectedValue();
        if (i == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un invitado de la lista para editar.");
            return;
        }
        if (formularioInvitado(i) != null) {
            gestor.actualizar();
            listaInvitados.repaint();
        }
    }

    private void eliminarInvitado() {
        Invitado i = listaInvitados.getSelectedValue();
        if (i != null) {
            evento.eliminarInvitado(i);
            gestor.actualizar();
            cargarListas();
        }
    }

    private Invitado formularioInvitado(Invitado inv) {
        JTextField nombre = new JTextField(inv != null ? inv.getNombre() : "");
        JTextField email = new JTextField(inv != null ? inv.getEmail() : "");
        
        while (true) {
            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("Nombre:")); p.add(nombre);
            p.add(new JLabel("Email:")); p.add(email);
            
            int op = JOptionPane.showConfirmDialog(this, p, inv == null ? "Registrar Invitado" : "Editar", JOptionPane.OK_CANCEL_OPTION);
            if (op != JOptionPane.OK_OPTION) return null;
            
            if (nombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                continue;
            }
            if (!validadorEmail.esEmailValido(email.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Email inválido. Verifique que tenga '@' y un dominio válido.");
                continue;
            }
            
            if (inv == null) return new Invitado(nombre.getText().trim(), email.getText().trim());
            
            inv.setNombre(nombre.getText().trim());
            inv.setEmail(email.getText().trim());
            return inv;
        }
    }

    private void agregarServicio() {
        ServicioContratado s = formularioServicio(null);
        if (s != null) {
            evento.agregarServicio(s);
            gestor.actualizar();
            cargarListas();
        }
    }

    private void editarServicio() {
        ServicioContratado s = listaServicios.getSelectedValue();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio de la lista para editar.");
            return;
        }
        if (formularioServicio(s) != null) {
            gestor.actualizar();
            listaServicios.repaint();
        }
    }

    private void eliminarServicio() {
        ServicioContratado s = listaServicios.getSelectedValue();
        if (s != null) {
            evento.eliminarServicio(s);
            gestor.actualizar();
            cargarListas();
        }
    }

    private ServicioContratado formularioServicio(ServicioContratado s) {
        JTextField nombre = new JTextField(s != null ? s.getNombre() : "");
        JComboBox<CategoriaServicio> categoria = new JComboBox<>(CategoriaServicio.values());
        JTextField precio = new JTextField(s != null ? String.valueOf(s.getPrecio()) : "0.0");
        
        if (s != null) categoria.setSelectedItem(s.getCategoria());
        
        while(true) {
            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("Proveedor/Detalle:")); p.add(nombre);
            p.add(new JLabel("Categoría:")); p.add(categoria);
            p.add(new JLabel("Precio ($):")); p.add(precio);
            
            int op = JOptionPane.showConfirmDialog(this, p, s == null ? "Agregar Servicio" : "Editar Servicio", JOptionPane.OK_CANCEL_OPTION);
            if (op != JOptionPane.OK_OPTION) return null;
            
            if (nombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del proveedor no puede estar vacío.");
                continue;
            }
            
            double precioParsed;
            try {
                precioParsed = Double.parseDouble(precio.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese un precio válido (solo números).");
                continue;
            }
            
            if (s == null) return new ServicioContratado(nombre.getText().trim(), (CategoriaServicio)categoria.getSelectedItem(), precioParsed);
            
            s.setNombre(nombre.getText().trim());
            s.setCategoria((CategoriaServicio)categoria.getSelectedItem());
            s.setPrecio(precioParsed);
            return s;
        }
    }
}