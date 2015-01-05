package edu.itla.administrador.reportes;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import edu.itla.administrador.conexion.Conexion;

public class Reporte extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboReportes = null;
	private JButton btnCancelar = null;
	private JButton btnGenerar = null;
	private static JPanel panelParametros = null;
	private static Reporte instancia = null;

	public static Reporte getInstancia()
	{
		if(instancia == null)
		{
			instancia = new Reporte();
		}
		return instancia;
	}
	public Reporte() 
	{
		setResizable(false);
		setTitle("Generaci\u00F3n de Reportes");
		getContentPane().setLayout(null);
		
		JPanel panelReportes = new JPanel();
		panelReportes.setBorder(new TitledBorder(null, "Reportes:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelReportes.setBounds(12, 11, 400, 63);
		getContentPane().add(panelReportes);
		panelReportes.setLayout(null);
		
		JLabel lblReportesDisponibles = new JLabel("Reportes disponibles: ");
		lblReportesDisponibles.setBounds(30, 29, 135, 14);
		panelReportes.add(lblReportesDisponibles);
		
		comboReportes = new JComboBox<String>();
		comboReportes.addItem("Ninguno");
		comboReportes.addItem("Reporte de Deudas");
		comboReportes.addItem("Reporte Personas no Contactadas");
		comboReportes.addItem("Reporte Actividades Representantes");
		comboReportes.addItem("Reporte Deficiencia Llamadas");
		//comboReportes.addItem("Reporte de Pagos");
		comboReportes.setBounds(177, 26, 213, 20);
		panelReportes.add(comboReportes);
		
		panelParametros = new JPanel();
		panelParametros.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Parametros:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelParametros.setBounds(12, 85, 496, 63);
		getContentPane().add(panelParametros);
		panelParametros.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(399, 159, 97, 23);
		getContentPane().add(btnCancelar);
		
		btnGenerar = new JButton("Generar Reporte");
		btnGenerar.setBounds(239, 159, 148, 23);
		getContentPane().add(btnGenerar);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(529,226);
		setVisible(true);
		setLocationRelativeTo(null);
		
		comboReportes.addActionListener(this);
		btnCancelar.addActionListener(this);
		btnGenerar.addActionListener(this);
	}
	public void limpiarReporte(JPanel panel) throws ParseException, SQLException
	{
		panelParametros.removeAll();
		if(panel != null)
		{
			new ReporteParametros(panel);
		}
		repaint();
		setVisible(true);
	}
	public void actionPerformed(ActionEvent evento) 
	{
		try {
			if(evento.getSource().equals(comboReportes))
			{
				switch (comboReportes.getSelectedItem().toString()) 
				{
				case "Ninguno":
					limpiarReporte(null);
					break;
				case "Reporte de Deudas":
					limpiarReporte(panelParametros);
					break;
				case "Reporte Personas no Contactadas":
					limpiarReporte(panelParametros);
					break;
				case "Reporte Actividades Representantes":
					limpiarReporte(null);
					break;
				case "Reporte Deficiencia Llamadas":
					limpiarReporte(panelParametros);
					break;
				default:
					JOptionPane.showMessageDialog(this, "Esta no es una de las opciones disponibles.", "Reporte no disponible", JOptionPane.ERROR_MESSAGE);
					break;
				}
				/*if(comboReportes.getSelectedItem().equals("Reporte de Pagos"))
				{
					panelParametros.removeAll();
					new ReporteParametros(panelParametros);
					new ReporteDePagos(panelParametros, false);
					this.repaint();
					setVisible(true);
				}*/
			}
			if(evento.getSource().equals(btnGenerar))
			{
				if(comboReportes.getSelectedItem().equals("Ninguno"))
				{
					JOptionPane.showMessageDialog(this, "Debe seleccionar un reporte", "SELECCION REPORTES", JOptionPane.WARNING_MESSAGE);
				}
				if(comboReportes.getSelectedItem().equals("Reporte de Deudas"))
				{
					if(ReporteParametros.getPrimeraFecha().getDate()!= null && ReporteParametros.getSegundaFecha().getDate() != null)
					{
						Conexion.getInstancia().crearReporte("ReporteDeuda.jrxml", "Reporte de Deudas", ReporteParametros.getPrimeraFecha().getDate(), ReporteParametros.getSegundaFecha().getDate());
					}
					else
					{
						JOptionPane.showMessageDialog(this, "Los parametros no pueden ser nulos", "Seleccion de parametros", JOptionPane.WARNING_MESSAGE);
					}
				}
				if(comboReportes.getSelectedItem().equals("Reporte Personas no Contactadas"))
				{
					if(ReporteParametros.getPrimeraFecha().getDate()!= null && ReporteParametros.getSegundaFecha().getDate() != null)
					{
						Conexion.getInstancia().crearReporte("ReporteNoContactados.jrxml", "Reporte de Personas no Contactadas", ReporteParametros.getPrimeraFecha().getDate(), ReporteParametros.getSegundaFecha().getDate());	
					}
					else
					{
						JOptionPane.showMessageDialog(this, "Los parametros no pueden ser nulos", "Seleccion de parametros", JOptionPane.WARNING_MESSAGE);
					}
				}
				if(comboReportes.getSelectedItem().equals("Reporte Actividades Representantes"))
				{
					Conexion.getInstancia().crearReporte("ReporteActividadRepresentante.jrxml", "Reportes de Actividades de los Representantes");
				}
				if(comboReportes.getSelectedItem().equals("Reporte Deficiencia Llamadas"))
				{
					if(ReporteParametros.getPrimeraFecha().getDate()!= null && ReporteParametros.getSegundaFecha().getDate() != null)
					{
						Conexion.getInstancia().crearReporte("ReporteDeficienciaLlamadas.jrxml", "Reporte de deficiencia", ReporteParametros.getPrimeraFecha().getDate(), ReporteParametros.getSegundaFecha().getDate());
					}
					else
					{
						JOptionPane.showMessageDialog(this, "Los parametros no pueden estar nulos", "Seleccion de parametros", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		if(evento.getSource().equals(btnCancelar))
		{
			dispose();
		}
	}
}
