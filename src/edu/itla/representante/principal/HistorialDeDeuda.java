package edu.itla.representante.principal;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import edu.itla.administrador.conexion.Conexion;
import edu.itla.administrador.principal.Buscador;
import edu.itla.tabla.RenderizadorTablaHistorial;

public class HistorialDeDeuda extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTable tablaHistorial = null;
	private JScrollPane scrollPane = null;
	private JTextField txtBuscador;

	public HistorialDeDeuda() throws SQLException 
	{
		setLayout(new BorderLayout());
		tablaHistorial = new JTable();
		scrollPane = new JScrollPane(tablaHistorial);
		add(scrollPane, BorderLayout.CENTER);
		tablaHistorial.setDefaultRenderer(Object.class, new RenderizadorTablaHistorial(tablaHistorial));
		
		Conexion.getInstancia().consultaHistorialLlamada(tablaHistorial, GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 0).toString(), GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 6).toString());
		JPanel panelTemporal = new JPanel();
		add(panelTemporal, BorderLayout.NORTH);
		panelTemporal.setLayout(new BorderLayout(0, 0));
		
		JPanel panelLocalidad = new JPanel();
		panelTemporal.add(panelLocalidad, BorderLayout.CENTER);
		
		JLabel lblNombre = new JLabel("Nombre Cliente:");
		panelLocalidad.add(lblNombre);
		
		JLabel textoNombre = new JLabel();
		panelLocalidad.add(textoNombre);
		textoNombre.setText(GestionDeDeuda.getTablaGestion().getValueAt(GestionDeDeuda.getTablaGestion().getSelectedRow(), 1).toString());
		
		JPanel panelBuscador = new JPanel();
		panelTemporal.add(panelBuscador, BorderLayout.EAST);
		
		JLabel lblBuscador = new JLabel("Buscador:   ");
		panelBuscador.add(lblBuscador);
		
		txtBuscador = new JTextField();
		txtBuscador.addKeyListener(new KeyAdapter() 
		{
			public void keyReleased(KeyEvent e) {
				crearBuscador(Buscador.getInstancia());				
			}
		});
		panelBuscador.add(txtBuscador);
		txtBuscador.setColumns(10);
		tablaHistorial.addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{
				if(e.getClickCount() == 2)
				{
					new PopUpFrame(tablaHistorial);
				}
			}			
		});	
	}
	public void crearBuscador(Buscador buscador)
	{
		buscador.buscar(tablaHistorial, txtBuscador.getText());
	}
}
