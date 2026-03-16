package bibliosystem.ui;

import bibliosystem.model.Estudiante;
import javax.swing.*;

public class EstudianteMenuJFrame extends JFrame {
    
    public EstudianteMenuJFrame(Estudiante estudiante) {
        setTitle("Menú Estudiante - " + estudiante.getNombreCompleto());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel label = new JLabel("Menú de Estudiante", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(24.0f));
        add(label);
    }
}