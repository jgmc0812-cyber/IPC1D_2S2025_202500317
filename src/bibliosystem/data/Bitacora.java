package bibliosystem.data;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bitacora {
    
    private static final String NOMBRE_ARCHIVO = "bitacora.txt";
    private static final DateTimeFormatter FORMATO_FECHA = 
            DateTimeFormatter.ofPattern("dd/MM/yy");
    private static final DateTimeFormatter FORMATO_HORA = 
            DateTimeFormatter.ofPattern("hh:mm a");
    
    /**
     * Registra una operación en la bitácora
     * Formato: [OPERACION][USUARIO][MODULO][FECHA][HORA]
     */
    public static void registrar(String operacion, String usuario, String modulo) {
        try {
            FileWriter archivo = new FileWriter(NOMBRE_ARCHIVO, true); // true = append
            PrintWriter escritor = new PrintWriter(archivo);
            
            LocalDateTime ahora = LocalDateTime.now();
            String fecha = ahora.format(FORMATO_FECHA);
            String hora = ahora.format(FORMATO_HORA);
            
            String linea = "[" + operacion + "][" + usuario + "][" + modulo + "][" + 
                          fecha + "][" + hora + "]";
            
            escritor.println(linea);
            escritor.close();
            
        } catch (Exception e) {
            System.err.println("Error al escribir en bitácora: " + e.getMessage());
        }
    }
    
    /**
     * Versión simplificada para cuando no hay usuario (ej: login fallido)
     */
    public static void registrar(String operacion, String modulo) {
        registrar(operacion, "SISTEMA", modulo);
    }
    
    /**
     * Métodos de conveniencia para tipos comunes de operaciones
     */
    public static void loginExitoso(String usuario) {
        registrar("LOGIN_EXITOSO", usuario, "AUTENTICACION");
    }
    
    public static void loginFallido(String username) {
        registrar("LOGIN_FALLIDO", username, "AUTENTICACION");
    }
    
    public static void cierreSesion(String usuario) {
        registrar("CIERRE_SESION", usuario, "AUTENTICACION");
    }
    
    public static void operacionExitosa(String operacion, String usuario, String modulo) {
        registrar(operacion, usuario, modulo);
    }
    
    public static void operacionErronea(String operacion, String usuario, String modulo, String motivo) {
        registrar("ERROR_" + operacion + "_" + motivo, usuario, modulo);
    }
}
