package de.tet.inputmaskTemplate.plugins.mathPlugin;

/**
 * Static class which provides functions to convert Quantities to other Units.
 * @author Martin Predki
 *
 */
public class Converter {
	/**
	 * Multiply converter. Is used to convert Quantities by factors (e.g. mm -> m).
	 * @param q Quantity which should be converted
	 * @param convertFunction the convert function 
	 * @param exp an Exponent. E.g. 2 if mm^2 -> m^2.
	 * @return new Quantity with converted Value.
	 */
	public static Quantity MultiplyConvert(Quantity q, String convertFunction, double exp)
	{
		double factor =  convert(1, convertFunction);
		return new Quantity(q.getValue() * Math.pow(factor, exp), q.getUnit());
	}
	
	private static native double convert(double x, String func)
	/*-{
		var f = new Function('x', 'return ' + func + ';');
		return f(x);
	}-*/;

}
