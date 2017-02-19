package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {

	private static String[] ReadConf(){
		
		String content = null;
		try{
			content = new Scanner(new File("./configuration.txt")).useDelimiter("\\Z").next();
		}catch(IOException e){
			
			System.out.println("Hubo un error leyendo el archivo de configuración. Compruebe el formato.");
		}
		
		String[] parts = content.split(";");
		
		
		return parts;
	}
	
	public static List<String> ReadFilesFromConf(){
		
		List<String> res = new ArrayList<String>();
		
		String files = ReadConf()[0];
		
		String[] parts = files.split(",");
		
		for(int i=0;i<parts.length;i++){
			
			res.add(parts[i]);
		}
		
		return res;
		
	}
	
	public static String MethodFromConf(){
		
		String res = ReadConf()[1];
		return res;
	}
	
	public static Integer IntervalFromConf(){
		
		String str = ReadConf()[2];
		Integer res = new Integer(str);
		
		return res;
	}
	
	public static String IncidenceFileFromConf(){
		
		String res = ReadConf()[3];
		return res;
	}
}
