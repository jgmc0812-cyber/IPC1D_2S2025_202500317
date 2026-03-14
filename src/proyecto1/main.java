package proyecto1;


import proyecto1.admin;
import javax.swing.*;
import java.awt.Color;
import helper_classes.*;

public class main {
    static String userMain = "user003";
    static String pass = "";

    public static ESTUDIANTE[] usuarioEstudiantes = new ESTUDIANTE[2]; // <- definir arreglo para almacenar estudiantes
    public static Bibliotecario[] usuarioBibliotecarios = new Bibliotecario[2]; // <- definir arreglo para almacenar bibliotecarios
    public static void main(String[] args) {
    
    //INICIO DE JFRAME----------------------------------------------------------------------------------
        initialUsers();
        JFrame frame = new JFrame("Sistema de Gestión de Biblioteca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1063, 573);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.decode("#eeeeee"));

        JLabel element1 = new JLabel("GESTIÓN DE BIBLIOTECA");
        element1.setBounds(368, 70, 367, 46);
        element1.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 26));
        element1.setForeground(Color.decode("#1b1b1b"));
        panel.add(element1);

        JLabel element2 = new JLabel("INICIO DE SESIÓN");
        element2.setBounds(439, 126, 195, 26);
        element2.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 20));
        element2.setForeground(Color.decode("#1b1b1b"));
        panel.add(element2);


        JTextField element3 = new JTextField("");
        element3.setBounds(435, 252, 192, 37);
        element3.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 25));
        element3.setBackground(Color.decode("#ffffff"));
        element3.setForeground(Color.decode("#737674"));
        element3.setBorder(new RoundedBorder(2, Color.decode("#626262"), 1));
        OnFocusEventHelper.setOnFocusText(element3, "USUARIO", Color.decode("#1b1b1b"),   Color.decode("#737674"));
        panel.add(element3);
        userMain = element3.getText();

        JPasswordField element4 = new JPasswordField("");
        element4.setBounds(434, 315, 195, 23);
        element4.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 25));
        element4.setBackground(Color.decode("#ffffff"));
        element4.setForeground(Color.decode("#737674"));
        element4.setBorder(new RoundedBorder(2, Color.decode("#626262"), 1));
        OnFocusEventHelper.setOnFocusText(element4, "CONTRASEÑA", Color.decode("#1b1b1b"),   Color.decode("#737674"));
        panel.add(element4);
        pass = String.valueOf(element4.getPassword());

        JButton element5 = new JButton("LOG IN");
        element5.setBounds(485, 409, 106, 29);
        element5.setBackground(Color.decode("#ffffff"));
        element5.setForeground(Color.decode("#1b1b1b"));
        element5.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        element5.setBorder(new RoundedBorder(4, Color.decode("#626262"), 1));
        element5.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(element5, Color.decode("#c2c2c2"), Color.decode("#ffffff"));
        panel.add(element5);

        frame.add(panel);
        frame.setVisible(true);
        element5.addActionListener(e -> {
            userMain = element3.getText();
            pass = String.valueOf(element4.getPassword());

            System.out.println("Usuario ingresado: " + userMain);
            System.out.println("CONTRASEÑA ingresada: " + pass);
            initialUsers();
            buscarEstudiante(userMain);
            buscarBibliotecario(userMain);
            login();
        });

        

        System.out.println("Hola vv");
    }

    public static void login(){
        if (buscarBibliotecario(userMain) != null){
            System.out.println("LOGIN EXITOSO");
            System.out.println("BIENVENIDO: " + buscarBibliotecario(userMain).getnombre());
            System.out.println("TU USUARIO ES: " + buscarBibliotecario(userMain).getuser());
            System.out.println("TU ID DE BIBLIOTECARIO ES: " + buscarBibliotecario(userMain).getidBibliotecario());
            System.out.println("TU TURNO ES: " + buscarBibliotecario(userMain).getturno());
            Bibliotecario bibliotecarioVentana = buscarBibliotecario(userMain);
            bibliotecarioVentana.panelBibliotecario();


        } else if (buscarEstudiante(userMain) != null){
            System.out.println("LOGIN EXITOSO");
            System.out.println("BIENVENIDO: " + buscarEstudiante(userMain).getnombre());
            System.out.println("TU USUARIO ES: " + buscarEstudiante(userMain).getuser());
            System.out.println("TU CARRERA ES: " + buscarEstudiante(userMain).getCarrera());
            System.out.println("TU SEMESTRE ES: " + buscarEstudiante(userMain).getSemestre());
            ESTUDIANTE estudianteVentana = buscarEstudiante(userMain);
            estudianteVentana.panelEstudiante();


        }else if(userMain.equals("admin") && pass.equals("admin123")){
            System.out.println("LOGIN EXITOSO");
            admin ventanaAdmin = new admin();
            ventanaAdmin.ventanaAdmin();
            

        } else {
            System.out.println("USUARIO NO ENCONTRADO");
            //JFRAME PARA NOTIFICAR USUARIO NO ENCONTRADO

            JFrame nuevaVentana = new JFrame("Error 404: NotFound");
            nuevaVentana.setSize(300, 150);
            nuevaVentana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo cierra esta ventana
    
            // Configurar contenido
            JPanel panelNuevo = new JPanel();
            panelNuevo.setBackground(Color.decode("#f0f0f0"));
            panelNuevo.add(new JLabel("NO SE ENCONTRÓ EL USUARIO"));
    
            // Botón en la nueva ventana
            JButton btnCerrar = new JButton("CERRAR");
            btnCerrar.addActionListener(ev -> nuevaVentana.dispose());
            panelNuevo.add(btnCerrar);
    
            nuevaVentana.add(panelNuevo);
            nuevaVentana.setLocationRelativeTo(null); // Centrar en pantalla
            nuevaVentana.setVisible(true);
        }
    }







    public static void initialUsers(){
        ESTUDIANTE estudianteUno = new ESTUDIANTE("SISTEMAS", 2, "202505036", "Samuel", "user001", "3735407520101","samuel@gamil.com", 
        "pass0", 'M', 35036725, 19,'S');
        
        usuarioEstudiantes[0] = estudianteUno;

        ESTUDIANTE estudianteDos = new ESTUDIANTE("SISTEMAS", 2, "202505036", "Samuel", "user002", "3735407520101","samuel@gamil.com", 
        "pass1", 'M', 35036725, 19,'S');
        
        usuarioEstudiantes[1] = estudianteDos;

        Bibliotecario bibliotecarioUno = new Bibliotecario("Moal", "BIB001", 3000, 2020, "Ana", "user003", "1234567890101", "iolo@gmail.com","pass3", 'F', 52286767, 30, 'C');
    
        usuarioBibliotecarios[0] = bibliotecarioUno;

        Bibliotecario bibliotecarioDos = new Bibliotecario("Tarde", "BIB002", 3200, 2019, "Luis", "user004", "1098765432101", "uala@gmail.com","pass4", 'M', 59876543, 28, 'S');
        
        usuarioBibliotecarios[1] = bibliotecarioDos;
    }



    public static ESTUDIANTE buscarEstudiante(String userMain){
    for (int i = 0; i < usuarioEstudiantes.length; i++){
        if ((usuarioEstudiantes[i].getuser()).equals(userMain)){
            return usuarioEstudiantes[i];

        }
    }
        return null;
  }

  public static Bibliotecario buscarBibliotecario(String userMain){
    for (int i = 0; i < usuarioBibliotecarios.length; i++){
        if ((usuarioBibliotecarios[i].getuser()).equals(userMain)){
            return usuarioBibliotecarios[i];

        }
    }
        return null;
  }
}


