package edu.itla.tabla;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ModeloDeTabla 
{
	private static final Color TABLE_GRID_COLOR = new Color(0xd9d9d9);

    public static void rellenarTabla(ResultSet rs, DefaultTableModel modelo, JTable tabla) 
    {
        try {
        	tabla.setModel(modelo);
        	diseñoTabla(tabla);
            configuracionColumnas(rs, modelo);
            vaciaFilasModelo(modelo);
            añadirFilasModelo(rs, modelo);
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDeTabla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
	public static void diseñoTabla(final JTable tabla)
	{	
		
		tabla.setAutoCreateColumnsFromModel(false);
		tabla.setFillsViewportHeight(true);
		tabla.setOpaque(false);
	    tabla.setShowGrid(false);
	    tabla.getTableHeader().setResizingAllowed(false);
	    tabla.getTableHeader().setReorderingAllowed(false);
	    tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    tabla.setGridColor(TABLE_GRID_COLOR);
	    tabla.setIntercellSpacing(new Dimension(0, 0));   
	}
	public static void rellenarTabla(ResultSet rs, JTable tabla) 
    { 
		try {
		DefaultTableModel modelo = new DefaultTableModel()
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		 	tabla.setModel(modelo);
            configuracionColumnas(rs, modelo);
            vaciaFilasModelo(modelo);
            añadirFilasModelo(rs, modelo);
		 	diseñoTabla(tabla);

        } catch (SQLException ex) {
            Logger.getLogger(ModeloDeTabla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void añadirFilasModelo(ResultSet rs, DefaultTableModel modelo) 
    {
        try {
            while (rs.next()) 
            {
                Object[] datosFila = new Object[modelo.getColumnCount()];
                for (int i = 0; i < modelo.getColumnCount(); i++) 
                {
                    datosFila[i] = rs.getObject(i + 1);                   
                }
                modelo.addRow(datosFila);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void vaciaFilasModelo(final DefaultTableModel modelo) 
    {
    	try {

            while (modelo.getRowCount() > 0) 
            {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void configuracionColumnas(final ResultSet rs, final DefaultTableModel modelo) throws SQLException 
    {
        ResultSetMetaData metaDatos = rs.getMetaData();

        int numeroColumnas = metaDatos.getColumnCount();

        Object[] etiquetas = new Object[numeroColumnas];
        for (int i = 0; i < numeroColumnas; i++) {
            etiquetas[i] = metaDatos.getColumnLabel(i + 1);
        }

        modelo.setColumnIdentifiers(etiquetas);
    } 
}

