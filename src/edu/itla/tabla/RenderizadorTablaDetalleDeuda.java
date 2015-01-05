package edu.itla.tabla;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class RenderizadorTablaDetalleDeuda implements TableCellRenderer {

	DefaultTableCellRenderer renderer;
	private static final Color EVEN_ROW_COLOR = new Color(241, 245, 250);
	
	public RenderizadorTablaDetalleDeuda(JTable table)
	{
	    renderer = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	{
		JLabel etiqueta = new JLabel();
        etiqueta.setOpaque(true);
    
        if(column == 3 || column == 4 || column == 5)
        {
        	etiqueta.setHorizontalAlignment(JLabel.RIGHT);
        }
        if (row % 2 == 0) 
        {
            etiqueta.setBackground(EVEN_ROW_COLOR);

        }  else {
            etiqueta.setBackground(Color.WHITE);

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
}
