package bibliosystem.model;

public class Estudiante extends Usuario {
    // Atributos adicionales
    private String carne;
    private String carrera;
    private NodoPrestamo cabezaHistorial; // Para la lista enlazada de préstamos
    
    // Constructor vacío
    public Estudiante() {
        super();
        this.rol = "ESTUDIANTE";
        this.cabezaHistorial = null;
    }
    
    // Constructor con parámetros
    public Estudiante(String username, String password, String nombreCompleto, 
                      String carne, String carrera) {
        super(username, password, nombreCompleto, "ESTUDIANTE");
        this.carne = carne;
        this.carrera = carrera;
        this.cabezaHistorial = null;
    }
    
    // Getters y Setters
    public String getCarne() {
        return carne;
    }
    
    public void setCarne(String carne) {
        this.carne = carne;
    }
    
    public String getCarrera() {
        return carrera;
    }
    
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    
    public NodoPrestamo getCabezaHistorial() {
        return cabezaHistorial;
    }
    
    public void setCabezaHistorial(NodoPrestamo cabezaHistorial) {
        this.cabezaHistorial = cabezaHistorial;
    }
    
    // Métodos para la lista enlazada de préstamos
    public void agregarPrestamoAlHistorial(Prestamo prestamo) {
        NodoPrestamo nuevoNodo = new NodoPrestamo(prestamo);
        nuevoNodo.setSiguiente(cabezaHistorial); // Insertar al inicio (más reciente primero)
        cabezaHistorial = nuevoNodo;
    }
    
    // Método para contar préstamos activos
    public int contarPrestamosActivos() {
        int contador = 0;
        NodoPrestamo actual = cabezaHistorial;
        
        while (actual != null) {
            if (actual.getPrestamo().getEstado().equals("ACTIVO")) {
                contador++;
            }
            actual = actual.getSiguiente();
        }
        
        return contador;
    }
    
    // Método para verificar si tiene préstamos vencidos
    public boolean tienePrestamosVencidos() {
        NodoPrestamo actual = cabezaHistorial;
        
        while (actual != null) {
            Prestamo p = actual.getPrestamo();
            if (p.getEstado().equals("ACTIVO") && p.estaVencido()) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        
        return false;
    }
}
