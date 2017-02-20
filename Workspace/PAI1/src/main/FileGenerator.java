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
	
	public static File hashedFiles(){
		File result;
		List<String> files;
		List<String> hashes = new ArrayList<String>();
		List<String> oldHashes = null;
		
		// Obtiene la lista de archivos del lector de configuración
		files = Reader.ReadFilesFromConf();
		Hasher.type = Reader.ReadMethodFromConf();
		// Los hashea
		for (String file: files){
			Path filepath = Paths.get(executionPath ,file);
			String hash = Hasher.hashFile(filepath.toString());
			hashes.add(hash);
		}
		
		//Busca/Designa el path abstracto para el archivo de los hashes independientemente del SO.
		Path path = Paths.get(executionPath, "SecureHashes.txt");
		//Ahora tiene que hacer dos cosas. Comprobar si ese archivo tiene cosas y después encriptar nuestros hashes.
		//La clave se va a suponer que siempre es la misma, puesto que el sistema va a tener una sola ejecución, aún así, hay formas de guardar la key mejor
		if (days != 0){
			oldHashes = readEncryptedHashes(path);
		}
		
		generateIndicators(hashes, oldHashes);
		
		
		result = writeEncryptedHashes(path, hashes);
		
		return result;
	}
	
	//Lee el archivo encriptado otro día. 
	private static List<String> readEncryptedHashes(Path path){
		File file = path.toFile();
		List<String> hashes = null;
		try {
			CryptoUtils.decrypt(CryptoUtils.getKey().toString(), file, file);
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
			CryptoUtils.encrypt(CryptoUtils.getKey().toString(), result, result);
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
		Calendar time;
		Double ratio;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		List<String> lines = new ArrayList<String>();
		Path file = Paths.get(executionPath, "indicadores.txt");
		
		// Si es el primer día de ejecución, introduce las cabeceras
		if (days == 0){
			lines.add("Día				Ratio					Tendencia");
		}
		
		time = Calendar.getInstance();
		ratio = compareHashings(hashes, oldHashes);
		results = format.format(time)+ "			|	" + ratio.toString() + "				|	" + getTendency(ratio).toString();
		lines.add(results);
		try {
			Files.write(file, lines, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
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
	
		failed.addAll(hashes);
		tester.addAll(hashes);
		if (!oldHashes.isEmpty()){
			tester.retainAll(oldHashes);
			failed.removeAll(tester);
			if(tester.size() == oldHashes.size()){
				success = true;
			}else{
				success = false;
			}
			if (!success){
				createIncidence(failed);
			}
		}
		result = new Double(tester.size()/hashes.size());
		
		return result;
	}
	
	private static Character getTendency(Double ratio){
		Path file = Paths.get(executionPath, "indicadores.txt");
		String lastLine;
		char result = "e".charAt(0);
		
		
		try {
			List<String> lines = Files.lines(file).collect(Collectors.toList());
			lastLine = lines.get(lines.size()-1);
			String[] params = lastLine.split("|");
			Double prevRat = new Double(params[1].trim());
			if (prevRat < ratio) {
				result = "u".charAt(0);
			}else if(prevRat == ratio){
				result = "e".charAt(0);
			}else
				result = "d".charAt(0);
			
		} catch (IOException e) {
			createError(e.getMessage());
		}
		
		return result;
	}
	
	private static void createIncidence(List<String> missing){
		
		List<String> lines = new ArrayList<String>();
		Calendar moment;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm");
		String date;
		String time;
		
		moment = Calendar.getInstance();
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
		Calendar moment;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm");
		String date;
		String time;
		
		moment = Calendar.getInstance();
		date = formatter.format(moment);
		time = formatter2.format(moment);
		
		String filename = "errors.txt";
		String executionPath = System.getProperty("user.dir");
		Path file = Paths.get(executionPath, filename);
		
		try {
			lines = Files.lines(file).collect(Collectors.toList());
		} catch (IOException e) {
			lines.add("Día 				Hora				Fichero no Íntegro");
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
