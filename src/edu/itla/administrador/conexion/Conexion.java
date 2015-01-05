package edu.itla.administrador.conexion;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import edu.itla.administrador.principal.Administrador;
import edu.itla.administrador.principal.Iniciador;
import edu.itla.representante.principal.GestionDeDeuda;
import edu.itla.tabla.ModeloDeTabla;

public class Conexion
{ 
	private Connection conexion;
	private static Conexion intancia;
	private Statement enunciado;
	private ResultSet resultado;
	private String entrada;
	private int contador = 0;
	private SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	
	public static synchronized Conexion getInstancia() {
		if (intancia == null)
		{
			intancia = new Conexion();
		}
		return intancia;
	}
	private Conexion() {
		Configuracion configuracion = new Configuracion();
		configuracion.leerConfiguracion();
		String URL = "jdbc:sqlserver://127.0.0.1:1433;databasename=" + configuracion.obtenerValor("DB_DOMAIN") + ";selectMethod=cursor";
		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String usuario = configuracion.obtenerValor("DB_USER");
        String contrasena = configuracion.obtenerValor("DB_PASW");
        
		try
		{
			Class.forName(driver);
			conexion = DriverManager.getConnection(URL, usuario, contrasena);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "No se ha podido conectar con la base de datos '"+e.getMessage()+"' ","EEROR EN LA CONEXION" , JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} 
	}
	public void actualizarDatos(String sql) throws SQLException
	{
		enunciado = conexion.createStatement();
		enunciado.executeUpdate(sql);
	}
		
