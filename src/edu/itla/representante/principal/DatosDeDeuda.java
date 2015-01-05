package edu.itla.representante.principal;

import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class DatosDeDeuda extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static JTabbedPane pestaña;

	public DatosDeDeuda(boolean condicion) throws SQLException 
	{
		super("Detalle de Llamada");
		pestaña = new JTabbedPane();
		pestaña.addTab("Historial de Deuda", new HistorialDeDeuda());
		pestaña.addTab("Detalle de Llamada", new DetalleDeLlamada());		
		add(pestaña);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800,500);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		
		if(condicion == true)
		{
			pestaña.setEnabledAt(1, false);
		}
	}

}
