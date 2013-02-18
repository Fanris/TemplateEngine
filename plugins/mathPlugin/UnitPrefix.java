package de.tet.inputmaskTemplate.plugins.mathPlugin;

/**
 * A data structure for Unit-Prefixes.
 * @author Martin Predki
 *
 */
public class UnitPrefix {
	private String id;
	private String html;
	private double factor;
	
	/**
	 * Constructor
	 * @param id unique ID
	 * @param html Text-representation in HTML-Code.
	 * @param factor numeric factor that is used by conversion to SI-Units.
	 */
	public UnitPrefix(String id, String html, double factor)
	{
		this.setFactor(factor);
		this.setId(id);
		this.setHtml(html);
	}
	
	/**
	 * Returns the unique ID of this Prefix.
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the unique ID of this prefix.
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the factor of this prefix.
	 * @return
	 */
	public double getFactor() {
		return factor;
	}
	
	/**
	 * Sets the factor of this prefix.
	 * @param factor
	 */
	public void setFactor(double factor) {
		this.factor = factor;
	}
	
	/**
	 * Returns the HTML-Representation of this prefix.
	 * @return
	 */
	public String getHtml() {
		return html;
	}
	
	/**
	 * Sets the HTML-Representation of this prefix.
	 * @param html
	 */
	public void setHtml(String html) {
		this.html = html;
	}
	
}
