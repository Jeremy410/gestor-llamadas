package edu.itla.administrador.seguimiento;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.toedter.calendar.JDateChooser;

import edu.itla.administrador.conexion.Conexion;
import edu.itla.administrador.principal.Buscador;
import edu.itla.administrador.principal.Mantenimiento;
import edu.itla.tabla.RenderizadorTablaSeguimiento;

public class MantenimientoDeSeguimiento extends Mantenimiento implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static JTable tablaSeguimiento = null;
	private JButton btnModificar = null;
	private JButton btnInhabilitar = null;
	private JPanel panelTemporal = null;
	private JScrollPane scrollPane = null;
	private JTextField txtBuscador = null;
	
	public MantenimientoDeSeguimiento() throws SQLException
	{
		setSize(719,395);
		setLayout(new BorderLayout());
		tablaSeguimiento = new JTable();
		scrollPane = new JScrollPane(tablaSeguimiento);
		add(scrollPane, BorderLayout.CENTER);
		Conexion.getInstancia().consultaSeguimiento(tablaSeguimiento);
		tablaSeguimiento.setDefaultRenderer(Object.class, new RenderizadorTablaSeguimiento(tablaSeguimiento));
		
		panelTemporal = new JPanel();
		add(panelTemporal, BorderLayout.SOUTH);
		
		btnModificar = new JButton("Modificar Seguimiento");
		btnModificar.setIcon(getImagen("modify.jpg"));
		panelTemporal.add(btnModificar);
		
		btnInhabilitar = new JButton("Inhabilitar Seguimiento");
		btnInhabilitar.setIcon(getImagen("RemoveSmall.png"));
		panelTemporal.add(btnInhabilitar);
		
		btnInhabilitar.addActionListener(this);
		btnModificar.addActionListener(this);
		
		JPanel panelNorte = new JPanel();
		add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BorderLayout(0, 0));
		
		JPanel panelBuscador = new JPanel();
		panelNorte.add(panelBuscador, BorderLayout.EAST);
		
		JLabel lblBuscador = new JLabel("Buscador:   ");
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
		
		JLabel lblTitulo = new JLabel("                            Mantenimiento de Seguimientos");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		panelNorte.add(lblTitulo, BorderLayout.CENTER);
		setVisible(true);
	}
	@Override
	public void ajustarTamañoColumnas()
	{
		JViewport viewPort = (JViewport)tablaSeguimiento.getParent();
        int ancho = viewPort.getWidth();
        int anchoColumna = 0; 
        TableColumnModel modeloColumna = tablaSeguimiento.getColumnModel(); 
        TableColumn columnaTabla;
        
        for (int i = 0; i < tablaSeguimiento.getColumnCount(); i++) 
        { 
            columnaTabla = modeloColumna.getColumn(i); 
            switch(i)
            { 
                case 0: anchoColumna = (-20*ancho)/100; 
                        break; 
                case 1: anchoColumna = (56*ancho)/100; 
                        break; 
                case 2: anchoColumna = (1*ancho)/100; 
                        break;
                case 3: anchoColumna = (1*ancho)/100; 
                		break; 		
            }                      
            columnaTabla.setPreferredWidth(anchoColumna);            
        } 
	}	
	public static JTable getTablaSeguimiento() 
	{
		return tablaSeguimiento;
	}

	public static void obtenerDatos(JTextField txtCliente, JTextField txtSeguimiento, JDateChooser txtFecha) throws ParseException
	{
		int fila = tablaSeguimiento.getSelectedRow();
		String IDCliente = tablaSeguimiento.getValueAt(fila, 0).toString();
		String nombre = tablaSeguimiento.getValueAt(fila, 1).toString();
		txtCliente.setText(IDCliente + " - " + nombre);
		
		String IDSeguimiento = tablaSeguimiento.getValueAt(fila, 2).toString();
		txtSeguimiento.setText(IDSeguimiento);
		
		String fecha = tablaSeguimiento.getValueAt(fila, 3).toString();
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
		txtFecha.setDate(date);
	}

	public void actionPerformed(ActionEvent e) 
	{
		try {
			if(e.getSource().equals(btnModificar))
			{
				if(tablaSeguimiento.getSelectedColumn() >= 0)
				{
					new VentanaDeModificacion();
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Es necesario seleccionar un campo.","DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(e.getSource().equals(btnInhabilitar))
			{
				if(tablaSeguimiento.getSelectedRow() >= 0)
				{
					int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea inhabilitar el seguimiento?","CONFIRMACION", JOptionPane.YES_NO_OPTION);
					if(respuesta == JOptionPane.YES_OPTION)
					{
						Conexion.getInstancia().inhabilitarSeguimiento(tablaSeguimiento.getValueAt(tablaSeguimiento.getSelectedRow(), 2).toString());
						Conexion.getInstancia().consultaSeguimiento(tablaSeguimiento);
						JOptionPane.showMessageDialog(this, "Se ha inhabilitado el seguimiento correctamente.","COMPLETADO", JOptionPane.INFORMATION_MESSAGE);
					}	
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Es necesario seleccionar un campo. ","DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	@Override
	public void crearBuscador(Buscador buscador) {
		buscador.buscar(tablaSeguimiento, txtBuscador.getText());
	}
	@Override
	public ImageIcon getImagen(String nombreImagen) {
		URL pathImagen = MantenimientoDeSeguimiento.class.getClassLoader().getResource("com/recursos/"+nombreImagen+"");
		ImageIcon icono = new ImageIcon(pathImagen);
		return icono;
	}
}
