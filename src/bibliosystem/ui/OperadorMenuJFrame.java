package bibliosystem.ui;

import bibliosystem.model.Operador;
import javax.swing.*;

public class OperadorMenuJFrame extends JFrame {
    
    public OperadorMenuJFrame(Operador operador) {
        setTitle("Menú Operador - " + operador.getNombreCompleto());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel label = new JLabel("Menú de Operador", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(24.0f));
        add(label);
    }
}