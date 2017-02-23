package main;

import java.util.TimerTask;

public class RunTimer extends TimerTask{
	
	public void run() {
		
		FileGenerator.hashedFiles();
		
	}

//aqui es de donde coge la clase Temporizador.java la tarea a realizar. 
//Es decir, aquí es donde se supone que debería de ejecutarse toda la funcionalidad del sistema (es la clase Main vaya)

}