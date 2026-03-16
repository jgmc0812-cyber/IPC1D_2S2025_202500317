package bibliosystem.data;

import bibliosystem.model.*;

public class DataManager {
    
    // ===== ARREGLOS ESTÁTICOS (TAMAÑO FIJO) =====
    public static final int MAX_USUARIOS = 100;
    public static final int MAX_LIBROS = 100;
    public static final int MAX_PRESTAMOS = 500;
    
    // Usuarios: 0 = ADMIN (fijo), 1-99 = operadores y estudiantes
    public static Usuario[] usuarios = new Usuario[MAX_USUARIOS];
    public static int totalUsuarios = 0;
    
    // Libros
    public static Libro[] libros = new Libro[MAX_LIBROS];
    public static int totalLibros = 0;
    
    // Préstamos (arreglo general para búsquedas rápidas)
    public static Prestamo[] prestamos = new Prestamo[MAX_PRESTAMOS];
    public static int totalPrestamos = 0;
    
    // ===== MÉTODOS DE INICIALIZACIÓN =====
    
    /**
     * Inicializa los datos del sistema
     * Se llama al arrancar la aplicación
     */
    public static void inicializarSistema() {
        System.out.println("Inicializando sistema...");
        
        // 1. Crear usuario ADMIN por defecto (si no existe en archivo)
        Usuario admin = new Usuario("admin", "admin", "Administrador del Sistema", "ADMIN");
        usuarios[0] = admin;
        totalUsuarios = 1;
        
        // 2. Cargar datos desde archivos
        ArchivoCuentas.cargarCuentas();
        ArchivoPrestamos.cargarPrestamos();
        
        // 3. Inicializar libros de prueba (OPCIONAL - para pruebas)
        inicializarLibrosPrueba();
        
        System.out.println("Sistema inicializado. Usuarios: " + totalUsuarios + 
                           ", Libros: " + totalLibros + 
                           ", Préstamos: " + totalPrestamos);
    }
    
    /**
     * Crea algunos libros de ejemplo para poder probar el sistema
     * (Esto es opcional, el enunciado dice que los libros NO se persisten)
     */
    private static void inicializarLibrosPrueba() {
        if (totalLibros == 0) {
            System.out.println("Creando libros de prueba...");
            
            libros[0] = new Libro("L001", "978-3-16-148410-0", "El Principito", 
                                  "Antoine Saint-Exupéry", "Infantil", 1943, 5);
            libros[1] = new Libro("L002", "978-0-7432-7356-5", "Cien Años de Soledad", 
                                  "Gabriel García Márquez", "Realismo Mágico", 1967, 3);
            libros[2] = new Libro("L003", "978-0-452-28423-4", "1984", 
                                  "George Orwell", "Ciencia Ficción", 1949, 4);
            libros[3] = new Libro("L004", "978-0-06-112008-4", "Don Quijote de la Mancha", 
                                  "Miguel de Cervantes", "Clásico", 1605, 2);
            
            totalLibros = 4;
        }
    }
    
    // ===== MÉTODOS DE UTILIDAD PARA USUARIOS =====
    
