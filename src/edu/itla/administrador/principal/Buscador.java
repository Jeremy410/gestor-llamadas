package edu.itla.administrador.principal;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Buscador 
{
	private static Buscador instancia = null;
	
	public static Buscador getInstancia()
	{
		if(instancia == null)
		{
			instancia = new Buscador();
		}
		return instancia;
	}
	public void buscar(JTable table, String valorBusqueda)
	{
		TableRowSorter <TableModel> modeloOrdenado = new TableRowSorter <TableModel> (table.getModel());
		table.setRowSorter(modeloOrdenado);
		int cantColum = table.getColumnCount();
		
		for (int i = 0; i < cantColum; i++)
		{
			modeloOrdenado.setRowFilter(RowFilter.regexFilter("(?i)"+valorBusqueda, i));
			int count = table.getRowCount();
			if (count < 1) 
			{
				continue;
			}
			else
			{
				break;
			}
		}
		table.setModel(table.getModel());
	}
	/*public void buscar(JTable table, String valorBusqueda, int colum)
	{
		TableRowSorter <TableModel> modeloOrdenado = new TableRowSorter <TableModel> (table.getModel());
		table.setRowSorter(modeloOrdenado);
		modeloOrdenado.setRowFilter(RowFilter.regexFilter(valorBusqueda, colum));
		table.setModel(table.getModel());
	}*/
}
