package edu.itla.representante.principal;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import Atxy2k.CustomTextField.RestrictedTextField;
import com.toedter.calendar.JDateChooser;
import edu.itla.administrador.conexion.Conexion;
import edu.itla.tabla.ModeloDeTabla;

public class Acuerdo extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JDateChooser txtFecha = null;
	private JTextField txtMontoAcuerdo = null;
	private JTable tablaAcuerdo = null;
	private JButton btnAgregar = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private JButton btnBorrar = null;
	private String fecha = null;
	private String idDeuda = null;
	private RestrictedTextField restriccionTexto = null;
	private DefaultTableModel modelo = null;
	private JTextField txtCuotaPendiente = null;
	private SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	private String montoDeuda = null;

	public Acuerdo() throws SQLException 
	{
		setTitle("Creacion de Acuerdo");
		getContentPane().setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBounds(10, 96, 616, 227);
		getContentPane().add(panelTabla);
		panelTabla.setLayout(new BorderLayout(0, 0));
		
		String [] nombreColumnas = {"ID Detalle Deuda", "Fecha", "Monto"};
		modelo = new DefaultTableModel(null, nombreColumnas);
		tablaAcuerdo = new JTable(modelo);
		ModeloDeTabla.diseñoTabla(tablaAcuerdo);
		tablaAcuerdo.setModel(modelo);
		JScrollPane scrollPane = new JScrollPane(tablaAcuerdo);
		panelTabla.add(scrollPane, BorderLayout.CENTER);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(530, 345, 96, 23);
		getContentPane().add(btnCancelar);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(430, 345, 90, 23);
		getContentPane().add(btnAceptar);
		
		JPanel panelAcuerdo = new JPanel();
		panelAcuerdo.setLayout(null);
		
		panelAcuerdo.setBorder(new TitledBorder("Datos acuerdo"));
		panelAcuerdo.setBounds(10, 11, 454, 83);
		getContentPane().add(panelAcuerdo);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(355, 54, 87, 23);
		panelAcuerdo.add(btnAgregar);
		
		JLabel lblFechaPago = new JLabel("Fecha Acuerdo:");
		lblFechaPago.setBounds(10, 25, 99, 14);
		panelAcuerdo.add(lblFechaPago);
		
		txtFecha = new JDateChooser("yyyy-MM-dd", "####-##-##", '_');
		txtFecha.setMinSelectableDate(new Date());
		txtFecha.setBounds(119, 23, 99, 20);
		panelAcuerdo.add(txtFecha);
		
		JLabel lblMonto = new JLabel("Monto Acuerdo:");
		lblMonto.setBounds(10, 58, 99, 14);
		panelAcuerdo.add(lblMonto);
		
		txtMontoAcuerdo = new JTextField();
		restriccionTexto = new RestrictedTextField(txtMontoAcuerdo);
		restriccionTexto.setOnlyNums(true);
		txtMontoAcuerdo.setBounds(119, 54, 99, 20);
		panelAcuerdo.add(txtMontoAcuerdo);
		txtMontoAcuerdo.setColumns(10);
		idDeuda = Conexion.getInstancia().obtenerIdDeuda(String.valueOf(GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0)), GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 6).toString());
		montoDeuda = String.valueOf(Conexion.getInstancia().montoCuotaPendiente(idDeuda));
		txtMontoAcuerdo.addKeyListener(new KeyAdapter() 
		{
			public void keyReleased(KeyEvent e) 
			{
				double cuotaPendiente =  Double.parseDouble(montoDeuda);
				if(tablaAcuerdo.getRowCount() != 0)
				{
					double numeroTemporal = 0;
					for(int i = 0; i < tablaAcuerdo.getRowCount(); i++)
					{
						numeroTemporal = numeroTemporal + Double.parseDouble(tablaAcuerdo.getValueAt(i, 2).toString());
					}
					cuotaPendiente = cuotaPendiente - numeroTemporal;
				}
				double restaMonto = 0;
				if(txtMontoAcuerdo.getText().trim().length() != 0)
				{
					double numero1 = cuotaPendiente;
					double numero2 = Double.parseDouble(txtMontoAcuerdo.getText());
					restaMonto = numero1 - numero2;
					txtCuotaPendiente.setText(String.valueOf(restaMonto));
				}
				else
				{
					txtCuotaPendiente.setText(String.valueOf(cuotaPendiente));
				}
			}
		});
		
		JLabel lblMontoCuota = new JLabel("Cuota Pendiente:");
		lblMontoCuota.setBounds(233, 25, 99, 14);
		panelAcuerdo.add(lblMontoCuota);
		
		txtCuotaPendiente = new JTextField();
		txtCuotaPendiente.setText(montoDeuda);
		txtCuotaPendiente.setEditable(false);
		txtCuotaPendiente.setColumns(10);
		txtCuotaPendiente.setBounds(339, 22, 105, 20);
		panelAcuerdo.add(txtCuotaPendiente);
		
		btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(474, 65, 91, 23);
		getContentPane().add(btnBorrar);
		
		btnAceptar.addActionListener(this);
		btnAgregar.addActionListener(this);
		btnCancelar.addActionListener(this);
		btnBorrar.addActionListener(this);
				
		setSize(643,407);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);	
	}
	public void agregarAcuerdo()
	{
		try {
			String [] datos = new String [3];
			fecha = formatoFecha.format(txtFecha.getDate());
			//Date fechaDia = formatoFecha.parse(Conexion.getInstancia().obtenerFechaDia());
			
				/*if(txtFecha.getDate().before(fechaDia))
				{
					JOptionPane.showMessageDialog(this,"La fecha no puede ser anterior al día de hoy.","DENEGADO" ,JOptionPane.ERROR_MESSAGE);	
				}
				else
				{*/
					datos[1] = fecha;
					datos[2] = txtMontoAcuerdo.getText();
					datos[0] = Conexion.getInstancia().obtenerIdDetalleDeuda(String.valueOf(GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0))); 
					modelo.addRow(datos);
					txtMontoAcuerdo.setText("");
					txtFecha.setDate(null);
				//}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(this,"El acuerdo necesita una fecha de pago. ","DENEGADO" ,JOptionPane.ERROR_MESSAGE);	
			}
	}
	public void insertarAcuerdo()
	{
		if(tablaAcuerdo.getRowCount() > 1)
		{
			double validacion = 0.0;
			int cantidadFilas = tablaAcuerdo.getRowCount();
			int contador = 0;
			boolean boleano = true;
			HashSet<Date> validadorFechas = new HashSet<Date>();

			try {
				while(contador != cantidadFilas)
				{
					validadorFechas.add(formatoFecha.parse(String.valueOf(tablaAcuerdo.getValueAt(contador, 1))));
					validacion = validacion + Double.parseDouble(String.valueOf(tablaAcuerdo.getValueAt(contador, 2)));
					contador++;
				}
				
				if(validadorFechas.size() != tablaAcuerdo.getRowCount())
				{
					JOptionPane.showMessageDialog(this, "Hay fechas identicas, los acuerdos no pueden tener una misma fecha de pago. ","DENEGADO" ,JOptionPane.ERROR_MESSAGE);
					boleano = false;
				}
				if(boleano == true)
				{	
					int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea guardar los cambios?", "CONFIRMACION" , JOptionPane.YES_NO_OPTION);				
					if(Conexion.getInstancia().datosAcuerdo(idDeuda, validacion, tablaAcuerdo, true) == true)
					{
						if(respuesta == JOptionPane.YES_OPTION)
						{
							JOptionPane.showMessageDialog(this, "Se ha agregado un acuerdo correctamente.","COMPLETADO" ,JOptionPane.INFORMATION_MESSAGE);
							Conexion.getInstancia().insertarDatosDetalleLlamada(GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0).toString(), "AUTOMATICO", DetalleDeLlamada.getTxtTelefono().getText(), "CREACION DE ACUERDO, FECHA: , "+ Conexion.getInstancia().obtenerFechaDia() +"", GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 6).toString());
							dispose();
							GestionDeDeuda.getDatos().dispose();
							Conexion.getInstancia().consultaGestionEnFechas(GestionDeDeuda.getTablaGestion(), GestionDeDeuda.getFecha1(), GestionDeDeuda.getFecha2());
						}
						else
						{
							validacion = 0;
						}
					}
				}
				
			} catch (SQLException e) 
			{
				JOptionPane.showMessageDialog(this, "Ha ocurrido un error en la transferencia de datos con la base de datos. '"+e.getMessage()+ "'", "DENEGADO", JOptionPane.ERROR_MESSAGE);

			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Un acuerdo debe tener dos o mas fechas de pago.","DENEGADO" ,JOptionPane.ERROR_MESSAGE);
		}
	}

	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource().equals(btnAgregar))
		{
			if(txtMontoAcuerdo.getText().trim().length() == 0)
			{
				JOptionPane.showMessageDialog(this,"El acuerdo necesita un monto de pago.","DENEGADO" ,JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				agregarAcuerdo();
			}
		}
		if(evento.getSource().equals(btnAceptar))
		{
			insertarAcuerdo();
		}
		if(evento.getSource().equals(btnBorrar))
		{
			if(tablaAcuerdo.getSelectedRow() >= 0)
			{	
				double numero1 = Double.parseDouble(tablaAcuerdo.getValueAt(tablaAcuerdo.getSelectedRow(),2).toString());
				double numero2 = Double.parseDouble(txtCuotaPendiente.getText());
				txtCuotaPendiente.setText(String.valueOf(numero1+ numero2));
				modelo.removeRow(tablaAcuerdo.getSelectedRow());
			}
			else
			{
				JOptionPane.showMessageDialog(this,"Seleccione el pago de acuerdo que desea eliminar. ","DENEGADO" ,JOptionPane.ERROR_MESSAGE);
			}
		}
		if(evento.getSource().equals(btnCancelar))
		{
			dispose();
		}
	}
}