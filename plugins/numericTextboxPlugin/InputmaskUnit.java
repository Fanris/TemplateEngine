package de.tet.inputmaskTemplate.plugins.numericTextboxPlugin;

/**
 * Data structure to manage Unit-Selection within the NumericTextboxPlugin. Contains
 * values for Unit recognition and valid min/max values of the Field.
 * @author Martin Predki
 *
 */
public class InputmaskUnit {
	private String id = "";
	private String html = "";
	private String unit = "";
	private String prefix = "";
	private double min = Double.MIN_VALUE;
	private double max = Double.MAX_VALUE;
	
	/**
	 * Returns the Unit-ID
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the Unit-ID
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the HTML-Representation of this Unit.
	 * @return
	 */
	public String getHtml() {
		return html;
	}
	
	/**
	 * Sets the HTML-Representation of this Unit.
	 * @param html
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/**
	 * Returns the minimal allowed value.
	 * @return
	 */
	public double getMin() {
		return min;
	}
	
	/**
	 * Sets the minimal allowed value.
	 * @param min
	 */
	public void setMin(double min) {
		this.min = min;
	}
	
	/**
	 * Returns the maximal allowed value.
	 * @return
	 */
	public double getMax() {
		return max;
	}
	
	/**
	 * Sets the maximal allowed valued.
	 * @param max
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * Returns the Unit-String.
	 * @return
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the Unit-String
	 * @param unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Returns the ID of the {@link UnitPrefix}.
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the ID of the {@link UnitPrefix}.
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}			
}
