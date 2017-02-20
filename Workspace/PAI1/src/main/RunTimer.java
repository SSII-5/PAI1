package main;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RunTimer extends TimerTask{
	
	public void run() {
		
		//Lee el fichero de configuracion
		Reader.ReadConf();
		
		//cada 30 dias de ejecucion del programa, hace llamada al metodo generateInform (esa llamada se pone donde "task")
		Date primeraVez = new Date(System.currentTimeMillis());
		Timer informe = new Timer();
		long tiempoEntreEjecuciones = 86400000 * Reader.ReadIntervalFromConf(); 
		informe.schedule(task, primeraVez, tiempoEntreEjecuciones);
		
	}

//aqui es de donde coge la clase Temporizador.java la tarea a realizar. 
//Es decir, aquí es donde se supone que debería de ejecutarse toda la funcionalidad del sistema (es la clase Main vaya)

}