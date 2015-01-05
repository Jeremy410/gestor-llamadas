package edu.itla.administrador.seguimiento;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import edu.itla.administrador.conexion.Conexion;

public class VentanaDeModificacion extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JDateChooser txtFecha = null;
	private JTextField txtSeguimiento = null;
	private JTextField txtCliente = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private String fecha = null;
	
	public VentanaDeModificacion() throws ParseException
	{
		setTitle("Modificar Seguimiento");
		getContentPane().setLayout(null);
		
		JPanel panelTemporal = new JPanel();
		panelTemporal.setBounds(10, 11, 286, 139);
		
		panelTemporal.setBorder(new TitledBorder("Datos seguimiento"));
		getContentPane().add(panelTemporal);
		panelTemporal.setLayout(null);
		
		txtFecha = new JDateChooser("yyyy-MM-dd", "####-##-##", '_');
		txtFecha.setMinSelectableDate(new Date());
		txtFecha.setBounds(49, 96, 106, 20);
		panelTemporal.add(txtFecha);
		
		JLabel lblCliente = new JLabel("Cliente:");
		lblCliente.setBounds(10, 32, 80, 14);
		panelTemporal.add(lblCliente);
		
		JLabel lblSeguimiento = new JLabel("Seguimiento:");
		lblSeguimiento.setBounds(10, 77, 103, 14);
		panelTemporal.add(lblSeguimiento);
		
		txtSeguimiento = new JTextField();
		txtSeguimiento.setBounds(10, 96, 29, 20);
		txtSeguimiento.setEditable(false);
		panelTemporal.add(txtSeguimiento);
		txtSeguimiento.setColumns(10);
		
		txtCliente = new JTextField();
		txtCliente.setColumns(10);
		txtCliente.setEditable(false);
		txtCliente.setBounds(10, 46, 145, 20);
		panelTemporal.add(txtCliente);
	
		MantenimientoDeSeguimiento.obtenerDatos(txtCliente, txtSeguimiento, txtFecha);

		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(98, 161, 91, 23);
		getContentPane().add(btnAceptar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(199, 161, 91, 23);
		getContentPane().add(btnCancelar);
		
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);
		
		setResizable(false);
		setSize(312,227);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(btnAceptar))
		{
			try {
				SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
				fecha = formatoFecha.format(txtFecha.getDate());
				
				Conexion.getInstancia().actualizarSeguimiento(fecha, txtSeguimiento.getText());
				JOptionPane.showMessageDialog(this, "Se ha actualiado el seguimiento correctamente","COMPLETADO", JOptionPane.INFORMATION_MESSAGE);
				Conexion.getInstancia().consultaSeguimiento(MantenimientoDeSeguimiento.getTablaSeguimiento());
				dispose();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(evento.getSource().equals(btnCancelar))
		{
			dispose();
		}
	}
}
