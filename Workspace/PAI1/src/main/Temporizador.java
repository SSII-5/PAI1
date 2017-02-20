package main;

import java.util.Date;
import java.util.Timer;

public class Temporizador {
	
public static void main (String[] args) {
		
		Date runTime = new Date(System.currentTimeMillis());
		
		// El temporizador actualiza cada 24h (una vez al dia)
		Integer tiempoRepeticion = 86400000 * Reader.ReadIntervalFromConf(); 
		
		// Programamos el temporizador para que el programa se ejecute cada dia que toque 
		Timer temporizador = new Timer();
		temporizador.schedule(new RunTimer(), runTime, (long) tiempoRepeticion);
		
	}

}
