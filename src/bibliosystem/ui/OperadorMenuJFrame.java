package bibliosystem.ui;

import bibliosystem.data.*;
import bibliosystem.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OperadorMenuJFrame extends JFrame {
    
    private Operador operador;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    
    public OperadorMenuJFrame(Operador operador) {
        this.operador = operador;
        
        setTitle("BiblioSystem - Operador: " + operador.getNombreCompleto());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Bitacora.operacionExitosa("ACCESO_MODULO_OPERADOR", operador.getUsername(), "OPERADOR");
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(0, 100, 100));
        barraSuperior.setPreferredSize(new Dimension(getWidth(), 50));
        
        JLabel lblTitulo = new JLabel("PANEL DE OPERADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        barraSuperior.add(lblTitulo, BorderLayout.CENTER);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        barraSuperior.add(btnCerrarSesion, BorderLayout.EAST);
        
        add(barraSuperior, BorderLayout.NORTH);
        
        JPanel menuLateral = new JPanel();
        menuLateral.setPreferredSize(new Dimension(200, getHeight()));
        menuLateral.setBackground(new Color(240, 240, 240));
        menuLateral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        JButton btnLibros = crearBotonMenu("Gestión de Libros");
        JButton btnEstudiantes = crearBotonMenu("Gestión de Estudiantes");
        JButton btnPrestamos = crearBotonMenu("Control de Préstamos");
        JButton btnReportes = crearBotonMenu("Reportes");
        
        btnLibros.addActionListener(e -> cardLayout.show(panelPrincipal, "libros"));
        btnEstudiantes.addActionListener(e -> cardLayout.show(panelPrincipal, "estudiantes"));
        btnPrestamos.addActionListener(e -> cardLayout.show(panelPrincipal, "prestamos"));
        btnReportes.addActionListener(e -> cardLayout.show(panelPrincipal, "reportes"));
        
        menuLateral.add(btnLibros, gbc);
        menuLateral.add(btnEstudiantes, gbc);
        menuLateral.add(btnPrestamos, gbc);
        menuLateral.add(btnReportes, gbc);
        
        add(menuLateral, BorderLayout.WEST);
        
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        
        panelPrincipal.add(new GestionLibrosJFrame().getPanelPrincipal(), "libros");
        panelPrincipal.add(new GestionEstudiantesJFrame().getPanelPrincipal(), "estudiantes");
        panelPrincipal.add(new PrestamosJFrame().getPanelPrincipal(), "prestamos");
        panelPrincipal.add(new ReportesJFrame().getPanelPrincipal(), "reportes");
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        cardLayout.show(panelPrincipal, "libros");
    }
    
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(180, 40));
        boton.setBackground(new Color(0, 140, 140));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }
    
    private void cerrarSesion() {
        Bitacora.cierreSesion(operador.getUsername());
        new LoginJFrame().setVisible(true);
        dispose();
    }
}