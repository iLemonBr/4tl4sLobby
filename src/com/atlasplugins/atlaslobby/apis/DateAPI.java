package com.atlasplugins.atlaslobby.apis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateAPI {

	public static String getCurrentDate() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo")); // gets current instance of the calendar
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
		String full = date.format(calendar.getTime()) + " às " + hora.format(calendar.getTime());
		return full;
	}
	
}
