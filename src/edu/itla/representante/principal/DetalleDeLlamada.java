package edu.itla.representante.principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import Atxy2k.CustomTextField.RestrictedTextField;
import edu.itla.administrador.conexion.Conexion;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DetalleDeLlamada extends JPanel implements ActionListener  {

	private static final long serialVersionUID = 1L;
	private static JTextField txtReceptor = null;
	private static JTextField txtNumero;
	private JButton btnAcuerdo = null;
	private JButton btnCerrarGestion = null;
	private JButton btnSeguimiento = null;
	private static JTextArea textArea = null;
	private RestrictedTextField restriccionTexto = null;
	private JButton btnCancelar = null;
	private JTextField txtCelular;
	private JTextField txtTelefono;
	private JTextField txtTrabajo;
	private JTextField txtCelularF;
	private JTextField txtCasaF;
	private JTextField txtTrabajoF;
	private JTextField txtNombreF;

	public DetalleDeLlamada() throws SQLException 
	{
		setLayout(null);
		JLabel lblReceptor = new JLabel("Receptor:");
		lblReceptor.setBounds(32, 55, 63, 14);
		add(lblReceptor);
		
		txtReceptor = new JTextField();
		restriccionTexto = new RestrictedTextField(txtReceptor);
		restriccionTexto.setLimit(20);
		validacionSoloLetras(txtReceptor);
		txtReceptor.setBounds(100, 52, 135, 20);
		add(txtReceptor);
		txtReceptor.setColumns(10);
		
		JLabel lblNumero = new JLabel("Telefono:");
		lblNumero.setBounds(32, 80, 63, 14);
		add(lblNumero);
		
		txtNumero = new JTextField();
		txtNumero.setEditable(false);
		txtNumero.setColumns(10);
		txtNumero.setBounds(100, 77, 135, 20);
		add(txtNumero);
		
		JLabel lblComentario = new JLabel("Comentario:");
		lblComentario.setBounds(14, 194, 86, 14);
		add(lblComentario);
		
		btnAcuerdo = new JButton("Acuerdo");
		btnAcuerdo.setBounds(542, 389, 106, 23);
		add(btnAcuerdo);
		
		btnCerrarGestion = new JButton("Cerrar Gestion");
		btnCerrarGestion.setBounds(310, 389, 106, 23);
		add(btnCerrarGestion);
		
		btnSeguimiento = new JButton("Seguimiento");
		btnSeguimiento.setBounds(426, 389, 106, 23);
		add(btnSeguimiento);
		
		JLabel lblDetalleDeDeuda = new JLabel("DETALLE LLAMADA");
		lblDetalleDeDeuda.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDetalleDeDeuda.setHorizontalAlignment(SwingConstants.CENTER);
		lblDetalleDeDeuda.setBounds(321, 11, 141, 14);
		add(lblDetalleDeDeuda);
			
		btnSeguimiento.addActionListener(this);
		btnAcuerdo.addActionListener(this);
		btnCerrarGestion.addActionListener(this);
		
		JPanel panelArea = new JPanel();
		panelArea.setBounds(14, 213, 751, 165);
		add(panelArea);
		panelArea.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		panelArea.add(scrollPane, BorderLayout.CENTER);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(651, 389, 106, 23);
		add(btnCancelar);
		
		btnCancelar.addActionListener(this);
		
		JPanel panelCliente = new JPanel();
		panelCliente.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Llamada", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCliente.setBounds(23, 28, 257, 155);
		add(panelCliente);
		panelCliente.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panelDetalle = new JPanel();
		panelDetalle.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Cliente", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDetalle.setBounds(290, 28, 231, 155);
		add(panelDetalle);
		panelDetalle.setLayout(null);
		
		txtCelular = new JTextField();
		txtCelular.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				obtenerNumero(txtCelular);
			}
		});
		txtCelular.setEditable(false);
		txtCelular.setColumns(10);
		txtCelular.setBounds(78, 57, 135, 20);
		panelDetalle.add(txtCelular);
		
		JLabel lblCelular = new JLabel("Celular:");
		lblCelular.setBounds(10, 60, 63, 14);
		panelDetalle.add(lblCelular);
		
		JLabel lblCasa = new JLabel("Casa:");
		lblCasa.setBounds(10, 32, 63, 14);
		panelDetalle.add(lblCasa);
		
		txtTelefono = new JTextField();
		txtTelefono.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				obtenerNumero(txtTelefono);
			}
		});
		txtTelefono.setEditable(false);
		txtTelefono.setColumns(10);
		txtTelefono.setBounds(78, 29, 135, 20);
		panelDetalle.add(txtTelefono);
		
		JLabel lblTrabajo = new JLabel("Trabajo:");
		lblTrabajo.setBounds(10, 91, 63, 14);
		panelDetalle.add(lblTrabajo);
		
		txtTrabajo = new JTextField();
		txtTrabajo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				obtenerNumero(txtTrabajo);
			}
		});
		txtTrabajo.setEditable(false);
		txtTrabajo.setColumns(10);
		txtTrabajo.setBounds(78, 88, 135, 20);
		panelDetalle.add(txtTrabajo);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fiador", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(531, 28, 231, 155);
		add(panel);
		panel.setLayout(null);
		
		txtCasaF = new JTextField();
		txtCasaF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				obtenerNumero(txtCasaF);
			}
		});
		txtCasaF.setEditable(false);
		txtCasaF.setColumns(10);
		txtCasaF.setBounds(78, 30, 135, 20);
		panel.add(txtCasaF);
		
		JLabel lblCasaF = new JLabel("Casa:");
		lblCasaF.setBounds(10, 33, 63, 14);
		panel.add(lblCasaF);
		
		JLabel lblTrabajoF = new JLabel("Trabajo:");
		lblTrabajoF.setBounds(10, 92, 63, 14);
		panel.add(lblTrabajoF);
		
		txtTrabajoF = new JTextField();
		txtTrabajoF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				obtenerNumero(txtTrabajoF);
			}
		});
		txtTrabajoF.setEditable(false);
		txtTrabajoF.setColumns(10);
		txtTrabajoF.setBounds(78, 89, 135, 20);
		panel.add(txtTrabajoF);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(10, 120, 63, 14);
		panel.add(lblNombre);
		
		txtNombreF = new JTextField();
		txtNombreF.setEditable(false);
		txtNombreF.setColumns(10);
		txtNombreF.setBounds(78, 117, 135, 20);
		panel.add(txtNombreF);
		
		Conexion.getInstancia().obtenerCliente(txtTelefono, txtTrabajo, txtCelular, txtNumero);
		
		txtCelularF = new JTextField();
		txtCelularF.setBounds(78, 61, 135, 20);
		panel.add(txtCelularF);
		txtCelularF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				obtenerNumero(txtCelularF);
			}
		});
		txtCelularF.setEditable(false);
		txtCelularF.setColumns(10);
		Conexion.getInstancia().obtenerFiador(txtNombreF, txtCasaF, txtCelularF, txtTrabajoF);
		
		JLabel lblCelularF = new JLabel("Celular:");
		lblCelularF.setBounds(10, 64, 63, 14);
		panel.add(lblCelularF);
	}
	public static JTextField getTxtReceptor() {
		return txtReceptor;
	}
	public static JTextField getTxtTelefono() {
		return txtNumero;
	}
	public static JTextArea getTextArea() {
		return textArea;
	}

	public void obtenerNumero(JTextField telefono){
		String telefonoTemporal = telefono.getText();
		txtNumero.setText(telefonoTemporal);		
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
		try {
			if(evento.getSource().equals(btnCancelar))
			{
				GestionDeDeuda.getDatos().dispose();
			}
			if(evento.getSource().equals(btnSeguimiento))
			{
				new Seguimiento();		
			}
			if(evento.getSource().equals(btnAcuerdo))
			{
				new Acuerdo();
			}
			if(evento.getSource().equals(btnCerrarGestion))
			{
				if(txtReceptor.getText().trim().length() == 0 || textArea.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(this, "No puede haber campos vacios, verifique su informacion. ", "DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					int respuesta = JOptionPane.showConfirmDialog(this, "¿Seguro que desea cerrar la gestión?", "ATENCION", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(respuesta == JOptionPane.YES_OPTION)
					{
						Conexion.getInstancia().insertarDatosDetalleLlamada(GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0).toString(), txtReceptor.getText(), txtNumero.getText(), textArea.getText(), GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 6).toString());
						JOptionPane.showMessageDialog(this, "La gestion se ha cerrado satisfactoriamente. ", "COMPLETADO", JOptionPane.INFORMATION_MESSAGE);
						GestionDeDeuda.getDatos().dispose();
						Conexion.getInstancia().consultaGestionEnFechas(GestionDeDeuda.getTablaGestion(), GestionDeDeuda.getFecha1(), GestionDeDeuda.getFecha2());
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
