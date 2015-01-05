package edu.itla.administrador.acuerdo;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import edu.itla.administrador.conexion.Conexion;
import edu.itla.administrador.principal.Buscador;
import edu.itla.administrador.principal.Mantenimiento;
import edu.itla.tabla.RenderizadorTablaAcuerdo;

public class MantenimientoDeAcuerdo extends Mantenimiento implements  ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static JTable tablaAcuerdo = null;
	private JButton btnModificar = null;
	private JButton btnInhabilitar = null;
	private JPanel panelTemporal = null;
	private JPanel panelNorte;
	private JScrollPane scrollPane = null;
	private JTextField txtBuscar;
	
	public MantenimientoDeAcuerdo() throws SQLException 
	{
		setSize(719,395);
		setLayout(new BorderLayout());
		tablaAcuerdo = new JTable();
		scrollPane = new JScrollPane(tablaAcuerdo);
		add(scrollPane, BorderLayout.CENTER);
		Conexion.getInstancia().consultaAcuerdo(tablaAcuerdo);
		panelTemporal = new JPanel();
		add(panelTemporal, BorderLayout.SOUTH);
		tablaAcuerdo.setDefaultRenderer(Object.class, new RenderizadorTablaAcuerdo(tablaAcuerdo));
		
		btnModificar = new JButton("Modificar Acuerdo");
		btnModificar.setIcon(getImagen("modify.jpg"));
		panelTemporal.add(btnModificar);
		
		btnInhabilitar = new JButton("Inhabilitar Acuerdo");
		btnInhabilitar.setIcon(getImagen("RemoveSmall.png"));
		panelTemporal.add(btnInhabilitar);
		btnModificar.addActionListener(this);
		btnInhabilitar.addActionListener(this);
		
		panelNorte = new JPanel();
		add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BorderLayout(0, 0));
		
		JPanel panelBuscador = new JPanel();
		panelNorte.add(panelBuscador, BorderLayout.EAST);
		
		JLabel lblBuscar = new JLabel("Buscador:   ");
		panelBuscador.add(lblBuscar);
		
		txtBuscar = new JTextField();
		panelBuscador.add(txtBuscar);
		txtBuscar.setColumns(10);
		
		JLabel lblTitulo = new JLabel("                                     Mantenimiento de Acuerdos");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		panelNorte.add(lblTitulo, BorderLayout.CENTER);
		setVisible(true);
		txtBuscar.addKeyListener(new KeyAdapter() 
		{

			@Override
			public void keyReleased(KeyEvent e) {
				super.keyTyped(e);
				crearBuscador(Buscador.getInstancia());
			}
		});
	}
	@Override
	public void ajustarTamañoColumnas()
	{
		JViewport viewPort = (JViewport)tablaAcuerdo.getParent();
        int ancho = viewPort.getWidth();
        int anchoColumna = 0; 
        TableColumnModel modeloColumna = tablaAcuerdo.getColumnModel(); 
        TableColumn columnaTabla;
        
        for (int i = 0; i < tablaAcuerdo.getColumnCount(); i++) 
        { 
            columnaTabla = modeloColumna.getColumn(i); 
            switch(i)
            { 
                case 0: anchoColumna = (5*ancho)/100; 
                        break; 
                case 1: anchoColumna = (5*ancho)/100; 
                        break; 
                case 2: anchoColumna = (5*ancho)/100; 
                        break;
                case 3: anchoColumna = (5*ancho)/100; 
                		break; 
                case 4: anchoColumna = (30*ancho)/100; 
                		break; 
                		
            }                      
            columnaTabla.setPreferredWidth(anchoColumna);            
        } 
	}
	public static JTable getTablaAcuerdo() 
	{
		return tablaAcuerdo;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		try 
		{
			if(e.getSource().equals(btnModificar))
			{
				if(tablaAcuerdo.getSelectedRow() >= 0)
				{
					new VentanaDeModificacion();				
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Es necesario seleccionar un campo." , "DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(e.getSource().equals(btnInhabilitar))
			{
				if(tablaAcuerdo.getSelectedRow() >= 0)
				{
					int respuesta = JOptionPane.showConfirmDialog(this, "Al inhabilitar un acuerdo de pago se suprimiran todos los acuerdo de la misma deuda. ¿Seguro que desea proceder?", "ATENCION", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(respuesta == JOptionPane.YES_OPTION)
					{
						String idDeuda = String.valueOf(tablaAcuerdo.getValueAt(tablaAcuerdo.getSelectedRow(), 0));
						String idDetalle = String.valueOf(tablaAcuerdo.getValueAt(tablaAcuerdo.getSelectedRow(), 1));
						Conexion.getInstancia().inhabilitarAcuerdo(idDeuda, idDetalle);
						Conexion.getInstancia().consultaAcuerdo(tablaAcuerdo);
						JOptionPane.showMessageDialog(this, "Se ha inhabilitado el acuerdo y sus cuotas correctamente." , "COMPLETADO", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Es necesario seleccionar un campo." , "DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
	}
	@Override
	public void crearBuscador(Buscador buscador) {
		buscador.buscar(tablaAcuerdo, txtBuscar.getText());
	}
	@Override
	public ImageIcon getImagen(String nombreImagen) {
		URL pathImagen = MantenimientoDeAcuerdo.class.getClassLoader().getResource("com/recursos/"+nombreImagen+"");
		ImageIcon icono = new ImageIcon(pathImagen);
		return icono;
	}
}
