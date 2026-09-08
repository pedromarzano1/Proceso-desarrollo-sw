import java.util.List;

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