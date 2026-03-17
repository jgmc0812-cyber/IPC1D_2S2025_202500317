package bibliosystem.utils;

import bibliosystem.data.DataManager;
import bibliosystem.model.*;

public class Validaciones {
    
    /**
     * Valida que un texto no sea nulo ni vacío
     */
    public static boolean noVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    /**
     * Valida que un número sea positivo
     */
    public static boolean positivo(int numero) {
        return numero > 0;
    }
    
    /**
     * Valida formato de ISBN (10 o 13 dígitos, con guiones opcionales)
     */
    public static boolean isbnValido(String isbn) {
        if (isbn == null) return false;
        
        // Eliminar guiones comunes
        String limpio = isbn.replace("-", "").replace(" ", "");
        
        // ISBN-10: 10 dígitos (el último puede ser X)
        if (limpio.length() == 10) {
            boolean valido = true;
            for (int i = 0; i < 9; i++) {
                if (!Character.isDigit(limpio.charAt(i))) {
                    valido = false;
                    break;
                }
            }
            char ultimo = limpio.charAt(9);
            return valido && (Character.isDigit(ultimo) || ultimo == 'X' || ultimo == 'x');
        }
        
        // ISBN-13: 13 dígitos
        if (limpio.length() == 13) {
            for (int i = 0; i < 13; i++) {
                if (!Character.isDigit(limpio.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * Valida que el año esté en un rango razonable
     */
    public static boolean anioValido(int anio) {
        int anioActual = java.time.Year.now().getValue();
        return anio >= 1000 && anio <= anioActual;
    }
    
    /**
     * Valida que un estudiante pueda pedir préstamo
     */
    public static String validarPrestamoEstudiante(String carnet, String codigoLibro) {
        // 1. Verificar que el estudiante exista
        Usuario usuario = DataManager.buscarUsuario(carnet);
        if (usuario == null || !(usuario instanceof Estudiante)) {
            return "Estudiante no encontrado";
        }
        
        Estudiante estudiante = (Estudiante) usuario;
        
        // 2. Verificar que no tenga préstamos vencidos
        if (DataManager.tienePrestamosVencidos(carnet)) {
            return "El estudiante tiene préstamos vencidos";
        }
        
        // 3. Verificar que no tenga 3 préstamos activos
        int activos = DataManager.contarPrestamosActivosEstudiante(carnet);
        if (activos >= 3) {
            return "El estudiante ya tiene 3 préstamos activos";
        }
        
        // 4. Verificar que el libro exista
        Libro libro = DataManager.buscarLibro(codigoLibro);
        if (libro == null) {
            libro = DataManager.buscarLibroPorIsbn(codigoLibro);
        }
        
        if (libro == null) {
            return "Libro no encontrado";
        }
        
        // 5. Verificar que haya ejemplares disponibles
        if (!libro.tieneEjemplaresDisponibles()) {
            return "No hay ejemplares disponibles de este libro";
        }
        
        return null; // null = válido
    }
    
    /**
     * Valida que un operador pueda ser eliminado
     */
    public static boolean operadorEliminable(String username) {
        // No se puede eliminar al admin
        if (username.equals("admin")) return false;
        
        // Verificar que exista
        Usuario usuario = DataManager.buscarUsuario(username);
        return usuario != null && usuario.getRol().equals("OPERADOR");
    }
    
    /**
     * Valida que un libro pueda ser eliminado
     */
    public static boolean libroEliminable(String codigoInterno) {
        return !DataManager.tienePrestamosActivos(codigoInterno);
    }
    
    /**
     * Valida que un estudiante pueda ser eliminado
     */
    public static boolean estudianteEliminable(String carnet) {
        int activos = DataManager.contarPrestamosActivosEstudiante(carnet);
        boolean vencidos = DataManager.tienePrestamosVencidos(carnet);
        
        return activos == 0 && !vencidos;
    }
}