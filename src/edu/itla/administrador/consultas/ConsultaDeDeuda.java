package edu.itla.administrador.consultas;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import edu.itla.administrador.conexion.Conexion;
import edu.itla.administrador.principal.Buscador;
import edu.itla.administrador.principal.Consulta;
import edu.itla.tabla.RenderizadorTablaDeuda;

public class ConsultaDeDeuda extends Consulta implements ActionListener
{
	private static final long serialVersionUID = 1L;	
	private JTable tablaDeuda;
	private JScrollPane scrollPane;
	private JTextField txtBuscador;
	private JLabel lblConsultaDeDeudas;
	private JPopupMenu popUpMenu = null;
	private JMenuItem insertarPDF = null;
	private JMenuItem obtenerPDF = null;
	private JFileChooser buscarPDF = new JFileChooser();
	private FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
	
	public ConsultaDeDeuda() throws Exception 
	{
		setLayout(new BorderLayout(0, 0));
		tablaDeuda = new JTable();
		scrollPane = new JScrollPane(tablaDeuda);
		add(scrollPane);
		Conexion.getInstancia().consultaDeuda(tablaDeuda);
		tablaDeuda.setDefaultRenderer(Object.class, new RenderizadorTablaDeuda(tablaDeuda));
		setSize(719,395);
		
		popUpMenu = new JPopupMenu();
		insertarPDF = new JMenuItem("Insertar Contrato");
		popUpMenu.add(insertarPDF);
		obtenerPDF = new JMenuItem("Ver Contrato");
		popUpMenu.add(obtenerPDF);
		tablaDeuda.setComponentPopupMenu(popUpMenu);
		
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
		
		lblConsultaDeDeudas = new JLabel("                                Consulta de Deudas");
		lblConsultaDeDeudas.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblConsultaDeDeudas.setHorizontalAlignment(SwingConstants.CENTER);
		panelTemporal.add(lblConsultaDeDeudas, BorderLayout.CENTER);	
		
		insertarPDF.addActionListener(this);
		obtenerPDF.addActionListener(this);
	}
	public void crearBuscador(Buscador buscador) 
	{
		buscador.buscar(tablaDeuda, txtBuscador.getText());
	}

	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource().equals(insertarPDF))
		{
			if(tablaDeuda.getSelectedRow() >= 0)
			{
				buscarPDF.setFileFilter(filter);
				buscarPDF.setAcceptAllFileFilterUsed(false);
				int resultado = buscarPDF.showOpenDialog(this);
				if(resultado == JFileChooser.APPROVE_OPTION)
				{
					Conexion.getInstancia().insertarPDF(buscarPDF.getSelectedFile().getPath(), tablaDeuda);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Debe seleccionar una deuda para generar el Contrato de la Deuda. ","ATENCION", JOptionPane.WARNING_MESSAGE);
			}
		}
		if(evento.getSource().equals(obtenerPDF))
		{
			if(tablaDeuda.getSelectedRow() >= 0)
			{
				Conexion.getInstancia().obtenerPDF(String.valueOf(tablaDeuda.getValueAt(tablaDeuda.getSelectedRow(), 2)));
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Debe seleccionar una deuda para ver el Contrato de la Deuda. ","ATENCION", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	@Override
	public void ajustarTamañoColumnas() {
		JViewport viewPort = (JViewport)tablaDeuda.getParent();
        int ancho = viewPort.getWidth();
        int anchoColumna = 0; 
        TableColumnModel modeloColumna = tablaDeuda.getColumnModel(); 
        TableColumn columnaTabla;
        
        for (int i = 0; i < tablaDeuda.getColumnCount(); i++) 
        { 
            columnaTabla = modeloColumna.getColumn(i); 
            switch(i)
            { 
                case 0: anchoColumna = (1*ancho)/100; 
                        break; 
                case 1: anchoColumna = (15*ancho)/100; 
                        break; 
                case 2: anchoColumna = (1*ancho)/100; 
                        break;
                case 3: anchoColumna = (1*ancho)/100; 
                		break; 
                case 4: anchoColumna = (1*ancho)/100; 
                		break; 
                case 5: anchoColumna = (1*ancho)/100; 
        				break; 
                case 6: anchoColumna = (1*ancho)/100; 
        				break;
                case 7: anchoColumna = (1*ancho)/100; 
        				break; 
            }                      
            columnaTabla.setPreferredWidth(anchoColumna);            
        } 
	}
}