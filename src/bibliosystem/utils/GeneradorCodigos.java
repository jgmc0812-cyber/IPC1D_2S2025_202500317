package bibliosystem.utils;

import bibliosystem.data.DataManager;

public class GeneradorCodigos {
    
    private static int contadorPrestamos = 1000;
    
    /**
     * Genera un código único para préstamo
     * Formato: PR-YYYY-XXXX
     */
    public static String generarCodigoPrestamo() {
        String anio = String.valueOf(java.time.Year.now().getValue());
        contadorPrestamos++;
        
        // Buscar un código que no exista
        String codigo;
        do {
            codigo = "PR-" + anio + "-" + contadorPrestamos;
            contadorPrestamos++;
        } while (DataManager.buscarPrestamo(codigo) != null);
        
        return codigo;
    }
    
    /**
     * Genera código interno para libro (si se quisiera auto-generar)
     */
    public static String generarCodigoLibro() {
        int contador = DataManager.totalLibros + 1;
        return "LIB" + String.format("%03d", contador);
    }
}