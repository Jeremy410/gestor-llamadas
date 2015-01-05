package edu.itla.representante.principal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.toedter.calendar.JDateChooser;

import edu.itla.administrador.conexion.Conexion;
import edu.itla.administrador.principal.Buscador;
import edu.itla.tabla.RenderizadorTablaGestion;

public class GestionDeDeuda extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private static JTable tablaGestion = null;
	private JButton btnAcceder = null;
	private JScrollPane scrollPane = null;
	private JPanel panelTemporal = null;
	private JPanel panelTitulo = null;
	private JPanel panelCheck = null;
	private JPanel panelCalendario = null;
	private JCheckBox checkCompletado = null;
	private JCheckBox checkPendientes = null;
	private JDateChooser segundaFecha;
	private JDateChooser primeraFecha;
	private JLabel lblFechaInicial = null;
	private JLabel lblFechaFinal = null;
	private static String fecha1 = "";
	private static String fecha2 = "";
	private SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	private JLabel lblBuscador;
	private JLabel lblNulo;
	private JTextField txtBuscador;
	private JLabel lblNulo2;
	private JButton btnCancelar;
	private static DatosDeDeuda datos;
	private JPopupMenu popUpMenu = null;
	private JMenuItem obtenerPDF = null;
	
	public GestionDeDeuda() throws SQLException, ParseException 
	{
		setTitle("Gestiones de Llamada");
		getContentPane().setLayout(new BorderLayout());
		
		tablaGestion = new JTable();
		scrollPane = new JScrollPane(tablaGestion);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		tablaGestion.setDefaultRenderer(Object.class, new RenderizadorTablaGestion(tablaGestion));
		popUpMenu = new JPopupMenu();
		obtenerPDF = new JMenuItem("Ver Contrato del Cliente");
		popUpMenu.add(obtenerPDF);
		tablaGestion.setComponentPopupMenu(popUpMenu);
		tablaGestion.addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{
				if(e.getClickCount() >= 2)
				{
					try {
						crearDatosDeDeuda();
					} catch (ArrayIndexOutOfBoundsException e2) {
						JOptionPane.showMessageDialog(GestionDeDeuda.this, "Esta fila es invalida. No se puede seleccionar. ","FILA INCORRECTA" , JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		Conexion.getInstancia().consultaGestionEnFechas(tablaGestion, fecha1, fecha2);
		
		panelTemporal = new JPanel();
		getContentPane().add(panelTemporal, BorderLayout.SOUTH);
		panelTemporal.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnAcceder = new JButton("Accesar");
		btnAcceder.setHorizontalAlignment(SwingConstants.RIGHT);
		btnAcceder.setIcon(getImagen("Go.png"));
		panelTemporal.add(btnAcceder);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setIcon(getImagen("Cancel.png"));
		panelTemporal.add(btnCancelar);
		
		panelTemporal = new JPanel();
		getContentPane().add(panelTemporal, BorderLayout.NORTH);
		panelTemporal.setLayout(new BorderLayout(0, 0));
		
		panelTitulo = new JPanel();
		panelTemporal.add(panelTitulo, BorderLayout.NORTH);
		
		JLabel lblTitulo = new JLabel("AGENDA DE LLAMADA");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelTitulo.add(lblTitulo);
		
		panelCheck = new JPanel();
		panelTemporal.add(panelCheck, BorderLayout.WEST);
		panelCheck.setLayout(new GridLayout(0, 1));
		
		checkCompletado = new JCheckBox("Gestiones Completadas");
		checkCompletado.setRolloverEnabled(false);
		panelCheck.add(checkCompletado);
		
		checkPendientes = new JCheckBox("Gestiones Pendientes");
		checkPendientes.setRolloverEnabled(false);
		panelCheck.add(checkPendientes);
		
		panelCalendario = new JPanel();
		panelTemporal.add(panelCalendario, BorderLayout.EAST);
		panelCalendario.setLayout(new GridLayout(2, 4, 0, 5));

		lblFechaInicial = new JLabel("Fecha Inicial:");
		primeraFecha = new JDateChooser("yyyy-MM-dd", "####-##-##", '_');
		lblFechaFinal = new JLabel("Fecha Final:");
		segundaFecha = new JDateChooser("yyyy-MM-dd", "####-##-##", '_');
		panelCalendario.add(lblFechaInicial);
		panelCalendario.add(primeraFecha);
		panelCalendario.add(lblFechaFinal);
		panelCalendario.add(segundaFecha);
		lblNulo = new JLabel("");
		panelCalendario.add(lblNulo);
		lblNulo2 = new JLabel("");
		panelCalendario.add(lblNulo2);
		
		primeraFecha.addPropertyChangeListener(new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent e)
		{
			if("date".equals(e.getPropertyName()))
			{
				fecha1 = formatoFecha.format(e.getNewValue());
				try {
					Conexion.getInstancia().consultaGestionEnFechas(tablaGestion, fecha1, fecha2);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		});
		primeraFecha.setDate(formatoFecha.parse(Conexion.getInstancia().obtenerFechaParametro()));
		
		segundaFecha.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) 
			{
				if("date".equals(e.getPropertyName()))
				{
					fecha2 = formatoFecha.format(e.getNewValue());
					try {
						Conexion.getInstancia().consultaGestionEnFechas(tablaGestion, fecha1, fecha2);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			});
			segundaFecha.setDate(formatoFecha.parse(Conexion.getInstancia().obtenerFechaDia()));
	
		lblBuscador = new JLabel("Buscador:");
		panelCalendario.add(lblBuscador);
		
		txtBuscador = new JTextField();
		txtBuscador.addKeyListener(new KeyAdapter() 
		{
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				crearBuscador(Buscador.getInstancia());
			}
		});
		panelCalendario.add(txtBuscador);
		txtBuscador.setColumns(10);
		
		obtenerPDF.addActionListener(this);
		checkCompletado.addChangeListener(this);
		checkPendientes.addChangeListener(this);
		btnAcceder.addActionListener(this);
		btnCancelar.addActionListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(839,565);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	public ImageIcon getImagen(String nombreImagen){
		URL pathImagen = GestionDeDeuda.class.getClassLoader().getResource("com/recursos/"+nombreImagen+"");
		ImageIcon icono = new ImageIcon(pathImagen);
		return icono;
	}
	public void ajustarTamañoColumnas()
	{
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
                case 0: anchoColumna = (1*ancho)/100; 
                        break; 
                case 1: anchoColumna = (25*ancho)/100; 
                        break; 
                case 2: anchoColumna = (4*ancho)/100; 
                        break;
                case 3: anchoColumna = (4*ancho)/100; 
                		break; 
                case 4: anchoColumna = (1*ancho)/100; 
                		break;
                case 5: anchoColumna = (1*ancho)/100; 
        				break; 
                case 6: anchoColumna = (4*ancho)/100; 
        				break; 
            }                      
            columnaTabla.setPreferredWidth(anchoColumna);            
        } 
	}
	public void crearDatosDeDeuda()
	{
		try {
			String validacionGestion = tablaGestion.getValueAt(tablaGestion.getSelectedRow(), 5).toString();
			if(validacionGestion.equals("Completada"))
			{
				datos = new DatosDeDeuda(true);
			}
			else
			{
				datos = new DatosDeDeuda(false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static DatosDeDeuda getDatos() {
		return datos;
	}

	public static String getFecha1() {
		return fecha1;
	}

	public static String getFecha2() {
		return fecha2;
	}
	public static JTable getTablaGestion() 
	{
		return tablaGestion;
	}
	public void crearBuscador(Buscador buscador)
	{
		buscador.buscar(tablaGestion, txtBuscador.getText());
	}
	
	public void actionPerformed(ActionEvent evento) 
	{
		try {	
			if(evento.getSource().equals(obtenerPDF))
			{
				if(tablaGestion.getSelectedRow() >= 0 )
				{
					String deuda = Conexion.getInstancia().obtenerIdDeuda(String.valueOf(tablaGestion.getValueAt(tablaGestion.getSelectedRow(), 0)), String.valueOf(tablaGestion.getValueAt(tablaGestion.getSelectedRow(), 6)));
					Conexion.getInstancia().obtenerPDF(deuda);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Debe seleccionar una gestion para ver el Contrato de la Deuda del Cliente. ","ATENCION", JOptionPane.WARNING_MESSAGE);
				}
			}
			if(evento.getSource().equals(btnCancelar))
			{
				System.exit(0);
			}
			if(evento.getSource().equals(btnAcceder))
			{
				if(tablaGestion.getSelectedRow() >= 0)
				{
					crearDatosDeDeuda();
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Es necesario seleccionar un campo.", "DENEGADO", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	public void stateChanged(ChangeEvent e) 
	{
		try {	
			if(checkCompletado.isSelected() == true)
			{
				Conexion.getInstancia().consultaCheckCompletado(tablaGestion);		
			}
			if(checkPendientes.isSelected() == true)
			{
				Conexion.getInstancia().consultaCheckPendientes(tablaGestion);
			}
			if(checkCompletado.isSelected() == true && checkPendientes.isSelected() == true)
			{
				Conexion.getInstancia().consultaAmbosCheck(tablaGestion);
			}
			if(checkCompletado.isSelected() == false && checkPendientes.isSelected() == false)
			{				
				Conexion.getInstancia().consultaGestionEnFechas(tablaGestion, fecha1, fecha2);		
			}
		} catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}
}
