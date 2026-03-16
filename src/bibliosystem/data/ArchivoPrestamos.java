package bibliosystem.data;

import bibliosystem.model.*;
import java.io.*;
import java.time.LocalDate;

public class ArchivoPrestamos {
    
    private static final String NOMBRE_ARCHIVO = "prestamos.txt";
    
    /**
     * Carga los préstamos desde el archivo al iniciar el sistema
     * Formato: codPrestamo;carnet;codLibro;fechaPrestamo;fechaLimite;fechaDevolucion;estado
     */
    public static void cargarPrestamos() {
        File archivo = new File(NOMBRE_ARCHIVO);
        
        if (!archivo.exists()) {
            System.out.println("Archivo prestamos.txt no existe. Se creará al guardar.");
            return;
        }
        
        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivo));
            String linea;
            int contador = 0;
            
            while ((linea = lector.readLine()) != null && 
                   DataManager.totalPrestamos < DataManager.MAX_PRESTAMOS) {
                
                if (linea.trim().isEmpty()) continue;
                
                // Usar el método de la clase Prestamo para convertir desde archivo
                Prestamo prestamo = Prestamo.fromArchivoString(linea);
                
                if (prestamo != null) {
                    DataManager.prestamos[DataManager.totalPrestamos] = prestamo;
                    DataManager.totalPrestamos++;
                    contador++;
                    
                    // También hay que agregar este préstamo al historial del estudiante
                    Usuario usuario = DataManager.buscarUsuario(prestamo.getCarnetEstudiante());
                    if (usuario != null && usuario instanceof Estudiante) {
                        Estudiante estudiante = (Estudiante) usuario;
                        estudiante.agregarPrestamoAlHistorial(prestamo);
                    }
                }
            }
            
            lector.close();
            System.out.println("Préstamos cargados: " + contador);
            
        } catch (Exception e) {
            System.err.println("Error al cargar préstamos: " + e.getMessage());
        }
    }
    
    /**
     * Guarda UN NUEVO préstamo en el archivo
     * (No reescribe todo el archivo, solo agrega al final)
     */
    public static boolean guardarPrestamo(Prestamo prestamo) {
        try {
            FileWriter archivo = new FileWriter(NOMBRE_ARCHIVO, true);
            PrintWriter escritor = new PrintWriter(archivo);
            
            escritor.println(prestamo.toArchivoString());
            escritor.close();
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al guardar préstamo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza un préstamo existente en el archivo
     * (Reescribe TODO el archivo porque cambió un préstamo)
     */
    public static boolean actualizarPrestamo(Prestamo prestamoActualizado) {
        try {
            // Primero actualizar en memoria
            int index = DataManager.buscarPrestamoIndex(prestamoActualizado.getCodigoPrestamo());
            if (index != -1) {
                DataManager.prestamos[index] = prestamoActualizado;
            }
            
            // Reescribir todo el archivo
            PrintWriter escritor = new PrintWriter(NOMBRE_ARCHIVO);
            
            for (int i = 0; i < DataManager.totalPrestamos; i++) {
                Prestamo p = DataManager.prestamos[i];
                if (p != null) {
                    escritor.println(p.toArchivoString());
                }
            }
            
            escritor.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al actualizar préstamo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Reescribe TODO el archivo con los préstamos actuales en memoria
     * Útil cuando hay múltiples cambios
     */
    public static boolean reescribirArchivo() {
        try {
            PrintWriter escritor = new PrintWriter(NOMBRE_ARCHIVO);
            
            for (int i = 0; i < DataManager.totalPrestamos; i++) {
                Prestamo p = DataManager.prestamos[i];
                if (p != null) {
                    escritor.println(p.toArchivoString());
                }
            }
            
            escritor.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al reescribir archivo de préstamos: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca préstamos de un estudiante específico
     */
    public static Prestamo[] getPrestamosPorEstudiante(String carnet) {
        // Primero contar
        int contador = 0;
        for (int i = 0; i < DataManager.totalPrestamos; i++) {
            Prestamo p = DataManager.prestamos[i];
            if (p != null && p.getCarnetEstudiante().equals(carnet)) {
                contador++;
            }
        }
        
        // Llenar arreglo
        Prestamo[] resultado = new Prestamo[contador];
        int index = 0;
        for (int i = 0; i < DataManager.totalPrestamos; i++) {
            Prestamo p = DataManager.prestamos[i];
            if (p != null && p.getCarnetEstudiante().equals(carnet)) {
                resultado[index] = p;
                index++;
            }
        }
        
        return resultado;
    }
    
    /**
     * Busca préstamos por libro
     */
    public static Prestamo[] getPrestamosPorLibro(String codigoLibro) {
        // Primero contar
        int contador = 0;
        for (int i = 0; i < DataManager.totalPrestamos; i++) {
            Prestamo p = DataManager.prestamos[i];
            if (p != null && p.getCodigoLibro().equals(codigoLibro)) {
                contador++;
            }
        }
        
        // Llenar arreglo
        Prestamo[] resultado = new Prestamo[contador];
        int index = 0;
        for (int i = 0; i < DataManager.totalPrestamos; i++) {
            Prestamo p = DataManager.prestamos[i];
            if (p != null && p.getCodigoLibro().equals(codigoLibro)) {
                resultado[index] = p;
                index++;
            }
        }
        
        return resultado;
    }
    
    /**
     * Obtiene todos los préstamos vencidos
     */
    public static Prestamo[] getPrestamosVencidos() {
        // Primero contar
        int contador = 0;
        for (int i = 0; i < DataManager.totalPrestamos; i++) {
            Prestamo p = DataManager.prestamos[i];
            if (p != null && p.getEstado().equals("ACTIVO") && p.estaVencido()) {
                contador++;
            }
        }
        
        // Llenar arreglo
        Prestamo[] vencidos = new Prestamo[contador];
        int index = 0;
        for (int i = 0; i < DataManager.totalPrestamos; i++) {
            Prestamo p = DataManager.prestamos[i];
            if (p != null && p.getEstado().equals("ACTIVO") && p.estaVencido()) {
                vencidos[index] = p;
                index++;
            }
        }
        
        return vencidos;
    }
}