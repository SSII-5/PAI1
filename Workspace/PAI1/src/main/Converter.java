package main;

public class Converter {
	
	
    private static String[] hexDigits ={"0", "1", "2", "3","4", "5", "6", "7","8", "9", "a", "b","c", "d", "e", "f"};
    //Convierte un byte a una cadena hexadecimal
	public static String byteToHexString(byte b)
	{
        int n=b;
        if(n <0) n=256 +n;
	    int d1= n/16;
	    int d2=n%16;
	    return hexDigits[d1]+hexDigits[d2];
     }
     //Convierte un array de bytes a una cadena hexadecimal
     public static String byteArrayToHexString(byte[] b) {
          String result="";
      for(int i=0; i< b.length;++i)
	result+= byteToHexString(b[i]);
	return result;
     }
}       

