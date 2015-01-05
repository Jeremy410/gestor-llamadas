package edu.itla.administrador.acuerdo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import com.toedter.calendar.JDateChooser;
import edu.itla.administrador.conexion.Conexion;
import edu.itla.tabla.RenderizadorTablaAcuerdoDetalle;

public class VentanaDeModificacion extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField txtMontoAcuerdo = null;
	private JDateChooser txtFecha = null;
	private static JTable tablaAcuerdoDetalle = null;
	private JButton btnModificar;
	private JButton btnCancelar = null;
	private JButton btnGuardar = null;
	private int fila = 0;
	private String date = null;
	private String idDeuda = null;
	private SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	private JTextField txtCuotaPendiente;
	private JButton btnBorrar = null;
	private String idDetalle = null;
	private JButton btnAgregar = null;
	private DefaultTableModel modelo = null;
	private int contadorGeneral = 0;
	
	public VentanaDeModificacion() throws SQLException 
	{
		setTitle("Modificar Acuerdo");
		getContentPane().setLayout(null);

		JPanel panelTemporal = new JPanel();
		panelTemporal.setBorder(new TitledBorder("Datos acuerdo:"));
		panelTemporal.setBounds(20, 21, 488, 100);
		getContentPane().add(panelTemporal);
		panelTemporal.setLayout(null);
		
		JLabel lblFechaAcuerdo = new JLabel("Fecha de acuerdo:");
		lblFechaAcuerdo.setBounds(15, 30, 130, 14);
		panelTemporal.add(lblFechaAcuerdo);
		
		txtFecha = new JDateChooser("yyyy-MM-dd", "####-##-##", '_');
		txtFecha.setMinSelectableDate(new Date());
		txtFecha.setBounds(149, 26, 95, 20);
		panelTemporal.add(txtFecha);
		
		JLabel lblMontoAcuerdo = new JLabel("Monto de acuerdo:");
		lblMontoAcuerdo.setBounds(15, 61, 130, 14);
		panelTemporal.add(lblMontoAcuerdo);
		
		idDeuda = String.valueOf(MantenimientoDeAcuerdo.getTablaAcuerdo().getValueAt(MantenimientoDeAcuerdo.getTablaAcuerdo().getSelectedRow(), 0));
		txtCuotaPendiente = new JTextField();
		final String monto = String.valueOf(Conexion.getInstancia().montoCuotaPendiente(idDeuda));
		txtCuotaPendiente.setText(monto);
		txtCuotaPendiente.setEditable(false);
		txtCuotaPendiente.setColumns(10);
		txtCuotaPendiente.setBounds(368, 27, 95, 20);
		panelTemporal.add(txtCuotaPendiente);	
		
		txtMontoAcuerdo = new JTextField();
		txtMontoAcuerdo.setColumns(10);
		txtMontoAcuerdo.setBounds(149, 59, 95, 20);
	/*	txtMontoAcuerdo.addKeyListener(new KeyAdapter() 
		{
			public void keyReleased(KeyEvent e) 
			{
				double cuotaPendiente =  Double.parseDouble(monto);
				if(tablaAcuerdoDetalle.getRowCount() != 0)
				{
					double numeroTemporal = 0;
					for(int i = 0; i < tablaAcuerdoDetalle.getRowCount(); i++)
					{
						numeroTemporal = numeroTemporal + Double.parseDouble(tablaAcuerdoDetalle.getValueAt(i, 2).toString());
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
		});*/
		panelTemporal.add(txtMontoAcuerdo);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(374, 57, 89, 23);
		panelTemporal.add(btnModificar);
		
		JLabel lblCuotaPendiente = new JLabel("Cuota Pendiente:");
		lblCuotaPendiente.setBounds(265, 30, 104, 14);
		panelTemporal.add(lblCuotaPendiente);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(275, 57, 89, 23);
		panelTemporal.add(btnAgregar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(541, 352, 89, 23);
		getContentPane().add(btnCancelar);
		
		btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(442, 352, 89, 23);
		getContentPane().add(btnGuardar);
		
		JPanel panel = new JPanel();
		panel.setBounds(20, 132, 616, 209);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout());
		
		String [] nombreColumnas = {"ID Acuerdo", "ID Deuda" ,"Detalle Deuda", "             Fecha Acuerdo", "            Monto Acuerdo"};
		modelo = new DefaultTableModel(null, nombreColumnas);
		tablaAcuerdoDetalle = new JTable(modelo);
		JScrollPane scrollPane = new JScrollPane(tablaAcuerdoDetalle);
		panel.add(scrollPane, BorderLayout.CENTER);
		tablaAcuerdoDetalle.setDefaultRenderer(Object.class, new RenderizadorTablaAcuerdoDetalle(tablaAcuerdoDetalle));
		idDetalle = String.valueOf(MantenimientoDeAcuerdo.getTablaAcuerdo().getValueAt(MantenimientoDeAcuerdo.getTablaAcuerdo().getSelectedRow(), 1));
		Conexion.getInstancia().consultaAcuerdoDetalle(tablaAcuerdoDetalle, idDetalle, idDeuda, modelo);
		
		btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(518, 80, 89, 23);
		getContentPane().add(btnBorrar);
		
		tablaAcuerdoDetalle.addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				try {
					datosTabla();

				} catch (ParseException ex) 
				{
					ex.printStackTrace();
				}
				catch (ArrayIndexOutOfBoundsException ex) 
				{
					JOptionPane.showMessageDialog(VentanaDeModificacion.this, "Esta no es una fila valida, por favor intente nuevamente. ","DENEGADO" ,JOptionPane.ERROR_MESSAGE);	
				}
			}			
		});
		btnGuardar.addActionListener(this);
		btnCancelar.addActionListener(this);
		btnModificar.addActionListener(this);
		btnBorrar.addActionListener(this);
		btnAgregar.addActionListener(this);
		
		setSize(662,415);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);

	}
	public void datosTabla() throws ParseException
	{
		fila = tablaAcuerdoDetalle.getSelectedRow();
		String fecha = tablaAcuerdoDetalle.getValueAt(fila, 3).toString();
		String monto = tablaAcuerdoDetalle.getValueAt(fila, 4).toString();
		
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
		txtFecha.setDate(date);
		
		txtMontoAcuerdo.setText(monto);
				
	}
	public static JTable getTablaAcuerdoDetalle() 
	{
		return tablaAcuerdoDetalle;
	}
	
	public void accionAgregar(JTable tabla) throws ParseException, SQLException
	{
		if(tabla.getSelectedRow() >= 0)
		{
			Date fechaDia = formatoFecha.parse(Conexion.getInstancia().obtenerFechaDia());
			if(txtFecha.getDate().before(fechaDia))
			{
				JOptionPane.showMessageDialog(this,"La fecha no puede ser anterior al día de hoy.","DENEGADO" ,JOptionPane.ERROR_MESSAGE);	
			}
			else
			{
				String monto = txtMontoAcuerdo.getText();
				date = formatoFecha.format(txtFecha.getDate());
				tabla.setValueAt(monto, tabla.getSelectedRow(), 4);
				tabla.setValueAt(date, tabla.getSelectedRow(), 3);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Es necesario seleccionar un campo." , "DENEGADO", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void actualizarDatos(JTable tabla)
	{
		double validacion = 0.0;
		int cantidadFilas = tabla.getRowCount();
		int contador = 0;
		boolean boleano = true;
		HashSet<Date> validadorFechas = new HashSet<Date>();
		
		try {
			//Validacion acuerdo
			while(contador != cantidadFilas)
			{
				validadorFechas.add(formatoFecha.parse(String.valueOf(tabla.getValueAt(contador, 3))));
				validacion = validacion + Double.parseDouble(String.valueOf(tabla.getValueAt(contador, 4)));
				contador++;
			}
			if(validadorFechas.size() != cantidadFilas)
			{
				JOptionPane.showMessageDialog(this, "Hay fechas identicas, los acuerdos no pueden tener una misma fecha de pago. ","DENEGADO" ,JOptionPane.ERROR_MESSAGE);
				boleano = false;
			}
			if(boleano == true)
			{	
				int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea guardar los cambios?", "CONFIRMACION" , JOptionPane.YES_NO_OPTION);
				if(respuesta == JOptionPane.YES_OPTION)
				{
					if(Conexion.getInstancia().datosAcuerdo(idDeuda, validacion, tabla, false) == true)
					{
						JOptionPane.showMessageDialog(this, "Se ha modificado el acuerdo correctamente.","COMPLETADO" ,JOptionPane.INFORMATION_MESSAGE);
						Conexion.getInstancia().consultaAcuerdo(MantenimientoDeAcuerdo.getTablaAcuerdo());
						dispose();
					}
					else
					{
						validacion = 0;
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public void agregarDatos(JTable tabla) throws SQLException, ParseException 
	{	
		if(txtFecha.getDate() == null)
		{
			JOptionPane.showMessageDialog(this, "El acuerdo debe tener una fecha de pago. " , "DENEGADO", JOptionPane.ERROR_MESSAGE);
		}
		else 
		{
			String datos [] = new String [5];
			datos[0] = Conexion.getInstancia().obtenerIdAcuerdo(contadorGeneral);
			contadorGeneral++;
			datos[1] = String.valueOf(tablaAcuerdoDetalle.getValueAt(0, 1));
			datos[2] = String.valueOf(tablaAcuerdoDetalle.getValueAt(0, 2));
			datos[3] = String.valueOf(formatoFecha.format(txtFecha.getDate()));
			datos[4] = txtMontoAcuerdo.getText();
			modelo.addRow(datos);
			
			/*if(txtFecha.getDate().before(formatoFecha.parse(Conexion.getInstancia().obtenerFechaDia())))
			{
				JOptionPane.showMessageDialog(this, "La fecha no puede ser anterior al día de hoy. ","DENEGADO" ,JOptionPane.ERROR_MESSAGE);
				modelo.removeRow(tabla.getRowCount() - 1);
			}*/
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		try 
		{
			if(e.getSource().equals(btnModificar))
			{
				accionAgregar(tablaAcuerdoDetalle);
			}
			if(e.getSource().equals(btnGuardar))
			{
				actualizarDatos(tablaAcuerdoDetalle);
			}
			if(e.getSource().equals(btnBorrar))
			{
				int respuesta = JOptionPane.showConfirmDialog(this, "Esta apunto de borrar la fecha de un pago. El siguiente dato no se podra recuperar. ¿Seguro que desea proceder?", "ATENCION", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(respuesta == JOptionPane.YES_OPTION)
				{
					Conexion.getInstancia().borrarAcuerdoModificacion(String.valueOf(tablaAcuerdoDetalle.getValueAt(tablaAcuerdoDetalle.getSelectedRow(), 0)));
					Conexion.getInstancia().consultaAcuerdoDetalle(tablaAcuerdoDetalle, idDetalle, idDeuda, modelo);
					Conexion.getInstancia().consultaAcuerdo(MantenimientoDeAcuerdo.getTablaAcuerdo());
					JOptionPane.showMessageDialog(this, "¡Cuidado!, al haberse borrado una fecha de pago el Acuerdo esta Inestable. Verifique los pagos del Acuerdo.", "DESVALUACION DE ACUERDO", JOptionPane.WARNING_MESSAGE);
				}
			}
			if(e.getSource().equals(btnAgregar))
			{
				if(txtMontoAcuerdo.getText().equals(""))
				{
					JOptionPane.showMessageDialog(this, "El acuerdo debe tener un monto de pago. " , "DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					agregarDatos(tablaAcuerdoDetalle);
				}
			}
			if(e.getSource().equals(btnCancelar))
			{
				dispose();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
