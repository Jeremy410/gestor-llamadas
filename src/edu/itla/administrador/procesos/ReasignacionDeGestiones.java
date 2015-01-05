package edu.itla.administrador.procesos;

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
import edu.itla.tabla.RenderizadorTablaReasignacion;

public class ReasignacionDeGestiones extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static JTable tablaGestion = null;
	private JButton btnReasignar = null;
	private JScrollPane scrollPane = null;
	private JPanel panelTemporal = null;
	private JPanel panelLocalidad;
	private JLabel lblTitulo = null;
	private JTextField txtBuscador;
	
	public ReasignacionDeGestiones() throws SQLException
	{
		setLayout(new BorderLayout(0, 0));
		tablaGestion = new JTable();
		scrollPane = new JScrollPane(tablaGestion);
		add(scrollPane, BorderLayout.CENTER);
		panelTemporal = new JPanel();
		add(panelTemporal, BorderLayout.SOUTH);
		tablaGestion.setDefaultRenderer(Object.class, new RenderizadorTablaReasignacion(tablaGestion));
		Conexion.getInstancia().consultaReasignacion(tablaGestion);
		
		btnReasignar = new JButton("Reasignar Gestion");
		btnReasignar.setIcon(getImagen("modify.jpg"));
		panelTemporal.add(btnReasignar);
		
		panelLocalidad = new JPanel();
		add(panelLocalidad, BorderLayout.NORTH);
		panelLocalidad.setLayout(new BorderLayout(0, 0));
		lblTitulo = new JLabel("                              REASIGNACION DE GESTIONES");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelLocalidad.add(lblTitulo);
		
		JPanel panelBuscador = new JPanel();
		panelLocalidad.add(panelBuscador, BorderLayout.EAST);
		
		JLabel lblBuscador = new JLabel("Buscador:    ");
		panelBuscador.add(lblBuscador);
		
		txtBuscador = new JTextField();
		txtBuscador.addKeyListener(new KeyAdapter() 
		{		
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				crearBuscador(Buscador.getInstancia());
			}
		});
		panelBuscador.add(txtBuscador);
		txtBuscador.setColumns(10);
		
		this.setSize(719,395);
		btnReasignar.addActionListener(this);
		setVisible(true);
	}
	public void crearBuscador(Buscador buscador) {
		buscador.buscar(tablaGestion, txtBuscador.getText());
	}
	public static JTable getTablaGestion() 
	{
		return tablaGestion;
	}
	public ImageIcon getImagen(String nombreImagen){
		URL pathImagen = ReasignacionDeGestiones.class.getClassLoader().getResource("com/recursos/"+nombreImagen+"");
		ImageIcon icono = new ImageIcon(pathImagen);
		return icono;
	}
	public static void obtenerDatos(JTextField representante)
	{
		int fila = tablaGestion.getSelectedRow();
		String idRepresentante = tablaGestion.getValueAt(fila, 0).toString();
		String nombre = tablaGestion.getValueAt(fila, 1).toString();
		representante.setText(idRepresentante + " - " + nombre);
	}
	public void ajustarTamañoColumnas(){
		JViewport viewPort = (JViewport)tablaGestion.getParent();
        int ancho = viewPort.getWidth();
        int anchoColumna = 0; 
        TableColumnModel modeloColumna = tablaGestion.getColumnModel(); 
        TableColumn columnaTabla;
        
        for (int i = 0; i < tablaGestion.getColumnCount(); i++) 
        { 
            columnaTabla = modeloColumna.getColumn(i); 
            switch(i)
            { 
                case 0: anchoColumna = (3*ancho)/100; 
                        break; 
                case 1: anchoColumna = (10*ancho)/100; 
                        break; 
                case 2: anchoColumna = (1*ancho)/100; 
                        break;
                case 3: anchoColumna = (15*ancho)/100; 
                		break; 
                case 4: anchoColumna = (1*ancho)/100; 
                		break;
            }                      
            columnaTabla.setPreferredWidth(anchoColumna);            
        } 
	}
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(btnReasignar) && tablaGestion.getSelectedRow() >= 0)
		{
			try {
				new VentanaDeReasignacion();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Es necesario seleccionar un campo. ","DENEGADO", JOptionPane.ERROR_MESSAGE);
		}
	}
}
