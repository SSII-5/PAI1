package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FileGenerator {

	
	public static int days = 0;
	public static String executionPath;
	public static String hashHash;
	
	public static void hashedFiles(){
		File result;
		List<String> files;
		List<String> hashes = new ArrayList<String>();
		List<String> oldHashes = null;
		
		// Obtiene la lista de archivos del lector de configuración
		files = Reader.ReadFilesFromConf();
		Hasher.type = Reader.ReadMethodFromConf();
		// Los hashea
		for (String file: files){
			String hash = Hasher.hashFile(file);
			hashes.add(hash);
		}
		
		//Busca/Designa el path abstracto para el archivo de los hashes independientemente del SO.
		Path path = Paths.get(executionPath, "SecureHashes.txt");
		//Ahora tiene que hacer dos cosas. Comprobar si ese archivo tiene cosas y después encriptar nuestros hashes.
//		try {
//			oldHashes = Files.lines(path).collect(Collectors.toList());
//		} catch (IOException e) {
//			
//		}

		oldHashes = FileGenerator.readEncryptedHashes(path);
		
		generateIndicators(hashes, oldHashes);
		
		FileGenerator.writeEncryptedHashes(path, hashes);
		
//		try {
//			Files.write(path, hashes, Charset.forName("UTF-8"));
//		} catch (IOException e) {
//			createError(e.getMessage());
//		}
		
	}
	
	//Lee el archivo encriptado otro día. 
	private static List<String> readEncryptedHashes(Path path){
		File file = path.toFile();
		List<String> hashes = null;
		try {
			CryptoUtils.decrypt(file, file);
			hashes = Files.lines(path).collect(Collectors.toList());
			
		} catch (Exception e) {
			createError(e.getMessage());
		}
		return hashes;
		
	}
	
	//Encripta los hashings
	private static File writeEncryptedHashes(Path path, List<String> hashes){
		File result = new File(path.toString());
		try {
			Files.write(path, hashes, Charset.forName("UTF-8"));
			CryptoUtils.encrypt(result,result);
		} catch (IOException e) {
			createError(e.getMessage());
		}
		 catch (Exception e) {
				createError(e.getMessage());
		}
		return result;
	}
	
	
	//Este método es el principal para generar los archivos, pero se tienen que hacer más métodos, porque hay que modularizar o nos vamos a la puta
	private static void generateIndicators(List<String> hashes, List<String> oldHashes){
		
		String results = "";
		Date time;
		Double ratio;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		List<String> lines = new ArrayList<String>();
		Path file = Paths.get(executionPath, "indicadores.txt");
		
		// Si es el primer día de ejecución, introduce las cabeceras
		try {
			lines = Files.lines(file).collect(Collectors.toList());
		} catch (IOException e) {
			lines.add("Día				Ratio				Tendencia");
		}
		
		time = new Date();
		ratio = compareHashings(hashes, oldHashes);
		results = format.format(time)+ "			|	" + ratio.toString() + "			|	" + getTendency(ratio).toString();
		lines.add(results);
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			createError(e.getMessage());
		}
		
	}
	
	//Tratamiento de las dos listas de Hashing para sacar indicadores
	private static Double compareHashings(List<String> hashes, List<String> oldHashes){
		Double result;
		boolean success;
		List<String> tester = new ArrayList<String>();
		List<String> failed = new ArrayList<String>();
		Double tsize ;
		Double hsize = new Double(hashes.size());
	
		failed.addAll(hashes);
		tester.addAll(hashes);
		tsize = new Double(tester.size());
		
		if(oldHashes != null){
			if (!oldHashes.isEmpty()){
				tester.retainAll(oldHashes);
				failed.removeAll(tester);
				tsize = new Double(tester.size());
				if(tester.size() == oldHashes.size()){
					success = true;
				}else{
					success = false;
				}
				if (!success){
					createIncidence(failed);
				}
			}
		}
		result = new Double(tsize/hsize);
		
		return result;
	}
	
	private static Character getTendency(Double ratio){
		Path file = Paths.get(executionPath, "indicadores.txt");
		String lastLine;
		char result = "=".charAt(0);
		
		
		try {
			List<String> lines = Files.lines(file).collect(Collectors.toList());
			lastLine = lines.get(lines.size()-1);
			String[] params = lastLine.split("\\|");
			Double prevRat = new Double(params[1].trim());
			if (prevRat < ratio) {
				result = "+".charAt(0);
			}else if(prevRat == ratio){
				result = "=".charAt(0);
			}else if(prevRat > ratio){
				result = "-".charAt(0);
			}
			
		} catch (IOException e) {
			createError(e.getMessage());
		}
		
		return result;
	}
	
	private static void createIncidence(List<String> missing){
		
		List<String> lines = new ArrayList<String>();
		Date moment;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm");
		String date;
		String time;
		
		moment = new Date();
		date = formatter.format(moment);
		time = formatter2.format(moment);
		
		String filename = Reader.ReadIncidenceFileFromConf();
		String executionPath = System.getProperty("user.dir");
		Path file = Paths.get(executionPath, filename);
		
		try {
			lines = Files.lines(file).collect(Collectors.toList());
		} catch (IOException e) {
			lines.add("Día 				Hora				Fichero no Íntegro");
		}
		for(String hash: missing){
			lines.add(date + "			|	" + time + "			|	" + hash.split(":")[0].trim()); //Coge solo el nombre del archivo
		}
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			
			createError(e.getMessage());
		}
		
		
	}
	
	public static void createError(String error){
		
		List<String> lines = new ArrayList<String>();
		Date moment;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm");
		String date;
		String time;
		
		moment = new Date();
		date = formatter.format(moment);
		time = formatter2.format(moment);
		
		String filename = "errors.txt";
		String executionPath = System.getProperty("user.dir");
		Path file = Paths.get(executionPath, filename);
		
		try {
			lines = Files.lines(file).collect(Collectors.toList());
		} catch (IOException e) {
			lines.add("Día 				Hora				Tipo de error");
		}
			lines.add(date + "			|	" + time + "			|	" + error); 
		
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			createError(e.getMessage());
		}
		
		
	}

	public static void setExecutionPath(){
		FileGenerator.executionPath = System.getProperty("user.dir");
	}
}
