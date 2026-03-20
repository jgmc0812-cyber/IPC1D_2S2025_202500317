package bibliosystem.ui;

import bibliosystem.data.*;
import bibliosystem.model.*;
import bibliosystem.utils.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionLibrosJFrame extends JFrame {
    
    private JPanel panelPrincipal;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JTextField txtCodigo, txtIsbn, txtTitulo, txtAutor, txtGenero, txtAnio, txtEjemplares;
    
    public GestionLibrosJFrame() {
        initComponents();
    }
    
    private void initComponents() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("GESTIÓN DE LIBROS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar/Modificar Libro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        String[] labels = {"Código Interno:", "ISBN:", "Título:", "Autor:", "Género:", "Año:", "Ejemplares:"};
        JTextField[] fields = {txtCodigo = new JTextField(15), txtIsbn = new JTextField(15), 
                               txtTitulo = new JTextField(15), txtAutor = new JTextField(15),
                               txtGenero = new JTextField(15), txtAnio = new JTextField(15), 
                               txtEjemplares = new JTextField(15)};
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panelFormulario.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panelFormulario.add(fields[i], gbc);
        }
        
        JPanel panelBotones = new JPanel();
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        
        btnRegistrar.addActionListener(e -> registrarLibro());
        btnModificar.addActionListener(e -> modificarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        
        gbc.gridx = 0; gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);
        
        panelPrincipal.add(panelFormulario, BorderLayout.WEST);
        
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Título", "Autor", "ISBN"});
        
        btnBuscar.addActionListener(e -> buscarLibros(txtBuscar.getText(), comboTipo.getSelectedIndex()));
        
        panelBusqueda.add(new JLabel("Buscar por:"));
        panelBusqueda.add(comboTipo);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        
        JButton btnVerTodos = new JButton("Ver Todos");
        btnVerTodos.addActionListener(e -> cargarLibros());
        panelBusqueda.add(btnVerTodos);
        
        panelDerecho.add(panelBusqueda, BorderLayout.NORTH);
        
        String[] columnas = {"Código", "ISBN", "Título", "Autor", "Género", "Año", "Total", "Disp."};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarLibroSeleccionado();
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        panelDerecho.add(scrollPane, BorderLayout.CENTER);
        
        panelPrincipal.add(panelDerecho, BorderLayout.CENTER);
        
        cargarLibros();
    }
    
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
    
    private void cargarLibros() {
        modeloTabla.setRowCount(0);
        for (int i = 0; i < DataManager.totalLibros; i++) {
            Libro l = DataManager.libros[i];
            if (l != null) {
                modeloTabla.addRow(new Object[]{
                    l.getCodigoInterno(), l.getIsbn(), l.getTitulo(), l.getAutor(),
                    l.getGenero(), l.getAnioPublicacion(), l.getTotalEjemplares(), l.getDisponibles()
                });
            }
        }
    }
    
    private void buscarLibros(String criterio, int tipo) {
        modeloTabla.setRowCount(0);
        for (int i = 0; i < DataManager.totalLibros; i++) {
            Libro l = DataManager.libros[i];
            if (l == null) continue;
            
            boolean coincide = false;
            switch (tipo) {
                case 0: coincide = l.getTitulo().toLowerCase().contains(criterio.toLowerCase()); break;
                case 1: coincide = l.getAutor().toLowerCase().contains(criterio.toLowerCase()); break;
                case 2: coincide = l.getIsbn().equals(criterio); break;
            }
            
            if (coincide || criterio.isEmpty()) {
                modeloTabla.addRow(new Object[]{
                    l.getCodigoInterno(), l.getIsbn(), l.getTitulo(), l.getAutor(),
                    l.getGenero(), l.getAnioPublicacion(), l.getTotalEjemplares(), l.getDisponibles()
                });
            }
        }
    }
    
    private void registrarLibro() {
        try {
            String codigo = txtCodigo.getText().trim();
            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String genero = txtGenero.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int ejemplares = Integer.parseInt(txtEjemplares.getText().trim());
            
            if (codigo.isEmpty() || isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Campos obligatorios vacíos");
                return;
            }
            
            if (!Validaciones.isbnValido(isbn)) {
                JOptionPane.showMessageDialog(this, "ISBN no válido");
                return;
            }
            
            if (!Validaciones.anioValido(anio)) {
                JOptionPane.showMessageDialog(this, "Año no válido");
                return;
            }
            
            Libro nuevoLibro = new Libro(codigo, isbn, titulo, autor, genero, anio, ejemplares);
            
            if (DataManager.agregarLibro(nuevoLibro)) {
                Bitacora.operacionExitosa("REGISTRO_LIBRO", "SISTEMA", "LIBROS");
                cargarLibros();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Libro registrado");
            } else {
                JOptionPane.showMessageDialog(this, "Código o ISBN ya existen");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Año y ejemplares deben ser números");
        }
    }
    
    private void modificarLibro() {
    int fila = tablaLibros.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un libro");
        return;
    }
    
    String codigo = modeloTabla.getValueAt(fila, 0).toString();
    Libro libro = DataManager.buscarLibro(codigo);
    
    if (libro != null) {
        try {
            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String genero = txtGenero.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int ejemplares = Integer.parseInt(txtEjemplares.getText().trim());
            
            // Validar que no sea menor a los prestados
            if (ejemplares < libro.getEjemplaresPrestados()) {
                JOptionPane.showMessageDialog(this, 
                    "No puede reducir ejemplares por debajo de los prestados");
                return;
            }
            
            // ACTUALIZAR TODOS LOS DATOS
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setGenero(genero);
            libro.setAnioPublicacion(anio);
            
            // ACTUALIZAR TOTAL Y DISPONIBLES
            int prestados = libro.getEjemplaresPrestados(); // Cuántos están prestados
            libro.setTotalEjemplares(ejemplares);
            libro.setDisponibles(ejemplares - prestados); // Disponibles = total - prestados
            
            Bitacora.operacionExitosa("MODIFICAR_LIBRO", "SISTEMA", "LIBROS");
            cargarLibros(); // Refrescar la tabla
            limpiarCampos();
            
            JOptionPane.showMessageDialog(this, 
                "Libro modificado:\n" +
                "Total ejemplares: " + ejemplares + "\n" +
                "Disponibles: " + (ejemplares - prestados));
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Año y ejemplares deben ser números");
        }
    }
}
    
    private void eliminarLibro() {
        int fila = tablaLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro");
            return;
        }
        
        String codigo = modeloTabla.getValueAt(fila, 0).toString();
        
        if (!Validaciones.libroEliminable(codigo)) {
            JOptionPane.showMessageDialog(this, "No se puede eliminar: tiene préstamos activos");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Eliminar libro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (DataManager.eliminarLibro(codigo)) {
                Bitacora.operacionExitosa("ELIMINAR_LIBRO", "SISTEMA", "LIBROS");
                cargarLibros();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Libro eliminado");
            }
        }
    }
    
    private void cargarLibroSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila != -1) {
            txtCodigo.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtIsbn.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtTitulo.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtAutor.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtGenero.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtAnio.setText(modeloTabla.getValueAt(fila, 5).toString());
            txtEjemplares.setText(modeloTabla.getValueAt(fila, 6).toString());
        }
    }
    
    private void limpiarCampos() {
        txtCodigo.setText(""); txtIsbn.setText(""); txtTitulo.setText("");
        txtAutor.setText(""); txtGenero.setText(""); txtAnio.setText(""); txtEjemplares.setText("");
    }
}
