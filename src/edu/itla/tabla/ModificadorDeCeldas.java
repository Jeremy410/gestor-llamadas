package edu.itla.tabla;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIDefaults;
import javax.swing.table.DefaultTableCellRenderer;

public class ModificadorDeCeldas extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	private JComponent componente = null;
	private static final Color EVEN_ROW_COLOR = new Color(241, 245, 250);

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	{
		componente = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if(row % 2 == 0)
		{
			componente.setBackground(EVEN_ROW_COLOR);
		}
		else
		{
			componente.setBackground(Color.WHITE);
		}
		if(isSelected)
		{
			UIDefaults defaults = javax.swing.UIManager.getDefaults();
			componente.setBackground(defaults.getColor("List.selectionBackground"));
		}
						
		return componente;
	}

}
