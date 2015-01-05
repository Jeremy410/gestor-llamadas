package edu.itla.tabla;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class RenderizadorTablaHistorial implements TableCellRenderer {

	DefaultTableCellRenderer renderer;
	private static final Color EVEN_ROW_COLOR = new Color(241, 245, 250);
	
	public RenderizadorTablaHistorial(JTable table)
	{
	    renderer = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	{
		
		JLabel etiqueta = new JLabel();
        etiqueta.setOpaque(true);
        etiqueta.setToolTipText("Puede hacer doble click para agrandar el Resultado del Detalle.");
    
    
        if(column == 1)
        {
        	etiqueta.setHorizontalAlignment(JLabel.RIGHT);
        }
        if(column == 2)
        {
        	value = " " + value;
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

