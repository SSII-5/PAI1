package main;

import java.util.Date;
import java.util.Timer;

public class Temporizador {
	
public static void main (String[] args) {
		
		Date runTime = new Date(System.currentTimeMillis());
		
		// ahora cogemos el tiempo del intervalo (en milisegundos)
		Integer tiempoRepeticion = 86400000 * Reader.ReadIntervalFromConf(); 
		
		FileGenerator.hashedFiles();
		
		// Programamos el temporizador para que el programa se ejecute cada dia que toque 
		Timer temporizador = new Timer();
		temporizador.schedule(new RunTimer(), runTime, (long) tiempoRepeticion);
		
	}

}
