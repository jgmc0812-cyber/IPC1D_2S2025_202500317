package bibliosystem.ui;

import bibliosystem.data.*;
import bibliosystem.model.*;
import bibliosystem.utils.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class AdminMenuJFrame extends JFrame {
    
    private Operador admin;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    
    public AdminMenuJFrame(Operador admin) {
        this.admin = admin;
        
        setTitle("BiblioSystem - Administrador: " + admin.getNombreCompleto());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Bitacora.operacionExitosa("ACCESO_MODULO_ADMIN", admin.getUsername(), "ADMIN");
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // ===== BARRA SUPERIOR =====
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(0, 70, 140));
        barraSuperior.setPreferredSize(new Dimension(getWidth(), 50));
        
        JLabel lblTitulo = new JLabel("PANEL DE ADMINISTRADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        barraSuperior.add(lblTitulo, BorderLayout.CENTER);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        barraSuperior.add(btnCerrarSesion, BorderLayout.EAST);
        
        add(barraSuperior, BorderLayout.NORTH);
        
        // ===== MENÚ LATERAL =====
        JPanel menuLateral = new JPanel();
        menuLateral.setPreferredSize(new Dimension(200, getHeight()));
        menuLateral.setBackground(new Color(240, 240, 240));
        menuLateral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        JButton btnOperadores = crearBotonMenu("Gestión de Operadores");
        JButton btnLibros = crearBotonMenu("Gestión de Libros");
        JButton btnEstudiantes = crearBotonMenu("Gestión de Estudiantes");
        JButton btnPrestamos = crearBotonMenu("Control de Préstamos");
        JButton btnReportes = crearBotonMenu("Reportes");
        
        btnOperadores.addActionListener(e -> cardLayout.show(panelPrincipal, "operadores"));
        btnLibros.addActionListener(e -> cardLayout.show(panelPrincipal, "libros"));
        btnEstudiantes.addActionListener(e -> cardLayout.show(panelPrincipal, "estudiantes"));
        btnPrestamos.addActionListener(e -> cardLayout.show(panelPrincipal, "prestamos"));
        btnReportes.addActionListener(e -> cardLayout.show(panelPrincipal, "reportes"));
        
        menuLateral.add(btnOperadores, gbc);
        menuLateral.add(btnLibros, gbc);
        menuLateral.add(btnEstudiantes, gbc);
        menuLateral.add(btnPrestamos, gbc);
        menuLateral.add(btnReportes, gbc);
        
        add(menuLateral, BorderLayout.WEST);
        
        // ===== PANEL CENTRAL =====
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        
        panelPrincipal.add(new PanelGestionOperadores(), "operadores");
        panelPrincipal.add(new GestionLibrosJFrame().getPanelPrincipal(), "libros");
        panelPrincipal.add(new GestionEstudiantesJFrame().getPanelPrincipal(), "estudiantes");
        panelPrincipal.add(new PrestamosJFrame().getPanelPrincipal(), "prestamos");
        panelPrincipal.add(new ReportesJFrame().getPanelPrincipal(), "reportes");
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        cardLayout.show(panelPrincipal, "operadores");
    }
    
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(180, 40));
        boton.setBackground(new Color(0, 120, 200));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.PLAIN, 14));
        return boton;
    }
    
    private void cerrarSesion() {
        Bitacora.cierreSesion(admin.getUsername());
        new LoginJFrame().setVisible(true);
        dispose();
    }
    
    private class PanelGestionOperadores extends JPanel {
        private JTable tablaOperadores;
        private DefaultTableModel modeloTabla;
        private JTextField txtUsername, txtNombre, txtPassword;
        
        public PanelGestionOperadores() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel titulo = new JLabel("GESTIÓN DE OPERADORES", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 18));
            add(titulo, BorderLayout.NORTH);
            
            JPanel panelFormulario = new JPanel(new GridBagLayout());
            panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Operador"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            gbc.gridx = 0; gbc.gridy = 0;
            panelFormulario.add(new JLabel("Username:"), gbc);
            gbc.gridx = 1;
            txtUsername = new JTextField(15);
            panelFormulario.add(txtUsername, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            panelFormulario.add(new JLabel("Nombre Completo:"), gbc);
            gbc.gridx = 1;
            txtNombre = new JTextField(15);
            panelFormulario.add(txtNombre, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            panelFormulario.add(new JLabel("Contraseña:"), gbc);
            gbc.gridx = 1;
            txtPassword = new JTextField(15);
            panelFormulario.add(txtPassword, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            gbc.gridwidth = 2;
            JButton btnGuardar = new JButton("Registrar Operador");
            btnGuardar.addActionListener(e -> registrarOperador());
            panelFormulario.add(btnGuardar, gbc);
            
            add(panelFormulario, BorderLayout.NORTH);
            
            String[] columnas = {"Username", "Nombre Completo"};
            modeloTabla = new DefaultTableModel(columnas, 0) {
                @Override public boolean isCellEditable(int row, int col) { return false; }
            };
            
            tablaOperadores = new JTable(modeloTabla);
            JScrollPane scrollPane = new JScrollPane(tablaOperadores);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder("Operadores Registrados"));
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            
            JButton btnEliminar = new JButton("Eliminar Operador Seleccionado");
            btnEliminar.addActionListener(e -> eliminarOperador());
            panelTabla.add(btnEliminar, BorderLayout.SOUTH);
            
            add(panelTabla, BorderLayout.CENTER);
            
            cargarOperadores();
        }
        
        private void cargarOperadores() {
            modeloTabla.setRowCount(0);
            for (int i = 0; i < DataManager.totalUsuarios; i++) {
                Usuario u = DataManager.usuarios[i];
                if (u != null && u.getRol().equals("OPERADOR")) {
                    modeloTabla.addRow(new Object[]{u.getUsername(), u.getNombreCompleto()});
                }
            }
        }
        
        private void registrarOperador() {
            String username = txtUsername.getText().trim();
            String nombre = txtNombre.getText().trim();
            String password = txtPassword.getText().trim();
            
            if (username.isEmpty() || nombre.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
                return;
            }
            
            if (DataManager.buscarUsuario(username) != null) {
                JOptionPane.showMessageDialog(this, "El username ya existe");
                return;
            }
            
            Operador nuevoOperador = new Operador(username, password, nombre);
            
            if (DataManager.agregarUsuario(nuevoOperador)) {
                ArchivoCuentas.guardarCuenta(nuevoOperador);
                Bitacora.operacionExitosa("REGISTRO_OPERADOR", admin.getUsername(), "ADMIN");
                cargarOperadores();
                txtUsername.setText(""); txtNombre.setText(""); txtPassword.setText("");
                JOptionPane.showMessageDialog(this, "Operador registrado exitosamente");
            }
        }
        
        private void eliminarOperador() {
            int fila = tablaOperadores.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un operador");
                return;
            }
            
            String username = modeloTabla.getValueAt(fila, 0).toString();
            
            if (username.equals("admin")) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar al administrador");
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Eliminar al operador " + username + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (DataManager.eliminarUsuario(username)) {
                    ArchivoCuentas.reescribirArchivo();
                    Bitacora.operacionExitosa("ELIMINAR_OPERADOR", admin.getUsername(), "ADMIN");
                    cargarOperadores();
                }
            }
        }
    }
}