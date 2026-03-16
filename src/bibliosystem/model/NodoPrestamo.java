package bibliosystem.model;

public class NodoPrestamo {
    // Atributos
    private Prestamo prestamo;
    private NodoPrestamo siguiente;
    
    // Constructor
    public NodoPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
        this.siguiente = null;
    }
    
    // Getters y Setters
    public Prestamo getPrestamo() {
        return prestamo;
    }
    
    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }
    
    public NodoPrestamo getSiguiente() {
        return siguiente;
    }
    
    public void setSiguiente(NodoPrestamo siguiente) {
        this.siguiente = siguiente;
    }
}
