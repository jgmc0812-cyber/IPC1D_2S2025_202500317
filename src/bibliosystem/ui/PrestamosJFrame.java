package bibliosystem.ui;

import bibliosystem.data.*;
import bibliosystem.model.*;
import bibliosystem.utils.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PrestamosJFrame extends JFrame {
    
    private JPanel panelPrincipal;
    private JTable tablaActivos;
    private DefaultTableModel modeloActivos;
    private JTextField txtCarnet, txtCodigoLibro, txtCodigoPrestamo;
    
    public PrestamosJFrame() {
        initComponents();
    }
    
    private void initComponents() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("CONTROL DE PRÉSTAMOS Y DEVOLUCIONES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        
        JPanel panelOperaciones = new JPanel(new GridLayout(2, 1, 10, 10));
        panelOperaciones.setPreferredSize(new Dimension(350, 250));
        
        JPanel panelPrestamo = new JPanel(new GridBagLayout());
        panelPrestamo.setBorder(BorderFactory.createTitledBorder("Registrar Préstamo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelPrestamo.add(new JLabel("Carné:"), gbc);
        gbc.gridx = 1;
        txtCarnet = new JTextField(15);
        panelPrestamo.add(txtCarnet, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelPrestamo.add(new JLabel("Código/ISBN:"), gbc);
        gbc.gridx = 1;
        txtCodigoLibro = new JTextField(15);
        panelPrestamo.add(txtCodigoLibro, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton btnPrestar = new JButton("Registrar Préstamo");
        btnPrestar.addActionListener(e -> registrarPrestamo());
        panelPrestamo.add(btnPrestar, gbc);
        
        panelOperaciones.add(panelPrestamo);
        
        JPanel panelDevolucion = new JPanel(new GridBagLayout());
        panelDevolucion.setBorder(BorderFactory.createTitledBorder("Registrar Devolución"));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelDevolucion.add(new JLabel("Código Préstamo:"), gbc);
        gbc.gridx = 1;
        txtCodigoPrestamo = new JTextField(15);
        panelDevolucion.add(txtCodigoPrestamo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelDevolucion.add(new JLabel("o Carné:"), gbc);
        gbc.gridx = 1;
        JTextField txtCarnetDev = new JTextField(15);
        panelDevolucion.add(txtCarnetDev, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton btnDevolver = new JButton("Registrar Devolución");
        btnDevolver.addActionListener(e -> registrarDevolucion(txtCarnetDev.getText()));
        panelDevolucion.add(btnDevolver, gbc);
        
        panelOperaciones.add(panelDevolucion);
        
        panelPrincipal.add(panelOperaciones, BorderLayout.WEST);
        
        JPanel panelTablas = new JPanel(new BorderLayout(5, 5));
        
        String[] columnas = {"Código", "Carné", "Libro", "Fecha Préstamo", "Fecha Límite", "Estado"};
        modeloActivos = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tablaActivos = new JTable(modeloActivos);
        JScrollPane scrollPane = new JScrollPane(tablaActivos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Préstamos Activos"));
        panelTablas.add(scrollPane, BorderLayout.CENTER);
        
        panelPrincipal.add(panelTablas, BorderLayout.CENTER);
        
        cargarPrestamosActivos();
    }
    
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
    
    private void cargarPrestamosActivos() {
        modeloActivos.setRowCount(0);
        Prestamo[] activos = DataManager.getPrestamosActivos();
        
        for (Prestamo p : activos) {
            if (p != null) {
                Libro libro = DataManager.buscarLibro(p.getCodigoLibro());
                String titulo = (libro != null) ? libro.getTitulo() : p.getCodigoLibro();
                
                String estado = p.getEstado();
                if (p.estaVencido()) estado = "VENCIDO";
                
                modeloActivos.addRow(new Object[]{
                    p.getCodigoPrestamo(),
                    p.getCarnetEstudiante(),
                    titulo,
                    p.getFechaPrestamo().format(Prestamo.FORMATO_FECHA),
                    p.getFechaLimite().format(Prestamo.FORMATO_FECHA),
                    estado
                });
            }
        }
    }
    
    private void registrarPrestamo() {
        String carnet = txtCarnet.getText().trim();
        String codigo = txtCodigoLibro.getText().trim();
        
        if (carnet.isEmpty() || codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }
        
        String validacion = Validaciones.validarPrestamoEstudiante(carnet, codigo);
        
        if (validacion != null) {
            JOptionPane.showMessageDialog(this, validacion, "Error", JOptionPane.ERROR_MESSAGE);
            Bitacora.operacionErronea("PRESTAMO", "OPERADOR", "PRESTAMOS", validacion);
            return;
        }
        
        Usuario usuario = DataManager.buscarUsuario(carnet);
        if (!(usuario instanceof Estudiante)) {
            JOptionPane.showMessageDialog(this, "El usuario no es un estudiante válido");
            return;
        }
        
        Libro libro = DataManager.buscarLibro(codigo);
        if (libro == null) libro = DataManager.buscarLibroPorIsbn(codigo);
        
        String codigoPrestamo = GeneradorCodigos.generarCodigoPrestamo();
        Prestamo nuevoPrestamo = new Prestamo(codigoPrestamo, carnet, libro.getCodigoInterno());
        
        if (libro.prestarEjemplar()) {
            DataManager.agregarPrestamo(nuevoPrestamo);
            ArchivoPrestamos.guardarPrestamo(nuevoPrestamo);
            Bitacora.operacionExitosa("PRESTAMO_EXITOSO", "OPERADOR", "PRESTAMOS");
            
            JOptionPane.showMessageDialog(this, 
                "Préstamo registrado exitosamente\n" +
                "Código: " + codigoPrestamo,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            cargarPrestamosActivos();
            txtCarnet.setText(""); txtCodigoLibro.setText("");
        }
    }
    
    private void registrarDevolucion(String carnet) {
        String codigoPrestamo = txtCodigoPrestamo.getText().trim();
        
        if (codigoPrestamo.isEmpty() && carnet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese código de préstamo o carné");
            return;
        }
        
        Prestamo prestamo = null;
        
        if (!codigoPrestamo.isEmpty()) {
            prestamo = DataManager.buscarPrestamo(codigoPrestamo);
        } else {
            Prestamo[] prestamos = ArchivoPrestamos.getPrestamosPorEstudiante(carnet);
            for (Prestamo p : prestamos) {
                if (p != null && p.getEstado().equals("ACTIVO")) {
                    prestamo = p;
                    break;
                }
            }
        }
        
        if (prestamo == null) {
            JOptionPane.showMessageDialog(this, "No se encontró préstamo activo");
            return;
        }
        
        Libro libro = DataManager.buscarLibro(prestamo.getCodigoLibro());
        
        prestamo.registrarDevolucion();
        libro.devolverEjemplar();
        
        ArchivoPrestamos.actualizarPrestamo(prestamo);
        Bitacora.operacionExitosa("DEVOLUCION", "OPERADOR", "PRESTAMOS");
        
        JOptionPane.showMessageDialog(this, "Devolución registrada exitosamente");
        cargarPrestamosActivos();
        txtCodigoPrestamo.setText("");
    }
}