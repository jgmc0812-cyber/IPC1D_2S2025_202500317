import javax.swing.*;
import java.awt.*;

public class admin {
    
    public void ventanaAdmin(){
        // Crear la ventana principal
        JFrame frame = new JFrame("Administración");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        
        // Crear el panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Crear contenido para cada pestaña
        //--------------------------------DASHBOARD--------------------------------
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("DASHBOARD"));
        panel1.add(new JButton("Botón 1"));
        


        //--------------------------------BIBLIOTECARIOS--------------------------------
        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("BIBLIOTECARIOS"));
        panel2.add(new JTextField(20));
        
        //--------------------------------LIBROS--------------------------------
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout());
        panel3.add(new JTextArea("LIBROS"), BorderLayout.CENTER);
        
        
        



        //--------------------------------ESTUDIANTES--------------------------------
        JPanel panel4 = new JPanel();
        panel4.add(new JLabel("ESTUDIANTES"));
        panel4.add(new JButton("Botón 1"));
        //--------------------------------CARGA-MASIVA--------------------------------
        JPanel panel5 = new JPanel();
        panel5.add(new JLabel("CARGA-MASIVA"));
        panel5.add(new JButton("Botón 1"));





        
        // Agregar pestañas con íconos (opcional)
        ImageIcon icon = new ImageIcon("icono.png"); // Ruta de tu ícono
        
        tabbedPane.addTab("DASHBOARD", icon, panel1, "Tooltip para pestaña 1");
        tabbedPane.addTab("BIBLIOTECARIOS", panel2);
        tabbedPane.addTab("LIBROS", panel3);
        tabbedPane.addTab("ESTUDIANTES", panel4);
        tabbedPane.addTab("CARGA-MASIVA", panel5);
        
        // Configurar posición de las pestañas (TOP, BOTTOM, LEFT, RIGHT)
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        
        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null); // Centrar ventana
        frame.setVisible(true);

    }










    
    public static int sumarNumeros() {
        int a = 5;
        int b = 10;
        System.out.println("Suma de " + a + " + " + b + " = " + (a + b));
        return a + b;
    }



}
