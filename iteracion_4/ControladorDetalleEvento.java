/*
 * Controlador del caso de uso "Gestionar invitados y servicios de un evento".
 * La ventana DialogoDetalleEvento le pasa los datos y él valida, le pide al
 * Evento que cree/borre (Creador) y le pide al GestorEventos que guarde.
 * Patrones:
 *  - Controlador de caso de uso (GRASP): así GestorEventos no se convierte
 *    en un "God Controller" que hace todo.
 *  - Alta cohesión / SRP: solo se ocupa del detalle de UN evento.
 */
public class ControladorDetalleEvento {
    private final Evento evento;
    private final GestorEventos gestor;
    private final ValidadorEmail validadorEmail = new ValidadorEmail();

    public ControladorDetalleEvento(Evento evento, GestorEventos gestor) {
        this.evento = evento;
        this.gestor = gestor;
    }

    public Evento getEvento() { return evento; }

    // --- Invitados ---

    public void agregarInvitado(String nombre, String email) {
        validarInvitado(nombre, email);
        evento.agregarInvitado(nombre, email);   // Creador: lo crea el Evento
        gestor.guardarCambios();
    }

    public void editarInvitado(Invitado invitado, String nombre, String email) {
        validarInvitado(nombre, email);
        invitado.setNombre(nombre);
        invitado.setEmail(email);
        gestor.guardarCambios();
    }

    public void eliminarInvitado(Invitado invitado) {
        evento.eliminarInvitado(invitado);
        gestor.guardarCambios();
    }

    private void validarInvitado(String nombre, String email) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (!validadorEmail.esEmailValido(email))
            throw new IllegalArgumentException("Email inválido. Verifique que tenga '@' y un dominio válido.");
    }

    // --- Servicios ---

    public void agregarServicio(String nombre, CategoriaServicio categoria, double precio) {
        validarServicio(nombre, precio);
        evento.agregarServicio(nombre, categoria, precio);   // Creador: lo crea el Evento
        gestor.guardarCambios();
    }

    public void editarServicio(ServicioContratado servicio, String nombre, CategoriaServicio categoria, double precio) {
        validarServicio(nombre, precio);
        servicio.setNombre(nombre);
        servicio.setCategoria(categoria);
        servicio.setPrecio(precio);
        gestor.guardarCambios();
    }

    public void eliminarServicio(ServicioContratado servicio) {
        evento.eliminarServicio(servicio);
        gestor.guardarCambios();
    }

    private void validarServicio(String nombre, double precio) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del proveedor no puede estar vacío.");
        if (precio < 0)
            throw new IllegalArgumentException("El precio no puede ser negativo.");
    }
}
