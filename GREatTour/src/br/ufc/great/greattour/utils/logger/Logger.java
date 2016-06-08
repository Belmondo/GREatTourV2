package br.ufc.great.greattour.utils.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

/**
 * Statically records certain actions from the user and outputs them to a .txt
 * file. Uses an ArrayLits of LogEntries, which is a structure that represents
 * an action or situation and the time that it took place. (HH:MM:SS:mmm format)
 *
 * @author Edmilson Rocha
 *
 */
public class Logger {
	private static ArrayList<LogEntry> entries;
	private static String completeLog;
	private static Calendar calendar;
	private static boolean logging;

	static {
		completeLog = "ACTION'S NAME \t - TIME (HH:MM:SS:mmm FORMAT)\n";
		entries = new ArrayList<LogEntry>();
		turnLoggingOn();
	}

	public static void addEntry(String entryName) {
		Log.i("LOGGER", "What...");
		if (isLogging()) {
			calendar = Calendar.getInstance();
			String entryTime = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + ":" + calendar.get(Calendar.MILLISECOND);
			LogEntry entry = new LogEntry(entryName, entryTime);
			entries.add(entry);
			insertEntryIntoLog(entry);
		}
	}

	private static void turnLoggingOn() {
		logging = true;
	}

	private static void turnLoggingOff() {
		logging = false;
	}

	private static void insertEntryIntoLog(LogEntry entry) {
		completeLog = completeLog + "\n" + entry;
		exportLog();
	}

	/**
	 * Generates the .txt file.
	 */
	public static void exportLog() {
		Log.i("LOGGER", "Exporting log...");
		try {
	        File gpxfile = new File("/sdcard/GREatTour2_LogHistory.txt");
	        FileWriter writer = new FileWriter(gpxfile, true);
	        for (LogEntry le : entries){
	        	writer.append(le.toString());
	        }

	        writer.flush();
	        writer.close();
			Log.i("LOGGER", "Log Exported.");
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("LOGGER", "Log Failed...");
		}
	}

	public static boolean isLogging() {
		return logging;
	}

}
