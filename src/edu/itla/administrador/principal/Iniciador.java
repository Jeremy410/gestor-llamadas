package edu.itla.administrador.principal;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.jdesktop.xswingx.PromptSupport;
import edu.itla.administrador.conexion.Conexion;

public class Iniciador extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField txtContrasena = null;
	private JTextField txtUsuario;
	private JButton btnIniciar = null;
	private JButton btnCancelar = null;
	private static Iniciador instancia = null;
	Serializador serializador = new Serializador();
	File fichero = new File("archivo.dat");
	private JCheckBox chckbxRecordar = null;
	
	public static synchronized Iniciador getInstancia()
	{
		if (instancia == null)
		{
			instancia = new Iniciador();
		}
		return instancia;
	}
	
	public Iniciador()
	{
		setTitle("Autenticaci\u00F3n");
		setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(268, 96, 79, 14);
		getContentPane().add(lblUsuario);
		
		JLabel lblContrasena = new JLabel("Contrase\u00F1a:");
		lblContrasena.setBounds(268, 121, 79, 14);
		getContentPane().add(lblContrasena);
		
		txtContrasena = new JPasswordField();
		PromptSupport.setPrompt("Contraseña", txtContrasena);
		txtContrasena.setBounds(353, 118, 141, 20);
		getContentPane().add(txtContrasena);
		
		chckbxRecordar = new JCheckBox("Recordar");
		chckbxRecordar.setBounds(495, 92, 85, 23);
		getContentPane().add(chckbxRecordar);
		
		txtUsuario = new JTextField();
		PromptSupport.setPrompt("Usuario", txtUsuario);
		txtUsuario.setBounds(353, 93, 141, 20);
		getContentPane().add(txtUsuario);
		txtUsuario.setColumns(10);
		
		btnIniciar = new JButton("Iniciar Sesion");
		btnIniciar.setBounds(268, 146, 113, 23);
		getContentPane().add(btnIniciar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(391, 146, 103, 23);
		getContentPane().add(btnCancelar);
		
		getRootPane().setDefaultButton(btnIniciar);
		
		JLabel lblSistemaDeLlamadas = new JLabel("SISTEMA DE LLAMADAS DE COBRO");
		lblSistemaDeLlamadas.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSistemaDeLlamadas.setHorizontalAlignment(SwingConstants.CENTER);
		lblSistemaDeLlamadas.setBounds(0, 25, 580, 14);
		getContentPane().add(lblSistemaDeLlamadas);
		
		JLabel imagen = new JLabel();
		imagen.setIcon(getImagen("Phone.jpg"));
		imagen.setBounds(0, 0, 258, 246);
		getContentPane().add(imagen);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblLogin.setBounds(446, 64, 48, 27);
		getContentPane().add(lblLogin);
	
		btnIniciar.addActionListener(this);
		btnCancelar.addActionListener(this);
		
		if(fichero.exists()){
			txtUsuario.setText(serializador.deserializar("archivo.dat"));
			chckbxRecordar.setSelected(true);
		}
		setSize(568,275);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		Conexion.getInstancia();
	}
	

	public ImageIcon getImagen(String nombreImagen){
		URL pathImagen = Iniciador.class.getClassLoader().getResource("com/recursos/"+nombreImagen+"");
		ImageIcon icono = new ImageIcon(pathImagen);
		return icono;
	}
	
	public JTextField getTxtContrasena() {
		return txtContrasena;
	}

	public JTextField getTxtRepresentante() {
		return txtUsuario;
	}

	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource().equals(btnIniciar))
		{
			try {
				if(chckbxRecordar.isSelected())
				{
					serializador.serializar(txtUsuario.getText(), "archivo.dat");
				}
				else {
					fichero.delete();
				}
				Conexion.getInstancia().iniciarSesion();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(evento.getSource().equals(btnCancelar))
		{
			System.exit(0);
		}
	}
}
