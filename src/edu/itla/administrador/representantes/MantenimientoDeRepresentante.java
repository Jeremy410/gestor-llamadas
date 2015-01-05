package edu.itla.administrador.representantes;

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
import edu.itla.tabla.RenderizadorTablaRepresentante;

public class MantenimientoDeRepresentante extends Mantenimiento implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JTable tablaRepresentante = null;
	private JButton btnAgregar = null;
	private JButton btnModificar = null;
	private JButton btnInhabilitar = null;
	private JPanel panelTemporal = null;
	private JScrollPane scrollPane = null;
	private JTextField txtBuscador;
	
	public MantenimientoDeRepresentante() throws SQLException
	{
		setSize(719,395);
		setLayout(new BorderLayout());
		tablaRepresentante = new JTable();
		scrollPane = new JScrollPane(tablaRepresentante);
		add(scrollPane, BorderLayout.CENTER);
		Conexion.getInstancia().consultaRepresentante(tablaRepresentante);
		tablaRepresentante.setDefaultRenderer(Object.class, new RenderizadorTablaRepresentante(tablaRepresentante));
		
		panelTemporal = new JPanel();
		add(panelTemporal, BorderLayout.SOUTH);
		
		btnAgregar = new JButton("Agregar Representante");
		btnAgregar.setIcon(getImagen("add.png"));
		panelTemporal.add(btnAgregar);
		
		btnModificar = new JButton("Modificar Representante");
		btnModificar.setIcon(getImagen("modify.jpg"));
		panelTemporal.add(btnModificar);
		
		btnInhabilitar = new JButton("Inhabilitar Representante");
		btnInhabilitar.setIcon(getImagen("RemoveSmall.png"));
		panelTemporal.add(btnInhabilitar);
		
		btnAgregar.addActionListener(this);
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
			public void keyReleased(KeyEvent e) 
			{
				super.keyReleased(e);
				crearBuscador(Buscador.getInstancia());
			}
		});
		panelBuscador.add(txtBuscador);
		txtBuscador.setColumns(10);
		
		JLabel lblTitulo = new JLabel("                      Mantenimiento de Representantes");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelNorte.add(lblTitulo, BorderLayout.CENTER);
		setVisible(true);
	}
	public void ajustarTamañoColumnas()
	{
		JViewport viewPort = (JViewport)tablaRepresentante.getParent();
        int ancho = viewPort.getWidth();
        int anchoColumna = 0; 
        TableColumnModel modeloColumna = tablaRepresentante.getColumnModel(); 
        TableColumn columnaTabla;
        
        for (int i = 0; i < tablaRepresentante.getColumnCount(); i++) 
        { 
            columnaTabla = modeloColumna.getColumn(i); 
            switch(i)
            { 
                case 0: anchoColumna = (10*ancho)/100; 
                        break; 
                case 1: anchoColumna = (80*ancho)/100; 
                        break; 
                case 2: anchoColumna = (5*ancho)/100; 
                        break;               		
            }                      
            columnaTabla.setPreferredWidth(anchoColumna);            
        } 
	}
	public static void obtenerDatos(JTextField nombre, JTextField apellido, JTextField usuario, JTextField clave)
	{
		try {
			String nombreCompleto = tablaRepresentante.getValueAt(tablaRepresentante.getSelectedRow(), 1).toString();
			String nombrePartido[] = nombreCompleto.split(" ");
			String id = tablaRepresentante.getValueAt(tablaRepresentante.getSelectedRow(), 0).toString();
			nombre.setText(nombrePartido[0]);
			apellido.setText(nombrePartido[1]);
			usuario.setText(Conexion.getInstancia().traerNombreRep(id));
			clave.setText(Conexion.getInstancia().traerClaveRep(id));
				
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	public static JTable getTablaRepresentante() {
		return tablaRepresentante;
	}
	public void actionPerformed(ActionEvent evento) 
	{
		try {
			if(evento.getSource().equals(btnAgregar))
			{
				new VentanaDeAgregar();
			}
			if(evento.getSource().equals(btnModificar))
			{
				if(tablaRepresentante.getSelectedRow() >= 0)
				{
					new VentanaDeModificacion();
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Es necesario seleccionar una fila.", "DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(evento.getSource().equals(btnInhabilitar))
			{
				if(tablaRepresentante.getSelectedRow() >= 0)
				{
					int respuesta = JOptionPane.showConfirmDialog(this,"¿Está seguro que desea inhabilitar el representante?", "CONFIRMACION", JOptionPane.YES_NO_OPTION);
					if(respuesta == JOptionPane.YES_OPTION)
					{
						String id = tablaRepresentante.getValueAt(tablaRepresentante.getSelectedRow(), 0).toString();
						Conexion.getInstancia().inhabilitarRepresentante(id);
						Conexion.getInstancia().consultaRepresentante(tablaRepresentante);
						JOptionPane.showMessageDialog(this, "Se ha inhabilitado el representante correctamente.","COMPLETADO", JOptionPane.INFORMATION_MESSAGE);
					}			
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Es necesario seleccionar una fila", "DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void crearBuscador(Buscador buscador) {
		buscador.buscar(tablaRepresentante, txtBuscador.getText());
	}
	@Override
	public ImageIcon getImagen(String nombreImagen) {
		URL pathImagen = MantenimientoDeRepresentante.class.getClassLoader().getResource("com/recursos/"+nombreImagen+"");
		ImageIcon icono = new ImageIcon(pathImagen);
		return icono;
	}
}
