package de.tet.inputmaskTemplate.plugins.mathPlugin;

import java.util.ArrayList;

import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;

/**
 * This class provides a structure to represent mathematical units an functions for unit calculation.
 * All Units could be represented by the exponents of the 5 base-Units s, m, kg, A, K, mol, cd. For a better
 * usage of the System, complex Units can be provided via an XML-File which is stored on the server (units.xml).
 * This XML-File must be placed within an "units" folder in the Portlet folder (e.g. ../units/units.xml).   
 * @author Martin Predki
 *
 */
public class Unit {
	public static enum SIUnit {
		s,
		m,
		kg,
		A,
		K,
		mol,
		cd
	}

	private String unitId = "";
	private String html = "";
	private String unitType = "";
	private String siUnit = "";
	private boolean isSiUnit;	
	private String convertToSIFunction = "";
	private String convertFromSIFunction = "";
	private float[] unitExponent = {0,0,0,0,0,0,0};
	private ArrayList<Unit> unitHistory = new ArrayList<Unit>();

	/**
	 * Standard Constructor.
	 */
	public Unit()
	{ }
	
	/**
	 * Constructor. Creates a Unit from the given String. If complex Units are used within
	 * the String, they must be provided via the units.xml file.
	 * @param unitString
	 */
	public Unit(String unitString)
	{
		String[] uString = unitString.split("\\s");
		for(String s : uString)
		{
			TemplateLogger.addLog(LogType.DEBUGINFO, "Unit(String)", "parse: " + s, 4);
			if(s.contains("^"))
			{
				TemplateLogger.addLog(LogType.DEBUGINFO, "Unit(String)", "contains ^...", 4);
				String[] inner = s.split("\\^");	
				
				for(int i = 0; i < inner.length; i++)
					TemplateLogger.addLog(LogType.DEBUGINFO, "Unit(String)", "Part " + i + ": " + inner[i], 4);
						
				float pow = Float.parseFloat(inner[1]);
				TemplateLogger.addLog(LogType.DEBUGINFO, "Unit(String)", "Power: " + pow, 4);
				
				Unit u = UnitLoader.getUnit(inner[0]);
				TemplateLogger.addLog(LogType.DEBUGINFO, "Unit(String)", "Unit: " + u.getUnitId(), 4);
				
				this.setUnitExponents(this.multiply(u.pow(pow)));
			}
			else
				this.setUnitExponents(this.multiply(UnitLoader.getUnit(s)));
		}
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	public ArrayList<Unit> getUnitHistory()
	{
		return this.unitHistory;
	}
	
	/**
	 * Sets if this unit is an SI-Unit.
	 * @param value
	 */
	public void setIsSiUnit(boolean value) {
		this.isSiUnit = value;
	}
	
	/**
	 * Sets the corresponding SI-Unit to this unit. E.g the
	 * corresponding SI-Unit for "bar" is "Pascal" (Pa).
	 * @param siUnit
	 */
	public void setSiUnit(String siUnit) {
		this.siUnit = siUnit;
	}

	/**
	 * Sets the Unit ID
	 * @param unitId
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	/**
	 * Sets the UnitType
	 * @deprecated
	 * @param unitType
	 */
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	/**
	 * Sets the UnitExponents to the Exponents of the given unit.
	 * @param unit
	 */
	public void setUnitExponents(Unit unit)
	{
		for(int i = 0; i < 7; i++)
			this.unitExponent[i] = unit.getUnitExponent(i);
	}

	/**
	 * Sets the unit exponents.
	 * @param exponents
	 */
	public void setUnitExponents(float[] exponents)
	{
		this.unitExponent = exponents;
	}
	
	/**
	 * Sets the unit exponents.
	 * @param exponents
	 * @throws NumberFormatException
	 */
	public void setUnitExponents(String[] exponents) throws NumberFormatException
	{
		for(SIUnit u: SIUnit.values())
		{
			try
			{
				float exp = Float.parseFloat(exponents[u.ordinal()].trim());
				this.setUnitExponent(u, exp);
			}
			catch(NumberFormatException e)
			{
				throw e;
			}
			catch(IndexOutOfBoundsException indexEx)
			{
				this.setUnitExponent(u, 0);
			}
		}
	}
	
	/**
	 * Sets the Exponent of the given base Unit
	 * @param unit
	 * @param value
	 */
	public void setUnitExponent(SIUnit unit, float value)
	{
		this.unitExponent[unit.ordinal()] = value;
	}

	/**
	 * Sets the "convert to SI"-function
	 * @param convertToSIFunction
	 */
	public void setConvertToSIFunction(String convertToSIFunction) {
		this.convertToSIFunction = convertToSIFunction;
	}

	/**
	 * Sets the "convert from SI"-function
	 * @param convertFromSIFunction
	 */
	public void setConvertFromSIFunction(String convertFromSIFunction) {
		this.convertFromSIFunction = convertFromSIFunction;
	}

	/**
	 * Sets the HTML-Representation for this unit.
	 * @param html
	 */
	public void setHtml(String html)
	{
		this.html = html;
	}

	/**
	 * Returns the corresponding SI-Unit.
	 * @return
	 */
	public String getSiUnit() {
		return siUnit;
	}

	/**
	 * Returns the ID
	 * @return
	 */
	public String getUnitId() {
		return this.unitId;
	}

	/**
	 * Returns the Unit Type
	 * @deprecated
	 * @return
	 */
	public String getUnitType() {
		return unitType;
	}

	/**
	 * Creates a String-Representation of this unit.
	 * @return
	 */
	public String getUnitAsString()
	{
		String unitString = "";
		
		for(SIUnit u: SIUnit.values())
		{
			if(this.unitExponent[u.ordinal()] != 0)
				if(this.unitExponent[u.ordinal()] == 1)
					unitString += u.toString() + " ";
				else
					unitString += u.toString() + this.unitExponent[u.ordinal()] + " ";
		}
				
		return unitString.trim();
	}		
	
	/**
	 * Creates a HTML-Representation of this Unit.
	 * @return
	 */
	public String getUnitAsHTML()
	{
		try{		
			String unitString = "";
			TemplateLogger.addLog(LogType.DEBUGINFO, "Unit.getUnitAsHtml", "Exponents: " + this.getUnitExponents(), 4);
			for(SIUnit u: SIUnit.values())
			{
				if(this.unitExponent[u.ordinal()] != 0)
					if(this.unitExponent[u.ordinal()] == 1)
						unitString += u.toString() + " ";
					else
						unitString += u.toString() + "<sup>" + this.unitExponent[u.ordinal()] + "</sup> ";
			}
					
			return unitString.trim();
		}
		catch(Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "Unit.getUnitAsHtml", "Error on create HTML: " + e.toString(), 1);
			return "";
		}
	}	
	
