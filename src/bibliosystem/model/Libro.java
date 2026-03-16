package bibliosystem.model;

public class Libro {
    // Atributos
    private String codigoInterno; // Único
    private String isbn; // Único (10 o 13 dígitos)
    private String titulo;
    private String autor;
    private String genero;
    private int anioPublicacion;
    private int totalEjemplares;
    private int disponibles;
    
    // Constructor vacío
    public Libro() {
    }
    
    // Constructor con parámetros
    public Libro(String codigoInterno, String isbn, String titulo, String autor, 
                 String genero, int anioPublicacion, int totalEjemplares) {
        this.codigoInterno = codigoInterno;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anioPublicacion = anioPublicacion;
        this.totalEjemplares = totalEjemplares;
        this.disponibles = totalEjemplares; // Inicialmente disponibles = total
    }
    
    // Getters y Setters
    public String getCodigoInterno() {
        return codigoInterno;
    }
    
    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public int getAnioPublicacion() {
        return anioPublicacion;
    }
    
    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }
    
    public int getTotalEjemplares() {
        return totalEjemplares;
    }
    
    public void setTotalEjemplares(int totalEjemplares) {
        this.totalEjemplares = totalEjemplares;
    }
    
    public int getDisponibles() {
        return disponibles;
    }
    
    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }
    
    // Métodos útiles
    public boolean prestarEjemplar() {
        if (disponibles > 0) {
            disponibles--;
            return true;
        }
        return false;
    }
    
    public boolean devolverEjemplar() {
        if (disponibles < totalEjemplares) {
            disponibles++;
            return true;
        }
        return false;
    }
    
    public boolean tieneEjemplaresDisponibles() {
        return disponibles > 0;
    }
    
    public int getEjemplaresPrestados() {
        return totalEjemplares - disponibles;
    }
    
    @Override
    public String toString() {
        return codigoInterno + " - " + titulo + " (" + disponibles + "/" + totalEjemplares + ")";
    }
}