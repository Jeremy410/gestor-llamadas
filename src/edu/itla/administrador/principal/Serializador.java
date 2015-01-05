package edu.itla.administrador.principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializador {

	public void serializar(String objeto, String nombreArchivo)
	{
		try 
		{
			FileOutputStream salidaArchivo = new FileOutputStream(nombreArchivo);
			ObjectOutputStream salidaObjeto = new ObjectOutputStream(salidaArchivo);
			salidaObjeto.writeObject(objeto);
			salidaObjeto.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String deserializar(String nombreArchivo)
	{
		String retorno = "";
	try
		{
			FileInputStream entradaArchivo = new FileInputStream(nombreArchivo);
			ObjectInputStream entradaObjeto = new ObjectInputStream(entradaArchivo);
			retorno = String.valueOf(entradaObjeto.readObject());
			entradaObjeto.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return retorno;
	}
}
