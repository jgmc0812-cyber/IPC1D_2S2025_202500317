package bibliosystem.ui;

import bibliosystem.data.*;
import bibliosystem.model.*;
import bibliosystem.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginJFrame extends JFrame {
    
    // Componentes de la interfaz
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrarEstudiante;
    private JLabel lblMensaje;
    
    public LoginJFrame() {
        // Configurar la ventana
        setTitle("BiblioSystem - Inicio de Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);
        
        // Inicializar el sistema (cargar datos)
        DataManager.inicializarSistema();
        
        // Crear los componentes
        initComponents();
    }
    
    private void initComponents() {
        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ===== PANEL SUPERIOR (TÍTULO) =====
        JLabel lblTitulo = new JLabel("BIBLIOSYSTEM", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 70, 140));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        
        // ===== PANEL CENTRAL (FORMULARIO) =====
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Usuario (para estudiantes es su carné)
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Usuario/Carné:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtUsername = new JTextField(15);
        panelFormulario.add(txtUsername, gbc);
        
        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtPassword = new JPasswordField(15);
        panelFormulario.add(txtPassword, gbc);
        
        // Mensaje de error/éxito
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        panelFormulario.add(lblMensaje, gbc);
        
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        
        // ===== PANEL INFERIOR (BOTONES) =====
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(150, 35));
        btnLogin.setBackground(new Color(0, 120, 200));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
        
        btnRegistrarEstudiante = new JButton("Registrar Estudiante");
        btnRegistrarEstudiante.setPreferredSize(new Dimension(150, 35));
        btnRegistrarEstudiante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirRegistroEstudiante();
            }
        });
        
        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistrarEstudiante);
        
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        // Agregar panel principal a la ventana
        add(panelPrincipal);
        
        // Atajo de teclado para Enter
        getRootPane().setDefaultButton(btnLogin);
    }
    
    private void iniciarSesion() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        
        // Validar que no estén vacíos
        if (username.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Usuario y contraseña son obligatorios");
            return;
        }
        
        // Buscar usuario en DataManager
        Usuario usuario = DataManager.buscarUsuario(username);
        
        if (usuario != null && usuario.validarLogin(username, password)) {
            // Login exitoso
            lblMensaje.setForeground(new Color(0, 150, 0));
            lblMensaje.setText("¡Login exitoso! Bienvenido " + usuario.getNombreCompleto());
            
            // Registrar en bitácora
            Bitacora.loginExitoso(username);
            
            // Abrir menú según rol
            abrirMenuSegunRol(usuario);
            
            // Cerrar ventana de login
            dispose();
        } else {
            // Login fallido
            lblMensaje.setForeground(Color.RED);
            lblMensaje.setText("Usuario o contraseña incorrectos");
            
            // Registrar en bitácora
            Bitacora.loginFallido(username);
            
            // Limpiar campos
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }
    
    private void abrirMenuSegunRol(Usuario usuario) {
        switch (usuario.getRol()) {
            case "ADMIN":
                new AdminMenuJFrame((Operador) usuario).setVisible(true);
                break;
            case "OPERADOR":
                new OperadorMenuJFrame((Operador) usuario).setVisible(true);
                break;
            case "ESTUDIANTE":
                new EstudianteMenuJFrame((Estudiante) usuario).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, 
                    "Rol de usuario no válido", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirRegistroEstudiante() {
        // Crear un diálogo modal para registro
        JDialog dialog = new JDialog(this, "Registro de Estudiante", true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ===== CAMPOS DEL FORMULARIO =====
        // Según el enunciado: carné, nombre, carrera y contraseña
        // El carné será el username
        
        JTextField txtCarne = new JTextField(15);
        JTextField txtNombre = new JTextField(15);
        JTextField txtCarrera = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);
        JPasswordField txtConfirmPassword = new JPasswordField(15);
        JLabel lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(Color.RED);
        
        // Agregar campos al panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Carné (usuario):*"), gbc);
        gbc.gridx = 1;
        panel.add(txtCarne, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre Completo:*"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Carrera:*"), gbc);
        gbc.gridx = 1;
        panel.add(txtCarrera, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Contraseña:*"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Confirmar Contraseña:*"), gbc);
        gbc.gridx = 1;
        panel.add(txtConfirmPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(lblMensaje, gbc);
        
        // ===== BOTONES =====
        gbc.gridy = 6;
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Registrarse");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener valores de los campos
                String carne = txtCarne.getText().trim();
                String nombre = txtNombre.getText().trim();
                String carrera = txtCarrera.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
                
                // ===== VALIDACIONES =====
                // 1. Campos obligatorios
                if (carne.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || password.isEmpty()) {
                    lblMensaje.setText("Todos los campos son obligatorios");
                    return;
                }
                
                // 2. Confirmar contraseña
                if (!password.equals(confirmPassword)) {
                    lblMensaje.setText("Las contraseñas no coinciden");
                    return;
                }
                
                // 3. Validar que el carné (username) no exista
                if (DataManager.buscarUsuario(carne) != null) {
                    lblMensaje.setText("El carné ya está registrado como usuario");
                    return;
                }
                
                // 4. Validar que el carné no exista entre estudiantes (por si acaso)
                boolean carneExiste = false;
                for (int i = 0; i < DataManager.totalUsuarios; i++) {
                    Usuario u = DataManager.usuarios[i];
                    if (u instanceof Estudiante && ((Estudiante) u).getCarne().equals(carne)) {
                        carneExiste = true;
                        break;
                    }
                }
                
                if (carneExiste) {
                    lblMensaje.setText("El carné ya está registrado");
                    return;
                }
                
                // ===== CREAR NUEVO ESTUDIANTE =====
                // IMPORTANTE: El username ES el carné
                Estudiante nuevoEstudiante = new Estudiante(carne, password, nombre, carne, carrera);
                
                // ===== GUARDAR EN EL SISTEMA =====
                if (DataManager.agregarUsuario(nuevoEstudiante)) {
                    // Guardar en archivo
                    ArchivoCuentas.guardarCuenta(nuevoEstudiante);
                    
                    // Registrar en bitácora
                    Bitacora.operacionExitosa("REGISTRO_ESTUDIANTE", carne, "AUTENTICACION");
                    
                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(dialog, 
                        "¡Estudiante registrado exitosamente!\n\n" +
                        "Ya puede iniciar sesión con:\n" +
                        "Usuario: " + carne + "\n" +
                        "Contraseña: " + password, 
                        "Registro Exitoso", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    dialog.dispose();
                } else {
                    lblMensaje.setText("Error al registrar estudiante (límite alcanzado)");
                }
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panel.add(panelBotones, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}