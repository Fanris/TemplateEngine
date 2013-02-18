package de.tet.inputmaskTemplate.plugins.numericTextboxPlugin;

/**
 * Data structure which represents a constraint for a numericTextboxField
 * by another Field. 
 * @author Martin Predki
 *
 */
public class Constraint {
	private String fieldId;
	private Constraints constrainedBy;
	
	/**
	 * Returns the ID of the Field by which is constrained.
	 * @return
	 */
	public String getFieldId() {
		return fieldId;
	}
	
	/**
	 * Sets the ID of the Field by which is constrained.
	 * @param fieldId
	 */
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	
	/**
	 * Returns the Constraint
	 * @return
	 */
	public Constraints getConstraintBy() {
		return constrainedBy;
	}
	
	/**
	 * Sets the Constraint
	 * @param constraintBy
	 */
	public void setConstraintBy(Constraints constraintBy) {
		this.constrainedBy = constraintBy;
	}
	
	/**
	 * Constructor.
	 * @param fieldId Field ID by which should be constrained.
	 * @param constrainedBy Constraint
	 */
	public Constraint(String fieldId, Constraints constrainedBy)
	{
		this.fieldId = fieldId;
		this.constrainedBy = constrainedBy;
	}
}
