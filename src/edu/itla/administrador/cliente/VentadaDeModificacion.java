package edu.itla.administrador.cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import edu.itla.administrador.conexion.Conexion;

public class VentadaDeModificacion extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboBox = null;
	private JButton btnCancelar = null;
	private JButton btnAceptar = null;
	private JTextField txtCliente;
	
	public VentadaDeModificacion() throws SQLException 
	{
		setTitle("Asignar Representante");
		getContentPane().setLayout(null);

		JPanel panelTemporal = new JPanel();
		panelTemporal.setBorder(new TitledBorder("Datos Representante:"));
		panelTemporal.setBounds(10, 78, 282, 95);
		getContentPane().add(panelTemporal);
		panelTemporal.setLayout(null);
		
		JLabel lblRepresentante = new JLabel("Representante:");
		lblRepresentante.setBounds(23, 27, 130, 14);
		panelTemporal.add(lblRepresentante);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(23, 47, 162, 20);
		comboBox.addItem("REMOVER REPRESENTANTE");
		Conexion.getInstancia().llenarCombo(comboBox);
		panelTemporal.add(comboBox);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(203, 184, 89, 23);
		getContentPane().add(btnCancelar);
		
		btnAceptar = new JButton("Aceptar ");
		btnAceptar.setBounds(104, 184, 89, 23);
		getContentPane().add(btnAceptar);
		
		txtCliente = new JTextField();
		txtCliente.setEditable(false);
		txtCliente.setBounds(33, 44, 160, 20);
		getContentPane().add(txtCliente);
		txtCliente.setColumns(10);
		
		JLabel lblCliente = new JLabel("Cliente:");
		lblCliente.setBounds(34, 26, 56, 14);
		getContentPane().add(lblCliente);
		
		MantenimientoDeCliente.obtenerCliente(txtCliente);
		
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);

		setSize(315,263);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	private void actualizarDatos() throws Exception
	{
		Conexion.getInstancia().consultaCliente(MantenimientoDeCliente.getTablaCliente());
	}
	public void actionPerformed(ActionEvent evento) 
	{	
		try {
		if(evento.getSource().equals(btnAceptar))
		{
			if(comboBox.getSelectedIndex() != 0)
			{
				String nombreCliente = txtCliente.getText();
				String [] idCliente = nombreCliente.split("-");
				
				String nombreRepresentante = comboBox.getSelectedItem().toString();
				String[] idRepresentante = nombreRepresentante.split("-");
				
				try {
					Conexion.getInstancia().asignarRepresentante(idRepresentante[0], idCliente[0]);
					actualizarDatos();
					JOptionPane.showMessageDialog(this, "Se le ha asignado un represetante al cliente.", "COMPLETADO" , JOptionPane.INFORMATION_MESSAGE);
					dispose();
					
				} catch (SQLException e)
				{
					JOptionPane.showMessageDialog(this, "No se ha podido conectar con la base de datos. '"+e.getMessage()+"' ","Error en la Conexion" , JOptionPane.ERROR_MESSAGE);

				} catch (Exception e) 
				{
					JOptionPane.showMessageDialog(this, "'"+e.getMessage()+"' ","Ha surgido un error" , JOptionPane.ERROR_MESSAGE);

				}
			}
			else
			{
				String nombreCliente = txtCliente.getText();
				String [] idCliente = nombreCliente.split("-");
		
				Conexion.getInstancia().asignarRepresentanteNulo(idCliente[0]);
				actualizarDatos();
				JOptionPane.showMessageDialog(this, "Se le ha removido el representante al cliente.", "COMPLETADO" , JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(evento.getSource().equals(btnCancelar))
		{
			dispose();
		}
	}
}
