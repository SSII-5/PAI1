package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
	
	

	
	public static String type;


	public static String hashFile(String file){
		String hashed = null;
		String result;
		Path filepath = Paths.get(FileGenerator.executionPath ,file);
		try {
			InputStream fis =  new FileInputStream(filepath.toFile());

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
		     hashed = Converter.byteArrayToHexString(hash);
		} catch (NoSuchAlgorithmException e) {
			FileGenerator.createError(e.getMessage());
		} catch (IOException e) {
			FileGenerator.createError(e.getMessage());
		}
		result = file + ": " + hashed;
		return result;
	     
	}
	
	
}
