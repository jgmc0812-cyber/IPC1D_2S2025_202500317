package bibliosystem.ui;

import bibliosystem.data.*;
import bibliosystem.model.*;
import bibliosystem.utils.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EstudianteMenuJFrame extends JFrame {
    
    private Estudiante estudiante;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    
    public EstudianteMenuJFrame(Estudiante estudiante) {
        this.estudiante = estudiante;
        
        setTitle("BiblioSystem - Estudiante: " + estudiante.getNombreCompleto());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Bitacora.operacionExitosa("ACCESO_MODULO_ESTUDIANTE", estudiante.getUsername(), "ESTUDIANTE");
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
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
        
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblBienvenida = new JLabel("Bienvenido " + estudiante.getNombreCompleto());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelCentral.add(lblBienvenida, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        panelCentral.add(new JLabel("Carné:"), gbc);
        gbc.gridx = 1;
        panelCentral.add(new JLabel(estudiante.getCarne()), gbc);
        
        gbc.gridy = 2; gbc.gridx = 0;
        panelCentral.add(new JLabel("Carrera:"), gbc);
        gbc.gridx = 1;
        panelCentral.add(new JLabel(estudiante.getCarrera()), gbc);
        
        gbc.gridy = 3; gbc.gridx = 0;
        panelCentral.add(new JLabel("Préstamos activos:"), gbc);
        gbc.gridx = 1;
        int activos = DataManager.contarPrestamosActivosEstudiante(estudiante.getCarne());
        panelCentral.add(new JLabel(String.valueOf(activos)), gbc);
        
        gbc.gridy = 4; gbc.gridx = 0;
        panelCentral.add(new JLabel("¿Tiene vencidos?:"), gbc);
        gbc.gridx = 1;
        boolean vencidos = DataManager.tienePrestamosVencidos(estudiante.getCarne());
        panelCentral.add(new JLabel(vencidos ? "SÍ" : "NO"), gbc);
        
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        JButton btnSolicitar = new JButton("Solicitar Nuevo Préstamo");
        btnSolicitar.setPreferredSize(new Dimension(200, 40));
        btnSolicitar.addActionListener(e -> solicitarPrestamo());
        panelCentral.add(btnSolicitar, gbc);
        
        add(panelCentral, BorderLayout.CENTER);
        
        String[] columnas = {"Código", "Libro", "Fecha Préstamo", "Fecha Límite", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tablaHistorial = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Mi Historial de Préstamos"));
        scrollPane.setPreferredSize(new Dimension(750, 200));
        
        add(scrollPane, BorderLayout.SOUTH);
        
        cargarHistorial();
    }
    
    private void cargarHistorial() {
        modeloTabla.setRowCount(0);
        NodoPrestamo actual = estudiante.getCabezaHistorial();
        
        while (actual != null) {
            Prestamo p = actual.getPrestamo();
            Libro libro = DataManager.buscarLibro(p.getCodigoLibro());
            String titulo = (libro != null) ? libro.getTitulo() : p.getCodigoLibro();
            
            String estado = p.getEstado();
            if (p.getEstado().equals("ACTIVO") && p.estaVencido()) {
                estado = "VENCIDO";
            }
            
            modeloTabla.addRow(new Object[]{
                p.getCodigoPrestamo(),
                titulo,
                p.getFechaPrestamo().format(Prestamo.FORMATO_FECHA),
                p.getFechaLimite().format(Prestamo.FORMATO_FECHA),
                estado
            });
            
            actual = actual.getSiguiente();
        }
    }
    
    private void solicitarPrestamo() {
        String codigo = JOptionPane.showInputDialog(this, 
            "Ingrese código o ISBN del libro:", "Solicitar Préstamo", JOptionPane.QUESTION_MESSAGE);
        
        if (codigo == null || codigo.trim().isEmpty()) return;
        
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
                "Préstamo registrado exitosamente\n" +
                "Código: " + codigoPrestamo + "\n" +
                "Libro: " + libro.getTitulo() + "\n" +
                "Fecha límite: " + nuevoPrestamo.getFechaLimite().format(Prestamo.FORMATO_FECHA),
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            cargarHistorial();
        }
    }
    
    private void cerrarSesion() {
        Bitacora.cierreSesion(estudiante.getUsername());
        new LoginJFrame().setVisible(true);
        dispose();
    }
}