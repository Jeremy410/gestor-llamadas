package edu.itla.administrador.reportes;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.itla.administrador.conexion.Conexion;
import edu.itla.administrador.principal.Buscador;

public class ReporteDePagos extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JTable tablaCliente;
	private JButton btnAceptar;
	private JTextField txtBuscador;
	private JButton btnCancelar;
	private JButton btnSeleccion;
	private JTextField txtSeleccion;
	private static String ID;
	
	public ReporteDePagos(Container contenedor, boolean setVisible) throws Exception 
	{
		setTitle("Seleccion de Cliente");
		tablaCliente = new JTable();
		JScrollPane scrollPane = new JScrollPane(tablaCliente);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		Conexion.getInstancia().consultaClienteParametros(tablaCliente);
		
		JPanel panelSur = new JPanel();
		getContentPane().add(panelSur, BorderLayout.SOUTH);
		
		btnSeleccion = new JButton("Seleccionar Cliente");
		contenedor.add(btnSeleccion);
		
		JLabel lblSeleccionCliente = new JLabel("Seleccion de Cliente");
		contenedor.add(lblSeleccionCliente);
		
		txtSeleccion = new JTextField();
		txtSeleccion.setColumns(20);
		txtSeleccion.setText("0 - TODOS LOS CLIENTES");
		txtSeleccion.setEditable(false);
		contenedor.add(txtSeleccion);
		
		btnAceptar = new JButton("Aceptar");
		panelSur.add(btnAceptar);
		
		btnCancelar = new JButton("Cancelar");
		panelSur.add(btnCancelar);
		
		JPanel panelNorte = new JPanel();
		getContentPane().add(panelNorte, BorderLayout.NORTH);
		
		JLabel lblBuscadorDeClientes = new JLabel("Buscador de Clientes:");
		panelNorte.add(lblBuscadorDeClientes);
		
		txtBuscador = new JTextField();
		txtBuscador.addKeyListener(new KeyAdapter() 
		{
			public void keyReleased(KeyEvent e) {
				crearBuscador(Buscador.getInstancia());
			}
			
		});
		panelNorte.add(txtBuscador);
		txtBuscador.setColumns(15);
		
		setSize(574,374);
		setVisible(setVisible);
		setLocationRelativeTo(null);
		
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);
		btnSeleccion.addActionListener(this);
	}
	public void crearBuscador(Buscador buscador)
	{
		buscador.buscar(tablaCliente, txtBuscador.getText());
	}
	public void obtenerCliente(JTable tabla, JTextField txtCliente)
	{
		int fila = tabla.getSelectedRow();
		ID = String.valueOf(tabla.getValueAt(fila, 0));
		String nombre = String.valueOf(tabla.getValueAt(fila, 1));
		txtCliente.setText(ID + " - " + nombre);
	}
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource().equals(btnAceptar))
		{
			obtenerCliente(tablaCliente, txtSeleccion);
			this.dispose();
		}
		if(e.getSource().equals(btnCancelar))
		{
			this.dispose();
		}
		if(e.getSource().equals(btnSeleccion))
		{
			try {
				new ReporteDePagos(this, true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	
}
