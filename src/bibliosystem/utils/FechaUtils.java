package bibliosystem.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FechaUtils {
    
    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Convierte String a LocalDate
     */
    public static LocalDate parseFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr, FORMATO);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Formatea LocalDate a String
     */
    public static String formatFecha(LocalDate fecha) {
        if (fecha == null) return "null";
        return fecha.format(FORMATO);
    }
    
    /**
     * Verifica si una fecha es válida
     */
    public static boolean fechaValida(String fechaStr) {
        return parseFecha(fechaStr) != null;
    }
    
    /**
     * Calcula días de diferencia entre dos fechas
     */
    public static long diasEntre(LocalDate inicio, LocalDate fin) {
        return java.time.temporal.ChronoUnit.DAYS.between(inicio, fin);
    }
}