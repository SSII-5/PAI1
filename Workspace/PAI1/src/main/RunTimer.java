package main;

import java.util.TimerTask;

public class RunTimer extends TimerTask{
	
	public void run() {
		
		//Lee el fichero de configuracion
		Reader.ReadConf();	
		
	}

//aqui es de donde coge la clase Temporizador.java la "task" a realizar. 
//Es decir, aquí es donde se supone que debería de ejecutarse toda la funcionalidad del sistema.

}
