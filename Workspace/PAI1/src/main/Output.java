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
	

	
	
	
	// Se crea la clave de encriptación de los archivos.
	private void obtainKey() throws NoSuchAlgorithmException{
		
		
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256); 
		SecretKey secretKey = keyGen.generateKey();
		
		
		Output.key = secretKey;
	}
	
	
	
	//El main LUL, Se suma un día al final. Hay que hacer que cuando lleve %30 = 0, crea un informe, es decir, pdf?
	public static void main(String[] args) {
		

		
		
		FileGenerator.days++;
	}

}
