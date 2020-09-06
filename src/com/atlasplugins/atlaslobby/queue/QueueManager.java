package com.atlasplugins.atlaslobby.queue;

import java.util.HashMap;

public class QueueManager {

	public static HashMap<String,QueueServer> filas = new HashMap<String,QueueServer>();

	public static HashMap<String, QueueServer> getFilas() {
		return filas;
	}
	
}
