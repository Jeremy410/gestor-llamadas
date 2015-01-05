package edu.itla.administrador.principal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import edu.itla.administrador.acuerdo.MantenimientoDeAcuerdo;
import edu.itla.administrador.cliente.MantenimientoDeCliente;
import edu.itla.administrador.consultas.ConsultaDeCliente;
import edu.itla.administrador.consultas.ConsultaDeDetalleDeuda;
import edu.itla.administrador.consultas.ConsultaDeDeuda;
import edu.itla.administrador.consultas.ConsultaDePago;
import edu.itla.administrador.procesos.ReasignacionDeGestiones;
import edu.itla.administrador.reportes.Reporte;
import edu.itla.administrador.representantes.MantenimientoDeRepresentante;
import edu.itla.administrador.seguimiento.MantenimientoDeSeguimiento;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;

public class Administrador extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	JMenuItem btnCerrarSesion;
	JMenuItem btnCliente;
	JMenuItem btnAcuerdo;
	JMenuItem btnRepresentante;
	JMenuItem btnSeguimiento;
	private JMenu barraProcesos;
	private JMenu barraConsultas;
	private JMenuItem btnReasignar;
	private JMenuItem btnPago;
	private JMenuItem btnDeuda;
	private JMenuItem btnClienteConsulta;
	private JMenuItem btnDetalleDeuda;
	private JMenuItem btnSalir;
	private JMenuItem btnGenerarReporte;
	private JLabel lblFondo = null;
	private JLabel lblTitulo;
	private JPanel panelTitulo;

	public Administrador() 
	{
		setTitle("Administrador");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		panelTitulo = new JPanel();
		panelTitulo.setBorder(new EmptyBorder(0, 0, 10, 10));
		getContentPane().add(panelTitulo, BorderLayout.NORTH);
		panelTitulo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblTitulo = new JLabel("Panel Administrativo");
		panelTitulo.add(lblTitulo);
		lblTitulo.setFont(new Font("Calibri", Font.BOLD, 17));
		
		lblFondo = new JLabel();
		lblFondo.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblFondo, BorderLayout.CENTER);
		lblFondo.setIcon(new ImageIcon(Administrador.class.getResource("/com/recursos/fondo.png")));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu barraUsuario = new JMenu("Usuario");
		barraUsuario.setIcon(new ImageIcon(Administrador.class.getResource("/com/recursos/Usuario.png")));
		menuBar.add(barraUsuario);
		
		btnCerrarSesion = new JMenuItem("Cerrar sesion");
		barraUsuario.add(btnCerrarSesion);
		
		btnSalir = new JMenuItem("Salir");
		barraUsuario.add(btnSalir);
		
		JMenu barraMantenimientos = new JMenu("Mantenimientos");
		barraMantenimientos.setIcon(new ImageIcon(Administrador.class.getResource("/com/recursos/Mantenimiento.png")));
		menuBar.add(barraMantenimientos);
		
		btnCliente = new JMenuItem("Cliente");
		barraMantenimientos.add(btnCliente);
		
		btnAcuerdo = new JMenuItem("Acuerdo");
		barraMantenimientos.add(btnAcuerdo);
		
		btnRepresentante = new JMenuItem("Representante");
		barraMantenimientos.add(btnRepresentante);
		
		btnSeguimiento = new JMenuItem("Seguimiento");
		barraMantenimientos.add(btnSeguimiento);
		
		barraConsultas = new JMenu("Consultas");
		barraConsultas.setIcon(new ImageIcon(Administrador.class.getResource("/com/recursos/Consulta.png")));
		menuBar.add(barraConsultas);
		
		btnClienteConsulta = new JMenuItem("Cliente");
		barraConsultas.add(btnClienteConsulta);
		
		btnPago = new JMenuItem("Pago");
		barraConsultas.add(btnPago);
		
		btnDeuda = new JMenuItem("Deuda");
		barraConsultas.add(btnDeuda);
		
		btnDetalleDeuda = new JMenuItem("Detalle Deuda");
		barraConsultas.add(btnDetalleDeuda);
		
		barraProcesos = new JMenu("Procesos");
		barraProcesos.setIcon(new ImageIcon(Administrador.class.getResource("/com/recursos/Procesos.png")));
		menuBar.add(barraProcesos);
		
		btnReasignar = new JMenuItem("Re-asignamiento de Representante");
		barraProcesos.add(btnReasignar);
		
		JMenu barraReportes = new JMenu("Reportes");
		barraReportes.setIcon(new ImageIcon(Administrador.class.getResource("/com/recursos/Reportes.png")));
		menuBar.add(barraReportes);
		
		btnGenerarReporte = new JMenuItem("Generar Reportes");
		barraReportes.add(btnGenerarReporte);
		
		setSize(921,564);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		
		//btnPanelExterno.addActionListener(this);
		btnAcuerdo.addActionListener(this);
		btnRepresentante.addActionListener(this);
		btnSeguimiento.addActionListener(this);
		btnCliente.addActionListener(this);
		btnReasignar.addActionListener(this);
		btnClienteConsulta.addActionListener(this);
		btnPago.addActionListener(this);
		btnDeuda.addActionListener(this);
		btnDetalleDeuda.addActionListener(this);
		btnCerrarSesion.addActionListener(this);
		btnSalir.addActionListener(this);
		btnGenerarReporte.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent evento) 
	{
		try {
			/*if(evento.getSource().equals(btnPanelExterno))
			{
				JTextArea area = new JTextArea();
				area.setText("Prueba");
				getContentPane().add(area, BorderLayout.WEST);
				setVisible(true);
			}*/
			if(evento.getSource().equals(btnAcuerdo))
			{	
				getContentPane().removeAll();
				MantenimientoDeAcuerdo mantAcuerdo = new MantenimientoDeAcuerdo();
				getContentPane().add(mantAcuerdo);
				setVisible(true);
				mantAcuerdo.ajustarTamañoColumnas();
				
			}
			if(evento.getSource().equals(btnCliente))
			{
				getContentPane().removeAll();
				try {
					MantenimientoDeCliente mantCliente = new MantenimientoDeCliente();
					getContentPane().add(mantCliente);
					setVisible(true);
					mantCliente.ajustarTamañoColumnas();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(evento.getSource().equals(btnRepresentante))
			{
				getContentPane().removeAll();
				try {
					MantenimientoDeRepresentante mantRepresentante = new MantenimientoDeRepresentante();
					getContentPane().add(mantRepresentante);
					setVisible(true);
					mantRepresentante.ajustarTamañoColumnas();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(evento.getSource().equals(btnSeguimiento))
			{
				getContentPane().removeAll();
				try {
					MantenimientoDeSeguimiento mantSeguimiento = new MantenimientoDeSeguimiento();
					getContentPane().add(mantSeguimiento);
					setVisible(true);
					mantSeguimiento.ajustarTamañoColumnas();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(evento.getSource().equals(btnReasignar))
			{
				getContentPane().removeAll();
				ReasignacionDeGestiones reasignacion = new ReasignacionDeGestiones();;
				getContentPane().add(reasignacion);
				setVisible(true);
				reasignacion.ajustarTamañoColumnas();
			}
			if(evento.getSource().equals(btnClienteConsulta))
			{
				getContentPane().removeAll();
				ConsultaDeCliente consultaCliente = new ConsultaDeCliente();
				getContentPane().add(consultaCliente);
				setVisible(true);
				consultaCliente.ajustarTamañoColumnas();
				
			}
			if(evento.getSource().equals(btnDetalleDeuda))
			{
				getContentPane().removeAll();
				ConsultaDeDetalleDeuda consultaDetalle = new ConsultaDeDetalleDeuda();
				getContentPane().add(consultaDetalle);
				setVisible(true);
				consultaDetalle.ajustarTamañoColumnas();
				
			}
			if(evento.getSource().equals(btnPago))
			{
				getContentPane().removeAll();
				ConsultaDePago consultaPago = new ConsultaDePago();
				getContentPane().add(consultaPago);
				setVisible(true);
				consultaPago.ajustarTamañoColumnas();
				
			}
			if(evento.getSource().equals(btnDeuda))
			{
				getContentPane().removeAll();
				ConsultaDeDeuda consultaDeuda = new ConsultaDeDeuda();
				getContentPane().add(consultaDeuda);
				setVisible(true);
				consultaDeuda.ajustarTamañoColumnas();
				
			}
			if(evento.getSource().equals(btnCerrarSesion))
			{
				this.dispose();
				Iniciador.getInstancia().setVisible(true);
				Iniciador.getInstancia().getTxtContrasena().setText("");
				Iniciador.getInstancia().getTxtRepresentante().setText("");
			}
			if(evento.getSource().equals(btnSalir))
			{
				System.exit(0);
			}
			if(evento.getSource().equals(btnGenerarReporte))
			{
				new Reporte();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}