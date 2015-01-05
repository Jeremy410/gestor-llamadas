package edu.itla.administrador.cliente;

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
import edu.itla.tabla.RenderizadorTablaCliente;

public class MantenimientoDeCliente extends Mantenimiento implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private static JTable tablaCliente = null;
	private JButton btnModificar = null;
	private JButton btnInhabilitar = null;
	private JPanel panelTemporal = null;
	private JScrollPane scrollPane = null;
	private JTextField txtBuscar = null;
	private static Mantenimiento  instancia = null;
		
	public MantenimientoDeCliente() throws Exception 
	{
		setSize(719,395);
		setLayout(new BorderLayout());
		tablaCliente = new JTable();
		scrollPane = new JScrollPane(tablaCliente);
		add(scrollPane, BorderLayout.CENTER);
		Conexion.getInstancia().consultaCliente(tablaCliente);
		tablaCliente.setDefaultRenderer(Object.class, new RenderizadorTablaCliente(tablaCliente));
		
		panelTemporal = new JPanel();
		add(panelTemporal, BorderLayout.SOUTH);
		
		btnModificar = new JButton("Modificar Cliente");
		btnModificar.setIcon(getImagen("modify.jpg"));
		panelTemporal.add(btnModificar);
		
		btnInhabilitar = new JButton("Inhabilitar Cliente");
		btnInhabilitar.setIcon(getImagen("RemoveSmall.png"));
		panelTemporal.add(btnInhabilitar);
		btnInhabilitar.addActionListener(this);
		btnModificar.addActionListener(this);
		
		panelTemporal = new JPanel();
		add(panelTemporal, BorderLayout.NORTH);
		panelTemporal.setLayout(new BorderLayout(0, 0));
		
		JPanel panelBuscador = new JPanel();
		panelTemporal.add(panelBuscador, BorderLayout.EAST);
		
		JLabel lblBuscador = new JLabel("Buscador:    ");
		panelBuscador.add(lblBuscador);
		
		txtBuscar = new JTextField();
		txtBuscar.addKeyListener(new KeyAdapter() 
		{
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				crearBuscador(Buscador.getInstancia());
			}
		});
		panelBuscador.add(txtBuscar);
		txtBuscar.setColumns(10);
		
		JLabel lblMantenimientoDeClientes = new JLabel("                       Mantenimiento de Clientes");
		lblMantenimientoDeClientes.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMantenimientoDeClientes.setHorizontalAlignment(SwingConstants.CENTER);
		panelTemporal.add(lblMantenimientoDeClientes, BorderLayout.CENTER);
		setVisible(true);
	}
	public static Mantenimiento getInstancia() throws Exception
	{
		if(instancia == null)
		{
			instancia = new MantenimientoDeCliente();
		}
		return instancia;
	}
	
	public ImageIcon getImagen(String nombreImagen){
		URL pathImagen = MantenimientoDeCliente.class.getClassLoader().getResource("com/recursos/"+nombreImagen+"");
		ImageIcon icono = new ImageIcon(pathImagen);
		return icono;
	}
	@Override
	public void ajustarTamañoColumnas()
	{
		JViewport viewPort = (JViewport)tablaCliente.getParent();
        int ancho = viewPort.getWidth();
        int anchoColumna = 0; 
        TableColumnModel modeloColumna = tablaCliente.getColumnModel(); 
        TableColumn columnaTabla;
        
        for (int i = 0; i < tablaCliente.getColumnCount(); i++) 
        { 
            columnaTabla = modeloColumna.getColumn(i); 
            switch(i)
            { 
                case 0: anchoColumna = (1*ancho)/100; 
                        break; 
                case 1: anchoColumna = (25*ancho)/100; 
                        break; 
                case 2: anchoColumna = (10*ancho)/100; 
                        break;
                case 3: anchoColumna = (2*ancho)/100; 
                		break; 
                case 4: anchoColumna = (20*ancho)/100; 
                		break; 
                		
            }                      
            columnaTabla.setPreferredWidth(anchoColumna);            
        } 
	}
	public static JTable getTablaCliente() 
	{
		return tablaCliente;
	}
	public static void obtenerCliente(JTextField texto)
	{
		int fila = tablaCliente.getSelectedRow();
		String id = tablaCliente.getValueAt(fila, 0).toString();
		String nombre = tablaCliente.getValueAt(fila, 1).toString();
		texto.setText(id+ " - " + nombre);
	}
	@Override
	public void crearBuscador(Buscador buscador) {
		
		buscador.buscar(tablaCliente, txtBuscar.getText());
	}
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(btnModificar))
		{
			if(tablaCliente.getSelectedRow() >= 0)
				try 
				{
					new VentadaDeModificacion();
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			else
			{
				JOptionPane.showMessageDialog(this, "Es necesario seleccionar un campo. ", "ATENCION" , JOptionPane.ERROR_MESSAGE);
			}
		}
		if(evento.getSource().equals(btnInhabilitar))
		{
			if(tablaCliente.getSelectedRow() >= 0)
			{
				try {
					int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea inhabilitar el cliente?", "CONFIRMACION" , JOptionPane.YES_NO_OPTION);
					if(respuesta == JOptionPane.YES_OPTION)
					{
						Conexion.getInstancia().inhabilitarCliente(tablaCliente.getValueAt(tablaCliente.getSelectedRow(), 0).toString());
						Conexion.getInstancia().consultaCliente(tablaCliente);
						JOptionPane.showMessageDialog(this, "Se ha inhabilitado el cliente correctamente.","COMPLETADO", JOptionPane.INFORMATION_MESSAGE);

					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Es necesario seleccionar un campo. ", "ATENCION" , JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}