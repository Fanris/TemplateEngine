package de.tet.inputmaskTemplate.logging;

/**
 * Possible Log-Types
 * @author Martin Predki
 *
 */
public enum LogType {
	ERROR("ERROR"), LOG("LOG"), MESSAGE("MESSAGE"), DEBUGINFO("DEBUGINFO");

	private final String myLogType;

	private LogType(String logType) {
		myLogType = logType;
	}

	@Override
	public String toString() {
		return myLogType;
	}
}
