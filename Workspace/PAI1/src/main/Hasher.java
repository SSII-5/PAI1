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
			FileGenerator.createError(e.getMessage());
		} catch (IOException e) {
			FileGenerator.createError(e.getMessage());
		}
		return file + ": " + result;
	     
	}
	
	
}
