package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 

public class CryptoUtils {
    private static final String TRANSFORMATION = "AES";
    private static SecretKey key;
    private static byte[] iv;
 
    public static void encrypt(File inputFile, File outputFile) throws Exception {
        doCrypto(Cipher.ENCRYPT_MODE, inputFile, outputFile);
    }
 
    public static void decrypt(File inputFile, File outputFile) throws Exception{
        doCrypto(Cipher.DECRYPT_MODE, inputFile, outputFile);
    }
 
    private static void doCrypto(int cipherMode,  File inputFile,
            File outputFile) throws Exception  {
    	Cipher cipher = null;
        try {
        	
        	cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       	
        	
            if (cipherMode ==  Cipher.DECRYPT_MODE ){
            	IvParameterSpec initVector = new IvParameterSpec(iv);
            	cipher.init(cipherMode, CryptoUtils.key, initVector);
            }else{
            	cipher.init(cipherMode, CryptoUtils.key);
            }
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
             
            byte[] outputBytes = cipher.doFinal(inputBytes);
             
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
             
            inputStream.close();
            outputStream.close();
            iv = cipher.getIV();
             
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new Exception("Error encrypting/decrypting file", ex);
        }
    }
    
  	
  	// Se crea la clave de encriptación de los archivos.
  	public static void obtainKey() throws NoSuchAlgorithmException{
  		
  		
  		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
  		keyGen.init(128);
  		SecretKey secretKey = keyGen.generateKey();
  		
  		
  		CryptoUtils.key = secretKey;
  	}
  	
  	public static SecretKey getKey(){
  		return CryptoUtils.key;
  	}
    
//        public static String encrypt(String key, String initVector, String value) {
//            try {
//                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
//                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//
//                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//
//                byte[] encrypted = cipher.doFinal(value.getBytes());
//                System.out.println("encrypted string: "
//                        + Base64.encodeBase64String(encrypted));
//
//                return Base64.encodeBase64String(encrypted);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//            return null;
//        }
//
//        public static String decrypt(String key, String initVector, String encrypted) {
//            try {
//                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
//                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//
//                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//
//                byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
//
//                return new String(original);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//            return null;
//        }
  	
}