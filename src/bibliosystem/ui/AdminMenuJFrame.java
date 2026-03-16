package bibliosystem.ui;

import bibliosystem.model.Operador;
import javax.swing.*;

public class AdminMenuJFrame extends JFrame {
    
    public AdminMenuJFrame(Operador admin) {
        setTitle("Menú Administrador - " + admin.getNombreCompleto());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel label = new JLabel("Menú de Administrador", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(24.0f));
        add(label);
    }
}