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
import edu.itla.tabla.RenderizadorTablaPago;

public class ConsultaDePago extends Consulta 
{
	private static final long serialVersionUID = 1L;
	private JTable tablaPago;
	private JScrollPane scrollPane;
	private JTextField txtBuscador;
	private JLabel lblTitulo;

	public ConsultaDePago() throws Exception 
	{
		setLayout(new BorderLayout(0, 0));
		tablaPago = new JTable();
		scrollPane = new JScrollPane(tablaPago);
		add(scrollPane);
		Conexion.getInstancia().consultaPago(tablaPago);
		tablaPago.setDefaultRenderer(Object.class, new RenderizadorTablaPago(tablaPago));
		setSize(719, 395);
		
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
		
		lblTitulo = new JLabel("                                    Consulta de Pagos");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelTemporal.add(lblTitulo, BorderLayout.CENTER);
	}

	public void crearBuscador(Buscador buscador) {
		buscador.buscar(tablaPago, txtBuscador.getText());
	}

	@Override
	public void ajustarTamañoColumnas() {
		JViewport viewPort = (JViewport)tablaPago.getParent();
        int ancho = viewPort.getWidth();
        int anchoColumna = 0; 
        TableColumnModel modeloColumna = tablaPago.getColumnModel(); 
        TableColumn columnaTabla;
        
        for (int i = 0; i < tablaPago.getColumnCount(); i++) 
        { 
            columnaTabla = modeloColumna.getColumn(i); 
            switch(i)
            { 
                case 0: anchoColumna = (1*ancho)/100; 
                        break; 
                case 1: anchoColumna = (50*ancho)/100; 
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
