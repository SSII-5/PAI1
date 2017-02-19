package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Output {
	
	//TODO Hallar una mejor forma de guardar la clave que en un static. O asignarle el mismo valor siempre. Es AES-128, tiene suficiente seguridad en principio
	private static SecretKey key;

	private File hashedFiles(){
		
		List<String> files;
		List<String> hashes = new ArrayList<String>();
		
		// Obtiene la lista de archivos del lector de configuración
		files = Reader.ReadFilesFromConf();
		
		// Los hashea
		for (String file: files){
			String hash = Hasher.hashFile(file);
			hashes.add(hash);
		}
		
		//Busca/Designa el path abstracto para el archivo de los hashes independientemente del SO.
		Path file = Paths.get("SecureHashes.txt");
		//Ahora tiene que hacer dos cosas. Comprobar si ese archivo tiene cosas y después encriptar nuestros hashes.
		//La clave se va a suponer que siempre es la misma, puesto que el sistema va a tener una sola ejecución, aún así, hay formas de guardar la key mejor
		//TODO Sacar a métodos externos la lectura y escritura de las cosas en el PATH.
		
		
		try {
			Files.write(file, hashes, Charset.forName("UTF-8"));
		} catch (IOException e) {
			System.out.println("No se pudo escribir el archivo");
		}
		File file1 = new File(file.toString());
		
		try {
			CryptoUtils.encrypt(key.toString(), file1, file1);
		} catch (Exception e) {
			System.out.println("No se ha podido encriptar el archivo");
		}
		return file1;
	}
	
	
	
	
	private void obtainKey() throws NoSuchAlgorithmException{
		
		
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256); 
		SecretKey secretKey = keyGen.generateKey();
		
		
		Output.key = secretKey;
	}
	
	
	

	public static void main(String[] args) {
		

	}

}
