package edu.itla.administrador.principal;

import javax.swing.JPanel;

public abstract class Consulta extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public abstract void crearBuscador(Buscador buscador);
	public abstract void ajustarTamañoColumnas();
	
}
