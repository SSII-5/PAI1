package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64.Encoder;

public class Hasher {
	
	
	public Hasher(String type){
		super();
		setType(type);
	}
	
	private String type;
	private static Encoder encoder;

	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String hashFile(String file){
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
		     result = encoder.encodeToString(hash);
		     return result;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Este algoritmo no está soportado por el sistema, por favor, pruebe con otro distinto.");
		} catch (IOException e) {
			System.out.println("El sistema no ha podido acceder al archivo: "+ file);
		}
		return result;
	     
	}
}
