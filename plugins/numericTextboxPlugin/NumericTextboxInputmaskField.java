package de.tet.inputmaskTemplate.plugins.numericTextboxPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
import de.tet.inputmaskTemplate.client.logic.InputmaskField;
import de.tet.inputmaskTemplate.client.logic.InputmaskLogic;
import de.tet.inputmaskTemplate.plugins.mathPlugin.MathVar;
import de.tet.inputmaskTemplate.plugins.mathPlugin.Quantity;
import de.tet.inputmaskTemplate.plugins.mathPlugin.UnitException;

/**
 * InputmaskField extended by specific Properties for the NumericTextboxPlugin. Extensions include
 * Unit-Management, Constraints, Quantities
 * @author Martin Predki
 *
 */
public class NumericTextboxInputmaskField extends InputmaskField {
	private String defaultUnit = null;	
	private InputmaskUnit selectedUnit = null;
	private HashMap<String, InputmaskUnit> units = new HashMap<String, InputmaskUnit>();
	private ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	
	/**
	 * Returns the ID of the default Unit of this Field.
	 * @return
	 */
	public String getDefaultUnit() {
		return defaultUnit;
	}
	
	/**
	 * Sets the ID of the default Unit of this Field.
	 * @param defaultUnit
	 */
	public void setDefaultUnit(String defaultUnit) {
		this.defaultUnit = defaultUnit;
	}		

	/**
	 * Adds a selectable {@link InputmaskUnit} to this field.
	 * @param unit
	 */
	public void addUnit(InputmaskUnit unit)
	{
		this.units.put(unit.getId(), unit);
	}
	
	/**
	 * Returns the number of selectable {@link InputmaskUnit}s
	 * @return
	 */
	public int getUnitCount()
	{
		return this.units.size();
	}
	
	/**
	 * Returns the selected {@link InputmaskUnit}
	 * @return
	 */
	public InputmaskUnit getSelectedUnit()
	{
		if(this.selectedUnit == null)
			return this.units.get(this.defaultUnit);
		else
			return this.selectedUnit;
	}
	
	/**
	 * Sets the selected {@link InputmaskUnit}.
	 * @param unit the ID of the InputmaskUnit which should be selected.
	 */
	public void setSelectedUnit(String unit)
	{
		if(!this.units.containsKey(unit))		
			TemplateLogger.addLog(LogType.LOG, "NumericTextboxInputmaskField.setSelectedUnit", "Unit: " + unit + " does not exist.", 4);
					
		this.selectedUnit = this.units.get(unit);
	}
	
	/**
	 * Creates and returns an ArrayList with all selectable {@link InputmaskUnit}s
	 * @return
	 */
	public List<InputmaskUnit> getUnits()
	{		
		ArrayList<InputmaskUnit> list = new ArrayList<InputmaskUnit> ();
		for(InputmaskUnit u : this.units.values())
			list.add(u);
				
		return list;
	}
	
	/**
	 * Returns the {@link InputmaskUnit} with the given ID or null
	 * if it doesn't exist.
	 * @param id
	 * @return
	 */
	public InputmaskUnit getUnit(String id)
	{
		return this.units.get(id);
	}
	
	/**
	 * Adds a {@link Constraint} to this Field
	 * @param c
	 */
	public void addConstraint(Constraint c)
	{
		this.constraints.add(c);
	}
	
	/**
	 * Removes a {@link} Constraint from this Field.
	 * @param c
	 */
	public void removeConstraint(Constraint c)
	{
		this.constraints.remove(c);
	}
	
	/**
	 * Removes the {@link Constarint} at the given index from this Field.
	 * @param index
	 */
	public void removeConstraint(int index)
	{
		this.constraints.remove(index);
	}
	
	/**
	 * Returns the List of {@link Constraint}s of this Field.
	 * @return
	 */
	public ArrayList<Constraint> getConstraints()
	{
		return this.constraints;
	}

	/**
	 * Creates a {@link Quantity} from this Fields values. 
	 * @return
	 */
	public Quantity createQuantity()
	{
		try {
			return new Quantity(this.getValue() + " " + this.getSelectedUnit().getId());
		} catch (UnitException e) {
			TemplateLogger.addLog(LogType.ERROR, "NumericTextboxInput.createQuantity", e.toString(), 1);
			return null;
		}
	}
	
