package bibliosystem.ui;

import bibliosystem.data.*;
import bibliosystem.model.*;
import bibliosystem.utils.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReportesJFrame extends JFrame {
    
    private JPanel panelPrincipal;
    
    public ReportesJFrame() {
        initComponents();
    }
    
    private void initComponents() {
        panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel titulo = new JLabel("GENERACIÓN DE REPORTES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 0;
        panelPrincipal.add(titulo, gbc);
        
        JButton btnVencidos = crearBotonReporte("Reporte de Préstamos Vencidos", e -> generarReporteVencidos());
        JButton btnDisponibles = crearBotonReporte("Reporte de Libros Disponibles", e -> generarReporteDisponibles());
        JButton btnMasPrestados = crearBotonReporte("Reporte de 5 Libros Más Prestados", e -> generarReporteMasPrestados());
        JButton btnEstudiantesActivos = crearBotonReporte("Reporte de Estudiantes con Préstamos Activos", e -> generarReporteEstudiantesActivos());
        
        gbc.gridy = 1; panelPrincipal.add(btnVencidos, gbc);
        gbc.gridy = 2; panelPrincipal.add(btnDisponibles, gbc);
        gbc.gridy = 3; panelPrincipal.add(btnMasPrestados, gbc);
        gbc.gridy = 4; panelPrincipal.add(btnEstudiantesActivos, gbc);
    }
    
    private JButton crearBotonReporte(String texto, java.awt.event.ActionListener listener) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(300, 50));
        boton.addActionListener(listener);
        return boton;
    }
    
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
    
    private String getFechaActual() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    private void generarReporteVencidos() {
        Prestamo[] vencidos = ArchivoPrestamos.getPrestamosVencidos();
        String nombreArchivo = "reporte_prestamos_vencidos_" + getFechaActual() + ".html";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("<html><head><title>Préstamos Vencidos</title>");
            writer.println("<style>table {border-collapse: collapse;} th, td {border: 1px solid black; padding: 8px;} th {background-color: #f2f2f2;}</style>");
            writer.println("</head><body>");
            writer.println("<h1>Reporte de Préstamos Vencidos</h1>");
            writer.println("<p>Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</p>");
            writer.println("<table><tr><th>Código</th><th>Estudiante</th><th>Carné</th><th>Libro</th><th>Fecha Límite</th><th>Días Atraso</th></tr>");
            
            for (Prestamo p : vencidos) {
                if (p != null) {
                    Estudiante e = (Estudiante) DataManager.buscarUsuario(p.getCarnetEstudiante());
                    String nombreEst = (e != null) ? e.getNombreCompleto() : "Desconocido";
                    
                    Libro l = DataManager.buscarLibro(p.getCodigoLibro());
                    String tituloLibro = (l != null) ? l.getTitulo() : p.getCodigoLibro();
                    
                    writer.println("<tr>");
                    writer.println("<td>" + p.getCodigoPrestamo() + "</td>");
                    writer.println("<td>" + nombreEst + "</td>");
                    writer.println("<td>" + p.getCarnetEstudiante() + "</td>");
                    writer.println("<td>" + tituloLibro + "</td>");
                    writer.println("<td>" + p.getFechaLimite().format(Prestamo.FORMATO_FECHA) + "</td>");
                    writer.println("<td>" + p.getDiasAtraso() + "</td>");
                    writer.println("</tr>");
                }
            }
            
            writer.println("</table>");
            writer.println("<p>Total de préstamos vencidos: " + vencidos.length + "</p>");
            writer.println("</body></html>");
            
            JOptionPane.showMessageDialog(this, "Reporte generado: " + nombreArchivo);
            Bitacora.operacionExitosa("REPORTE_VENCIDOS", "SISTEMA", "REPORTES");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage());
        }
    }
    
    private void generarReporteDisponibles() {
        int count = 0;
        String nombreArchivo = "reporte_libros_disponibles_" + getFechaActual() + ".html";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("<html><head><title>Libros Disponibles</title>");
            writer.println("<style>table {border-collapse: collapse;} th, td {border: 1px solid black; padding: 8px;} th {background-color: #f2f2f2;}</style>");
            writer.println("</head><body>");
            writer.println("<h1>Reporte de Libros Disponibles</h1>");
            writer.println("<p>Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</p>");
            writer.println("<table><tr><th>Código</th><th>Título</th><th>Autor</th><th>Disponibles</th></tr>");
            
            for (int i = 0; i < DataManager.totalLibros; i++) {
                Libro l = DataManager.libros[i];
                if (l != null && l.getDisponibles() > 0) {
                    writer.println("<tr>");
                    writer.println("<td>" + l.getCodigoInterno() + "</td>");
                    writer.println("<td>" + l.getTitulo() + "</td>");
                    writer.println("<td>" + l.getAutor() + "</td>");
                    writer.println("<td>" + l.getDisponibles() + "</td>");
                    writer.println("</tr>");
                    count++;
                }
            }
            
            writer.println("</table>");
            writer.println("<p>Total de libros disponibles: " + count + "</p>");
            writer.println("</body></html>");
            
            JOptionPane.showMessageDialog(this, "Reporte generado: " + nombreArchivo);
            Bitacora.operacionExitosa("REPORTE_DISPONIBLES", "SISTEMA", "REPORTES");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage());
        }
    }
    
    private void generarReporteMasPrestados() {
        String nombreArchivo = "reporte_libros_mas_prestados_" + getFechaActual() + ".html";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("<html><head><title>5 Libros Más Prestados</title>");
            writer.println("<style>table {border-collapse: collapse;} th, td {border: 1px solid black; padding: 8px;} th {background-color: #f2f2f2;}</style>");
            writer.println("</head><body>");
            writer.println("<h1>Reporte de los 5 Libros Más Prestados</h1>");
            writer.println("<p>Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</p>");
            writer.println("<table><tr><th>Título</th><th>Autor</th><th>Total Préstamos</th></tr>");
            
            int[] prestamosPorLibro = new int[DataManager.totalLibros];
            for (int i = 0; i < DataManager.totalPrestamos; i++) {
                Prestamo p = DataManager.prestamos[i];
                if (p != null) {
                    for (int j = 0; j < DataManager.totalLibros; j++) {
                        Libro l = DataManager.libros[j];
                        if (l != null && l.getCodigoInterno().equals(p.getCodigoLibro())) {
                            prestamosPorLibro[j]++;
                            break;
                        }
                    }
                }
            }
            
            for (int k = 0; k < 5; k++) {
                int maxIndex = -1;
                int maxValor = -1;
                
                for (int i = 0; i < DataManager.totalLibros; i++) {
                    if (DataManager.libros[i] != null && prestamosPorLibro[i] > maxValor) {
                        maxValor = prestamosPorLibro[i];
                        maxIndex = i;
                    }
                }
                
                if (maxIndex != -1 && maxValor > 0) {
                    Libro l = DataManager.libros[maxIndex];
                    writer.println("<tr>");
                    writer.println("<td>" + l.getTitulo() + "</td>");
                    writer.println("<td>" + l.getAutor() + "</td>");
                    writer.println("<td>" + maxValor + "</td>");
                    writer.println("</tr>");
                    prestamosPorLibro[maxIndex] = -1;
                }
            }
            
            writer.println("</table>");
            writer.println("</body></html>");
            
            JOptionPane.showMessageDialog(this, "Reporte generado: " + nombreArchivo);
            Bitacora.operacionExitosa("REPORTE_MAS_PRESTADOS", "SISTEMA", "REPORTES");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage());
        }
    }
    
    private void generarReporteEstudiantesActivos() {
        String nombreArchivo = "reporte_estudiantes_activos_" + getFechaActual() + ".html";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("<html><head><title>Estudiantes con Préstamos Activos</title>");
            writer.println("<style>table {border-collapse: collapse;} th, td {border: 1px solid black; padding: 8px;} th {background-color: #f2f2f2;}</style>");
            writer.println("</head><body>");
            writer.println("<h1>Reporte de Estudiantes con Préstamos Activos</h1>");
            writer.println("<p>Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</p>");
            writer.println("<table><tr><th>Nombre</th><th>Carné</th><th>Préstamos Activos</th><th>¿Vencido?</th></tr>");
            
            int count = 0;
            for (int i = 0; i < DataManager.totalUsuarios; i++) {
                Usuario u = DataManager.usuarios[i];
                if (u instanceof Estudiante) {
                    int activos = DataManager.contarPrestamosActivosEstudiante(((Estudiante) u).getCarne());
                    if (activos > 0) {
                        boolean vencidos = DataManager.tienePrestamosVencidos(((Estudiante) u).getCarne());
                        writer.println("<tr>");
                        writer.println("<td>" + u.getNombreCompleto() + "</td>");
                        writer.println("<td>" + ((Estudiante) u).getCarne() + "</td>");
                        writer.println("<td>" + activos + "</td>");
                        writer.println("<td>" + (vencidos ? "SÍ" : "NO") + "</td>");
                        writer.println("</tr>");
                        count++;
                    }
                }
            }
            
            writer.println("</table>");
            writer.println("<p>Total de estudiantes con préstamos activos: " + count + "</p>");
            writer.println("</body></html>");
            
            JOptionPane.showMessageDialog(this, "Reporte generado: " + nombreArchivo);
            Bitacora.operacionExitosa("REPORTE_ESTUDIANTES_ACTIVOS", "SISTEMA", "REPORTES");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage());
        }
    }
}