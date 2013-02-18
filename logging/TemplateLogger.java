package de.tet.inputmaskTemplate.logging;

import java.util.ArrayList;

import de.tet.inputmaskTemplate.client.logic.Callback;


/**
 * A static class to create and save debug- or error-logs. Contains a List for Logs and a
 * list for callback-Function which are executed whenever a new log ist saved. The callback functions should
 * be set by the application which uses the Inputmask-Template to show logs to the user or for debugging.
 * @author Martin Predki
 *
 */
public class TemplateLogger {
	private static ArrayList<Log> logs = new ArrayList<Log>();
	private static ArrayList<Callback<Log>> callbacks = new ArrayList<Callback<Log>>();
	
	/**
	 * Adds a new Log to the list.
	 * @param log
	 */
	public static void addLog(Log log)
	{
		logs.add(log);
		callCallbacks(log);
	}
	
	/**
	 * Adds a new Log to the list.
	 * @param logType
	 * @param function
	 * @param message
	 * @param level
	 */
	public static void addLog(LogType logType, String function, String message, int level)
	{
		Log l = new Log(logType, function, message, level);
		logs.add(l);
		
		callCallbacks(l);
	}
	
	/**
	 * Adds a callback-function which is executed whenever an new log ist added.
	 * @param c
	 */
	public static void addCallback(Callback<Log> c)
	{
		callbacks.add(c);
	}
	
	/**
	 * Returns an array with all logs.
	 * @return
	 */
	public static Log[] getLogs()
	{
		return logs.toArray(new Log[0]);
	}
	
	/**
	 * returns the last added Log
	 * @return
	 */
	public static Log getLastLog()
	{
		return logs.get(logs.size() - 1);
	}
	
	/**
	 * Calls all callback-functions for the given log. 
	 * @param log
	 */
	private static void callCallbacks(Log log)
	{
		if(!callbacks.isEmpty())
			for(Callback<Log> c: callbacks)
				c.call(log);
	}
}