	/**
	 * Creates a {@link MathVar} from this Fields values.
	 * @return
	 */
	public MathVar createMathVar()
	{
		try {
			return new MathVar(this.getID(), this.getFullName(), this.getDisplayName(), this.createQuantity());
		} catch (Exception e) {
			TemplateLogger.addLog(LogType.ERROR, "NumericTextboxInput.createMathVar", e.toString(), 1);
			return null;
		}
	}
	
	public boolean hasValueChanged()
	{
		if(this.getValue().equalsIgnoreCase(this.getDefaultValue()))
			return false;
		else
			return true;
	}
	
	public boolean hasUnitChanged()
	{
		if(this.getSelectedUnit().getId().equalsIgnoreCase(this.getDefaultUnit()))
			return false;
		else
			return true;
	}
	
	@Override
	public String getDataString()
	{
		return this.getValue() + " " + this.getSelectedUnit().getId();
	}
	
	@Override
	public void setDataString(String value)
	{
		String[] data = value.split("\\s");
		this.setValue(data[0]);
		String unit = "";
		for(int i = 1; i < data.length; i++)
		{
			unit += data[i] + " ";
		}
		this.setSelectedUnit(unit.trim());
		TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTexboxInputmaskField.setDataString", "Value set to: " + data[0] + ", Unit set to: " + unit, 4);
	}
		
	@Override
	public boolean verify()
	{
		TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.verify", "Begin verifing of " + this.getID() + "...", 6);
		double value = Double.NaN;
		
		try
		{
			value = Double.parseDouble(this.getValue());
		}
		catch (Exception e)
		{
			return false;
		}
		
		TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.verify", "value contains a double-values.", 6);
		// check Unit min / max
		if(value < this.getSelectedUnit().getMin())
			return false;
		
		TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.verify", "value > min-Value", 6);
		
		if(value > this.getSelectedUnit().getMax())
			return false;
		
		TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.verify", "value < max-value", 6);
		
		for(Constraint c: this.getConstraints())
		{
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.verify", "Check constraint: " + this.getID() + " is " + c.getConstraintBy().toString() + " " + c.getFieldId(), 6);
			InputmaskField i = InputmaskLogic.getInputData(c.getFieldId());
			if(!(i instanceof NumericTextboxInputmaskField))
			{
				TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.verify", "constraint invalid. Reason: compared Inputfield-Class isnt the same.", 6);
				continue;
			}
			
			NumericTextboxInputmaskField nif = (NumericTextboxInputmaskField)i;
			
			switch(c.getConstraintBy())
			{
				case EQUAL: 
				{					
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.equals(q2, false) )
						return false;
					break;
				}
				
				case LOWER_THAN: 
				{
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.isLowerThan(q2, false) )
						return false;
					break;
				}
				
				case LOWER_EQUAL_THAN: 
				{
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.isLowerEqualThan(q2, false) )
						return false;
					break;
				}
				
				case HIGHER_THAN: 
				{
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.isHigherThan(q2, false) )
						return false;
					break;
				}
				
				case HIGHER_EQUAL_THAN: 
				{
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.isHigherEqualThan(q2, false) )
						return false;
					break;
				}
				
				case EQUAL_IGNORE_UNIT: 
				{
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.equals(q2, true) )
						return false;
					break;
				}
				
				case LOWER_THAN_IGNORE_UNIT: 
				{
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.isLowerThan(q2, true) )
						return false;
					break;
				}
				
				case LOWER_EQUAL_THAN_IGNORE_UNIT: 
				{
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.isLowerEqualThan(q2, true) )
						return false;
					break;
				}
				
				case HIGHER_THAN_IGNORE_UNIT: 
				{
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.isHigherThan(q2, true) )
						return false;
					break;
				}
				
				case HIGHER_EQUAL_THAN_IGNORE_UNIT: 
				{
					Quantity q1 = this.createQuantity();
					Quantity q2 = nif.createQuantity();
					
					if(! q1.isHigherEqualThan(q2, true) )
						return false;
					break;
				}
				
				case INVALID: break;
			}
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.verify", "Constraint passed.", 6);
		}		
		return true;
	}	
}
