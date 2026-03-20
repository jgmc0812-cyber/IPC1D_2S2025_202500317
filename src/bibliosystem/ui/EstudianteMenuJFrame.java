package bibliosystem.ui;

import bibliosystem.data.*;
import bibliosystem.model.*;
import bibliosystem.utils.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class EstudianteMenuJFrame extends JFrame {
    
    private Estudiante estudiante;
    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;
    private JTable tablabLibrosDisponibles;
    private DefaultTableModel modeloLibros;
    
    public EstudianteMenuJFrame(Estudiante estudiante) {
        this.estudiante = estudiante;
        
        setTitle("BiblioSystem - Estudiante: " + estudiante.getNombreCompleto());
        setSize(900, 700); // Más grande para acomodar la nueva tabla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Bitacora.operacionExitosa("ACCESO_MODULO_ESTUDIANTE", estudiante.getUsername(), "ESTUDIANTE");
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // ===== BARRA SUPERIOR =====
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(140, 70, 0));
        barraSuperior.setPreferredSize(new Dimension(getWidth(), 50));
        
        JLabel lblTitulo = new JLabel("PANEL DE ESTUDIANTE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        barraSuperior.add(lblTitulo, BorderLayout.CENTER);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        barraSuperior.add(btnCerrarSesion, BorderLayout.EAST);
        
        add(barraSuperior, BorderLayout.NORTH);
        
        // ===== PANEL PRINCIPAL CON PESTAÑAS =====
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Panel 1: Datos del estudiante
        tabbedPane.addTab("Mis Datos", crearPanelDatos());
        
        // Panel 2: Libros disponibles (NUEVO)
        tabbedPane.addTab("Libros Disponibles", crearPanelLibros());
        
        // Panel 3: Solicitar préstamo
        tabbedPane.addTab("Solicitar Préstamo", crearPanelSolicitar());
        
        // Panel 4: Mi historial
        tabbedPane.addTab("Mi Historial", crearPanelHistorial());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelDatos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblBienvenida = new JLabel("Bienvenido " + estudiante.getNombreCompleto());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblBienvenida, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        panel.add(new JLabel("Carné:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(estudiante.getCarne()), gbc);
        
        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Carrera:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(estudiante.getCarrera()), gbc);
        
        gbc.gridy = 3; gbc.gridx = 0;
        panel.add(new JLabel("Préstamos activos:"), gbc);
        gbc.gridx = 1;
        int activos = DataManager.contarPrestamosActivosEstudiante(estudiante.getCarne());
        panel.add(new JLabel(String.valueOf(activos)), gbc);
        
        gbc.gridy = 4; gbc.gridx = 0;
        panel.add(new JLabel("¿Tiene vencidos?:"), gbc);
        gbc.gridx = 1;
        boolean vencidos = DataManager.tienePrestamosVencidos(estudiante.getCarne());
        panel.add(new JLabel(vencidos ? "SÍ" : "NO"), gbc);
        
        return panel;
    }
    
    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("LIBROS DISPONIBLES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Modelo de tabla para libros disponibles
        String[] columnas = {"Código", "ISBN", "Título", "Autor", "Género", "Año", "Disponibles"};
        modeloLibros = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tablabLibrosDisponibles = new JTable(modeloLibros);
        JScrollPane scrollPane = new JScrollPane(tablabLibrosDisponibles);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón para refrescar
        JButton btnRefrescar = new JButton("Refrescar Lista");
        btnRefrescar.addActionListener(e -> cargarLibrosDisponibles());
        panel.add(btnRefrescar, BorderLayout.SOUTH);
        
        cargarLibrosDisponibles();
        
        return panel;
    }
    
    private JPanel crearPanelSolicitar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("SOLICITAR NUEVO PRÉSTAMO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Ingrese código o ISBN del libro:"), gbc);
        
        gbc.gridx = 1;
        JTextField txtCodigo = new JTextField(20);
        panelFormulario.add(txtCodigo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        JButton btnSolicitar = new JButton("Solicitar Préstamo");
        btnSolicitar.setPreferredSize(new Dimension(200, 40));
        btnSolicitar.addActionListener(e -> {
            solicitarPrestamo(txtCodigo.getText().trim());
            txtCodigo.setText("");
        });
        panelFormulario.add(btnSolicitar, gbc);
        
        // Panel informativo
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información"));
        JLabel lblInfo = new JLabel("<html>"
            + "• Puede tener hasta 3 préstamos activos<br>"
            + "• Plazo máximo: 15 días<br>"
            + "• No puede pedir si tiene préstamos vencidos<br>"
            + "• Seleccione el libro de la lista de disponibles"
            + "</html>");
        panelInfo.add(lblInfo);
        
        gbc.gridy = 2;
        panelFormulario.add(panelInfo, gbc);
        
        panel.add(panelFormulario, BorderLayout.CENTER);
        
        // Mini tabla de libros disponibles en este panel también
        String[] columnas = {"Código", "Título", "Disponibles"};
        DefaultTableModel miniModelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        for (int i = 0; i < DataManager.totalLibros; i++) {
            Libro l = DataManager.libros[i];
            if (l != null && l.getDisponibles() > 0) {
                miniModelo.addRow(new Object[]{
                    l.getCodigoInterno(),
                    l.getTitulo(),
                    l.getDisponibles()
                });
            }
        }
        
        JTable miniTabla = new JTable(miniModelo);
        JScrollPane scrollMini = new JScrollPane(miniTabla);
        scrollMini.setBorder(BorderFactory.createTitledBorder("Libros con ejemplares disponibles"));
        scrollMini.setPreferredSize(new Dimension(400, 150));
        
        panel.add(scrollMini, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("MI HISTORIAL DE PRÉSTAMOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        String[] columnas = {"Código", "Libro", "Fecha Préstamo", "Fecha Límite", "Estado", "Devolución"};
        modeloHistorial = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tablaHistorial = new JTable(modeloHistorial);
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnRefrescar = new JButton("Refrescar Historial");
        btnRefrescar.addActionListener(e -> cargarHistorial());
        panel.add(btnRefrescar, BorderLayout.SOUTH);
        
        cargarHistorial();
        
        return panel;
    }
    
    private void cargarLibrosDisponibles() {
        modeloLibros.setRowCount(0);
        for (int i = 0; i < DataManager.totalLibros; i++) {
            Libro l = DataManager.libros[i];
            if (l != null && l.getDisponibles() > 0) {
                modeloLibros.addRow(new Object[]{
                    l.getCodigoInterno(),
                    l.getIsbn(),
                    l.getTitulo(),
                    l.getAutor(),
                    l.getGenero(),
                    l.getAnioPublicacion(),
                    l.getDisponibles()
                });
            }
        }
    }
    
    private void cargarHistorial() {
        modeloHistorial.setRowCount(0);
        NodoPrestamo actual = estudiante.getCabezaHistorial();
        
        while (actual != null) {
            Prestamo p = actual.getPrestamo();
            Libro libro = DataManager.buscarLibro(p.getCodigoLibro());
            String titulo = (libro != null) ? libro.getTitulo() : p.getCodigoLibro();
            
            String estado = p.getEstado();
            if (p.getEstado().equals("ACTIVO") && p.estaVencido()) {
                estado = "VENCIDO";
            }
            
            String fechaDev = (p.getFechaDevolucion() != null) ? 
                p.getFechaDevolucion().format(Prestamo.FORMATO_FECHA) : "-";
            
            modeloHistorial.addRow(new Object[]{
                p.getCodigoPrestamo(),
                titulo,
                p.getFechaPrestamo().format(Prestamo.FORMATO_FECHA),
                p.getFechaLimite().format(Prestamo.FORMATO_FECHA),
                estado,
                fechaDev
            });
            
            actual = actual.getSiguiente();
        }
    }
    
    private void solicitarPrestamo(String codigo) {
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código o ISBN");
            return;
        }
        
        String validacion = Validaciones.validarPrestamoEstudiante(estudiante.getCarne(), codigo);
        
        if (validacion != null) {
            JOptionPane.showMessageDialog(this, validacion, "Error", JOptionPane.ERROR_MESSAGE);
            Bitacora.operacionErronea("PRESTAMO", estudiante.getUsername(), "ESTUDIANTE", validacion);
            return;
        }
        
        Libro libro = DataManager.buscarLibro(codigo);
        if (libro == null) libro = DataManager.buscarLibroPorIsbn(codigo);
        
        String codigoPrestamo = GeneradorCodigos.generarCodigoPrestamo();
        Prestamo nuevoPrestamo = new Prestamo(codigoPrestamo, estudiante.getCarne(), libro.getCodigoInterno());
        
        if (libro.prestarEjemplar()) {
            DataManager.agregarPrestamo(nuevoPrestamo);
            ArchivoPrestamos.guardarPrestamo(nuevoPrestamo);
            Bitacora.operacionExitosa("PRESTAMO_EXITOSO", estudiante.getUsername(), "ESTUDIANTE");
            
            JOptionPane.showMessageDialog(this, 
                "¡Préstamo registrado exitosamente!\n\n" +
                "Código: " + codigoPrestamo + "\n" +
                "Libro: " + libro.getTitulo() + "\n" +
                "Fecha límite: " + nuevoPrestamo.getFechaLimite().format(Prestamo.FORMATO_FECHA) + "\n" +
                "Ejemplares restantes: " + libro.getDisponibles(),
                "Préstamo Exitoso", JOptionPane.INFORMATION_MESSAGE);
            
            // Refrescar las tablas
            cargarLibrosDisponibles();
            cargarHistorial();
        }
    }
    
    private void cerrarSesion() {
        Bitacora.cierreSesion(estudiante.getUsername());
        new LoginJFrame().setVisible(true);
        dispose();
    }
}