package bibliosystem.ui;

import bibliosystem.data.*;
import bibliosystem.model.*;
import bibliosystem.utils.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionEstudiantesJFrame extends JFrame {
    
    private JPanel panelPrincipal;
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;
    
    public GestionEstudiantesJFrame() {
        initComponents();
    }
    
    private void initComponents() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("GESTIÓN DE ESTUDIANTES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar por Carné");
        JButton btnListar = new JButton("Listar Todos");
        
        btnBuscar.addActionListener(e -> buscarEstudiante(txtBuscar.getText()));
        btnListar.addActionListener(e -> cargarEstudiantes());
        
        panelBusqueda.add(new JLabel("Carné:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnListar);
        
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        
        String[] columnas = {"Carné", "Nombre", "Carrera", "Préstamos Activos", "Vencidos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tablaEstudiantes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaEstudiantes);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnEliminar = new JButton("Eliminar Estudiante Seleccionado");
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        panelPrincipal.add(btnEliminar, BorderLayout.SOUTH);
        
        cargarEstudiantes();
    }
    
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
    
    private void cargarEstudiantes() {
        modeloTabla.setRowCount(0);
        for (int i = 0; i < DataManager.totalUsuarios; i++) {
            Usuario u = DataManager.usuarios[i];
            if (u instanceof Estudiante) {
                Estudiante e = (Estudiante) u;
                int activos = DataManager.contarPrestamosActivosEstudiante(e.getCarne());
                boolean vencidos = DataManager.tienePrestamosVencidos(e.getCarne());
                
                modeloTabla.addRow(new Object[]{
                    e.getCarne(), e.getNombreCompleto(), e.getCarrera(), activos, vencidos ? "SÍ" : "NO"
                });
            }
        }
    }
    
    private void buscarEstudiante(String carne) {
        modeloTabla.setRowCount(0);
        Usuario u = DataManager.buscarUsuario(carne);
        
        if (u instanceof Estudiante) {
            Estudiante e = (Estudiante) u;
            int activos = DataManager.contarPrestamosActivosEstudiante(carne);
            boolean vencidos = DataManager.tienePrestamosVencidos(carne);
            
            modeloTabla.addRow(new Object[]{
                e.getCarne(), e.getNombreCompleto(), e.getCarrera(), activos, vencidos ? "SÍ" : "NO"
            });
            
            JOptionPane.showMessageDialog(this, 
                "Datos del estudiante:\n" +
                "Nombre: " + e.getNombreCompleto() + "\n" +
                "Carrera: " + e.getCarrera() + "\n" +
                "Préstamos activos: " + activos + "\n" +
                "¿Vencidos?: " + (vencidos ? "SÍ" : "NO"));
        } else {
            JOptionPane.showMessageDialog(this, "Estudiante no encontrado");
        }
    }
    
    private void eliminarEstudiante() {
        int fila = tablaEstudiantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante");
            return;
        }
        
        String carne = modeloTabla.getValueAt(fila, 0).toString();
        
        if (!Validaciones.estudianteEliminable(carne)) {
            JOptionPane.showMessageDialog(this, 
                "No se puede eliminar: tiene préstamos activos o vencidos");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Eliminar estudiante " + carne + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (DataManager.eliminarUsuario(carne)) {
                ArchivoCuentas.reescribirArchivo();
                Bitacora.operacionExitosa("ELIMINAR_ESTUDIANTE", "SISTEMA", "ESTUDIANTES");
                cargarEstudiantes();
                JOptionPane.showMessageDialog(this, "Estudiante eliminado");
            }
        }
    }
}