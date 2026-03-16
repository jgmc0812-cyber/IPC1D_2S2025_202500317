package bibliosytem;

import bibliosystem.model.*;
import bibliosystem.ui.LoginJFrame;

public class BiblioSystemApp {
    
    public static void main(String[] args) {
        System.out.println("=== BIBLIOSYSTEM INICIANDO ===");
        
        // Aquí después cargaremos los archivos
        
        // Iniciar la ventana de login
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginJFrame().setVisible(true);
            }
        });
    }
}