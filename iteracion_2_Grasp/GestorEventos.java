import java.util.List;

/*
 * FUNCION PRINCIPAL: Administrar la colleccion de eventos en memoria (agregar,
 * eliminar, actualizar) y coordinar con RepositorioEventos para que cada
 * cambio quede persistido en disco.
 *
 * GRASP - Creador: es quien crea/instancia el RepositorioEventos y quien recibe
 * los objetos Evento para agregarlos a la lista, ya que es quien agrupa y
 * contiene la coleccion de eventos (condicion de Creador: "contiene o agrega
 * instancialmente" los objetos).
 * GRASP - Controlador (de caso de uso): actua como intermediario entre la
 * interfaz (VentanaPrincipal) y la persistencia (RepositorioEventos), sin
 * conocer detalles de la interfaz grafica.
 */
//Clase creador

public class GestorEventos {
    private final List<Evento> listaEventos;
    private final RepositorioEventos repositorio;

    public GestorEventos() { 
        this.repositorio = new RepositorioEventos();
        this.listaEventos = repositorio.cargar(); 
    }

    public List<Evento> getEventos() { return listaEventos; }
    
    public void agregarEvento(Evento nuevoEvento) { 
        listaEventos.add(nuevoEvento); 
        repositorio.guardar(listaEventos); 
    }
    
    public void eliminarEvento(Evento eventoAEliminar) { 
        listaEventos.remove(eventoAEliminar); 
        repositorio.guardar(listaEventos); 
    }
    
    public void actualizar() { 
        repositorio.guardar(listaEventos); 
    }
}