	/**
	 * Returns the Exponent of the given base-Unit
	 * @param unit
	 * @return
	 */
	public float getUnitExponent(SIUnit unit)
	{
		return this.unitExponent[unit.ordinal()];
	}
	
	/**
	 * Returns the Exponent of the given index.
	 * @param index
	 * @return
	 */
	public float getUnitExponent(int index)
	{
		return this.unitExponent[index];
	}
	
	/**
	 * Returns an Array with the Unit exponents.
	 * @return
	 */
	public float[] getUnitExponents()
	{
		return this.unitExponent;
	}
	
	/**
	 * Returns the "convert to SI"-Function.
	 * @return
	 */
	public String getConvertToSIFunction() {
		return convertToSIFunction;
	}

	/**
	 * Returns the "convert from SI"-Function
	 * @return
	 */
	public String getConvertFromSIFunction() {
		return convertFromSIFunction;
	}

	/**
	 * Returns the Unit-String in HTML-Format
	 * @return
	 */
	public String getHtml()
	{
		return this.html;
	}
	
	/**
	 * Returns true, if this Unit is a SI-Unit
	 * @return
	 */
	public boolean isSiUnit() {
		return this.isSiUnit;
	}
	
	/**
	 * Returns true, if all Unit-Exponents are Zero
	 * @return
	 */
	public boolean allExponentsZero() {
		for(SIUnit unit: SIUnit.values())
			if(this.getUnitExponent(unit) != 0)
				return false;
				
		return true;
	}
	
	/**
	 * Creates a new Unit-Object that is a copy of this Unit.
	 */
	public Unit clone()
	{
		Unit u = new Unit();
		
		u.setUnitId(this.getUnitId());
		u.setHtml(this.getHtml());
		u.setUnitType(this.getUnitType());
		u.setIsSiUnit(this.isSiUnit());
		u.setSiUnit(this.getSiUnit());
		u.setConvertToSIFunction(this.getConvertToSIFunction());
		u.setConvertFromSIFunction(this.getConvertFromSIFunction());
		u.setUnitExponents(this);
		
		return u;
	}	
	
	/**
	 * Multiplies this Unit with the given Unit.
	 * @param unit Unit to multiply with.
	 * @return A new Unit with the resulting Unit-Exponents
	 */
	public  Unit multiply(Unit unit)
	{
		Unit newUnit = new Unit();
		
		for(SIUnit u: SIUnit.values())		
			newUnit.setUnitExponent(u, this.getUnitExponent(u) + unit.getUnitExponent(u));	
		
		return newUnit; 
	}
	
	/**
	 * Divides this Unit by the divisor
	 * @param divisor The Unit by which should be divided
	 * @return A new Unit with the resulting Unit-Exponents
	 */
	public Unit divide(Unit divisor)
	{
		Unit newUnit = new Unit();
		
		for(SIUnit u: SIUnit.values())		
			newUnit.setUnitExponent(u, this.getUnitExponent(u) - divisor.getUnitExponent(u));		
				
		return newUnit; 
	}
	
	/**
	 * Returns a new Unit-Object containing the square root of this Unit.
	 * @return 
	 */
	public Unit SQRT()
	{
		Unit newUnit = new Unit();
		
		for(SIUnit u: SIUnit.values())
		{			
			newUnit.setUnitExponent(u, this.getUnitExponent(u) * 0.5f);
		}

		return newUnit; 
	}
	
	/**
	 * Returns a new Unit-Object containing the inverse of this Unit.
	 * @return
	 */
	public Unit inverse()
	{
		Unit newUnit = new Unit();
		for(SIUnit u : SIUnit.values())
		{
			newUnit.setUnitExponent(u, this.getUnitExponent(u) * -1);
		}
		return newUnit;
	}
	
	/**
	 * Returns a new Unit containing this Unit raised by the power of pow.
	 * @param pow
	 * @return
	 */
	public Unit pow(float pow)
	{
		if(pow == 0)
			return new Unit();
		
		Unit newUnit = this.clone();
		for(int i = 1; i < Math.abs(pow); i++)
			newUnit = newUnit.multiply(this);
		
		if(pow < 0)
			newUnit = newUnit.inverse();
		
		return newUnit;
	}
	
	/**
	 * Checks if obj contains the same values as this unit.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Unit)
		{
			for(SIUnit u: SIUnit.values())
				if(this.getUnitExponent(u) != ((Unit)obj).getUnitExponent(u))
					return false;
			
			return true;
					
		}
		else
			return false;
	}
}
