package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {

	
	//Lee el fichero de configuración y saca varias Strings
	public static String[] ReadConf(){
		
		String content = null;
		try{
			content = new Scanner(new File("./configuration.txt")).useDelimiter("\\Z").next();
		}catch(IOException e){
			
			FileGenerator.createError(e.getMessage());
		}
		
		String[] parts = content.split(";");
		
		
		return parts;
	}
	
	
	//extrae la lista de nombres de archivos del fichero de configuración.
	public static List<String> ReadFilesFromConf(){
		
		List<String> res = new ArrayList<String>();
		
		String files = ReadConf()[0].trim();
		
		String[] parts = files.split(",");
		
		for(int i=0;i<parts.length;i++){
			
			res.add(parts[i]);
		}
		
		return res;
		
	}
	
	
	//extrae el método de encriptación de la configuración
	public static String ReadMethodFromConf(){
		
		String res = ReadConf()[1].trim();
		return res;
	}
	
	//extrae el intervalo en días de la configuración
	public static Integer ReadIntervalFromConf(){
		
		String str = ReadConf()[2].trim();
		Integer res = new Integer(str);
		
		return res;
	}
	
	
	//extrae el nombre del archivo de incidendias de la configuración
	public static String ReadIncidenceFileFromConf(){
		
		String res = ReadConf()[3].trim();
		return res;
	}
	
}
