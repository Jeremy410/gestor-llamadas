package edu.itla.administrador.reportes;

import java.awt.Container;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;

import com.toedter.calendar.JDateChooser;

import edu.itla.administrador.conexion.Conexion;

public class ReporteParametros
{
	public static JDateChooser segundaFecha;
	public static JDateChooser primeraFecha;
	private SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	
	public ReporteParametros(Container contenedor) throws ParseException, SQLException
	{
		JLabel lblFechaInicial = new JLabel("Fecha Inicial: ");
		contenedor.add(lblFechaInicial);

		primeraFecha = new JDateChooser("yyyy-MM-dd", "####-##-##", '_');
		contenedor.add(primeraFecha);
		
		JLabel lblFechaFinal = new JLabel("Fecha Final: ");
		contenedor.add(lblFechaFinal);
		
		segundaFecha = new JDateChooser("yyyy-MM-dd", "####-##-##", '_');
		contenedor.add(segundaFecha);
		
		primeraFecha.setDate(formatoFecha.parse(Conexion.getInstancia().obtenerFechaInicial()));
		segundaFecha.setDate(formatoFecha.parse(Conexion.getInstancia().obtenerFechaFinal()));
	}
	public static JDateChooser getSegundaFecha() {
		return segundaFecha;
	}

	public static JDateChooser getPrimeraFecha() {
		return primeraFecha;
	}

}
