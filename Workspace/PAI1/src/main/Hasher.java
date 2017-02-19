package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
	
	

	
	public static String type;


	public static String hashFile(String file){
		String result = null;
		
		try {
			InputStream fis =  new FileInputStream(file);

		    byte[] buffer = new byte[8192];
		    MessageDigest complete;
		    byte[] hash;
		    
		    
			complete = MessageDigest.getInstance(type);
			
			
			int numRead;
		     do {
		      numRead = fis.read(buffer);
		      if (numRead > 0) {
		        complete.update(buffer, 0, numRead);
		        }
		      } while (numRead != -1);
		     fis.close();
		     hash = complete.digest();
		     result = Converter.byteArrayToHexString(hash);
		     return result;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Este algoritmo no está soportado por el sistema, por favor, pruebe con otro distinto.");
		} catch (IOException e) {
			System.out.println("El sistema no ha podido acceder al archivo: "+ file);
		}
		return file + ": " + result;
	     
	}
	
	
}
