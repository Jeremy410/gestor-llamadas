package edu.itla.representante.principal;

import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class DatosDeDeuda extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static JTabbedPane pesta�a;

	public DatosDeDeuda(boolean condicion) throws SQLException 
	{
		super("Detalle de Llamada");
		pesta�a = new JTabbedPane();
		pesta�a.addTab("Historial de Deuda", new HistorialDeDeuda());
		pesta�a.addTab("Detalle de Llamada", new DetalleDeLlamada());		
		add(pesta�a);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800,500);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		
		if(condicion == true)
		{
			pesta�a.setEnabledAt(1, false);
		}
	}

}
