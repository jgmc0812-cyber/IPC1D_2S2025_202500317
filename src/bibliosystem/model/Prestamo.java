package bibliosystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Prestamo {
    // Atributos
    private String codigoPrestamo; // Autogenerado
    private String carnetEstudiante;
    private String codigoLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion; // Null si aún no se ha devuelto
    private String estado; // "ACTIVO" o "DEVUELTO"
    
    // Formato de fechas
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Constructor vacío
    public Prestamo() {
    }
    
    // Constructor para nuevo préstamo
    public Prestamo(String codigoPrestamo, String carnetEstudiante, String codigoLibro) {
        this.codigoPrestamo = codigoPrestamo;
        this.carnetEstudiante = carnetEstudiante;
        this.codigoLibro = codigoLibro;
        this.fechaPrestamo = LocalDate.now();
        this.fechaLimite = fechaPrestamo.plusDays(15);
        this.fechaDevolucion = null;
        this.estado = "ACTIVO";
    }
    
    // Constructor completo (para cargar desde archivo)
    public Prestamo(String codigoPrestamo, String carnetEstudiante, String codigoLibro,
                    LocalDate fechaPrestamo, LocalDate fechaLimite, 
                    LocalDate fechaDevolucion, String estado) {
        this.codigoPrestamo = codigoPrestamo;
        this.carnetEstudiante = carnetEstudiante;
        this.codigoLibro = codigoLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }
    
    // Getters y Setters
    public String getCodigoPrestamo() {
        return codigoPrestamo;
    }
    
    public void setCodigoPrestamo(String codigoPrestamo) {
        this.codigoPrestamo = codigoPrestamo;
    }
    
    public String getCarnetEstudiante() {
        return carnetEstudiante;
    }
    
    public void setCarnetEstudiante(String carnetEstudiante) {
        this.carnetEstudiante = carnetEstudiante;
    }
    
    public String getCodigoLibro() {
        return codigoLibro;
    }
    
    public void setCodigoLibro(String codigoLibro) {
        this.codigoLibro = codigoLibro;
    }
    
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
    
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
    
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }
    
    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
    
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
    
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    // Métodos útiles
    public boolean estaVencido() {
        if (estado.equals("ACTIVO") && LocalDate.now().isAfter(fechaLimite)) {
            return true;
        }
        return false;
    }
    
    public long getDiasAtraso() {
        if (!estaVencido()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(fechaLimite, LocalDate.now());
    }
    
    public void registrarDevolucion() {
        this.fechaDevolucion = LocalDate.now();
        this.estado = "DEVUELTO";
    }
    
    // Métodos para formato de archivo
    public String toArchivoString() {
        // Formato: codPrestamo;carnet;codLibro;fechaPrestamo;fechaLimite;fechaDevolucion;estado
        String fechaDevStr = (fechaDevolucion == null) ? "null" : fechaDevolucion.format(FORMATO_FECHA);
        
        return codigoPrestamo + ";" + 
               carnetEstudiante + ";" + 
               codigoLibro + ";" + 
               fechaPrestamo.format(FORMATO_FECHA) + ";" + 
               fechaLimite.format(FORMATO_FECHA) + ";" + 
               fechaDevStr + ";" + 
               estado;
    }
    
    public static Prestamo fromArchivoString(String linea) {
        String[] partes = linea.split(";");
        if (partes.length >= 7) {
            try {
                String codPrestamo = partes[0];
                String carnet = partes[1];
                String codLibro = partes[2];
                LocalDate fechaPrestamo = LocalDate.parse(partes[3], FORMATO_FECHA);
                LocalDate fechaLimite = LocalDate.parse(partes[4], FORMATO_FECHA);
                LocalDate fechaDevolucion = partes[5].equals("null") ? null : LocalDate.parse(partes[5], FORMATO_FECHA);
                String estado = partes[6];
                
                return new Prestamo(codPrestamo, carnet, codLibro, fechaPrestamo, 
                                   fechaLimite, fechaDevolucion, estado);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        String info = codigoPrestamo + " - Libro: " + codigoLibro + " - Estudiante: " + carnetEstudiante;
        info += " - Fecha préstamo: " + fechaPrestamo.format(FORMATO_FECHA);
        info += " - Fecha límite: " + fechaLimite.format(FORMATO_FECHA);
        info += " - Estado: " + estado;
        
        if (fechaDevolucion != null) {
            info += " - Devuelto: " + fechaDevolucion.format(FORMATO_FECHA);
        }
        
        if (estaVencido()) {
            info += " - ¡VENCIDO! (" + getDiasAtraso() + " días)";
        }
        
        return info;
    }
}
