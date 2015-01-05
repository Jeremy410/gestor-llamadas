package edu.itla.administrador.representantes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import Atxy2k.CustomTextField.RestrictedTextField;
import edu.itla.administrador.conexion.Conexion;

public class VentanaDeModificacion extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtNombre = null;
	private JTextField txtApellido = null;
	private JButton btnGuardar = null;
	private JButton btnCancelar = null;
	private JTextField txtUsuario = null;
	private JTextField txtClave = null;
	private JComboBox<String> comboNivel = null;
	private JComboBox<String> comboTipo = null;	
	private JComboBox<String> comboSexo = null;
	private String ID = null;
	private RestrictedTextField restriccionTexto;
	
	public VentanaDeModificacion() throws SQLException
	{
		setTitle("Modificar Representante");
		getContentPane().setLayout(null);
		
		JPanel panelTemporal = new JPanel();
		panelTemporal.setBounds(10, 2, 361, 338);
		
		panelTemporal.setBorder(new TitledBorder("Datos Representante"));
		getContentPane().add(panelTemporal);
		panelTemporal.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(18, 16, 61, 20);
		panelTemporal.add(lblNombre);
		
		txtNombre = new JTextField();
		validacionSoloLetras(txtNombre);
		txtNombre.setBounds(18, 32, 159, 20);
		panelTemporal.add(txtNombre);
		
		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setBounds(18, 56, 61, 20);
		panelTemporal.add(lblApellido);
		
		txtApellido = new JTextField();
		restriccionTexto = new RestrictedTextField(txtApellido);
		restriccionTexto.setLimit(25);
		validacionSoloLetras(txtApellido);
		txtApellido.setBounds(18, 72, 159, 20);
		panelTemporal.add(txtApellido);
		
		JLabel lblNivel = new JLabel("Nivel:");
		lblNivel.setBounds(18, 96, 61, 20);
		panelTemporal.add(lblNivel);
		
		ID = MantenimientoDeRepresentante.getTablaRepresentante().getValueAt(MantenimientoDeRepresentante.getTablaRepresentante().getSelectedRow(), 0).toString();
		String [] niveles = {"Novato", "Experto", "Master"};
		comboNivel = new JComboBox<String>(niveles);
		comboNivel.setSelectedItem(MantenimientoDeRepresentante.getTablaRepresentante().getValueAt(MantenimientoDeRepresentante.getTablaRepresentante().getSelectedRow(), 2).toString());
		comboNivel.setBounds(18, 112, 98, 20);
		panelTemporal.add(comboNivel);
		
		String [] tipoUsuario = {"Administrador","Representante"};
		comboTipo = new JComboBox<String>(tipoUsuario);
		comboTipo.setSelectedItem(Conexion.getInstancia().obtenerTipoRepresentante(ID));
		comboTipo.setBounds(18, 159, 121, 20);
		
		String [] sexo = {"Hombre","Mujer"};
		comboSexo = new JComboBox<String>(sexo);
		comboSexo.setBounds(18, 206, 121, 20);
		String sexoTemporal = Conexion.getInstancia().obtenerSexo(ID);
	
		if(sexoTemporal.equals("0")){
			comboSexo.setSelectedItem("Hombre");
		}
		else if(sexoTemporal.equals("1")){
			comboSexo.setSelectedItem("Mujer");
		}
		panelTemporal.add(comboSexo);
		
		if(comboTipo.getSelectedItem().equals("Administrador"))
		{
			comboNivel.setSelectedItem("Master");
			comboNivel.setEnabled(false);
		}
		else if(comboTipo.getSelectedItem().equals("Representante"))
		{
			comboNivel.removeItem("Master");
		}
		comboTipo.addActionListener(this);
		panelTemporal.add(comboTipo);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(18, 238, 61, 20);
		panelTemporal.add(lblUsuario);
		
		txtUsuario = new JTextField();
		restriccionTexto = new RestrictedTextField(txtUsuario);
		restriccionTexto.setLimit(25);
		txtUsuario.setBounds(18, 254, 159, 20);
		panelTemporal.add(txtUsuario);
		
		JLabel lblClave = new JLabel("Contrase\u00F1a:");
		lblClave.setBounds(18, 283, 98, 20);
		panelTemporal.add(lblClave);
		
		txtClave = new JPasswordField();
		restriccionTexto = new RestrictedTextField(txtClave);
		restriccionTexto.setLimit(25);
		txtClave.setBounds(18, 302, 159, 20);
		panelTemporal.add(txtClave);
		
		JLabel lblTipoUsuario = new JLabel("Tipo Usuario:");
		lblTipoUsuario.setBounds(18, 143, 98, 20);
		panelTemporal.add(lblTipoUsuario);
		
		btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(188, 351, 87, 23);
		getContentPane().add(btnGuardar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(284, 351, 87, 23);
		getContentPane().add(btnCancelar);
		
		MantenimientoDeRepresentante.obtenerDatos(txtNombre, txtApellido, txtUsuario, txtClave);
		
		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(18, 186, 98, 20);
		panelTemporal.add(lblSexo);
		
		btnGuardar.addActionListener(this);
		btnCancelar.addActionListener(this);
		
		setResizable(false);
		setSize(393,419);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
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
		if(evento.getSource().equals(btnGuardar))
		{
			try {
				Conexion.getInstancia().actualizarRepresentante(txtNombre.getText(), txtApellido.getText(), comboNivel.getSelectedItem().toString(), txtClave.getText(), txtUsuario.getText(), comboTipo.getSelectedIndex() + 1, comboSexo.getSelectedIndex(), Integer.parseInt(ID));
				Conexion.getInstancia().consultaRepresentante(MantenimientoDeRepresentante.getTablaRepresentante());
				JOptionPane.showMessageDialog(this,"Se ha actualizado el representante correctamente", "COMPLETADO", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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
		if(evento.getSource().equals(btnCancelar))
		{
			dispose();
		}
	}
}
