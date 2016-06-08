package br.ufc.great.greattour.utils.logger;

/**
 * A LogEntry is a structure to represent an action or situation in the application
 * and the time it took place during the usage of the application.
 * @author Edmilson Rocha
 *
 */
public class LogEntry {
	private String entryName;
	private String entryTime;


	public LogEntry(String entryName, String entryTime) {
		setEntryName(entryName);
		setEntryTime(entryTime);
	}


	public String getEntryName() {
		return entryName;
	}


	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}


	public String getEntryTime() {
		return entryTime;
	}


	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	@Override
	public String toString() {
		return entryName + "\t \t - " + entryTime;
	}



}
