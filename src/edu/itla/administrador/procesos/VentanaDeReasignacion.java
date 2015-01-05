package edu.itla.administrador.procesos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import edu.itla.administrador.conexion.Conexion;

public class VentanaDeReasignacion extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JTextField txtRepresentante = null;
	private JButton btnGuardar = null;
	private JButton btnCancelar = null;
	private String validacionRepresentante = null;
	private JComboBox<String> comboRepresentante = null;
	
	public VentanaDeReasignacion() throws SQLException 
	{
		setResizable(false);
		setTitle("Reasignacion");
		getContentPane().setLayout(null);
		
		JLabel lblUsuario = new JLabel("Nuevo representante:");
		lblUsuario.setBounds(15, 83, 114, 14);
		getContentPane().add(lblUsuario);
		
		validacionRepresentante = ReasignacionDeGestiones.getTablaGestion().getValueAt(ReasignacionDeGestiones.getTablaGestion().getSelectedRow(), 0).toString();
		comboRepresentante = new JComboBox<String>();
		comboRepresentante.setBounds(176, 80, 205, 20);
		getContentPane().add(comboRepresentante);
		Conexion.getInstancia().llenarComboReasignacion(comboRepresentante, validacionRepresentante);
		
		JLabel lblRepresentanteActual = new JLabel("Representante actual:");
		lblRepresentanteActual.setBounds(15, 53, 150, 14);
		getContentPane().add(lblRepresentanteActual);
		
		txtRepresentante = new JTextField();
		txtRepresentante.setEditable(false);
		txtRepresentante.setBounds(175, 50, 205, 20);
		getContentPane().add(txtRepresentante);
		txtRepresentante.setColumns(10);
		
		ReasignacionDeGestiones.obtenerDatos(txtRepresentante);
		
		btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(197, 122, 89, 23);
		getContentPane().add(btnGuardar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(292, 122, 89, 23);
		getContentPane().add(btnCancelar);
		
		JLabel lblAsiganarGestion = new JLabel("REASIGNAR GESTION");
		lblAsiganarGestion.setBounds(149, 11, 124, 14);
		getContentPane().add(lblAsiganarGestion);
		
		btnCancelar.addActionListener(this);
		btnGuardar.addActionListener(this);
		
		setSize(409,185);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	

	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource().equals(btnGuardar))
		{
			try {
				String nombreRepresentante = comboRepresentante.getSelectedItem().toString();
				String[] idRepresentante = nombreRepresentante.split("-");
				String idGestion = String.valueOf(ReasignacionDeGestiones.getTablaGestion().getValueAt(ReasignacionDeGestiones.getTablaGestion().getSelectedRow(), 5));
				Conexion.getInstancia().actualizarReasignacion(idRepresentante[0],idGestion);
				Conexion.getInstancia().consultaReasignacion(ReasignacionDeGestiones.getTablaGestion());
				JOptionPane.showMessageDialog(this, "Se le ha asignado otro representante a la gestion correctamente.", "COMPLETA", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		if(evento.getSource().equals(btnCancelar))
		{
			dispose();
		}
	}
}
