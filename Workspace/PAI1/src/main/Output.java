package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Output {
	
	//TODO Hallar una mejor forma de guardar la clave que en un static. O asignarle el mismo valor siempre. Es AES-128, tiene suficiente seguridad en principio
	private static SecretKey key;
	private static int days = 0;

	private File hashedFiles(){
		File result;
		List<String> files;
		List<String> hashes = new ArrayList<String>();
		List<String> oldHashes;
		
		// Obtiene la lista de archivos del lector de configuración
		files = Reader.ReadFilesFromConf();
		Hasher.type = Reader.MethodFromConf();
		// Los hashea
		for (String file: files){
			String hash = Hasher.hashFile(file);
			hashes.add(hash);
		}
		
		//Busca/Designa el path abstracto para el archivo de los hashes independientemente del SO.
		Path path = Paths.get("SecureHashes.txt");
		//Ahora tiene que hacer dos cosas. Comprobar si ese archivo tiene cosas y después encriptar nuestros hashes.
		//La clave se va a suponer que siempre es la misma, puesto que el sistema va a tener una sola ejecución, aún así, hay formas de guardar la key mejor
		if (days != 0){
			oldHashes = readEncryptedHashes(path);
		}
		
		//TODO Aquí hay que meter el método que te genera los informes, todos ellos.
		
		
		result = writeEncryptedHashes(path, hashes);
		
		return file1;
	}
	
	//Lee el archivo encriptado otro día. 
	private List<String> readEncryptedHashes(Path path){
		File file = path.toFile();
		List<String> hashes = null;
		try {
			CryptoUtils.decrypt(key.toString(), file, file);
			hashes = Files.lines(path).collect(Collectors.toList());
			
		} catch (Exception e) {
			System.out.println("No se pudo desencriptar el archivo");
		}
		return hashes;
		
	}
	
	//Encripta los hashings
	private File writeEncryptedHashes(Path path, List<String> hashes){
		File result = new File(path.toString());
		
		try {
			Files.write(path, hashes, Charset.forName("UTF-8"));
			CryptoUtils.encrypt(key.toString(), result, result);
		} catch (IOException e) {
			System.out.println("No se pudo escribir el archivo");
		}
		 catch (Exception e) {
			System.out.println("No se ha podido encriptar el archivo");
		}
		return result;
	}
	
	
	//Este método es el principal para generar los archivos, pero se tienen que hacer más métodos, porque hay que modularizar o nos vamos a la puta
	private File generateIndicators(String filename, List<String> hashes, List<String> oldHashes){
		
		List<String> lines = new ArrayList<String>();
		String executionPath = System.getProperty("user.dir");
		Path file = Paths.get(executionPath, filename);
		
		// Si es el primer día de ejecución, introduce las cabeceras
		if (days == 0){
			lines.add("Día				Ratio					Tendencia");
		}
		//TODO Meter la información de debajo de las cabeceras, en compare Hashings ya está calculándose el ratio, queda la tendencia
		// Que sería leyendo la última línea del archivo.
		
		
		Files.write(file, lines, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
		
		
		return result;
	}
	
	//Tratamiento de las dos listas de Hashing para sacar indicadores
	private Double compareHashings(List<String> hashes, List<String> oldHashes){
		Double result;
		boolean success;
		List<String> tester = new ArrayList<String>();
		// Esto creo que es intuitivo, pero por si acaso: Se crea una lista que se rellena con los últimos hashing. 
		// Se compara con la antigua guardando los que coincidan. Si no coinciden, el tamaño será menor.
		// Faltaría una lista que en caso de que no coincidan al 100% te diga el archivo que falla (Que estará en oldHashes)
		tester.addAll(oldHashes);
		tester.retainAll(hashes);
		if(tester.size() == hashes.size()){
			success = true;
		}else{
			success = false;
		}
		if (!success){
			//TODO Crear el archivo de incidencias si no está creado, añadirle una línea si lo está. En otro método nuevo
		}
		result = new Double(tester.size()/hashes.size());
		
		return result;
	}
	
	
	// Se crea la clave de encriptación de los archivos.
	private void obtainKey() throws NoSuchAlgorithmException{
		
		
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256); 
		SecretKey secretKey = keyGen.generateKey();
		
		
		Output.key = secretKey;
	}
	
	
	
	//El main LUL, Se suma un día al final. Hay que hacer que cuando lleve %30 = 0, crea un informe, es decir, pdf?
	public static void main(String[] args) {
		

		
		
		days++;
	}

}
