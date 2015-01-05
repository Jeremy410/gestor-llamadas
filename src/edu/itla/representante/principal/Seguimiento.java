package edu.itla.representante.principal;

import java.awt.HeadlessException;
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

import com.toedter.calendar.JDateChooser;

import edu.itla.administrador.conexion.Conexion;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Seguimiento extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JDateChooser txtFecha = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

	public Seguimiento() 
	{
		setTitle("Seguimiento");
		getContentPane().setLayout(null);
		
		JLabel lblFecha = new JLabel("Fecha del Seguimiento:");
		lblFecha.setBounds(42, 35, 139, 14);
		getContentPane().add(lblFecha);
		
		txtFecha = new JDateChooser("yyyy-MM-dd", "####-##-##", '_');
		txtFecha.setMinSelectableDate(new Date());
		txtFecha.setBounds(42, 52, 107, 20);
		getContentPane().add(txtFecha);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(118, 125, 89, 23);
		getContentPane().add(btnAceptar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(217, 125, 89, 23);
		getContentPane().add(btnCancelar);
		
		JPanel panelSeguimiento = new JPanel();
		panelSeguimiento.setBorder(new TitledBorder(null, "Crear seguimiento", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSeguimiento.setBounds(10, 11, 296, 103);
		getContentPane().add(panelSeguimiento);
		
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);
		
		setSize(322,187);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	public static JDateChooser getTxtFecha() 
	{
		return txtFecha;
	}
	public void crearSeguimiento() throws SQLException, HeadlessException, ParseException
	{
		String fecha = formatoFecha.format(txtFecha.getDate());		
		String idCliente = GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0).toString();
		String idGestion = GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 6).toString();
		Conexion.getInstancia().insertarSeguimiento(idCliente, fecha, idGestion);
		Conexion.getInstancia().actualizarSeguimientoGestion(idCliente, idGestion);
		JOptionPane.showMessageDialog(this, "Se ha agregado el seguimiento correctamente.", "COMPLETADO", JOptionPane.INFORMATION_MESSAGE);
		Conexion.getInstancia().consultaGestionEnFechas(GestionDeDeuda.getTablaGestion(), GestionDeDeuda.getFecha1(), GestionDeDeuda.getFecha2());
		dispose();
		GestionDeDeuda.getDatos().dispose();
	}
	public void actionPerformed(ActionEvent evento) 
	{	
		try {
			if(evento.getSource().equals(btnAceptar))
			{
				if(txtFecha.getDate() != null)
				{
					if(DetalleDeLlamada.getTxtReceptor().getText().trim().length() == 0  && DetalleDeLlamada.getTextArea().getText().trim().length() == 0)
					{
						String fecha = Conexion.getInstancia().obtenerFechaDia();
						Conexion.getInstancia().insertarDatosDetalleLlamada(GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0).toString(), "AUTOMATICO", DetalleDeLlamada.getTxtTelefono().getText(), "CREACION DE SEGUIMIENTO, FECHA: , "+fecha+"", GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 6).toString());
						crearSeguimiento();
					}
					else if(DetalleDeLlamada.getTxtReceptor().getText().trim().length() != 0  && DetalleDeLlamada.getTextArea().getText().trim().length() == 0)
					{
						JOptionPane.showMessageDialog(this, "O todos los campos llenos o todos los campos vacios. ", "DENEGADO", JOptionPane.ERROR_MESSAGE);
					}
					else if(DetalleDeLlamada.getTxtReceptor().getText().trim().length() == 0  && DetalleDeLlamada.getTextArea().getText().trim().length() != 0)
					{
						JOptionPane.showMessageDialog(this, "O todos los campos llenos o todos los campos vacios. ", "DENEGADO", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						Conexion.getInstancia().insertarDatosDetalleLlamada(GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0).toString(), DetalleDeLlamada.getTxtReceptor().getText(), DetalleDeLlamada.getTxtTelefono().getText(), DetalleDeLlamada.getTextArea().getText(), GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 6).toString());
						crearSeguimiento();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Es necesario seleccionar una fecha.", "DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(evento.getSource().equals(btnCancelar))
			{
				dispose();
			}
		} catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(this, "Ha ocurrido un error en la transferencia de datos con la base de datos. '"+e.getMessage()+ "'", "DENEGADO", JOptionPane.ERROR_MESSAGE);

		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
