package de.tet.inputmaskTemplate.plugins.mathPlugin;

@SuppressWarnings("serial")
/**
 * Class for Unit exception handling. Contains a list in which the Units which cause the Exception 
 * are stored.
 * @author Martin Predki
 *
 */
public class UnitException extends Exception {
	private Unit[] units;
	
	public UnitException(String s, Unit...units)
	{				
		super(s);
		this.units = units;	
	}
	
	public Unit[] getUnits()
	{
		return this.units;
	}
	
	public int getUnitCount()
	{
		return this.units.length;
	}
}