    public static int buscarUsuarioIndex(String username) {
        for (int i = 0; i < totalUsuarios; i++) {
            if (usuarios[i] != null && usuarios[i].getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }
    
    public static Usuario buscarUsuario(String username) {
        int index = buscarUsuarioIndex(username);
        return (index != -1) ? usuarios[index] : null;
    }
    
    public static boolean agregarUsuario(Usuario nuevoUsuario) {
        if (totalUsuarios < MAX_USUARIOS) {
            // Verificar que no exista el username
            if (buscarUsuario(nuevoUsuario.getUsername()) == null) {
                usuarios[totalUsuarios] = nuevoUsuario;
                totalUsuarios++;
                return true;
            }
        }
        return false;
    }
    
    public static boolean eliminarUsuario(String username) {
        // No se puede eliminar al admin
        if (username.equals("admin")) {
            return false;
        }
        
        int index = buscarUsuarioIndex(username);
        if (index != -1) {
            // Desplazar elementos
            for (int i = index; i < totalUsuarios - 1; i++) {
                usuarios[i] = usuarios[i + 1];
            }
            usuarios[totalUsuarios - 1] = null;
            totalUsuarios--;
            return true;
        }
        return false;
    }
    
    // ===== MÉTODOS DE UTILIDAD PARA LIBROS =====
    
    public static int buscarLibroIndex(String codigoInterno) {
        for (int i = 0; i < totalLibros; i++) {
            if (libros[i] != null && libros[i].getCodigoInterno().equals(codigoInterno)) {
                return i;
            }
        }
        return -1;
    }
    
    public static Libro buscarLibro(String codigoInterno) {
        int index = buscarLibroIndex(codigoInterno);
        return (index != -1) ? libros[index] : null;
    }
    
    public static Libro buscarLibroPorIsbn(String isbn) {
        for (int i = 0; i < totalLibros; i++) {
            if (libros[i] != null && libros[i].getIsbn().equals(isbn)) {
                return libros[i];
            }
        }
        return null;
    }
    
    public static Libro[] buscarLibrosPorTitulo(String titulo) {
        // Primero contar cuántos coinciden
        int contador = 0;
        for (int i = 0; i < totalLibros; i++) {
            if (libros[i] != null && 
                libros[i].getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                contador++;
            }
        }
        
        // Crear arreglo con los resultados
        Libro[] resultados = new Libro[contador];
        int index = 0;
        for (int i = 0; i < totalLibros; i++) {
            if (libros[i] != null && 
                libros[i].getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultados[index] = libros[i];
                index++;
            }
        }
        
        return resultados;
    }
    
    public static boolean agregarLibro(Libro nuevoLibro) {
        if (totalLibros < MAX_LIBROS) {
            // Verificar que no exista código interno ni ISBN
            if (buscarLibro(nuevoLibro.getCodigoInterno()) == null && 
                buscarLibroPorIsbn(nuevoLibro.getIsbn()) == null) {
                libros[totalLibros] = nuevoLibro;
                totalLibros++;
                return true;
            }
        }
        return false;
    }
    
    public static boolean eliminarLibro(String codigoInterno) {
        // Verificar que no tenga préstamos activos
        if (tienePrestamosActivos(codigoInterno)) {
            return false;
        }
        
        int index = buscarLibroIndex(codigoInterno);
        if (index != -1) {
            // Desplazar elementos
            for (int i = index; i < totalLibros - 1; i++) {
                libros[i] = libros[i + 1];
            }
            libros[totalLibros - 1] = null;
            totalLibros--;
            return true;
        }
        return false;
    }
    
    // ===== MÉTODOS DE UTILIDAD PARA PRÉSTAMOS =====
    
    public static int buscarPrestamoIndex(String codigoPrestamo) {
        for (int i = 0; i < totalPrestamos; i++) {
            if (prestamos[i] != null && prestamos[i].getCodigoPrestamo().equals(codigoPrestamo)) {
                return i;
            }
        }
        return -1;
    }
    
    public static Prestamo buscarPrestamo(String codigoPrestamo) {
        int index = buscarPrestamoIndex(codigoPrestamo);
        return (index != -1) ? prestamos[index] : null;
    }
    
    public static boolean agregarPrestamo(Prestamo nuevoPrestamo) {
        if (totalPrestamos < MAX_PRESTAMOS) {
            prestamos[totalPrestamos] = nuevoPrestamo;
            totalPrestamos++;
            
            // También agregar al historial del estudiante
            Estudiante estudiante = (Estudiante) buscarUsuario(nuevoPrestamo.getCarnetEstudiante());
            if (estudiante != null) {
                estudiante.agregarPrestamoAlHistorial(nuevoPrestamo);
            }
            
            return true;
        }
        return false;
    }
    
    public static boolean actualizarPrestamo(Prestamo prestamoActualizado) {
        int index = buscarPrestamoIndex(prestamoActualizado.getCodigoPrestamo());
        if (index != -1) {
            prestamos[index] = prestamoActualizado;
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si un libro tiene préstamos activos
     */
    public static boolean tienePrestamosActivos(String codigoLibro) {
        for (int i = 0; i < totalPrestamos; i++) {
            if (prestamos[i] != null && 
                prestamos[i].getCodigoLibro().equals(codigoLibro) &&
                prestamos[i].getEstado().equals("ACTIVO")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Cuenta préstamos activos de un estudiante
     */
    public static int contarPrestamosActivosEstudiante(String carnet) {
        int contador = 0;
        for (int i = 0; i < totalPrestamos; i++) {
            if (prestamos[i] != null && 
                prestamos[i].getCarnetEstudiante().equals(carnet) &&
                prestamos[i].getEstado().equals("ACTIVO")) {
                contador++;
            }
        }
        return contador;
    }
    
    /**
     * Verifica si un estudiante tiene préstamos vencidos
     */
    public static boolean tienePrestamosVencidos(String carnet) {
        for (int i = 0; i < totalPrestamos; i++) {
            if (prestamos[i] != null && 
                prestamos[i].getCarnetEstudiante().equals(carnet) &&
                prestamos[i].getEstado().equals("ACTIVO") && 
                prestamos[i].estaVencido()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtiene todos los préstamos activos
     */
    public static Prestamo[] getPrestamosActivos() {
        // Contar
        int contador = 0;
        for (int i = 0; i < totalPrestamos; i++) {
            if (prestamos[i] != null && prestamos[i].getEstado().equals("ACTIVO")) {
                contador++;
            }
        }
        
        // Llenar arreglo
        Prestamo[] activos = new Prestamo[contador];
        int index = 0;
        for (int i = 0; i < totalPrestamos; i++) {
            if (prestamos[i] != null && prestamos[i].getEstado().equals("ACTIVO")) {
                activos[index] = prestamos[i];
                index++;
            }
        }
        
        return activos;
    }
}
