package de.tet.inputmaskTemplate.plugins.mathPlugin;

import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;

/**
 * Provides a structure and functions for mathematical calculations with units.
 * @author predki
 *
 */
public class Quantity {
	private double value;
	private Unit unit;
	
	/**
	 * Constructor. Creates an empty Quantity with no Unit.
	 */
	public Quantity ()
	{
		this.value = Double.NaN;
		this.unit = new Unit();
	}
	
	/**
	 * Creates an Quantity with the given value and no Unit.
	 * @param value
	 */
	public Quantity (double value)
	{
		this.value = value;
		this.unit = new Unit();
	}
	
	/**
	 * Creates a Quantity with the given value and {@link Unit}
	 * @param value
	 * @param unit
	 */
	public Quantity (double value, Unit unit)
	{
		this.value = value;
		this.unit = unit;
	}
	
	/**
	 * Creates a Quantity with the given value and Unit in exponential-representation.
	 * The unit is created by the Exponents of the 5 base-Units: s, m, kg, A, K, mol, cd 
	 * @param value 
	 * @param exponents an array of floats which represent the exponents of the 5 base units.
	 */
	public Quantity (double value, float[] exponents)
	{
		this.value = value;
		this.unit = new Unit();
		this.unit.setUnitExponents(exponents);
	}
	
	/**
	 * Creates a Quantity from the given String. The String is splitted in two parts for the value and the. 
	 * Then both parts are parsed. If the Unit contains a prefix (e.g. m for milli), the value is converted 
	 * to the corresponding SI-Unit.
	 * @param quantity
	 * @throws UnitException Throws an Unit-Exception if the Unit can't be parsed. 
	 */
	public Quantity(String quantity) throws UnitException
	{
		this.unit = new Unit();
		TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Create Quantity from: " + quantity, 4);
		
		String[] parts = quantity.split("\\s");
		TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "splitted in " + parts.length + " parts.", 4);
		
		int start = 0;
		
		TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "First Part a number?", 4);
		try
		{
			this.setValue(Double.parseDouble(parts[0]));
			start = 1;
			TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "True. parse and set Start = 1.", 4);			
		}
		catch (NumberFormatException e)
		{
			TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "false. Part is not a Number.", 4);
		}
		
		TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Parse parts...", 4);
		for(int i = start; i < parts.length; i++)
		{		
			float pow = 1;
			String part = parts[i];
			
			// Check for Exponents
			TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Part contains Exponent?", 4);
			if(parts[i].contains("^"))
			{
				String[] innerParts = part.split("\\^");
				part = innerParts[0];
				pow = Float.parseFloat(innerParts[1]);
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Exponent parsed.", 4);
			}

			// Check for Prefix
			TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Check for " + part + " for Prefix...", 4);
			if(UnitLoader.getUnit(part) == null)
			{
				String prefix = part.substring(0, 1);
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Prefix: " + prefix, 4);
				
				part = part.substring(1);
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Unit: " + part, 4);
				
				if( (UnitLoader.getUnit(part) == null) || (UnitLoader.getUnitPrefix(prefix) == null) )
					throw new UnitException("Unknown Unit or Prefix: " + prefix + part);
				
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Prefix: " + prefix, 4);
				
				double factor = UnitLoader.getUnitPrefix(prefix).getFactor();
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Prefix factor: " + factor, 4);
				
				this.value *= Math.pow(factor, pow);
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Value computed: " + this.value, 4);
				
				Unit u = UnitLoader.getUnit(part);
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Unit: " + u.getUnitAsString(), 4);
				
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Check for SI-Unit", 4);
				if(!u.isSiUnit())				
				{
					this.setValue(Converter.MultiplyConvert(this, u.getConvertToSIFunction(), pow).getValue());
					TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Unit converted to SI. New value: " + this.getValue(), 4);
				}
				
				this.setUnit(this.unit.multiply(u.pow(pow)));
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Unit set. " + this.getUnit().getUnitAsString(), 4);
			}
			else
			{
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", part + " is known Unit.", 4);
				Unit u = UnitLoader.getUnit(part);
				
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Check for SI-Unit", 4);
				if(!u.isSiUnit())				
				{
					this.setValue(Converter.MultiplyConvert(this, u.getConvertToSIFunction(), pow).getValue());
					TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "Unit converted to SI. New value: " + this.getValue(), 4);
				}
				
				TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "multiply by Exponent.", 4);
				this.setUnit(this.unit.multiply(u.pow(pow)));
			}
		}
		TemplateLogger.addLog(LogType.DEBUGINFO, "Quantity(String)", "created Quantity: " + this.toString(), 4);
	}
	
	/**
	 * Returns the value of this Quantity
	 * @return
	 */
	public double getValue()
	{
		return this.value;
	}
	
	/**
	 * Sets the value of this Quantity
	 * @param value
	 */
	public void setValue(double value)
	{
		this.value = value;
	}
	
	/**
	 * Returns the Unit of this Quantity.
	 * @return
	 */
	public Unit getUnit()
	{
		return this.unit;
	}
	
	/**
	 * Sets the Unit of this Quantity
	 * @param u
	 */
	public void setUnit(Unit u)
	{
		this.unit = u;
	}
	
	/**
	 * Creates a new Quantity-Object which is a copy of this Quantity.
	 */
	public Quantity clone()
	{
		return new Quantity(this.getValue(), this.getUnit().clone());
	}
	
	/**
	 * Adds the given Quantity to this Quantity. Throws an UnitException if the Units of both Quantities
	 * aren't the same.
	 * @param q the Quantity which should be added.
	 * @return new Quantity with the added value and the same Unit
	 * @throws UnitException
	 */
	public Quantity add(Quantity q) throws UnitException
	{
		if(this.getUnit().equals(q.getUnit()))
		{
			return new Quantity(this.getValue() + q.getValue(), this.getUnit());
		}
		else
		{
			throw new UnitException("Units doesnt match!", this.getUnit(), q.getUnit());
		}
	}
	
	/**
	 * Subtracts the given Quantity from this Quantity. Throws an UnitException if the Units of both Quantities
	 * aren't the same.
	 * @param q the Quantity which should be subtracted.
	 * @return new Quantity with the subtracted value and the same Unit
	 * @throws UnitException
	 */
	public Quantity subtract(Quantity q) throws UnitException
	{
		if(this.getUnit().equals(q.getUnit()))
		{
			return new Quantity(this.getValue() - q.getValue(), this.getUnit());
		}
		else
		{
			throw new UnitException("Units doesnt match!", this.getUnit(), q.getUnit());
		}
	}
	
	/**
	 * Multiplies the given Quantity with this one.
	 * @param q the Quantity which should be multiplied.
	 * @return a new Quantity with the multiplied value and the resulting unit.
	 * @throws UnitException
	 */
	public Quantity multiply(Quantity q) throws UnitException
	{
		return new Quantity(this.getValue() * q.getValue(), this.getUnit().multiply(q.getUnit()));
	}
	
	/**
	 * Multiplies this Quantity with an unit-less number a
	 * @param a
	 * @return a new Quantity with the multiplied value and same Unit.
	 * @throws UnitException
	 */
	public Quantity multiply(double a) throws UnitException
	{
		return new Quantity(this.getValue() * a, this.getUnit().clone());
	}
	
	/**
	 * 
	 * Divides this Quantity by the given one.
	 * @param q the Quantity to divide by. 
	 * @return a new Quantity with the divide value and the resulting unit.
	 * @throws UnitException
	 */
	public Quantity divideBy(Quantity q) throws UnitException
	{
		return new Quantity(this.getValue() / q.getValue(), this.getUnit().divide(q.getUnit()));
	}
	
	/**
	 * Divides this Quantity by an unit-less number a
	 * @param a
	 * @return a new Quantity with the divided value and same Unit.
	 * @throws UnitException
	 */
	public Quantity divideBy(double a) throws UnitException
	{
		return new Quantity(this.getValue() / a, this.getUnit().clone());
	}
	
	/**
	 * Computes the Squareroot of this Quantity. 
	 * @return a new Quantity with the new value an the resulting unit.
	 * @throws UnitException
	 */
	public Quantity sqrt() throws UnitException
	{
		return new Quantity(Math.sqrt(this.getValue()), this.getUnit().SQRT());
	}
	
	/**
	 * Computes the inverse of this Quantity
	 * @return a new Quantity with the inverse value and inverse Unit.
	 */
	public Quantity inverse()
	{
		return new Quantity(1 / this.getValue(), this.getUnit().inverse());
	}
	
	/**
	 * Powers this quantity by the power pow.
	 * @param pow
	 * @return a new Quantity with the resulting value and Unit.
	 */
	public Quantity pow(float pow)
	{
		return new Quantity(Math.pow(this.getValue(), pow), this.getUnit().pow(pow));
	}
	
	/**
	 * Returns e raised by this Quantity. Thorws an UnitException if the Quantity is not unit-less.
	 * @return a new Quantity with the resulting value and no Unit.
	 * @throws UnitException
	 */
	public Quantity exp() throws UnitException
	{
		if(this.getUnit().allExponentsZero())
			return new Quantity(Math.exp(this.getValue()), this.getUnit().clone());
		else
			throw new UnitException("Unit-Exponents are not Zero!", this.getUnit());
	}
	
	@Override
	/**
	 * Creates a String with the form "value unit". Unit exponents are shown as ^x.
	 */
	public String toString()
	{
		return Double.toString(this.value) + " " + this.getUnit().getUnitAsString();
	}
	
	/**
	 * Creates a String with HTML-Code for better visualization. Unit exponents are surrounded
	 * by the {@code <sup></sup>} HTML-Tags.
	 * @return
	 */
	public String toHtml()
	{
		return Double.toString(this.value) + " " + this.getUnit().getUnitAsHTML();
	}
		
	/**
	 * Checks if this Quantity and Quantity q are the same by comparing the values and units.
	 * @param q the Quantity which should be compared.
	 * @param ignoreUnits if true the Units of both Quantities aren't compared.
	 * @return
	 */
	public boolean equals(Quantity q, boolean ignoreUnits)
	{
		boolean units_equal = true;
		if(!ignoreUnits)
			units_equal = this.getUnit().equals(q.getUnit());
		
		if( (this.getValue() == q.getValue() & units_equal ) )
				return true;
		else
			return false;
	}
	
	/**
	 * Checks if this Quantity has a lower value than q
	 * @param q the Quantity which should be compared.
	 * @param ignoreUnits if true the Units of both Quantities aren't compared.
	 * @return
	 */
	public boolean isLowerThan(Quantity q, boolean ignoreUnits)
	{
		boolean units_equal = true;
		if(!ignoreUnits)
			units_equal = this.getUnit().equals(q.getUnit());
		
		if( (this.getValue() < q.getValue() & units_equal ) )
				return true;
		else
			return false;
	}
	
	/**
	 * Checks if this Quantity has a higher value than q.
	 * @param q the Quantity which should be compared.
	 * @param ignoreUnits if true the Units of both Quantities aren't compared.
	 * @return
	 */
	public boolean isHigherThan(Quantity q, boolean ignoreUnits)
	{
		boolean units_equal = true;
		if(!ignoreUnits)
			units_equal = this.getUnit().equals(q.getUnit());
		
		if( (this.getValue() > q.getValue() & units_equal ) )
				return true;
		else
			return false;
	}
	
	/**
	 * Checks if this Quantity is lower or equal than q
	 * @param q the Quantity which should be compared.
	 * @param ignoreUnits if true the Units of both Quantities aren't compared.
	 * @return
	 */
	public boolean isLowerEqualThan(Quantity q, boolean ignoreUnits)
	{
		boolean units_equal = true;
		if(!ignoreUnits)
			units_equal = this.getUnit().equals(q.getUnit());
		
		if( (this.getValue() <= q.getValue() & units_equal ) )
				return true;
		else
			return false;
	}
	
	/**
	 * Checks if this Quantity is higher or equal than q
	 * @param q the Quantity which should be compared.
	 * @param ignoreUnits if true the Units of both Quantities aren't compared.
	 * @return
	 */
	public boolean isHigherEqualThan(Quantity q, boolean ignoreUnits)
	{
		boolean units_equal = true;
		if(!ignoreUnits)
			units_equal = this.getUnit().equals(q.getUnit());
		
		if( (this.getValue() >= q.getValue() & units_equal ) )
				return true;
		else
			return false;
	}
}
