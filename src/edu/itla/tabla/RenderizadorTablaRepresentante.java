package edu.itla.tabla;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import edu.itla.administrador.conexion.Conexion;

public class RenderizadorTablaRepresentante implements TableCellRenderer {

	DefaultTableCellRenderer renderer;
	private static final Color EVEN_ROW_COLOR = new Color(241, 245, 250);
	
	public RenderizadorTablaRepresentante(JTable table)
	{
	    renderer = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	{
		JLabel etiqueta = new JLabel();
        etiqueta.setOpaque(true);
    
        if (row % 2 == 0) 
        {
            etiqueta.setBackground(EVEN_ROW_COLOR);

        }  else {
            etiqueta.setBackground(Color.WHITE);

        }
        if(column == 1){
        	try {
				String sexo = Conexion.getInstancia().obtenerSexo(String.valueOf(table.getValueAt(row, 0)));
				if(sexo.equals("0")){
					etiqueta.setIcon(getImagen("user.png"));
				}else if (sexo.equals("1")){
					etiqueta.setIcon(getImagen("user2.png"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        if(value == null)
        {
        	value = "";
        	etiqueta.setText(value.toString());
        }
        else
        {
        	etiqueta.setText(value.toString());
        }
        if (isSelected) {
            etiqueta.setBackground(new Color(151, 193, 215));
        }
		return etiqueta;
	}
	public ImageIcon getImagen(String nombreImagen){
		URL pathImagen = RenderizadorTablaRepresentante.class.getClassLoader().getResource("com/recursos/"+nombreImagen+"");
		ImageIcon icono = new ImageIcon(pathImagen);
		return icono;
	}
}

