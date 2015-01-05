package edu.itla.administrador.consultas;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

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
import edu.itla.tabla.RenderizadorTablaDetalleDeuda;

public class ConsultaDeDetalleDeuda extends Consulta  
{
	private static final long serialVersionUID = 1L;	
	private JTable tablaDetalleDeuda;
	private JScrollPane scrollPane;
	private JTextField txtBuscador;
	
	public ConsultaDeDetalleDeuda() throws SQLException
	{
		setLayout(new BorderLayout(0, 0));
		tablaDetalleDeuda = new JTable();
		scrollPane = new JScrollPane(tablaDetalleDeuda);
		add(scrollPane);
		Conexion.getInstancia().consultaDetalleDeuda(tablaDetalleDeuda);
		tablaDetalleDeuda.setDefaultRenderer(Object.class, new RenderizadorTablaDetalleDeuda(tablaDetalleDeuda));
		setSize(719,395);
		
		JPanel panelTemporal = new JPanel();
		add(panelTemporal, BorderLayout.NORTH);
		panelTemporal.setLayout(new BorderLayout(0, 0));
		
		JPanel panelBuscador = new JPanel();
		panelTemporal.add(panelBuscador, BorderLayout.EAST);
		
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
		
		JLabel lblTitulo = new JLabel("                         Consulta del Detalle de Deuda");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelTemporal.add(lblTitulo, BorderLayout.CENTER);
	}

	public void crearBuscador(Buscador buscador) {
		
		buscador.buscar(tablaDetalleDeuda, txtBuscador.getText());
		
	}

	@Override
	public void ajustarTamañoColumnas() {
		JViewport viewPort = (JViewport)tablaDetalleDeuda.getParent();
        int ancho = viewPort.getWidth();
        int anchoColumna = 0; 
        TableColumnModel modeloColumna = tablaDetalleDeuda.getColumnModel(); 
        TableColumn columnaTabla;
        
        for (int i = 0; i < tablaDetalleDeuda.getColumnCount(); i++) 
        { 
            columnaTabla = modeloColumna.getColumn(i); 
            switch(i)
            { 
                case 0: anchoColumna = (1*ancho)/100; 
                        break; 
                case 1: anchoColumna = (35*ancho)/100; 
                        break; 
                case 2: anchoColumna = (1*ancho)/100; 
                        break;
                case 3: anchoColumna = (1*ancho)/100; 
                		break; 
                case 4: anchoColumna = (1*ancho)/100; 
                		break;
            }                      
            columnaTabla.setPreferredWidth(anchoColumna);            
        } 
	}
}
