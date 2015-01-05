package edu.itla.administrador.principal;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public abstract class Mantenimiento extends JPanel
{
	private static final long serialVersionUID = 1L;
	public abstract void crearBuscador(Buscador buscador);
	public abstract ImageIcon getImagen(String nombreImagen);
	public abstract void ajustarTamañoColumnas();
}
