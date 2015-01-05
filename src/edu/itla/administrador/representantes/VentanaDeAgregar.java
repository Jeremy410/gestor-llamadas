package edu.itla.administrador.representantes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import Atxy2k.CustomTextField.RestrictedTextField;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.itla.administrador.conexion.Conexion;

public class VentanaDeAgregar extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField txtNombre = null;
	private JTextField txtApellido = null;
	private JButton btnAgregar = null;
	private JButton btnCancelar = null;
	private JTextField txtUsuario;
	private JTextField txtClave;
	private JComboBox<String> comboNivel = null;
	private JComboBox<String> comboTipo = null;
	private JComboBox<String> comboSexo = null;
	private RestrictedTextField restriccionTexto;

	public VentanaDeAgregar() 
	{
		setTitle("Agregar Representante");
		getContentPane().setLayout(null);
		
		JPanel panelTemporal = new JPanel();
		panelTemporal.setBounds(10, 2, 361, 326);
		
		panelTemporal.setBorder(new TitledBorder("Datos Representante"));
		getContentPane().add(panelTemporal);
		panelTemporal.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(18, 16, 61, 20);
		panelTemporal.add(lblNombre);
		
		txtNombre = new JTextField();
		restriccionTexto = new RestrictedTextField(txtNombre);
		validacionSoloLetras(txtNombre);
		restriccionTexto.setLimit(25);
		txtNombre.setBounds(18, 32, 159, 20);
		panelTemporal.add(txtNombre);
		
		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setBounds(18, 56, 61, 20);
		panelTemporal.add(lblApellido);
		
		txtApellido = new JTextField();
		restriccionTexto = new RestrictedTextField(txtApellido);
		validacionSoloLetras(txtApellido);
		restriccionTexto.setLimit(25);
		txtApellido.setBounds(18, 72, 159, 20);
		panelTemporal.add(txtApellido);
		
		JLabel lblNivel = new JLabel("Nivel:");
		lblNivel.setBounds(18, 96, 61, 20);
		panelTemporal.add(lblNivel);
		
		String [] niveles = {"Novato", "Experto", "Master"};
		comboNivel = new JComboBox<String>(niveles);
		comboNivel.setSelectedItem("Master");
		comboNivel.setEnabled(false);
		comboNivel.setBounds(18, 112, 98, 20);
		panelTemporal.add(comboNivel);
		
		String [] tipoUsuario = {"Administrador","Representante"};
		comboTipo = new JComboBox<String>(tipoUsuario);
		comboTipo.setBounds(18, 159, 121, 20);
		panelTemporal.add(comboTipo);
		
		String [] sexo = {"Hombre","Mujer"};
		comboSexo = new JComboBox<String>(sexo);
		comboSexo.setBounds(18, 206, 121, 20);
		panelTemporal.add(comboSexo);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(18, 230, 61, 20);
		panelTemporal.add(lblUsuario);
		
		txtUsuario = new JTextField();
		restriccionTexto = new RestrictedTextField(txtUsuario);
		restriccionTexto.setLimit(25);
		txtUsuario.setBounds(18, 246, 159, 20);
		panelTemporal.add(txtUsuario);
		
		JLabel lblClave = new JLabel("Contrase\u00F1a:");
		lblClave.setBounds(18, 275, 98, 20);
		panelTemporal.add(lblClave);
		
		txtClave = new JPasswordField();
		restriccionTexto = new RestrictedTextField(txtClave);
		restriccionTexto.setLimit(25);
		txtClave.setBounds(18, 294, 159, 20);
		panelTemporal.add(txtClave);
		
		JLabel lblTipoUsuario = new JLabel("Tipo Usuario:");
		lblTipoUsuario.setBounds(18, 143, 98, 20);
		panelTemporal.add(lblTipoUsuario);
	
		
		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(18, 190, 98, 20);
		panelTemporal.add(lblSexo);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(188, 339, 87, 23);
		getContentPane().add(btnAgregar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(284, 339, 87, 23);
		getContentPane().add(btnCancelar);
		
		setResizable(false);
		setSize(393,404);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);

		btnAgregar.addActionListener(this);
		btnCancelar.addActionListener(this);
		comboTipo.addActionListener(this);
		
	}
	public void validacionSoloLetras(JTextField JTextField)
	{
		JTextField.addKeyListener(new KeyAdapter() 
		{
			public void keyTyped(KeyEvent e) {
				char vocal = e.getKeyChar();
				if(Character.isDigit(vocal))
				{
					e.consume();
				}
			}
			
		});
	}
	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource().equals(btnAgregar))	
		{
			if(txtNombre.getText().trim().length() == 0 || txtApellido.getText().trim().length() == 0 || txtClave.getText().trim().length() == 0)
			{
				JOptionPane.showMessageDialog(this,"Todos los campos son requeridos, por favor rellene lo campos. ", "DENEGADO", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				try {
					Conexion.getInstancia().agregarRepresentante(txtNombre.getText(), txtApellido.getText(), comboNivel.getSelectedItem().toString(), txtClave.getText(), txtUsuario.getText(), comboTipo.getSelectedIndex() + 1, comboSexo.getSelectedIndex());
					Conexion.getInstancia().consultaRepresentante(MantenimientoDeRepresentante.getTablaRepresentante());
					JOptionPane.showMessageDialog(this,"Se ha agregado un nuevo representante", "COMPLETADO", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if(evento.getSource().equals(comboTipo))
		{
			if(comboTipo.getSelectedItem().equals("Administrador"))
			{
				comboNivel.addItem("Master");
				comboNivel.setSelectedItem("Master");
				comboNivel.setEnabled(false);
			}
			else if(comboTipo.getSelectedItem().equals("Representante"))
			{
				comboNivel.setEnabled(true);
				comboNivel.removeItem("Master");
			}
		}
		if(evento.getSource().equals(btnCancelar))	
		{
			dispose();
		}
	}
}
