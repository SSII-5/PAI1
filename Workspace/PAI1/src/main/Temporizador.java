package main;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Timer;

public class Temporizador {
	
public static void main (String[] args) {
		
		Date runTime = new Date(System.currentTimeMillis());
		
		FileGenerator.setExecutionPath();
		try {
			CryptoUtils.obtainKey();
		} catch (NoSuchAlgorithmException e) {
			FileGenerator.createError(e.getMessage());
		}
		
		
		// ahora cogemos el tiempo del intervalo (en milisegundos)
		Integer tiempoRepeticion = 86400000 * Reader.ReadIntervalFromConf(); 
		
		// Programamos el temporizador para que el programa se ejecute cada dia que toque 
		Timer temporizador = new Timer();
		temporizador.schedule(new RunTimer(), runTime, (long) tiempoRepeticion);
		
	}

}

