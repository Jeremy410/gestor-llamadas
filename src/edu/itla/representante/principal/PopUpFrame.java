package edu.itla.representante.principal;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class PopUpFrame extends JFrame 
{
	private static final long serialVersionUID = 1L;

	public PopUpFrame(JTable tabla) 
	{
		try {
			setTitle("Resultado Detalle");
			JTextArea areaTexto = new JTextArea();
			areaTexto.setLineWrap(true);
			JScrollPane scrollPane = new JScrollPane(areaTexto);
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			areaTexto.setText((String)tabla.getValueAt(tabla.getSelectedRow(), 3));
			setSize(530,289);
			setVisible(true);
			setLocationRelativeTo(null);
		} catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(this, "Esta fila es invalida. No se puede seleccionar. ","FILA INCORRECTA" ,JOptionPane.ERROR_MESSAGE);	
		}
	}
}
