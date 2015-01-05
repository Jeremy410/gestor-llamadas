package edu.itla.administrador.consultas;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
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
import edu.itla.administrador.principal.Consulta;
import edu.itla.tabla.RenderizadorTablaCliente;

public class ConsultaDeCliente extends Consulta 
{
	private static final long serialVersionUID = 1L;	
	private JTable tablaCliente;
	private JScrollPane scrollPane;
	private JTextField txtBuscador;
	
	public ConsultaDeCliente() throws Exception
	{
		setLayout(new BorderLayout(0, 0));
		tablaCliente = new JTable();
		scrollPane = new JScrollPane(tablaCliente);
		Conexion.getInstancia().consultaCliente(tablaCliente);
		add(scrollPane);
		tablaCliente.setDefaultRenderer(Object.class, new RenderizadorTablaCliente(tablaCliente));
		setSize(719,395);
		
		JPanel panelTemporal = new JPanel();
		add(panelTemporal, BorderLayout.NORTH);
		panelTemporal.setLayout(new BorderLayout(0, 0));
		
		JPanel panelBuscador = new JPanel();
		panelTemporal.add(panelBuscador, BorderLayout.EAST);
		
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
		
		JLabel lblTitulo = new JLabel("                         Consulta de Clientes");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		panelTemporal.add(lblTitulo, BorderLayout.CENTER);
	}

	public void crearBuscador(Buscador buscador) 
	{
		buscador.buscar(tablaCliente, txtBuscador.getText());
	}

	@Override
	public void ajustarTamañoColumnas() {
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
}
