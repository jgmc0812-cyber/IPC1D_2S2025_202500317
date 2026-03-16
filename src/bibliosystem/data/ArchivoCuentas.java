package bibliosystem.data;

import bibliosystem.model.*;
import java.io.*;

public class ArchivoCuentas {
    
    private static final String NOMBRE_ARCHIVO = "cuentas.txt";
    
    /**
     * Carga las cuentas desde el archivo al iniciar el sistema
     * Formato: username;password;nombreCompleto;rol;carnet;carrera
     * Para operadores, carnet y carrera van vacíos
     */
    public static void cargarCuentas() {
        File archivo = new File(NOMBRE_ARCHIVO);
        
        if (!archivo.exists()) {
            System.out.println("Archivo cuentas.txt no existe. Se creará al guardar.");
            return;
        }
        
        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivo));
            String linea;
            int contador = 0;
            
            while ((linea = lector.readLine()) != null && 
                   DataManager.totalUsuarios < DataManager.MAX_USUARIOS) {
                
                if (linea.trim().isEmpty()) continue;
                
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    String username = partes[0];
                    String password = partes[1];
                    String nombreCompleto = partes[2];
                    String rol = partes[3];
                    
                    // Verificar si ya existe (por si acaso)
                    if (DataManager.buscarUsuario(username) == null) {
                        Usuario usuario = null;
                        
                        if (rol.equals("ESTUDIANTE") && partes.length >= 6) {
                            // Es estudiante
                            String carne = partes[4];
                            String carrera = partes[5];
                            usuario = new Estudiante(username, password, nombreCompleto, 
                                                    carne, carrera);
                        } else {
                            // Es operador (o admin, pero admin no debería estar en archivo)
                            usuario = new Operador(username, password, nombreCompleto);
                        }
                        
                        DataManager.usuarios[DataManager.totalUsuarios] = usuario;
                        DataManager.totalUsuarios++;
                        contador++;
                    }
                }
            }
            
            lector.close();
            System.out.println("Cuentas cargadas: " + contador);
            
        } catch (Exception e) {
            System.err.println("Error al cargar cuentas: " + e.getMessage());
        }
    }
    
    /**
     * Guarda UNA NUEVA cuenta en el archivo
     * (No reescribe todo el archivo, solo agrega al final)
     */
    public static boolean guardarCuenta(Usuario usuario) {
        try {
            FileWriter archivo = new FileWriter(NOMBRE_ARCHIVO, true);
            PrintWriter escritor = new PrintWriter(archivo);
            
            String linea = usuario.getUsername() + ";" +
                          usuario.getPassword() + ";" +
                          usuario.getNombreCompleto() + ";" +
                          usuario.getRol();
            
            if (usuario instanceof Estudiante) {
                Estudiante est = (Estudiante) usuario;
                linea += ";" + est.getCarne() + ";" + est.getCarrera();
            } else {
                linea += ";;"; // Campos vacíos para operadores
            }
            
            escritor.println(linea);
            escritor.close();
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al guardar cuenta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Reescribe TODO el archivo con las cuentas actuales en memoria
     * Útil después de eliminar una cuenta
     */
    public static boolean reescribirArchivo() {
        try {
            PrintWriter escritor = new PrintWriter(NOMBRE_ARCHIVO);
            
            for (int i = 1; i < DataManager.totalUsuarios; i++) { // i=1 para saltar admin
                Usuario u = DataManager.usuarios[i];
                if (u != null && !u.getUsername().equals("admin")) {
                    String linea = u.getUsername() + ";" +
                                  u.getPassword() + ";" +
                                  u.getNombreCompleto() + ";" +
                                  u.getRol();
                    
                    if (u instanceof Estudiante) {
                        Estudiante est = (Estudiante) u;
                        linea += ";" + est.getCarne() + ";" + est.getCarrera();
                    } else {
                        linea += ";;";
                    }
                    
                    escritor.println(linea);
                }
            }
            
            escritor.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al reescribir archivo: " + e.getMessage());
            return false;
        }
    }
}
