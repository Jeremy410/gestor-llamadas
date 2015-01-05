package edu.itla.administrador.conexion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Configuracion {

	public  String leerConfiguracion() {
		String lectura = null;
		BufferedReader lector = null;
		
		try
		{
			File archivo = new File("config.cfg");
			lector = new BufferedReader(new FileReader(archivo));
			StringBuilder sb = new StringBuilder();
			String linea = lector.readLine();

			while (linea != null) 
			{
				sb.append(linea);
				sb.append('\n');
				linea = lector.readLine();
			}
			lectura = sb.toString();
			lector.close();
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ARCHIVO NO ENCONTRADO", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lectura;
	}

	public  String obtenerValor(String configuracion) 
	{
			String retorno = null;
			String split[] = leerConfiguracion().split("\n");
			
			for(int i = 0; i < split.length; i++)
			{
				String splitVal[] = split[i].split("=");
				if (configuracion.equals(splitVal[0]))
				{
					retorno = splitVal[1];
					break;
				}
			}
			return retorno;
		}
	}