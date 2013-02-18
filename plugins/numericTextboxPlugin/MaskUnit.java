package de.tet.inputmaskTemplate.plugins.numericTextboxPlugin;

/**
 * Data structure which is used for Unit-Management within the GUI. Contains the values 
 * loaded from the Layout-File
 * @author Martin Predki
 *
 */
public class MaskUnit {
	private String defaultPrefix = "";
	private String[] availablePrefixes;
	private double min = Double.MIN_VALUE;
	private double max = Double.MAX_VALUE;
	
	/**
	 * Returns the defaultPrefix for this {@link NumericTextboxInputmaskField}
	 * @return
	 */
	public String getDefaultPrefix() {
		return defaultPrefix;
	}
	
	/**
	 * Sets the defaultPrefix for this {@link NumericTextboxInputmaskField}
	 * @param defaultPrefix
	 */
	public void setDefaultPrefix(String defaultPrefix) {
		this.defaultPrefix = defaultPrefix;
	}
	
	/**
	 * Returns the minimal Value for this {@link NumericTextboxInputmaskField}
	 * @return
	 */
	public double getMin() {
		return min;
	}
	
	/**
	 * Sets the minimal Value for this {@link NumericTextboxInputmaskField}
	 * @param min
	 */
	public void setMin(double min) {
		this.min = min;
	}
	
	/**
	 * Returns the maximal Value for this {@link NumericTextboxInputmaskField}
	 * @return
	 */
	public double getMax() {
		return max;
	}
	
	/**
	 * Sets the maximal Value for this {@link NumericTextboxInputmaskField}
	 * @param max
	 */
	public void setMax(double max) {
		this.max = max;
	}	
	
	/**
	 * Returns a list with all IDs of the available {@link UnitPrefix}.
	 * @return
	 */
	public String[] getAvailablePrefixes()
	{
		return this.availablePrefixes;
	}
	
	/**
	 * Sets a list with all IDs of the available {@link UnitPrefix}.
	 * @param availablePrefixes
	 */
	public void setAvailablePrefixes(String[] availablePrefixes)
	{
		this.availablePrefixes = availablePrefixes;
	}
}
