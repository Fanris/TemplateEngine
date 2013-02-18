package de.tet.inputmaskTemplate.logging;

import java.util.Date;

/**
 * Objects of this class are used to saved data for Debug or error logs. The Logs containing values
 * for the Logtype, the date on which the occured, the function in which they occured, a message and 
 * a log-level. The log-level should indicate the importance of the log. E.g. Errors should be very 
 * important (log-level: 1), some Debug-Info should not be important (log-level > 4).
 * @author Martin Predki
 *
 */
public class Log {
	private LogType type;
	private Date date;
	private String function;
	private String message;
	private int level;
	
	public Log(LogType type, String function, String message, int level)
	{
		this.type = type;
		this.function = function;
		this.message = message;
		this.level = level;
		this.date = new Date();
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String toString()
	{
		String error = "";
		
		error = this.date.toString() + " " + "(" + this.type.toString() + ") ";
		error += "in function " + this.function + " (Log-Level: " + this.level + "): ";
		error += this.message;
		
		return error;
	}
}
