package bibliosystem.model;

public class Operador extends Usuario {
    
    // Constructor vacío
    public Operador() {
        super();
        this.rol = "OPERADOR";
    }
    
    // Constructor con parámetros
    public Operador(String username, String password, String nombreCompleto) {
        super(username, password, nombreCompleto, "OPERADOR");
    }
}