	public ResultSet traerDatos(String sql) throws SQLException
	{
		  enunciado = conexion.createStatement();
		  resultado = enunciado.executeQuery(sql);
		  return resultado;
	}
	public void iniciarSesion() throws ParseException
	{
		try {
			String sql = "SELECT id_rol, entrada_representante, clave_representante FROM REPRESENTANTE WHERE entrada_representante COLLATE Latin1_General_CS_AS = '"+Iniciador.getInstancia().getTxtRepresentante().getText()+"' AND clave_representante COLLATE Latin1_General_CS_AS = '"+Iniciador.getInstancia().getTxtContrasena().getText()+"'";

			resultado = Conexion.getInstancia().traerDatos(sql);
			String id_rol = "";		
			while(resultado.next())
			{
				id_rol = resultado.getString("id_rol");
				entrada = resultado.getString("entrada_representante");
			}
			switch(id_rol)
			{
			case "1":
				new Administrador();
				Iniciador.getInstancia().dispose();
				break;
				
			case "2":
				GestionDeDeuda gestion = new GestionDeDeuda();
				gestion.ajustarTamañoColumnas();
				Iniciador.getInstancia().dispose();
				break;
			default:
				JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecto, por favor verifique su informacion nuevamente. " , "DENEGADO", JOptionPane.ERROR_MESSAGE);
				Iniciador.getInstancia().getTxtRepresentante().setText("");
				Iniciador.getInstancia().getTxtContrasena().setText("");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void consultaCliente(JTable tabla) throws Exception
	{
		String sql = "SELECT a.id_cliente 'ID Cliente', a.nombre_cliente + ' ' + a.apellido_cliente 'Nombre Cliente', a.email_cliente'E-mail', a.telefono_cliente '             Telefono', b.nombre_representante + ' ' + b.apellido_representante 'Nombre Representante' FROM CLIENTE a LEFT OUTER JOIN REPRESENTANTE b ON a.id_representante = b.id_representante WHERE estado_cliente = 1";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaClienteParametros(JTable tabla) throws Exception
	{
		String sql = "SELECT id_cliente 'ID Cliente', nombre_cliente + ' '+ apellido_cliente 'Nombre Cliente' FROM CLIENTE WHERE estado_cliente = 1 UNION ALL SELECT 0, 'TODOS LOS CLIENTES' ORDER BY id_cliente";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaPago(JTable tabla) throws Exception
	{
		String sql = "SELECT b.id_cliente 'ID Cliente', a.nombre_cliente + ' ' + a.apellido_cliente 'Nombre Cliente', b.id_deuda 'ID Deuda', b.fecha_pago '        Fecha Pago', b.monto_pago '       Monto Pago'FROM CLIENTE a JOIN PAGO b ON a.id_cliente = b.id_cliente WHERE estado_pago = 1";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaDeuda(JTable tabla) throws Exception
	{
		String sql = "SELECT a.id_cliente 'ID Cliente', b.nombre_cliente + ' ' + b.apellido_cliente 'Nombre Cliente', a.id_deuda 'ID Deuda', a.nivel_morosidad 'Nivel Morosidad', a.monto_deuda '       Monto Deuda', a.cantidad_cuotas '  Cantidad Cuotas', a.monto_adeudado ' Monto Adeudado', a.inicio_deuda '        Inicio Deuda' FROM DEUDA a JOIN CLIENTE b ON b.id_cliente = a.id_cliente WHERE estado_deuda = 1;";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaDetalleDeuda(JTable tabla) throws SQLException
	{
		String sql = "SELECT id_detalle_deuda 'Numero de Cuota',  c.nombre_cliente + ' ' + c.apellido_cliente 'Nombre Cliente', a.id_deuda 'ID Deuda', fecha_cuota '       Fecha Cuota', monto_cuota '       Monto Cuota', monto_pagado '     Monto Pagado' FROM DETALLE_DEUDA a INNER JOIN DEUDA b on a.id_deuda = b.id_deuda INNER JOIN CLIENTE c ON b.id_cliente = c.id_cliente WHERE estado_detalle_deuda = 1 ORDER BY a.id_deuda, id_detalle_deuda ";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public String obtenerFechaParametro() throws SQLException
	{
		String fecha = null;
		String sql = "SELECT CAST(GETDATE() - (SELECT valor_parametro FROM PARAMETRO WHERE id_parametro = 2) AS DATE)";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			fecha = resultado.getString(1);
		}
		return fecha;
	}
	public void obtenerCliente(JTextField telefono, JTextField trabajo, JTextField celular, JTextField numero) throws SQLException
	{
		String datos[] = new String[3];
		String sql = "SELECT trabajo_cliente, telefono_cliente, celular_cliente FROM CLIENTE WHERE id_cliente = "+ GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0) + "";
		resultado = traerDatos(sql);
		
		if(resultado.next())
		{
			if(resultado.getString("telefono_cliente") != null)
			{
				datos[0] = resultado.getString("trabajo_cliente");
				datos[1] = resultado.getString("celular_cliente");
				datos[2] = resultado.getString("telefono_cliente");
			
				trabajo.setText(datos[0]);
				celular.setText(datos[1]);
				telefono.setText(datos[2]);
				numero.setText(datos[2]);
			}
			else if(resultado.getString("celular_cliente") != null)
			{
				datos[0] = resultado.getString("trabajo_cliente");
				datos[1] = resultado.getString("celular_cliente");
			
				trabajo.setText(datos[0]);
				celular.setText(datos[1]);
				numero.setText(datos[1]);
			}
			else
			{
				datos[0] = resultado.getString("trabajo_cliente");
				trabajo.setText(datos[0]);
				numero.setText(resultado.getString("trabajo_cliente"));
			}
		}
	}
	public void obtenerFiador(JTextField nombre, JTextField telefono, JTextField celular, JTextField trabajo) throws SQLException
	{
		String datos[] = new String[5];
		String sql = "SELECT nombre_fiador, apellido_fiador, telefono_fiador, celular_fiador, trabajo_fiador FROM FIADORES WHERE id_deuda = "+ obtenerIdDeuda(GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0).toString(), GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 6).toString()) +"";
		resultado = traerDatos(sql);
		
		if(resultado.next())
		{
			datos[0] = resultado.getString("nombre_fiador");
			datos[1] = resultado.getString("apellido_fiador");
			datos[2] = resultado.getString("telefono_fiador");
			datos[3] = resultado.getString("celular_fiador");
			datos[4] = resultado.getString("trabajo_fiador");
		
			nombre.setText(datos[0] + " " + datos[1]);
			telefono.setText(datos[2]);
			celular.setText(datos[3]);
			trabajo.setText(datos[4]);
		}
	}
	public String obtenerFechaDia() throws SQLException
	{
		String fecha = null;
		String sql = "SELECT CAST(GETDATE() AS DATE)";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			fecha = resultado.getString(1);
		}
		return fecha;
	}
	public String obtenerFechaInicial() throws SQLException
	{
		String fecha = null;
		String sql = "SELECT CAST(Inicial AS DATE) 'Fecha Inicial' FROM (Select  DATEADD(DD,day(fin) * -1, fin) + 1 Inicial, fin FROM (SELECT CAST(CAST(GETDATE() AS DATE) AS DATETIME) fin) a) a";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			fecha = resultado.getString(1);
		}
		return fecha;
	}
	
	public String obtenerFechaFinal() throws SQLException
	{
		String fecha = null;
		String sql = "SELECT CAST(fin AS DATE) 'Fecha Inicial' FROM (Select  DATEADD(DD,day(fin) * -1, fin) + 1 Inicial, fin FROM (SELECT CAST(CAST(GETDATE() AS DATE) AS DATETIME) fin) a) a";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			fecha = resultado.getString(1);
		}
		return fecha;
	}
	public String obtenerTipoRepresentante(String id) throws SQLException
	{
		String tipoRepresentante = "";
		String sql = "SELECT a.nombre_rol FROM ROL a INNER JOIN REPRESENTANTE b ON a.id_rol = b.id_rol WHERE id_representante = '"+ id +"'";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			tipoRepresentante = resultado.getString(1);
		}
		return tipoRepresentante;
	}
	public void consultaGestionEnFechas(JTable tabla, String fecha1, String fecha2) throws SQLException
	{
		if(contador == 0)
		{
			String sql = "SELECT a.id_cliente 'ID Cliente',c.nombre_cliente + ' ' + c.apellido_cliente 'Nombre Cliente', b.monto_deuda '       Monto Deuda', b.cantidad_cuotas 'Cantidad Cuotas',  a.fecha_gestion 'Fecha Gestion' , CASE a.estado_gestion WHEN 'TRUE' THEN 'Completada' ELSE 'Pendiente' END AS Estado, a.id_gestion 'Numero Gestion' FROM GESTION_LLAMADA a JOIN DEUDA b ON a.id_deuda = b.id_deuda JOIN CLIENTE c ON c.id_cliente = a.id_cliente Join REPRESENTANTE d on d.id_representante = a.id_representante WHERE fecha_gestion BETWEEN '"+ obtenerFechaParametro() +"' AND '"+ obtenerFechaDia() +"' AND entrada_representante = '"+entrada+"' ORDER BY a.id_cliente";
			ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
			contador++;
		}
		else
		{
			String sql = "SELECT a.id_cliente 'ID Cliente',c.nombre_cliente + ' ' + c.apellido_cliente 'Nombre Cliente', b.monto_deuda '       Monto Deuda', b.cantidad_cuotas 'Cantidad Cuotas',  a.fecha_gestion 'Fecha Gestion' , CASE a.estado_gestion WHEN 'TRUE' THEN 'Completada' ELSE 'Pendiente' END AS Estado, a.id_gestion 'Numero Gestion' FROM GESTION_LLAMADA a JOIN DEUDA b ON a.id_deuda = b.id_deuda JOIN CLIENTE c ON c.id_cliente = a.id_cliente Join REPRESENTANTE d on d.id_representante = a.id_representante WHERE fecha_gestion BETWEEN '"+fecha1+"' AND '"+fecha2+"' AND entrada_representante = '"+entrada+"' ORDER BY a.id_cliente";
			ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
		}
	}
	public String obtenerIdDeuda(String idCliente, String idGestion) throws SQLException
	{	
		String idDeuda = null;
		String sql = "SELECT a.id_deuda FROM DEUDA a INNER JOIN GESTION_LLAMADA b on a.id_deuda = b.id_deuda WHERE b.id_gestion = '"+idGestion+"' and b.id_cliente = '"+idCliente+"' ";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			idDeuda = resultado.getString(1);
		}
		return idDeuda;
	}
	public void consultaHistorialLlamada(JTable tabla, String idCliente, String idGestion) throws SQLException
	{
		String sql = "SELECT a.receptor_llamada 'Receptor Llamada', a.telefono_cliente 'Telefono Cliente', c.nombre_representante 'Nombre Representante', a.resultado_detalle 'Resultado Detalle' FROM DETALLE_LLAMADA a JOIN GESTION_LLAMADA b ON a.id_gestion = b.id_gestion JOIN REPRESENTANTE c ON c.id_representante = b.id_representante WHERE b.id_gestion = '"+idGestion+"'  AND b.id_deuda = '"+obtenerIdDeuda(idCliente, idGestion)+"'";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaSeguimiento(JTable tabla) throws SQLException
	{
		String sql = "SELECT a.id_cliente 'ID Cliente', a.nombre_cliente + ' ' + a.apellido_cliente 'Nombre Cliente', b.id_seguimiento 'ID Seguimiento', b.fecha_seguimiento ' Fecha Seguimiento' FROM DEUDA c JOIN SEGUIMIENTO b  ON c.id_deuda = b.id_deuda JOIN CLIENTE a ON c.id_cliente = a.id_cliente WHERE estado_seguimiento = 1";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaCheckCompletado(JTable tabla) throws SQLException
	{
		String sql = "SELECT a.id_cliente 'ID Cliente',c.nombre_cliente + ' ' + c.apellido_cliente  'Nombre Cliente', b.monto_deuda 'Monto Deuda', b.cantidad_cuotas 'Cantidad Cuotas',  a.fecha_gestion 'Fecha Gestion' , CASE a.estado_gestion WHEN 'TRUE' THEN 'Completada' ELSE 'Pendiente'  END AS Estado, a.id_gestion 'Numero Gestion' FROM GESTION_LLAMADA a JOIN DEUDA b ON a.id_deuda = b.id_deuda JOIN CLIENTE c  ON c.id_cliente = a.id_cliente JOIN REPRESENTANTE d on d.id_representante = a.id_representante WHERE estado_gestion = 1 AND entrada_representante = '"+entrada+ "' ORDER BY estado_gestion";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaCheckPendientes(JTable tabla) throws SQLException
	{
		String sql = "SELECT a.id_cliente 'ID Cliente',c.nombre_cliente + ' ' + c.apellido_cliente  'Nombre Cliente', b.monto_deuda 'Monto Deuda', b.cantidad_cuotas 'Cantidad Cuotas',  a.fecha_gestion 'Fecha Gestion' , CASE a.estado_gestion WHEN 'TRUE' THEN 'Completada' ELSE 'Pendiente'  END AS Estado, a.id_gestion 'Numero Gestion' FROM GESTION_LLAMADA a JOIN DEUDA b ON a.id_deuda = b.id_deuda JOIN CLIENTE c  ON c.id_cliente = a.id_cliente JOIN REPRESENTANTE d on d.id_representante = a.id_representante WHERE estado_gestion = 0 AND entrada_representante = '"+entrada+"' ORDER BY estado_gestion";
		
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaAmbosCheck(JTable tabla) throws SQLException
	{
		String sql = "SELECT a.id_cliente 'ID Cliente',c.nombre_cliente + ' ' + c.apellido_cliente 'Nombre Cliente', b.monto_deuda 'Monto Deuda', b.cantidad_cuotas 'Cantidad Cuotas',  a.fecha_gestion 'Fecha Gestion' ,  CASE a.estado_gestion WHEN 'TRUE' THEN 'Completada' ELSE 'Pendiente' END AS Estado , a.id_gestion 'Numero Gestion' FROM GESTION_LLAMADA a JOIN DEUDA b ON a.id_deuda = b.id_deuda JOIN CLIENTE c  ON c.id_cliente = a.id_cliente JOIN REPRESENTANTE d on d.id_representante = a.id_representante  WHERE entrada_representante = '"+ entrada +"' ORDER BY estado_gestion";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaAcuerdo(JTable tabla) throws SQLException
	{
		String sql = "SELECT a.id_deuda 'Deuda', a.id_detalle_deuda 'Cuota', a.fecha_acuerdo '                 Fecha Acuerdo', a.monto_acuerdo '                 Monto Acuerdo', c.nombre_cliente + ' ' + c.apellido_cliente 'Nombre Cliente' FROM ACUERDO a INNER JOIN DEUDA b on a.id_deuda = b.id_deuda INNER JOIN CLIENTE c on b.id_cliente = c.id_cliente WHERE estado_acuerdo = 0 and cumpliento_acuerdo = 1 ORDER BY nombre_cliente, fecha_acuerdo";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaRepresentante(JTable tabla) throws SQLException
	{
		String sql = "SELECT id_representante 'ID Representante', nombre_representante + ' ' + apellido_representante 'Nombre Representante', nivel_representante 'Nivel' FROM representante WHERE estado_representante = 1";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void consultaAcuerdoDetalle(JTable tabla, String idDetalle, String idDeuda, DefaultTableModel modelo) throws SQLException
	{
		String sql = "SELECT id_acuerdo 'ID Acuerdo', id_deuda 'ID Deuda',  id_detalle_deuda 'Detalle Deuda', fecha_acuerdo 'Fecha Acuerdo', monto_acuerdo 'Monto Acuerdo' FROM ACUERDO WHERE estado_acuerdo = 0 AND id_detalle_deuda = '"+idDetalle+"' AND id_deuda = '"+idDeuda+"'";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), modelo, tabla);
	}
	public void consultaReasignacion(JTable tabla) throws SQLException
	{
		String sql = "SELECT a.id_representante 'ID Representante', a.nombre_representante + ' ' + a.apellido_representante 'Nombre Representante',  b.id_cliente 'ID Cliente',  b.nombre_cliente + ' ' + b.apellido_cliente 'Nombre Cliente',  c.monto_deuda 'Monto Deuda',  d.id_gestion 'ID Gestion',  d.fecha_gestion 'Fecha Gestion',  CASE  WHEN d.estado_gestion = 1 THEN 'Completada' ELSE 'Pendiente'  END AS Estado FROM GESTION_LLAMADA d  Inner JOIN CLIENTE b ON d.id_cliente = b.id_cliente  Inner JOIN DEUDA c ON d.id_deuda = c.id_deuda  Inner JOIN REPRESENTANTE a ON d.id_representante = a.id_representante WHERE d.estado_gestion = 0";
		ModeloDeTabla.rellenarTabla(traerDatos(sql), tabla);
	}
	public void actualizarSeguimientoGestion(String idCliente, String idGestion) throws SQLException
	{
		String sql = "UPDATE GESTION_LLAMADA SET realizar_seguimiento = 1 WHERE id_gestion = '"+idGestion+"'";
		actualizarDatos(sql);
	}
	public void actualizarReasignacion(String id, String condicion) throws SQLException
	{
		String sql = "UPDATE GESTION_LLAMADA SET id_representante = '"+id+"' WHERE id_gestion = '"+condicion+"'";
		actualizarDatos(sql);
	}
	public boolean datosAcuerdo(String idDeuda, double validacionMonto, JTable tabla, boolean condicion) throws SQLException
	{
		double montoDetalle = montoCuotaPendiente(idDeuda);
		
		if(validacionMonto == montoDetalle)
		{
			int contador = 0;
			while(contador != tabla.getRowCount())
			{
				if(condicion == true)
				{
					insertarAcuerdo(tabla, idDeuda, contador);
					contador++;
				}
				if(condicion == false)
				{
					actualizarAcuerdo(tabla, contador);
					contador++;
				}
			}
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "El nuevo monto debe ser igual al monto de la cuota del mes: "+montoDetalle+".","DENEGADO", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	public void insertarAcuerdo(JTable tabla, String idDeuda, int contador) throws SQLException
	{
		String sql = "INSERT INTO ACUERDO(id_detalle_deuda, fecha_acuerdo, monto_acuerdo, id_deuda) VALUES ('"+tabla.getValueAt(contador, 0)+"', '"+tabla.getValueAt(contador, 1)+"', '"+ tabla.getValueAt(contador, 2) +"','"+idDeuda+"')";
		actualizarDatos(sql);
	}
	public void insertarAcuerdoDetalle(JTable tabla, String idDeuda) throws SQLException
	{
		String sql = "INSERT INTO ACUERDO(id_detalle_deuda, fecha_acuerdo, monto_acuerdo, id_deuda) VALUES ('"+tabla.getValueAt(0, 2)+"', '"+tabla.getValueAt(0, 3)+"', '"+ tabla.getValueAt(0, 4) +"','"+idDeuda+"')";
		actualizarDatos(sql);
	}
	public void actualizarAcuerdo(JTable tabla, int contador) throws SQLException
	{
		String sql = "If Not Exists (select * from ACUERDO where id_acuerdo = '"+tabla.getValueAt(contador, 0)+"') insert into ACUERDO (id_detalle_deuda, fecha_acuerdo,id_deuda, monto_acuerdo) values ('"+tabla.getValueAt(contador, 2)+"','"+tabla.getValueAt(contador, 3)+"', '"+tabla.getValueAt(contador,1)+"','"+tabla.getValueAt(contador, 4)+"'); ELSE Update ACUERDO set monto_acuerdo = '"+tabla.getValueAt(contador, 4)+"', fecha_acuerdo = '"+tabla.getValueAt(contador, 3)+"' where id_detalle_deuda = '"+tabla.getValueAt(contador, 2)+"' and id_deuda = '"+tabla.getValueAt(contador, 1)+"' and fecha_acuerdo = '"+tabla.getValueAt(contador, 3)+"'";
		actualizarDatos(sql);
	}
	public void actualizarRepresentante(String nombre, String apellido, String nivel, String clave, String entrada, int tipoRepresentante,int sexo, int id) throws SQLException
	{
		String sql = "UPDATE REPRESENTANTE SET nombre_representante = '"+nombre+"', apellido_representante = '"+apellido+"', nivel_representante = '"+nivel+"',clave_representante = '"+clave+"', entrada_representante = '"+entrada+"', id_rol = '"+tipoRepresentante+"', sexo_representante = '"+sexo+"' WHERE id_representante = '"+id+"' ";
		actualizarDatos(sql);
	}
	public void actualizarSeguimiento(String fecha, String id) throws SQLException
	{
		String sql = "UPDATE seguimiento SET fecha_seguimiento = '"+fecha+"' WHERE id_seguimiento = '"+id+"'";
		actualizarDatos(sql);
	}
	public void insertarSeguimiento(String idCliente, String fecha, String idGestion) throws SQLException
	{
		String sql = "INSERT INTO SEGUIMIENTO(id_deuda, fecha_seguimiento) VALUES ('"+ obtenerIdDeuda(idCliente, idGestion)+"', '"+fecha+"');";
		actualizarDatos(sql);
	}
	public void inhabilitarAcuerdo(String idDeuda, String idDetalle) throws SQLException
	{
		String sql = "UPDATE ACUERDO SET estado_acuerdo = 1, cumpliento_acuerdo = 0 WHERE id_deuda = '"+idDeuda+"' AND id_detalle_deuda = '"+idDetalle+"'";
		actualizarDatos(sql);
	}
	public void inhabilitarCliente(String id) throws SQLException
	{
		String sql = "UPDATE CLIENTE SET estado_cliente = 0 WHERE id_cliente = '"+ id +"' ";
		actualizarDatos(sql);
	}
	public void inhabilitarRepresentante(String idRepresentante) throws SQLException
	{
		String sql = "UPDATE representante SET estado_representante = 0 WHERE id_representante = '"+ idRepresentante +"'";
		actualizarDatos(sql);
	}
	public void inhabilitarSeguimiento(String id) throws SQLException
	{
		String sql = "UPDATE seguimiento SET estado_seguimiento = 0 WHERE id_seguimiento = '"+ id +"'";
		actualizarDatos(sql);
	}
	public void llenarCombo(JComboBox<String> combo) throws SQLException
	{
		String sql = "SELECT id_representante, nombre_representante, apellido_representante FROM REPRESENTANTE WHERE id_rol = 2 AND estado_representante = 1";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			String [] datos = new String[4];
			datos[0] = resultado.getString("id_representante");
			datos[1] = resultado.getString("nombre_representante");
			datos[2] = resultado.getString("apellido_representante");
			datos[3] = datos[0]+ " - " + datos[1] + " " + datos[2];
				
			combo.addItem(datos[3]);
		}
	}
	public void llenarComboReasignacion(JComboBox<String> combo, String id) throws SQLException
	{
		String sql = "SELECT id_representante, nombre_representante, apellido_representante FROM REPRESENTANTE WHERE id_rol = 2 AND id_representante != '"+ id +"' AND estado_representante = 1";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			String [] datos = new String[4];
			datos[0] = resultado.getString("id_representante");
			datos[1] = resultado.getString("nombre_representante");
			datos[2] = resultado.getString("apellido_representante");
			datos[3] = datos[0]+ " - " + datos[1] + " " + datos[2];
				
			combo.addItem(datos[3]);
		}
	}
	public void asignarRepresentante(String idRep, String idCliente) throws SQLException
	{
		String sql = "UPDATE CLIENTE SET id_representante = '"+ idRep +"' WHERE id_cliente = '"+ idCliente+"'";
		actualizarDatos(sql);
	}

	public void agregarRepresentante(String nombre, String apellido, String nivel, String clave, String entrada, int id, int sexo) throws SQLException
	{
		String sql = "INSERT INTO REPRESENTANTE(nombre_representante, apellido_representante, nivel_representante, clave_representante, entrada_representante, id_rol, sexo_representante) VALUES('"+nombre+"','"+apellido+"','"+nivel+"','"+clave+"','"+entrada+"', '"+id+"' , '"+sexo+"')";
		actualizarDatos(sql);
	}
	public String obtenerSexo(String id) throws SQLException
	{
		String sql = "SELECT sexo_representante FROM REPRESENTANTE WHERE id_representante = '"+ id +"'";
		String sexo = null;
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			sexo = resultado.getString(1);
		}
		return sexo;
	}
	public String traerNombreRep(String id) throws SQLException
	{
		String sql = "SELECT entrada_representante FROM REPRESENTANTE WHERE id_representante = '"+ id+"'";
		String nombre = null;
		resultado = traerDatos(sql);
		
		while(resultado.next())
		{
			nombre = resultado.getString("entrada_representante");
		}
		return nombre;
	}
	public String traerClaveRep(String id) throws SQLException
	{
		String sql = "SELECT clave_representante FROM REPRESENTANTE WHERE id_representante = '"+ id +"'";
		String clave = null;
		resultado = traerDatos(sql);
		
		while(resultado.next())
		{
			clave = resultado.getString(1);
		}
		return clave;
	}
	public double montoCuotaPendiente(String idDeuda) throws SQLException
	{
		String sql = "SELECT TOP 1 Cuota_pendiente FROM detalle_deuda WHERE Cuota_pendiente != 0 AND id_deuda = '"+idDeuda+"'";
		resultado = traerDatos(sql);
		double monto = 0.0;
		while(resultado.next())
		{
			monto = resultado.getInt(1);
		}
		return monto;
	}
	public void insertarDatosDetalleLlamada(String gestion, String receptorCliente, String telefonoCliente, String resultadoDetalle, String idGestion) throws SQLException
	{
		String sql = "INSERT INTO DETALLE_LLAMADA (id_gestion, receptor_llamada, telefono_cliente, resultado_detalle) VALUES ('"+ idGestion +"', '"+ receptorCliente +"', '"+ telefonoCliente +"', '"+resultadoDetalle+"')";
		actualizarDatos(sql);
	}
	public void asignarRepresentanteNulo(String idCliente) throws SQLException
	{
		String sql = "UPDATE CLIENTE SET id_representante = null WHERE id_cliente = '"+idCliente+"'";
		actualizarDatos(sql);
	}
	public String obtenerIdDetalleDeuda(String idCliente) throws SQLException
	{
		String idDeuda = null;
		String sql = "SELECT MIN(id_detalle_deuda) FROM detalle_deuda a INNER JOIN deuda b ON a.id_deuda = b.id_deuda WHERE id_cliente = '"+idCliente+"' AND cuota_pendiente != 0 ";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			idDeuda = resultado.getString(1);
		}
		return idDeuda;
	}
	public void borrarAcuerdoModificacion(String idAcuerdo) throws SQLException
	{
		String sql = "DELETE FROM ACUERDO WHERE id_acuerdo = '"+idAcuerdo+"'";
		actualizarDatos(sql);
	}
	public String obtenerIdAcuerdo(int contador) throws SQLException
	{
		String id_acuerdo = null;
		String sql = "SELECT MAX(id_acuerdo) + 1 + '"+contador+"' FROM acuerdo";
		resultado = traerDatos(sql);
		while(resultado.next())
		{
			id_acuerdo = resultado.getString(1);
		}
		return id_acuerdo;
	}
	public void insertarImagen(String consulta, PreparedStatement preparedStatement, FileInputStream flujoArchivo, int tamañoArchivo, String id) throws SQLException{
		consulta = "INSERT INTO CONTRATO_DEUDA(id_deuda, imagen) VALUES (?, ?)";					
		preparedStatement = conexion.prepareStatement(consulta);
		preparedStatement.setInt(1 ,Integer.parseInt(id));
		preparedStatement.setBinaryStream(2, flujoArchivo, tamañoArchivo);
		preparedStatement.executeUpdate();
		JOptionPane.showMessageDialog(null, "El contrato se ha agregado correctamente","CORRECTO" , JOptionPane.INFORMATION_MESSAGE);
	}
	public void insertarPDF(String nombreArchivo, JTable tabla)
	{
		int tamañoArchivo;
		String id = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 2));
		String consulta = "";
        PreparedStatement preparedStatement = null;   
        try {
			File archivo = new File(nombreArchivo);
			FileInputStream flujoArchivo = new FileInputStream(archivo);
			tamañoArchivo = (int) archivo.length();
			consulta = "SELECT imagen from CONTRATO_DEUDA WHERE id_deuda = "+ id +"";
			ResultSet resultado = traerDatos(consulta);
			if(resultado.next())
			{
				int respuesta = JOptionPane.showConfirmDialog(null, "Este cliente ya tiene un contrato, ¿está de acuerdo con sobre-escribirlo?","CONTRATO" ,JOptionPane.YES_NO_OPTION);
				if(respuesta == JOptionPane.YES_OPTION)
				{
					consulta = "DELETE FROM CONTRATO_DEUDA WHERE id_deuda = "+ id +"";
					actualizarDatos(consulta);
					insertarImagen(consulta, preparedStatement, flujoArchivo, tamañoArchivo, id);
				}
			}
			else
			{
				insertarImagen(consulta, preparedStatement, flujoArchivo, tamañoArchivo, id);
			}
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "El archivo especificado no se encuentra ", "NO SE ENCUENTRA EL ARCHIVO", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException ex){
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error con la base de datos " + ex.getMessage( ) +"' ","CONEXION BASE DE DATOS" ,JOptionPane.ERROR_MESSAGE);
		}
	}
	public void obtenerPDF(String deuda) 
	{
        byte[] fileBytes;
        try {
            String consulta = "SELECT imagen FROM CONTRATO_DEUDA WHERE id_deuda = '"+ deuda +"'";
            resultado = traerDatos(consulta);
            String URL = String.valueOf(getClass().getResource("Contratos/ContratoDeuda.pdf"));
            
            if (resultado.next())
            {
                fileBytes = resultado.getBytes(1);
                OutputStream targetFile = new FileOutputStream(URL);
                targetFile.write(fileBytes);
                targetFile.close();
                if (Desktop.isDesktopSupported())
                {
                	File myFile = new File(URL);
                    Desktop.getDesktop().open(myFile);
                    myFile.deleteOnExit();
                }
            }
            else
            {
				JOptionPane.showMessageDialog(null, "Este cliente no tiene contrato asignado, una vez agregado intente de nuevo.","CONTRATO", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void crearReporte(String nombreReporte, String tituloReporte) throws JRException 
	{
    	InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/recursos/"+nombreReporte+"");
    	JasperDesign jasperDesign = JRXmlLoader.load(resourceAsStream);
    	JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
    	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conexion);
    	JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
    	jasperViewer.setTitle(tituloReporte);
    	jasperViewer.setVisible(true);
	}
	public void crearReporte(String nombreReporte, String tituloReporte, Date fechaInicial, Date fechaFinal) throws JRException, ParseException
	{
		Map<String, Object> parametros = new HashMap<String, Object>();
    	parametros.put("Fecha Inicial",  formatoFecha.parse(formatoFecha.format(fechaInicial)));
    	parametros.put("Fecha Final", formatoFecha.parse(formatoFecha.format(fechaFinal)));
    	InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/recursos/"+nombreReporte+"");
    	JasperDesign jasperDesign = JRXmlLoader.load(resourceAsStream);
    	JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
    	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);
    	JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
    	jasperViewer.setTitle(tituloReporte);
    	jasperViewer.setVisible(true);
	}
}
	