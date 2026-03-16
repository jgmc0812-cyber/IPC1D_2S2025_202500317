package bibliosystem.model;

public class Usuario {
    // Atributos
    protected String username;
    protected String password;
    protected String nombreCompleto;
    protected String rol; // "ADMIN", "OPERADOR", "ESTUDIANTE"
    
    // Constructor vacío
    public Usuario() {
    }
    
    // Constructor con parámetros
    public Usuario(String username, String password, String nombreCompleto, String rol) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }
    
    // Getters y Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    // Método para validar login (lo usaremos después)
    public boolean validarLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}